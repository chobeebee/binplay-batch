# Java 21이 포함된 기본 이미지를 사용
FROM openjdk:21-jdk

# 작업 디렉토리를 설정
WORKDIR /app

# JAR 파일을 복사
COPY build/libs/binplaybatch-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션이 실행되는 포트를 외부에 노출
EXPOSE 8080

# JAR 파일을 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
