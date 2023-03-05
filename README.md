# ESM

---
## Dependencies

---
* `javax.servlet-api` -> `3.0.1`
* `org.springframework.spring-core` -> `6.0.5`
* `org.springframework.spring-context` -> `6.0.5`
* `org.springframework.spring-web` -> `6.0.5`
* `org.springframework.spring-webmvc` -> `6.0.5`
* `org.springframework.spring-tx` -> `6.0.5`
* `org.springframework.spring-jdbc` -> `6.0.5`
* `com.fasterxml.jackson.core:jackson-databind` -> `2.14.1`
* `jakarta.servlet-api` -> `6.0.0`
* `com.mysql:mysql-connector-j` -> `8.0.32`
* `org.apache.commons:commons-dbcp2` -> `2.9.0`
* `com.zaxxer:HikariCP` -> `5.0.1`
* `org.junit.jupiter:junit-jupiter-api` -> `5.9.2`
* `org.mockito:mockito-core` -> `4.8.1`
* `org.springframework:spring-test` -> `6.0.5`
* `org.mockito:mockito-junit-jupiter` -> `4.8.1`

---
## Build
```bash
mvn clean package war:war
```

---
## Deployment
To deploy the project, copy the `esm.war` file to the webapps directory of your Tomcat server.

---
## Test
Service layer 80%+ methods 