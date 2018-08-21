# spring-boot-employees-app

This is a sample application for users to manage employee information via a REST API. The following operations are supported:

* Get employee by an ID
* Create new employees
* Update existing employee
* Delete employee
* Get all employees

Please refer to the Example Requests and Documentation sections below for more information about how to
use these endpoints.

This application comes preloaded with a few sample records that can be used to quickly test endpoints.
Data is persisted in memory using [H2](http://h2database.com/html/main.html), so restarting the server will reset the database.

### Running application
After cloning this repo, you can use the included maven wrapper to run the server by calling the following command in the project's root directory:
```
./mvnw clean spring-boot:run
```

The application can also be run using the included Dockerfile. Once you have [Docker](https://www.docker.com/) installed,
you can run the following commands in the project's root directory to start the application:
```
# Build a tagged docker image
docker build -t employees-app

# Run the tagged image
docker run -p 8080:8080 -t employees-app
```

Both of the above methods will make the server available at http://localhost:8080/employees.

### Example requests
A [postman](https://www.getpostman.com/) collection has been provided in the repo's docs folder to demonstrate some example requests.
You can install postman and [import the collection](https://www.getpostman.com/docs/v6/postman/collections/data_formats#exporting-and-importing-postman-data)
 to use these examples.

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
