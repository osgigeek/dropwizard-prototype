package com.sandeep.prototypes.dropwizard.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sandeep.prototypes.address.entity.Address;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "A Person")
public class Person {

  private String firstName;
  private String lastName;
  private String message;
  private int id;
  private int age;

  private Address address;

  public Person() {}

  @JsonProperty
  public String getFirstName() {
    return firstName;
  }

  @JsonProperty
  @ApiModelProperty(value = "First Name", required = true)
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @JsonProperty
  public String getLastName() {
    return lastName;
  }

  @JsonProperty
  @ApiModelProperty(value = "Last Name", required = true)
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @JsonProperty
  public Integer getId() {
    return id;
  }

  @JsonProperty
  @ApiModelProperty(value = "Person Id", required = true)
  public void setId(Integer id) {
    this.id = id;
  }

  @JsonProperty
  public Integer getAge() {
    return age;
  }

  @JsonProperty
  @ApiModelProperty(value = "Person Age", required = true)
  public void setAge(Integer age) {
    this.age = age;
  }

  @JsonProperty
  public void setMessage(String message) {
    this.message = message;
  }

  @JsonProperty
  public String getMessage() {
    return message;
  }

  @JsonProperty
  public Address getAddress() {
    return address;
  }

  @JsonProperty
  public void setAddress(Address address) {
    this.address = address;
  }
}
