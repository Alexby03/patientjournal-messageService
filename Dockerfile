FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests -Dquarkus.profile=prod

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /build/target/quarkus-app/lib/ ./lib/
COPY --from=build /build/target/quarkus-app/*.jar ./
COPY --from=build /build/target/quarkus-app/app/ ./app/
COPY --from=build /build/target/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8083

ENV QUARKUS_DATASOURCE_USERNAME=db_admin
ENV QUARKUS_DATASOURCE_PASSWORD=admin123
ENV QUARKUS_DATASOURCE_JDBC_URL=jdbc:mysql://localhost:3306/patientjournaldb
ENV QUARKUS_PROFILE=prod

CMD ["java", "-jar", "quarkus-run.jar"]