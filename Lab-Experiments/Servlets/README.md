

# Java Servlet Practical

A Maven-based Java Servlet application demonstrating
Jakarta Servlet 6.1 with Apache Tomcat 11.

## Technologies
- Java 17
- Jakarta Servlet 6.1
- Apache Maven
- Apache Tomcat 11
- HTML5 and CSS3

## Features
- Hello Servlet with GET and POST
- Student registration form
- Login and logout using HttpSession
- Authentication and request logging filters
- Servlet life cycle
- ServletConfig and ServletContext
- Cookie handling
- ServletContextListener and HttpSessionListener

## Demo Login
- Username: admin
- Password: 1234

These credentials are for demonstration only.

## Requirements
- JDK 17 or newer
- Apache Maven
- Apache Tomcat 11

## Build
Run this command from the project root:

    mvn clean package

The generated WAR file is:

    target/servlet.war

## Deploy
1. Copy target/servlet.war into the Tomcat webapps folder.
2. Start Tomcat.
3. Open http://localhost:8080/servlet/

## Application URLs
- /hello
- /register
- /login
- /home
- /logout
- /lifecycle
- /config
- /context
- /cookie

## Notes
Student registration is a demonstration only.
It does not persist records to a database.
The sample login credentials are hard-coded
and must not be used in production.
