### Running Locally

> **Note:** You will need to finish the Cognito Setup guide, and Create S3 Bucket Setup before this guide.
> 
1. **Clone the Repository**

* Open a terminal and run the following command to clone the repository:

   ```bash
   git clone Distansakademin/cloudutveckling-spring-Arinsz
   ```
   ```bash
  cd cloudutveckling-spring-Arinsz
   ```

* Alternatively, you can open the project in IntelliJ or Visual Studio.


2. **Configure the Database**

* Install MySql and create a Database.
* Update the application.properties file with your MySQL database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yourDB
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=create-drop
```


4. **Build and Run the Application**
   ```bash
   mvn clean install
   ```
5. **After the build completes successfully, you can run the Spring Boot application using:**
   ```bash
   mvn spring-boot:run
   ```

* Once the build is successful, the application should be up and running.
