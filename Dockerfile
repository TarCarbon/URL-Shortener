FROM postgres
ENV POSTGRES_USER ${DBUSER}
ENV POSTGRES_PASSWORD ${DBPASSWORD}
EXPOSE 5432:5432
ENV POSTGRES_DB java

#docker build -t pg/dev13:v1 .
#docker run --name pg