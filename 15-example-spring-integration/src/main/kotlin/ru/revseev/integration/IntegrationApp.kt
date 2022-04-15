package ru.revseev.integration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.InboundChannelAdapter
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.integration.channel.QueueChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.core.MessageSource
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.MessageChannels
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.file.FileReadingMessageSource
import org.springframework.integration.file.filters.AcceptOnceFileListFilter
import org.springframework.integration.file.filters.CompositeFileListFilter
import org.springframework.integration.file.filters.RegexPatternFileListFilter
import org.springframework.integration.scheduling.PollerMetadata
import ru.revseev.integration.domain.ManagedFile
import ru.revseev.integration.domain.ManagedType
import java.io.File
import java.util.*


@SpringBootApplication
@EnableIntegration
class IntegrationApp {

    @Value("\${paths.directory.source}")
    lateinit var sourceDir: String

    @Bean
    fun readDirectory(): QueueChannel = MessageChannels.queue(10).get()

    @Bean(name = [PollerMetadata.DEFAULT_POLLER])
    fun poller(): PollerMetadata = Pollers.fixedRate(1000).maxMessagesPerPoll(3).get()

    @Bean
    fun managedFilesChannel(): PublishSubscribeChannel = MessageChannels.publishSubscribe().get()

    @Bean
    @InboundChannelAdapter(value = "readDirectory")
    fun readDirectoryAdapter(): MessageSource<File> {
        val matchWithoutCopySuffix = Regex("^.*(?<!_copy)\$", RegexOption.IGNORE_CASE).toPattern()
        return FileReadingMessageSource().apply {
            setDirectory(File(sourceDir))
            setFilter(
                CompositeFileListFilter(
                    listOf(AcceptOnceFileListFilter(), RegexPatternFileListFilter(matchWithoutCopySuffix))
                )
            )
        }
    }

    @Bean
    fun flow(): IntegrationFlow {
        return IntegrationFlows.from("readDirectory")
            .log()
            .filter<File> { it.isFile && !it.isHidden }
            .split()
            .handle("fileService", "analyze")
            .route<ManagedFile, ManagedType>({ it.type },
                { routerSpec ->
                    routerSpec.subFlowMapping(ManagedType.NORMAL) { it.handle("copyService", "copy") }
                        .subFlowMapping(ManagedType.COMPRESSIBLE) { it.handle("copyService", "copyAndCompress") }
                        .subFlowMapping(ManagedType.IGNORED) { it.nullChannel() }
                }
            )
            .handle("observingService", "observe")
            .channel("managedFilesChannel")
            .get()
    }

}

fun main(args: Array<String>) {
    val context = runApplication<IntegrationApp>(*args)
    context.registerShutdownHook()

    val scanner = Scanner(System.`in`)
    print("Please enter q and press <enter> to exit the program: ")

    while (true) {
        val input: String = scanner.nextLine()
        if ("q" == input.trim()) {
            break
        }
    }
    System.exit(0)
}
