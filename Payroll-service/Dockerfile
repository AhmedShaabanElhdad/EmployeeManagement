FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY shared shared
COPY Payroll-service Payroll-service

RUN ./gradlew :Payroll-service:assemble -x test

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /workspace/app/Payroll-service/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
