FROM openjdk:21-jdk-oracle

CMD ls -l
COPY  build/libs/currency-converter-1.0.0-SNAPSHOT.jar /app/app.jar
WORKDIR /app

#CMD java -Xmx1024m -jar /app/app.jar
#ENTRYPOINT []