package com.sandeep.prototypes.person.guice;

import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;

import org.apache.http.client.HttpClient;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.sandeep.prototypes.person.config.PersonConfiguration;
import com.sandeep.prototypes.person.dependency.AddressClient;

/**
 * <p>
 * Integration between guice and configuration. Guice provides a fully configured http-client object
 * which can be injected into clients from other services. See {@link AddressClient} for an example
 * </p>
 * 
 * @author Sandeep Nayak
 *
 */
public class PersonGuiceModule implements Module {
  private static final String HTTP_CLIENT_NAME = "http-client";
  private HttpClient client;

  public void configure(Binder binder) {}

  /**
   * Caches an http client which can be used to make requests
   * 
   * @param configuration access to the person service configuration
   * @param environment
   * @return
   */
  @Provides
  public HttpClient getHttpClient(PersonConfiguration configuration, Environment environment) {
    if (client == null) {
      client =
          new HttpClientBuilder(environment).using(configuration.getHttpClientConfiguration())
              .build(HTTP_CLIENT_NAME);
    }
    return client;
  }
}
