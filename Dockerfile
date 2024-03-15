FROM openjdk:17-jdk-alpine
COPY build/libs/laskomed.jar laskomed.jar
ENTRYPOINT ["java","-jar","/laskomed.jar"]