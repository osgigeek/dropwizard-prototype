package com.sandeep.prototypes.dropwizard.guice;

import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;

import org.apache.http.client.HttpClient;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.sandeep.prototypes.dropwizard.config.PersonConfiguration;

public class PersonGuiceModule implements Module {
  private HttpClient client;

  public void configure(Binder binder) {}

  @Provides
  @Named("addressUrl")
  public String getAddressUrl(PersonConfiguration configuration) {
    return configuration.getAddressUrl();
  }

  @Provides
  public HttpClient getHttpClient(PersonConfiguration configuration, Environment environment) {
    if (client == null) {
      client =
          new HttpClientBuilder(environment).using(configuration.getHttpClientConfiguration())
              .build("http-client");
    }
    return client;
  }
}
