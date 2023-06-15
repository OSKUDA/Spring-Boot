# Login-Registration REST API
This project implements a secure and efficient Login/Registration REST API using the popular Spring Boot framework. It provides a robust and flexible solution for managing user authentication and registration processes in your application using JWT authentication.


## Tools and Technologies Utilized

- SpringBoot
- SpringSecurity
- JSON Web Tokens (JWT)
- Sprint Data JPA
- Hybernate
- JUnit, Mockito, and RestAssured

# API Endpoints

Following endpoints are available

- GET "/api/v1/auth/verifyEmail"

- GET "/api/v1/auth/resendEmailVerification"

- GET "/api/v1/auth/forgetPassword"

- GET "/api/v1/auth/resendForgetPasswordEmail"

- POST "/api/v1/auth/register"

- POST "/api/v1/auth/authenticate"

- POST "/api/v1/auth/changePassword"

- POST  "/api/v1/auth/resetPassword"

# Prerequisites

Before getting started with the Login-Registration REST API application, ensure that you have the following prerequisites installed:

1. **Java Development Kit (JDK):** Ensure that you have Java JDK 17 or above installed on your system. You can download the JDK from the official Oracle website or use a package manager specific to your operating system.

2. **Maven:** Maven is a build automation tool used for managing dependencies and building Java projects. Install Maven by following the instructions provided on the Apache Maven website or use a package manager suitable for your operating system.

3. **Database System:** The application requires a compatible database system to store user information. Ensure that you have a MySQL set up and accessible. Make a note of the database connection details such as URL, username, and password.

4. **API Testing Tool:** You will need an API testing tool to interact with the API endpoints. Postman is a popular choice and can be downloaded and installed from the Postman website. Alternatively, you can use cURL or any other API testing tool of your preference.

5. **Text Editor or Integrated Development Environment (IDE):** You should have a text editor or an IDE installed on your system to view and modify the project files. Some popular options include Visual Studio Code, IntelliJ IDEA, and Eclipse.

Once you have these prerequisites fulfilled, you can proceed with the getting started section of the Login-Registration REST API application.

# Getting Started

To get started with the Login-Registration REST API application, follow the steps below:

**1. Clone the Repository**
```
$ git clone <repository_url>
```

**2. Install Dependencies**
```
$ cd login-registration-api
$ mvn install
```

**3. Configure the Application**
- Open the `application.properties` file.
- Modify the database connection settings according to your environment.
- Configure other necessary properties such as JWT secret key, token expiration time, etc.

**4. Build and Run the Application**
```
$ mvn spring-boot:run
```

**5. Explore the API Endpoints**
- Once the application is up and running, you can explore and test the available API endpoints using a tool like cURL or an API testing tool such as Postman.

**6. Register a User**
- Send a `POST` request to `/api/v1/auth/register` endpoint with the user details (username, email, password) in the request body.
- Upon successful registration, the API will return a response indicating the user registration was successful.

**7. Authenticate a User**
- Send a `POST` request to `/api/v1/auth/authenticate` endpoint with the user's credentials (username and password) in the request body.
- If the credentials are valid, the API will respond with a JWT token.

**8. Access Protected Endpoints**
- To access the protected endpoints, include the obtained JWT token in the `Authorization` header of your subsequent requests.
- The header should be in the format: `Authorization: Bearer <jwt_token>`.

**9. Change Password**
- Send a `POST` request to `/api/v1/auth/changePassword` endpoint with the user's current password and the new password in the request body.
- The API will verify the user's current password and update it with the new password.

**10. Reset Forgotten Password**
- Send a `POST` request to `/api/v1/auth/resetPassword` endpoint with the user's email address in the request body.
- The API will send an email with a password reset link to the user's registered email address.

By following these steps, you will have the Login-Registration REST API application up and running. You can register users, authenticate them, and perform password-related operations. Make sure to review the API documentation for more detailed information on the available endpoints and their request/response structures.

**Note:** This project is licensed under the MIT License. Please review the license file for more information.
