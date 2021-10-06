# Issue-Tracker App

 This is an issue tracking App that allows bug tracking and agile project management.
Issue-Tracker written in java and uses Spring Framework for backend and uses Mysql as database and Junit for Testing.

After registration you can create your projects and teams then add your coworkers to them, you can define bugs with different priorities and assigned them to anyone you want. 

## Running Reservation-System  localy
issue-tracker is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:


```
git clone https://github.com/zmilad97/issue-tracker.git
cd issue-tracker
./mvnw package
java -jar target/*.jar
```

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```

## In case you find a bug/suggested improvement for Issue-Tracker
Our issue tracker is available here: https://github.com/zmilad97/issue-tracker/issues



## Database configuration

In its default configuration, Issue-Tracker uses a MySql database on 3306 port which you must
create on your own and name it bugtracker.
