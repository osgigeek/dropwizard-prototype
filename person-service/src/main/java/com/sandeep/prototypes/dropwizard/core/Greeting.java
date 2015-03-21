package com.sandeep.prototypes.dropwizard.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Greeting {
  private final String name;
  private final String lastName;
  private final String greeting;

  public Greeting(String name, String lastName, String greeting) {
    this.name = name;
    this.lastName = lastName;
    this.greeting = greeting;
  }

  @JsonProperty
  public String getName() {
    return name;
  }

  @JsonProperty
  public String getLastName() {
    return lastName;
  }

  @JsonProperty
  public String getGreeting() {
    return greeting;
  }
}
