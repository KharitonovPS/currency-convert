FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl

COPY ../build/libs/currency-converter-1.0.0-SNAPSHOT.jar /app/app.jar
WORKDIR /app

CMD java -Xmx1024m -jar /app/app.jar