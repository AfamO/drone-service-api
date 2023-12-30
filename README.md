## Drones

[[_TOC_]]

---

### Introduction

There is a major new technology that is destined to be a disruptive force in the field of transportation: **the drone**. Just as the mobile phone allowed developing countries to leapfrog older technologies for personal communication, the drone has the potential to leapfrog traditional transportation infrastructure.

Useful drone functions include delivery of small items that are (urgently) needed in locations with difficult access.

---

### Task description

We have a fleet of **10 drones**. A drone is capable of carrying devices, other than cameras, and capable of delivering small loads. For our use case **the load is medications**.

A **Drone** has:
- serial number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has: 
- name (allowed only letters, numbers, ‘-‘, ‘_’);
- weight;
- code (allowed only upper case letters, underscore and numbers);
- image (picture of the medication case).

Develop a service via REST API that allows clients to communicate with the drones (i.e. **dispatch controller**). The specific communicaiton with the drone is outside the scope of this task. 

The service should allow:
- registering a drone;
- loading a drone with medication items;
- checking loaded medication items for a given drone; 
- checking available drones for loading;
- check drone battery level for a given drone;

> Feel free to make assumptions for the design approach. 

---

### Requirements

While implementing your solution **please take care of the following requirements**: 

#### Functional requirements

- Prevent the drone from being loaded with more weight that it can carry;
- Prevent the drone from being in LOADING state if the battery level is **below 25%**;
- Introduce a periodic task to check drones battery levels and create history/audit event log for this;
- There is no need for UI.

---

#### Non-functional requirements

- The project must be buildable and runnable;
- The project must have Unit tests;
- The project must have a README file with build/run/test instructions (use a DB that can be run locally, e.g. in-memory, via container);
- Any data required by the application to run (e.g. reference tables, dummy data) must be preloaded in the database;
- Input/output data must be in JSON format;
- Use a framework of your choice, but popular, up-to-date, and long-term support versions are recommended.

---


:scroll: **START**

## Assumptions:

- First Assumption: The medication **image** is uploaded and a service handles it. Alternatively, it could be done at the front-end with javascript file API, then converted with base64 encoding, send the resultant json object to a java spring controller  which converts it to a LOB and save it to DB,after converting it to standard java byte[]. Similarly, a another spring controller can help retrieve the image by streaming the bytes directly.

- Second Assumption: Validation and testing for empty and null values inside Validation.java class. I also assumed that other input such as state, model, medication code and name have all been properly validated perhaps with correct regex.

- Third Assumption: I implemented integration test, here I assumed that the integration tests ought have taken care of concrete and individual unit tests.

- Fourth Assumption: I configured the periodic audit event logs to occur every 15 seconds. In reality, it might be longer.


## Commands to build,run, install and test the service.
### Using Docker(there is a Dockerfile in the project folder):

- First: Build the Dockerfile: 
```
docker build --tag=drone-service:latest .
```

- Second: Startup and run the docker image on port 8080: 
```
docker run -p8080:8080 drone-service:latest
```


### Using Traditional Java and Maven Command:

 
#### Ensure JAVA_HOME is properly defined

- run: 
```bash
mvn clean package; java -jar target/demo-0.0.1-SNAPSHOT.jar
```
- install: 
```bash
mvn clean install
```
- test: 
```bash
mvn clean test
```

#### How to consume and test the api/service endpoint
##### Some dummy data were preloaded to the h2 database on startup

- Sample Request Payload for Drone Registeration: 

```
Http Method: POST

{
    "serialNumber": "151",
    "model": "HEAVY_WEIGHT",
    "weightLimit": 800,
    "batteryCapacity": 0.6,
    "state": "IDLE"
}
Endpoint: http://localhost:8080/drones/api/register

Sample Response:
{
    "httpStatus": 201,
    "message": "Successfull",
    "data": {
        "id": 5,
        "serialNumber": "151",
        "model": "HEAVY_WEIGHT",
        "weightLimit": 400,
        "batteryCapacity": 0.6,
        "state": "IDLE"
    }
}
```

- Sample Request Payload for Loading a drone with medications: 
```
Http Method: PUT

{
    "droneId": 3,
    "medicationIds": [2,3]
}
Endpoint: http://localhost:8080/drones/api/load/MedicationItems

Sample Response:
{
    "httpStatus": 200,
    "message": "Successfull",
    "data": {
        "drone": {
            "id": 3,
            "serialNumber": "131",
            "model": "MIDDLE_WEIGHT",
            "weightLimit": 300,
            "batteryCapacity": 0.8,
            "state": "LOADED"
        },
        "medicationItems": [
            {
                "id": 2,
                "name": "Panadol",
                "weight": 100.8,
                "code": "PA-31",
                "image": "pan.jpg",
                "hibernateLazyInitializer": {}
            },
            {
                "id": 3,
                "name": "Detol",
                "weight": 150.5,
                "code": "DET_20-D",
                "image": "det.jpg",
                "hibernateLazyInitializer": {}
            }
        ]
    }
}
```
- Sample Request Payload for checking loaded medications Item for a given drone: 

```

Http Method: GET

Endpoint: http://localhost:8080/drones/api/check/MedicationItems/2

Sample Response:
{
    "httpStatus": 404,
    "message": "The Drone with id '2' has not been loaded",
    "data": null
}
```
- Sample Request Payload for checking available drones for loading: 

```

Http Method: GET

Endpoint: http://localhost:8080/drones/api/check/AvailableDronesForLoading

Sample Response:
{
    "httpStatus": 200,
    "message": "Successfull",
    "data": [
        {
            "id": 1,
            "serialNumber": "111",
            "model": "LIGHT_WEIGHT",
            "weightLimit": 100,
            "batteryCapacity": 0.1,
            "state": "IDLE"
        },
        {
            "id": 2,
            "serialNumber": "121",
            "model": "HEAVY_WEIGHT",
            "weightLimit": 500,
            "batteryCapacity": 0.2,
            "state": "IDLE"
        },
        {
            "id": 4,
            "serialNumber": "141",
            "model": "CRUISER_WEIGHT",
            "weightLimit": 500,
            "batteryCapacity": 0.6,
            "state": "IDLE"
        },
        {
            "id": 5,
            "serialNumber": "151",
            "model": "HEAVY_WEIGHT",
            "weightLimit": 400,
            "batteryCapacity": 0.6,
            "state": "IDLE"
        }
    ]
}

```
- Sample Request Payload for checking drone battery level for a given drone: 

```

Http Method: GET

Endpoint: http://localhost:8080/drones/api/check/batteryLevel/2

{
    "httpStatus": 200,
    "message": "Successfull",
    "data": 0.2
}
NB: Here, the battery level is 0.2

```

:scroll: **END** 
