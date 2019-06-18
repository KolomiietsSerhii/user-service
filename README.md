# User-Service
Example project for Spring Boot by Serhii Kolomiiets

Repo has mvn wrapper, so in order to launch this, you need have Java 1.8 and Docker installed on your machine. By default service expect DB properties from Config Server. Without them it will run with default H2 database. But I'm using MySQL container in tests.
To build and run use:

##### mvn clean install

##### java -jar ./user-service-core/target/user-service-core-0.0.1-SNAPSHOT.jar

Alternatively, if you not have Docker installed on current machine, you may skip tests:

##### mvn clean install -DskipTests

After service start, proceed to [host]:8080/user-service/swagger-ui.html#/ in order to see Swagger UI page 

# User-Service overview

User service serves to store and retrieve info about registered users. Service controller has two main endpoints:

##### POST /users

##### GET /users/{userId}

More info about DTO you may find on Swagger page. Most important - if field "isSaved" == false in response from POST /users, that means that user with given email was saved before, and all another fields in this answer corresponds to this user.

Apart from that User Service has module "user-service-client", that may be downloaded as dependency, and used by other services in system in order to communicate with User Service.

Module "user-service-api" also may be used as dependency in order to know and understand data format. All main implementations (service, controller, etc) implements interface UserService, that declared in API. It is guarantee that service user will receive data in format, that declared in API module.

Nice to have but not implemented: more tests for God Of Tests, dockerfile and ability to run this project from container, extended telemetry.

# Project infrastructure overview

![Alt text](https://github.com/KolomiietsSerhii/user-service/blob/master/Infrastructure.PNG?raw=true "Infrastructure")

##### API Gateway
Compose all internal APIs, route user between services after login/registration process.

##### Eureka with Config Server
Helps services to communicate with each other. Register/deregister new instances and service replicas. Config Server - key/value properties storage for all services in infrastructure. No needs to store sensitive setting in service repos.

##### User Service
Maintain users profiles

##### Group Service
Maintain users groups

##### Chat Service
Main business logic of project

##### Login Service
Since security may be complicated, it is good to extract this aspect into separated service, that has it's own database. API Gateway must check every user request using this service.

##### Artifactory
Stores modules and artifacts from all our services. It is good to have ability to import API or another stuff with build tool, without copy-and-paste. Also we always may pick proper version, if for some reason "latest" is not suitable.