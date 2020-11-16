FROM maven:3.6.3-jdk-8 as build

WORKDIR /build
ADD src src
ADD pom.xml .

RUN mvn clean compile assembly:single

FROM jrottenberg/ffmpeg:4.1-alpine

COPY --from=build /build/target/ProbeServer-1.0-jar-with-dependencies.jar /app/
RUN apk add openjdk8

ENTRYPOINT ["java", "-jar"]
WORKDIR /app
CMD ["/app/ProbeServer-1.0-jar-with-dependencies.jar"]

