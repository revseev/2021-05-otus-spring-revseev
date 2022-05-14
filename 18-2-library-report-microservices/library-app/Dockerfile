#### Stage 1: Building Jar application
FROM openjdk:11 as build

# Set the current working directory inside the image
WORKDIR /library-app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless
# the pom.xml file has changed.
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN export MAVEN_OPTS="--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED"
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM openjdk:11

ARG DEPENDENCY=/library-app/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /library-app/lib
COPY --from=build ${DEPENDENCY}/META-INF /library-app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /library-app

ENTRYPOINT ["java","-cp","library-app:library-app/lib/*","ru.revseev.library.LibraryAppApplicationKt"]