## Environment
- Java version: 17
- Maven version: 3.*
- Spring Boot version: 3.0.6

## Read-Only Files:
- src/test/*

## Data:
Example of a flight data JSON object:
```
{
   "id": 1,
   "flight": "MH17"
   "origin": "Malesiya",
   "destination": "China",
   "speed_series": [200, 350, 400, 500, 650, 740, 600]
}
```

## Requirements:
The REST service must expose the /flight endpoint, which allows for managing the collection of flight records in the following way:


POST request to `/flight`:

- creates a new flight data record
- expects a valid flight data object as its body payload, except that it does not have an id property; you can assume that the given object is always valid
- adds the given object to the collection and assigns a unique integer id to it
- the response code is 201 and the response body is the created record, including its unique id


GET request to `/flight`:

- the response code is 200
- the response body is an array of matching records, ordered by their ids in increasing order
- accepts an optional query string parameter, origin, for example /flight/?origin=KTM. When this parameter is present, only the records with the matching origin are returned.
- accepts an optional query string parameter, orderBy, that can take one of two values: either "destination" or "-destination". If the value is "destination", then the ordering is by destination in ascending order. If it is "-destination", then the ordering is by date in descending order. If there are two records with the same destination, the one with the smaller id must come first.

GET request to `/flight/<id>`:

- returns a record with the given id
- if the matching record exists, the response code is 200 and the response body is the matching object
- if there is no record in the collection with the given id, the response code is 404

## Commands
- run: 
```bash
mvn clean spring-boot:run
```
- install: 
```bash
mvn clean install
```
- test: 
```bash
mvn clean test
```