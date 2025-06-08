# 1. OpenJDK 17 기반 이미지 (AMD64 아키텍처 보장)
FROM --platform=linux/amd64 openjdk:17-jdk-slim

# 2. Python3, pip 설치 (전역 설치)
RUN apt-get update && apt-get install -y \
    python3 python3-pip \
    && rm -rf /var/lib/apt/lists/*

# 3. 작업 디렉토리 지정
WORKDIR /app

# 4. 애플리케이션 관련 파일 복사
# - Spring Boot JAR
# - Python 파일 디렉토리
# - Python 패키지 목록
COPY build/libs/DeepLung-0.0.1-SNAPSHOT.jar .
COPY requirements.txt .
COPY python/ ./python/

# 5. 전역 pip로 Python 패키지 설치
RUN pip3 install --upgrade pip && \
    pip3 install -r requirements.txt

# 6. 서버 포트 오픈
EXPOSE 8080

# 7. Spring Boot JAR 실행
ENTRYPOINT ["java", "-jar", "DeepLung-0.0.1-SNAPSHOT.jar"]