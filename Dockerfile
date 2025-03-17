#build project
FROM maven:3.9.8-sapmachine-21 AS build
WORKDIR /app
COPY . .
RUN if [ ! -f target/JR-hibernate-final-project-1.0-SNAPSHOT-jar-with-dependencies.jar ];  \
    then mvn clean package; \
    else echo "Artifact is exist"; \
    fi

#run project
FROM openjdk:21-jdk-oracle
LABEL authors="Nikitin R.N."
WORKDIR /app
COPY --from=build /app/target/JR-hibernate-final-project-1.0-SNAPSHOT-jar-with-dependencies.jar hiber_final.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar hiber_final.jar"]
