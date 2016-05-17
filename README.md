# RoleBasedSecurity

[![Build Status](https://travis-ci.org/lukaswerner/RoleBasedSecurity.svg?branch=master)](https://travis-ci.org/lukaswerner/RoleBasedSecurity)

This is a reference implementation of a role based security model.
It utilizes [Spring Boot](http://projects.spring.io/spring-boot/) and an embedded
[H2 database](http://www.h2database.com/html/main.html). You can start this application out-of-the-box.

At the moment this project is under development, what means, that you should
not yet use it in a productive environment.

The whole concept builds on [Spring Security](http://projects.spring.io/spring-security/).

## Dependencies
You just need [Maven](http://maven.apache.org/) to build the project.

## Build
- Navigate to the root directory
- Execute `mvn install -DskipTests` to build jars

You can now find the application jar in *target/role-based-security-X.Y.jar*.

## Test
- Use your favorite tool to execute [JUnit](http://junit.org/junit4/) tests or
simply execute `mvn test` command to unit test the application(s).

## Test App
Please consider, that the embedded database in this test app is an in-memory database,
which means, that you shouldn't use important data in this app unless you change
the underlying data source to any persistant one.

## Release Notes

### v0.1 (2016-05-03)
- Created maven project
- Created basic login app
- Integrated [Travis CI](https://travis-ci.org/)
