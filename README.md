# KantorDocker - Dockerized Currency Exchange Application

This project is a Dockerized currency exchange application built with Spring Boot for the back-end, MongoDB for the database, and a Vue front-end.  <br> The project demonstrates how to use Docker to containerize a full-stack application. <br>
It was developed as part of a university course at the **Politechnika Łódzka.** <br>
**Fetching Currency Rates** <br>
The application includes a mechanism to fetch currency rates from an external API. The rates are fetched both at startup and daily at midnight. 
> **Note**: The application interface is in Polish.
## Prerequisites

Make sure you have the following installed on your machine:

- Docker
- Docker Compose

## Setup

### Clone the repository

```sh
git clone https://github.com/thejerremi/Kantor-dockerized.git
cd Kantor-dockerized
```
### Environment Variables
Create a .env file in the root directory of the project with the following variables:

```
# MongoDB
MONGO_INITDB_ROOT_USERNAME=yourMongoUsername
MONGO_INITDB_ROOT_PASSWORD=yourMongoPassword

# Mongo Express
ME_CONFIG_MONGODB_ADMINUSERNAME=yourMongoUsername
ME_CONFIG_MONGODB_ADMINPASSWORD=yourMongoPassword
ME_CONFIG_BASICAUTH_USERNAME=admin
ME_CONFIG_BASICAUTH_PASSWORD=pass
ME_CONFIG_MONGODB_SERVER=mongoDB
```
> Note: The .env file included in this repository does not contain any sensitive data and is provided solely for demonstration purposes to showcase example security configurations.

### Docker Compose
The docker-compose.yml file defines the following services:

- mongoDB: A MongoDB instance initialized with the specified username and password.
- mongo-express: A web-based MongoDB admin interface.
- springboot: The Spring Boot application.
- front-end: The Vue front-end application.
```yaml
services:
  mongoDB:
    image: mongo
    container_name: mongoDB
    environment:
      MONGO_INITDB_ROOT_USERNAME: ${MONGO_INITDB_ROOT_USERNAME}
      MONGO_INITDB_ROOT_PASSWORD: ${MONGO_INITDB_ROOT_PASSWORD}
    ports:
      - "27017:27017"
    volumes:
      - ./mongoDB:/data/db
    networks:
      - mongo-net
  mongo-express:
    image: mongo-express
    container_name: mongo-express
    depends_on:
      - mongoDB
    environment:
      ME_CONFIG_MONGODB_ADMINUSERNAME: ${ME_CONFIG_MONGODB_ADMINUSERNAME}
      ME_CONFIG_MONGODB_ADMINPASSWORD: ${ME_CONFIG_MONGODB_ADMINPASSWORD}
      ME_CONFIG_BASICAUTH_USERNAME: ${ME_CONFIG_BASICAUTH_USERNAME}
      ME_CONFIG_BASICAUTH_PASSWORD: ${ME_CONFIG_BASICAUTH_PASSWORD}
      ME_CONFIG_MONGODB_SERVER: ${ME_CONFIG_MONGODB_SERVER}
    ports:
      - "8081:8081"
    networks:
      - mongo-net
  springboot:
    build: ./springboot
    depends_on:
      - mongoDB
    ports:
      - "8080:8080"
    volumes:
      - ./springboot/src:/app/src
    working_dir: /app
    command: ["./mvnw", "spring-boot:run"]
    networks:
      - mongo-net
  front-end:
    build: kantor-frontend
    container_name: front-end
    ports:
      - "3000:3000"
    stdin_open: true
    tty: true
    depends_on:
      - springboot
    networks:
      - mongo-net
networks:
  mongo-net:
    driver: bridge
```
### Running the Application
To start the application, run the following command:
```
docker-compose up
```
or
```
docker-compose up -d
```
to start the application in detached mode.

To stop the application, run the following command:
```
docker-compose down
```

This command will build and start all the services defined in the docker-compose.yml file.

The Spring Boot application will be available at http://localhost:8080 <br>
The Vue front-end will be available at http://localhost:3000 <br>
The Mongo Express interface will be available at http://localhost:8081
