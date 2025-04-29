# Используем базовый образ с Java
FROM openjdk:19-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем war-файл в контейнер
COPY target/web-1.0-SNAPSHOT.jar app.jar

# Запускаем Spring Boot приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
