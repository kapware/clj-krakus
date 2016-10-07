# clj-krakus

Krakus story written in functions.

## Usage

### Run the application locally

`lein ring server`

### Packaging and running as standalone jar

```
lein do clean, ring uberjar
java -jar target/server.jar
```

### Packaging as war

`lein ring uberwar`


### Running for developmetn

`lein with-profile dev ring server`

Copyright ©kapware.com
