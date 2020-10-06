# springDBProject - application exposing RESTful API
API includes data from local database containing console games data.
Application is build using Spring Boot. Data access using Spring Data JPA repositories. Endpoints are exposed using Spring MVC REST controllers. To fullfill REST requirements Spring HATEOAS was used for links expose. Request bodies are validated using Hibernate Validator.

# Installation
Run executable JAR (Spring Boot) and connect consuming application. Backend permits requests (CORS) coming from 127.0.0.1:5500 <b>only</b> (look in configuration class).
