package com.sandeep.prototypes.dropwizard.config;

import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

// Hmm.. this makes little sense it should not import hibernate
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonConfiguration extends Configuration {

  @NotEmpty
  private String message;
  @NotEmpty
  private String version;
  @NotEmpty
  private String docsPath;
  @NotEmpty
  private String applicationTitle;
  @NotEmpty
  private String appDescription;
  @NotEmpty
  private String appTosUrl;
  @NotEmpty
  private String applicationContact;
  @NotEmpty
  private String license;
  @NotEmpty
  private String addressUrl;

  @Valid
  @NotNull
  private HttpClientConfiguration httpClient;

  @JsonProperty
  public String getMessage() {
    return message;
  }

  @JsonProperty
  public void setMessage(String message) {
    this.message = message;
  }

  @JsonProperty
  public String getVersion() {
    return version;
  }

  @JsonProperty
  public void setVersion(String version) {
    this.version = version;
  }

  @JsonProperty
  public void setDocsPath(String docsPath) {
    this.docsPath = docsPath;
  }

  @JsonProperty
  public String getDocsPath() {
    return docsPath;
  }

  @JsonProperty
  public String getApplicationTitle() {
    return applicationTitle;
  }

  @JsonProperty
  public void setApplicationTitke(String title) {
    applicationTitle = title;
  }

  @JsonProperty
  public String getApplicationDescription() {
    return appDescription;
  }

  @JsonProperty
  public void setApplicationDescription(String description) {
    appDescription = description;
  }

  @JsonProperty
  public String getApplicationTosUrl() {
    return appTosUrl;
  }

  @JsonProperty
  public void setApplicationTosUrl(String tosUrl) {
    this.appTosUrl = tosUrl;
  }

  @JsonProperty
  public String getApplicationContact() {
    return applicationContact;
  }

  @JsonProperty
  public void setApplicationContact(String appContact) {
    this.applicationContact = appContact;
  }

  @JsonProperty
  public String getLicense() {
    return license;
  }

  @JsonProperty
  public void setLicense(String license) {
    this.license = license;
  }

  @JsonProperty("httpClient")
  public HttpClientConfiguration getHttpClientConfiguration() {
    return httpClient;
  }

  public void setHttpClientConfiguration(HttpClientConfiguration httpClient) {
    this.httpClient = httpClient;
  }

  @JsonProperty("addressUrl")
  public String getAddressUrl() {
    return addressUrl;
  }

  public void setAddressUrl(String addressUrl) {
    this.addressUrl = addressUrl;
  }

}
