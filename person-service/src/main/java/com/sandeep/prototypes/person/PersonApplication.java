package com.sandeep.prototypes.person;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.apache.commons.configuration.ConfigurationException;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;
import com.sandeep.prototypes.person.config.PersonConfiguration;
import com.sandeep.prototypes.person.guice.PersonGuiceModule;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

/**
 * <p>
 * The prototype application pulls together models and configuration which makes up the complete
 * application
 * </p>
 * 
 * @author Sandeep Nayak
 *
 */
public class PersonApplication extends Application<PersonConfiguration> {

  private static final String CORS_METHODS = "POST,GET,DELETE,PUT,OPTIONS";
  private static final String CORS_HEADERS =
      "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
  private static final String CORS_FILTER = "CORS";
  private static final String PERSON_APP = "Person Application";

  @Override
  public String getName() {
    return PERSON_APP;
  }

  /**
   * <p>
   * Main entry point into the application
   * </p>
   * 
   * @param args the arguments to the application
   * @throws Exception we do not handle exceptions just throw them back
   */
  public static void main(String args[]) throws Exception {
    new PersonApplication().run(args);
  }

  @Override
  /**
   * <p>
   * Initializes the application with <a href="https://github.com/Netflix/archaius/wiki/Users-Guide">Archaius</a>
   * for configuration management and Guice for dependency injection.
   * 
   * </p>
   * 
   * @param bootStrapConfiguration 
   */
  public void initialize(Bootstrap<PersonConfiguration> bootStrapConfiguration) {
    try {
      DynamicPropertyFactory.initWithConfigurationSource(getPropertiesConfiguration());
    } catch (Throwable e) {
      throw new IllegalStateException(e);
    }
    GuiceBundle<PersonConfiguration> guiceBundle =
        GuiceBundle
            .<PersonConfiguration>newBuilder()
            .addModule(new PersonGuiceModule())
            .enableAutoConfig("com.sandeep.prototypes.person",
                "com.sandeep.prototypes.person.dependency", "com.sandeep.prototypes.person.entity",
                "com.sandeep.prototypes.person.config", "com.sandeep.prototypes.person.resources",
                "com.sandeep.prototypes.person.guice").setConfigClass(PersonConfiguration.class)
            .build();
    bootStrapConfiguration.addBundle(guiceBundle);
  }

  /**
   * <p>
   * In this method we are initializing the Archaius configuration manager to load configurations
   * from a properties file.
   * 
   * The steps to initializing the configuration is as follows
   * <ul>
   * <li>Create a configuration source for the file which contains the properties</li>
   * <li>Create a dynamic configuration around the source so that the source is polled, the polling
   * can be configured to be at a different frequency</li>
   * </ul>
   * 
   * @return DynamicConfiguration
   * @throws ConfigurationException
   * @throws MalformedURLException
   */
  private DynamicConfiguration getPropertiesConfiguration() throws ConfigurationException,
      MalformedURLException {
    String userHome = System.getProperty("user.home");
    Path properties = Paths.get(userHome, "config-root", "person", "person.properties");
    PolledConfigurationSource polledSource = new URLConfigurationSource(properties.toUri().toURL());
    return new DynamicConfiguration(polledSource, new FixedDelayPollingScheduler());
  }

  @Override
  public void run(PersonConfiguration configuration, Environment environment) throws Exception {
    // We need CORS enabled so that swagger can access the documentation endpoint and we can
    // retrieve the swagger compliant json
    FilterRegistration.Dynamic cors =
        environment.servlets().addFilter(CORS_FILTER, CrossOriginFilter.class);
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, CORS_HEADERS);
    cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, CORS_METHODS);
    cors.setInitParameter("allowCredentials", "true");

    environment.jersey().register(new ApiListingResourceJSON());
    environment.jersey().register(new ApiDeclarationProvider());
    environment.jersey().register(new ResourceListingProvider());

    // environment.jersey().register(new PersonResource(configuration.getMessage()));
    environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    BeanConfig config = new BeanConfig();
    config.setTitle(configuration.getApplicationTitle());
    config.setDescription(configuration.getApplicationDescription());
    config.setTermsOfServiceUrl(configuration.getApplicationTosUrl());
    config.setContact(configuration.getApplicationContact());
    config.setLicense(configuration.getLicense());
    config.setVersion(configuration.getVersion());
    config.setBasePath(configuration.getDocsPath());
    config.setResourcePackage("com.sandeep.prototypes.person.resources");
    config.setScan(true);

  }
}
