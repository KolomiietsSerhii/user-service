# user-service
Example project for Spring Boot by Serhii Kolomiiets

Repo has mvn wrapper, so in order to launch this, you need have Java 1.8 and Docker installed on your machine. To build and run use:

#####mvn clean install

#####java -jar ./user-service-core/target/user-service-core-0.0.1-SNAPSHOT.jar

Alternatively, if you not have Docker installed on current machine, you may skip tests:

#####mvn clean install -DskipTests

After service start, proceed to #####[host]:8080/user-service/swagger-ui.html#/ in order to see Swagger UI page 

#Project infrastructure overview
![Alt text](Infrastructure.png?raw=true "Infrastructure")
