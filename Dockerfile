#FROM postgres
#ENV POSTGRES_USER ${DBUSER}
#ENV POSTGRES_PASSWORD ${DBPASSWORD}
#EXPOSE 5432:5432
#ENV POSTGRES_DB java
#
##docker build -t pg/dev13:v1 .
##docker run --name pg

FROM openjdk:17
COPY build/libs/URL-Shortener-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
#ENTRYPOINT["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]