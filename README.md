# Integrated project - Backend
Term Paper

## Team
* [Humberto](https://github.com/hmberto)
* [Vinicius](https://github.com/vinimelo92)
* [Lucas](https://github.com)
* [Allan](https://github.com)

| Repository | URL |
|--- |--- |
| projeto-integrador | https://github.com/hmberto/projeto-integrador |

## Java API. Maven Jetty plugin
A REST API service to provide data for the front-end application.

## How it Works
The back-end service provides the front-end with data stored in a MySQL database. The data is requested as needed and sent to the client in json object format.

## Routes
The resource provides routes to Sing In, register and find items registered in the database.

### Variables:
In **./properties/data.properties** correctly fill your SQL database connection data. You must also define the queries for SELECT and INSERT into the database.

### Running the back-end service
~~~
mvn package jetty:run
~~~

## Why? 
This project is a team work and was created as a term paper for learning purposes only. Also, you can use this Project as you wish.

## Built With
* [Java](https://www.java.com/) - v1.8
* [Maven Jetty Plugin](https://wiki.eclipse.org/Jetty/Feature/Jetty_Maven_Plugin) - v6.1.26
