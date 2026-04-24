# Real-Time Chat Backend

Backend service for a real-time chat application built with **Spring Boot**, **WebSocket**, **Kafka**, **Redis**, **JWT Authentication**, and **MySQL/PostgreSQL**.

> ⚠️ **Important:** Clone the `develop-be` branch instead of the default branch.

---

# 🚀 Features

* User authentication with JWT
* Login / Register APIs
* Real-time messaging using WebSocket
* Kafka integration for message streaming
* Redis for caching and online status
* User profile management
* Avatar upload support
* RESTful APIs
* Role-based authorization
* Exception handling and validation
* 1-1 chat support
* Group chat *(Updating ...)*
* OAuth2 Login (Google / GitHub configurable)
* Email sending support (verification / notification)

---

# 🛠️ Tech Stack

* Java 17+
* Spring Boot
* Spring Security
* Spring WebSocket
* Spring Data JPA
* Kafka
* Redis
* MySQL / PostgreSQL
* Maven
* Docker (optional)

---

# 📥 Clone Project

Clone the correct branch:

```bash
git clone -b develop-be https://github.com/justlamvt05/Real-Time-Chat.git
cd Real-Time-Chat
```

Or if you already cloned the repository:

```bash
git checkout develop-be
```

---

# ⚙️ Environment Configuration

You must create and configure your own `application.yml` or `application.properties`.

Example:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/realtime_chat
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: realtime-chat-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring:
          json:
            trusted:
              packages: "*"
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

  data:
    redis:
      host: localhost
      port: 6380
      database: 0

jwt:
  secret: your-secret-key
  expiration: 86400000

cloudinary:
  cloud-name: your-cloud-name
  api-key: your-api-key
  api-secret: your-api-secret

mail:
  host: smtp.gmail.com
  port: 587
  username: your-email@gmail.com
  password: your-app-password
  properties:
    mail:
      smtp:
        auth: true
        starttls:
          enable: true

oauth2:
  google:
    client-id: your-google-client-id
    client-secret: your-google-client-secret
  github:
    client-id: your-github-client-id
    client-secret: your-github-client-secret
```

Example using `application.properties`:

```properties
# Kafka
spring.kafka.bootstrap-servers=localhost:9092

# Consumer
spring.kafka.consumer.group-id=realtime-chat-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*

# Producer
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6380
spring.data.redis.database=0

# Mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# OAuth2 Google
spring.security.oauth2.client.registration.google.client-id=your-google-client-id
spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret

# OAuth2 GitHub
spring.security.oauth2.client.registration.github.client-id=your-github-client-id
spring.security.oauth2.client.registration.github.client-secret=your-github-client-secret
```

---

# 🧩 Required Services

Before running the backend, ensure these services are available:

* MySQL or PostgreSQL
* Kafka
* Zookeeper
* Redis

You can run them locally or using Docker.

---

# 🐳 Docker Setup (Optional)

Example Docker services:

```bash
docker run -d --name redis -p 6379:6379 redis
```

Kafka + Zookeeper can be started using Docker Compose.

---

# ▶️ Run Project

Using Maven:

```bash
./mvnw spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

---

# 📂 Project Structure

```text
src/main/java
├── config
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
├── security
├── service
```

---

# 🔐 Authentication

JWT authentication is used.

Typical flow:

1. Register account
2. Login
3. Receive JWT token
4. Add token to request header:

```text
Authorization: Bearer your_token
```

---

# 📡 WebSocket Endpoint

Example:

```text
/ws
```

Example STOMP destination:

```text
/topic/messages
/app/chat
```

---

# 🧪 API Testing

Recommended tools:

* Postman
* Swagger (if enabled)
* Insomnia

---

# 📌 Notes

* Always use the `develop-be` branch.
* Configure your own database, Redis, Kafka, and Cloudinary credentials.
* Ensure Kafka and Redis are running before starting the application.
* Update application config according to your local environment.

---

# 👨‍💻 Author

Developed by Lam Vuong

GitHub: [https://github.com/justlamvt05](https://github.com/justlamvt05)
