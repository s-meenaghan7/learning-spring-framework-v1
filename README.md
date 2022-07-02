# learning-spring-framework-v1
A project created with the purpose of learning the Spring framework, with Spring Boot, hands-on.

### Why I created this application:
The intention behind this project is to learn everything I can about using the Spring framework modules to build web applications, including Spring Web, Spring MVC, JPA & Hibernate, testing Spring applications, and to progressively implement more modules such as Spring Security.

### Summary:
This is a simple CRUD application I created to give myself a better understanding of the Spring framework using Spring Boot. I had completed some free tutorials on YouTube that gave me good exposure to the framework, but following along with somebody else and typing the same code they do only does so much to build your own understanding; I wanted to really establish an understanding of the framework by making mistakes and learning from those mistakes. Naturally, I decided I would create my own project using Spring without the help of a tutorial.

My first idea for a project was a little too ambitious up-front, so I decided it would be better to start small and build my way up, implementing different parts of the Spring framework as I go.

### What does it do?
Currently, this application is a simple REST API, facilitating ```@RestController```s for creating, reading, updating, and deleting Person objects. I used Postman to test these HTTP requests, and later created JUnit tests for unit and integration testing. The application has 3 layers composed of the Controller, Service, and Repository layers. The Repository class extends the JpaRepository class and is configured to work with a PostgreSQL database instance. JPA and Hibernate made interacting with the database seamless and easy. The Service layer class is responsible for the business logic of the application. The Controller layer class handles the HTTP requests directly using Spring and implements the methods of the Service layer.

### TODO (currently in no particular order):
- Finish tests.
- Create a front-end for the application; currently planning on using Reactjs and Tailwind CSS (again: for learning purposes!).
- Utilizing Spring Security, allow users to create accounts (sign-up), login to the application, and interact with the API directly through the browser.