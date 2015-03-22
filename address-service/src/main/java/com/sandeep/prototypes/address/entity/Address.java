package com.sandeep.prototypes.address.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Model class for Address, this is also the response which is serialized out to the client
 * </p>
 * 
 * @author Sandeep Nayak
 *
 */
public class Address {
  private int id;
  private String street;
  private String city;
  private String state;
  private String zipCode;

  @JsonProperty
  public int getId() {
    return id;
  }

  @JsonProperty
  public void setId(int id) {
    this.id = id;
  }

  @JsonProperty
  public String getStreet() {
    return street;
  }

  @JsonProperty
  public void setStreet(String street) {
    this.street = street;
  }

  @JsonProperty
  public String getCity() {
    return city;
  }

  @JsonProperty
  public void setCity(String city) {
    this.city = city;
  }

  @JsonProperty
  public String getState() {
    return state;
  }

  @JsonProperty
  public void setState(String state) {
    this.state = state;
  }

  @JsonProperty
  public String getZipCode() {
    return zipCode;
  }

  @JsonProperty
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }
}
