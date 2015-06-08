# Micro-services prototype
A prototype with dropwizard which demonstrates attributes of microservices.

## Feature Set and Providers

* REST Endpoint Documentation: [Swagger](http://swagger.io/)
* Service Container: [DropWizard](http://dropwizard.io/)
* Dynamic Configuration: [Archaius](https://github.com/Netflix/archaius)
* Monitoring: [NewRelic](http://newrelic.com/)
* Circuit Breaker: [Hystrix](https://github.com/Netflix/Hystrix)
* Logging: [SLF4j](http://www.slf4j.org/)

## Services
There are two services here 

* **Person Service**: It has REST endpoints to create (POST) and find a person by id (GET). The person service connects to the address service to get an address for the person. All created Person entries are cached in an in-memory map.
* **Address Service**: The address service provides ability to create (POST) and find an address by id (GET). The address service is built to return sporadic errors, for every 10 requests, the service responds as follows
	* request-1: 200
	* request-2: 404
	* request-3: delayed response (delay of 1s) 
	* request-4: 200
	* request-5: 200
	* request-6: 503
	* request-7: 503
	* request-8: 404
	* request-9: 200
	* request-10: 200 (after which it resets to start back at 1)

## Pre-requisites
You need the following to exercise these samples.

* JDK 1.7 or higher
* Apache Maven version 3.2.5 or higher
* New Relic SDK version 3.14.x if you want to see metrics

## Setup and Build Steps

### Setup
If you have NewRelic account, download the newrelic zip file, see instructions here [Accessing NewRelic Java SDK](https://docs.newrelic.com/docs/agents/java-agent/custom-instrumentation/java-agent-api#access).

Once you have the newrelic zip file downloaded, unzip the contents into the person service such that newrelic directory is a sibling to the pom.xml.

### Build
To build the person and address service do the following

* mvn clean package at the folder where the pom.xml exists

### Setup Configuration 
Copy the person.properties and address.properties into ${user.home}/config-root/person/person.properties and ${user.home}/config-root/address/address.properties where `user.home is your home folder`.


## Running the samples
To exercise the samples, do the following

### Start the Address Service
You can start the address service by using the run.sh shell script. The address service by default starts at port 9100

### Create Address Entry
You can create an address using the following payload to the url

```
curl -d '{"id": 1, "street": "my street", "city": "fremont", "state": "california","zipCode": "94536"}' http://localhost:9100/address -H "Content-Type: application/json"

```
### Fetch an Address
You can fetch an address using the following url

```
curl -G http://localhost:9100/address/1 -H "Accept: application/json"

```

### Start the Person Service
You can start the address service by using the run-metrics.sh (if you have a new relic account) else use run.sh shell script. The address service by default starts at port 7100

### Create Person Entry
You can create a person entry using the following payload to the url

```
curl -d '{"id": 1,"firstName": "John","lastName": "Doe","age": 100}' http://localhost:7100/address -H "Content-Type: application/json"

```

### Fetch an Person Entry
You can fetch a person using the following url

```
curl -G http://localhost:7100/person/1 -H "Accept: application/json"

```

## Dynamic Configuration in action
* With the address service running, add an address and retrieve it. The second call should return a 404. 
* Now go to the address.properties located at `${user.home}/config-root/address/address.properties` and change the text for **404**. 
* Rerun the GET calls and on the next **404** you should see the updated message.

## Circuit Breaker
* With both Person and Address services running make calls to the Person service to create a Person
* Call GET Person and you should see that a few responses do not have an address. See below

**With Address**

```

{"firstName":"John","lastName":"Doe","message":"","id":1,"age":100,"address":{"id":1,"street":"my street","city":"fantastic-city","state":"NY","zipCode":"00000"}}

```

**Without Address**

```
{"firstName":"John","lastName":"Doe","message":"","id":1,"age":100}
```

* Now go to the Person configuration under ```${user.home}/config-root/person/person.configuration``` and change the property ```useFailSafe``` to **true**
* Kill the Address service and make sure it is no longer running
* Now make the same invokes and you should consistently see the address in the person GET response even if you shutdown the Address Service. What happens here is that whenever the HTTP Client experiences an exception the circuit trips and the fallback of fetch from cache kicks in.
