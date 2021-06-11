FROM adoptopenjdk:11-jdk-hotspot
COPY ./mvnw ./mvnw
COPY ./pom.xml pom.xml
COPY ./.mvn ./.mvn
COPY ./src ./src
ENTRYPOINT ["./mvnw", "spring-boot:run"]

