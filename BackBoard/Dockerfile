# 베이스 이미지 (openjdk만 있으면 Maven은 따로 설치)
FROM openjdk:17-jdk-slim

# Maven 설치를 위한 환경변수
ENV MAVEN_VERSION=3.9.6
ENV MAVEN_HOME=/usr/share/maven

# 필요한 패키지 설치 + Maven 설치
RUN apt-get update && apt-get install -y curl tar unzip \
    && curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xz -C /usr/share \
    && ln -s /usr/share/apache-maven-${MAVEN_VERSION} /usr/share/maven \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# 작업 디렉토리 생성
WORKDIR /app

# 프로젝트 복사
COPY . .

# 포트 열기
EXPOSE 8080

# ENTRYPOINT ["java", "-jar", "/app.jar"] 
CMD ["sh", "-c", "mvn clean package -DskipTests && java -jar target/*.jar"]