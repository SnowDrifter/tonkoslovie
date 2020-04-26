FROM bellsoft/liberica-openjdk-alpine:11

RUN apk add --update curl && rm -rf /var/cache/apk/*

COPY target/*.jar root.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/root.jar"]