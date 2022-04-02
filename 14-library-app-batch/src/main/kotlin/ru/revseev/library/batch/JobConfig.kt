package ru.revseev.library.batch

import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.lang.NonNull
import org.springframework.transaction.annotation.Transactional
import ru.revseev.library.batch.dto.BookWithComments
import ru.revseev.library.domain.Book
import ru.revseev.library.repo.CommentRepo

private val log = KotlinLogging.logger { }
internal const val MONGO_JOB_NAME = "mongo-to-postgres"
internal const val EXECUTE_SQL_SCHEMA_STEP_NAME = "execute-sql-schema-step"
internal const val MONGO_READ_STEP_NAME = "mongo-read-step"
internal const val MONGO_READER_NAME = "mongo-reader"
internal const val CHUNK_SIZE = 2
internal const val READER_PAGE_SIZE: Int = 2


@EnableBatchProcessing
@Configuration
class JobConfig(
    private val stepBuilderFactory: StepBuilderFactory,
    private val jobBuilderFactory: JobBuilderFactory,
    private val sqlExecutioner: SqlExecutioner,
) {

    @Bean
    fun mongoToPostgresJob(mongoReadStep: Step): Job = jobBuilderFactory
        .get(MONGO_JOB_NAME)
        .incrementer(RunIdIncrementer())
        .start(executeSQLSchemaStep())
        .next(mongoReadStep)
        .listener(object : JobExecutionListener {
            override fun beforeJob(@NonNull jobExecution: JobExecution) {
                log.info("Начало job")
            }

            override fun afterJob(@NonNull jobExecution: JobExecution) {
                log.info("Конец job")
            }
        })
        .build()

    @Bean
    fun executeSQLSchemaStep(): Step = stepBuilderFactory
        .get(EXECUTE_SQL_SCHEMA_STEP_NAME)
        .tasklet(executeSqlTasklet())
        .build()

    @Bean
    fun executeSqlTasklet(): Tasklet = MethodInvokingTaskletAdapter().apply {
        setTargetObject(sqlExecutioner)
        setTargetMethod("execute")
        setArguments(arrayOf("postgres_schema.sql"))
    }

    @Bean
    fun mongoReadStep(
        reader: ItemReader<Book>,
        enrichingBookProcessor: ItemProcessor<Book, BookWithComments>,
        postgresBookWriter: ItemWriter<BookWithComments>,
    ): Step = stepBuilderFactory
        .get(MONGO_READ_STEP_NAME)
        .chunk<Book, BookWithComments>(CHUNK_SIZE)
        .reader(reader)
        .processor(enrichingBookProcessor)
        .writer(postgresBookWriter)
        .build()


    @Bean
    @StepScope
    fun reader(mongoTemplate: MongoTemplate): ItemReader<Book> = MongoItemReaderBuilder<Book>()
        .template(mongoTemplate)
        .name(MONGO_READER_NAME)
        .targetType(Book::class.java)
        .jsonQuery("{}")
        .sorts(mapOf("_id" to Sort.Direction.ASC))
        .pageSize(READER_PAGE_SIZE)
        .build()

    @Bean
    fun enrichingBookProcessor(commentRepo: CommentRepo): ItemProcessor<Book, BookWithComments> =
        EnrichingBookProcessor(commentRepo)

    @Bean
    fun printingWriter(): ItemWriter<BookWithComments> {
        return ItemWriter { books -> println(books) }
    }

    internal open class EnrichingBookProcessor(
        private val commentRepo: CommentRepo,
    ) : ItemProcessor<Book, BookWithComments> {

        @Transactional(readOnly = true)
        override fun process(book: Book): BookWithComments {
            val comments = commentRepo.findAllById(book.commentIds)
            return BookWithComments(book, comments)
        }
    }
}
