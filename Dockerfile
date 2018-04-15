FROM openjdk:8u92-jre-alpine

EXPOSE 4500
ENTRYPOINT ["java", "-jar", "/home/encoder.jar"]

COPY ./build/libs/encoder.jar, /home/