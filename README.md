# spring-boot-employees-app

This is a sample application for users to manage employee information via a REST API. This application comes preloaded with a few
sample records that can be used to quickly test endpoints.

### Running application
After cloning this repo, you can use the included maven wrapper to run the server using the following command:
```
./mvnw clean spring-boot:run
```

The application can also be run using the included Dockerfile. Once you have [Docker](https://www.docker.com/) installed,
you can run the following commands to run the application:
```
# Build a tagged docker image
./mvnw install dockerfile:build

# Run the tagged image
docker run -p 8080:8080 -t employees/employees
```

Both of the above methods will make the server available at http://localhost:8080/employees.

### Example requests
A [postman](https://www.getpostman.com/) collection has been provided in the repo's docs folder for some example requests.
You can install postman and import the collection to begin interacting with this server.

### Documentation
Please refer to the [formal API documentation](http://localhost:8080/swagger-ui.html) after launching the web server to
learn how to interact with the application.

Authorization is only required for the delete API call. To use this call, you must use basic authentication with the
username "user" and a password of "password":

```
curl -X DELETE -u user:password http://localhost:8080/employees/1
```

### Running tests
```
./mvnw clean test
```
