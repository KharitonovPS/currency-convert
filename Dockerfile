FROM openjdk:21-jdk-oracle

COPY  ../build/libs/currency-converter-1.0.0-SNAPSHOT.jar /app/app.jar
WORKDIR /app
CMD ls
CMD java -Xmx1024m -jar /app/app.jar