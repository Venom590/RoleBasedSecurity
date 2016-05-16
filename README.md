# RoleBasedSecurity

[![Build Status](https://travis-ci.org/lukaswerner/RoleBasedSecurity.svg?branch=master)](https://travis-ci.org/lukaswerner/RoleBasedSecurity)

This is a library for [spring](https://spring.io/sp) applications to provide role based security.

At the moment this project is under development, what means, that you should
not yet use it in a productive environment.

Besides the library there is also a test application as a reference
implementation of the role concept. This is a [Spring Boot](http://projects.spring.io/spring-boot/) application with an
embedded [H2 database](http://www.h2database.com/html/main.html). You can start this application out-of-the-box.

The whole concept builds on [Spring Security](http://projects.spring.io/spring-security/).

## Dependencies
You just need [Maven](http://maven.apache.org/) to build the project.

## Build
- Navigate to the root directory
- Execute `mvn install -DskipTests` to build jars

You can now find the test application jar in *rbs-test-app/target/rbs-test-app-X.Y.jar*
and the library jar in *rbs-lib/target/rbs-lib-X.Y.jar*.

## Test
- Use your favorite tool to execute [JUnit](http://junit.org/junit4/) tests or
simply execute `mvn test` command to unit test the application(s).

## Test App
Please consider, that the embedded database in the test app is an in-memory database,
which means, that you shouldn't use important data in this app unless you change
the underlying data source to any persistant one.

## Release Notes

### v0.2 (2016-05-17)
- Created custom error page
- Changed in-memory login to database login
- Logout wasn't possible (fixed)
- Beautified test app
- Improved the database structure and test data
- Made users, roles and objects manageable

### v0.1 (2016-05-03)
- Created maven project
- Created basic login app
- Integrated [Travis CI](https://travis-ci.org/)
