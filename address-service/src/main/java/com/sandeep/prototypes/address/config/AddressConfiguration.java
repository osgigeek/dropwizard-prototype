package com.sandeep.prototypes.address.config;

import io.dropwizard.Configuration;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressConfiguration extends Configuration {
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
}
