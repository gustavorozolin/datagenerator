# datagenerator
Generate insert script for tables of database (only SQL Server for now, if you want improve adding others database welcome to the project)

## Build
```sh
$ gradle build
```
## Run
```sh
$ java -jar build/libs/datagenerator-1.0-SNAPSHOT.jar db=jdbc:sqlserver://<yourHost>;DatabaseName=DB_NAME;integratedSecurity=true;SelectMethod=cursor; user=<yourDbUser> password=<yourPassword> rows=5
```
just "db" parameter is requered

## What's generated?
If your database have only a table called "country" this command will generate a file "country.sql" with 5 insert script rows.

country.sql
```sh
INSERT INTO COUNTRY ( ID, NAME) VALUES  ( 1,'1 NAME')
INSERT INTO COUNTRY ( ID, NAME) VALUES  ( 2,'2 NAME')
INSERT INTO COUNTRY ( ID, NAME) VALUES  ( 3,'3 NAME')
INSERT INTO COUNTRY ( ID, NAME) VALUES  ( 4,'4 NAME')
INSERT INTO COUNTRY ( ID, NAME) VALUES  ( 5,'5 NAME')
```

## Datatypes

| Datatype | Supported |
| ------ | ------ |
| varchar | ✅ |
| char | ✅ |
| int | ✅ |
| datetime | ✅ |
| other | It will generate null value for column with the other types |

## Collaborate
All help is welcome :D
