# Getting Started

### Guide

To run the service locally, just run the command ```docker compose up -d```  from the root directory of the project. It will:
1. Execute Dockerfile
2. Build the project with ```mvn clean package```command
3. Package into docker-image and upload into local storage
4. Then start the services one by one

After all services are up and running, the service can be accessed via [swagger-ui](http://localhost:8080/swagger-ui/index.html) 

It uses the basic auth just for the sake of API audit. Credentials can be found in **src/main/kotlin/com/fancycomp/devicebooking/config/SecurityConfig.kt** file

It also starts [kafka-ui](http://localhost:8090/) to easy access to the topics

API Details
===========

## Device management ##

**Get information about all devices** - ```GET``` -
```http://localhost:8080/api/v1/device/management/all```

**Book a device by its id** - ```PUT``` -
```http://localhost:8080/api/v1/device/management/{id}/book```

**Book a device by device type id** - ```PUT``` -
```http://localhost:8080/api/v1/device/management/device-type/{id}/book```

**Release booked device by its id** - ```PUT``` -
```http://localhost:8080/api/v1/device/management/{id}/release```

Error Handling
=============
- The data validation error code starts with 400
- The device not found error starts with 404
- Other server run time exception is 500

Below are the two sample error scenario response.

```
{
    "errorCode": 400,
    "message": "Device with id $id is already booked"
}
```

```
{
    "errorCode": 404,
    "message": "Not found device with id $id"
}
```
# Database
The SQL migrations are under [Liquibase](https://www.liquibase.org/) control. 
Migrations along with test-data can be found in `src/main/resources/db/changelog` catalog

# Production readiness
This service exposes [actuator](http://localhost:8081/actuator) metrics that could be ready to be collected by [Zipkin](https://zipkin.io/)

It also exposes `livenessstate` and `readinessstate` for orchestration management, for example **k8s**

# Docker Compose support

This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* postgres: [`confluentinc/cp-kafka:7.6.0`](https://hub.docker.com/r/confluentinc/cp-kafka)
* postgres: [`confluentinc/cp-zookeeper:7.6.0`](https://hub.docker.com/r/confluentinc/cp-zookeeper)
* postgres: [`provectuslabs/kafka-ui:v0.7.1`](https://hub.docker.com/r/provectuslabs/kafka-ui)
* postgres: [`postgres:14-alpine`](https://hub.docker.com/_/postgres)

# Tests

Test coverage - **97%** of lines

This project
uses [Testcontainers at development time](https://docs.spring.io/spring-boot/docs/3.2.3/reference/html/features.html#features.testing.testcontainers.at-development-time).

Testcontainers has been configured to use the following Docker images:

* [`postgres:14-alpine`](https://hub.docker.com/_/postgres)
* [`confluentinc/cp-kafka:7.6.0`](https://hub.docker.com/r/confluentinc/cp-kafka)

# Points to consider

* The basic auth was added only to demonstrate that this service can capture the users from API calls
* There's no proper Kafka setup in the service as it may be considered only as a good starting point for decoupling in the future
* Integration tests are heavy by its nature. So in theory they can run separately only in main branch and Unit tests can be parallelized to speed up the pipeline