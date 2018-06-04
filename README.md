# Accounting

Simple accounting app with RESTful API

The app uses only 1 thread for two reasons:
* it's simple
* most of CPU is used for HTTP stuff (check performance tests)

### Prerequisites
* java 8
* gradle

##

## Build and test

Requires 8000 port to be available for tests

Use standard gradle tasks:
```
gradle build
gradle test
```
## Run as a local app

Requires 8000 port to be available

Start the server:
```
gradle run
```

Make some RESTful calls:
```
langostinko@langostinko-osx[~]:curl 'localhost:8000/ping'
pong
langostinko@langostinko-osx[~]:curl 'localhost:8000/create?id=my_account'
ok
langostinko@langostinko-osx[~]:curl 'localhost:8000/get_count?id=my_account'
0
langostinko@langostinko-osx[~]:curl 'localhost:8000/get_count?id=his_account'
com.langostinko.account.account.AccountNotFoundException
```

See more examples at src/test/java/com/langostinko/account/ServerTest.java
