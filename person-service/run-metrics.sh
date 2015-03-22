java -javaagent:./newrelic/newrelic.jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8888,suspend=n -jar target/person-service-1.0-SNAPSHOT.jar server person.yaml 
