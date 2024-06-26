# REST API Application for User Management

This is a REST API application built with Java version 22 and Spring Boot version 3.2.5. The application provides endpoints for managing users, allowing you to perform CRUD (Create, Read, Update, Delete) operations on user data.

## Features

Create User: Add a new user to the system (POST for endpoint "users/" with body (example data) {
"email": "qwerty@qwerty.com",
"firstName": "fghjk",
"lastName": "xdtcvbn",
"birthDate": "2000-01-10",
"address": "rtfygiuni yibunim",
"phoneNumber": "380999999999"
})

Read User: Retrieve user information by ID (GET for endpoint "users/" to get all users in database, GET for endpoint "users/{id}" to get user with specific id, GET for endpoint "users?from=2000-01-01&to=2005-01-01" to get users with birth date between "from" and "to" dates)

Update User: Update existing user information (PUT for endpoint "users/" with the same structure of body like for creating new one)

Delete User: Remove a user from the system (DELETE for endpoint "users/" to remove all users and DELETE for endpoint "users/{id}" to delete user with specific id)

## Technologies Used

Java 22

Spring Boot 3.2.5

H2 Database (runtime)

### Getting Started
To use this API, follow these steps:

1. Clone the repository to your local machine:
```
git clone https://github.com/anna-maria21/test_task.git
```
2. Navigate to Project Directory: Move into the project directory:
```
cd <project-directory>
```
3. Build and Run Application: Use Maven to build and run the application:
```
   mvn spring-boot:run
```
5. Access API Endpoints: Once the application is running, you can access the API endpoints using a tool like Postman or by sending HTTP requests programmatically.


### Database Configuration
The application uses an H2 in-memory database as the runtime database. You don't need to perform any additional configuration for the database setup. The H2 database console is available at the following URL:
```
http://localhost:8080/console/
```
### Author
Hanna-Mariia Strelchenko - strelchenko2010amg@gmail.com 








