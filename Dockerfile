FROM openjdk:21-jdk-oracle


CMD ls # "exec" format
COPY  build/libs/currency-converter-1.0.0-SNAPSHOT.jar /app/app.jar
WORKDIR /app

#CMD java -Xmx1024m -jar /app/app.jar
#ENTRYPOINT []