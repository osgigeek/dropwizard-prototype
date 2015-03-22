package com.sandeep.prototypes.address;

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
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;
import com.sandeep.prototypes.address.config.AddressConfiguration;
import com.sandeep.prototypes.address.resources.AddressResource;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

/**
 * <p>
 * The address application class which puts together the address application
 * </p>
 * 
 * @author Sandeep Nayak
 *
 */
public class AddressApplication extends Application<AddressConfiguration> {
  private static final String NAME = "Address Provider";

  @Override
  public String getName() {
    return NAME;
  }

  /**
   * Main entry point into the application
   * 
   * @param args the arguments to the application
   * @throws Exception we do not handle exceptions just throw them back
   */
  public static void main(String args[]) throws Exception {
    new AddressApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<AddressConfiguration> bootstrapConfig) {
    try {
      DynamicPropertyFactory.initWithConfigurationSource(getPropertiesConfiguration());
    } catch (Throwable e) {
      throw new IllegalStateException(e);
    }
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
    Path properties = Paths.get(userHome, "config-root", "address", "address.properties");
    PolledConfigurationSource polledSource = new URLConfigurationSource(properties.toUri().toURL());
    return new DynamicConfiguration(polledSource, new FixedDelayPollingScheduler());
  }

  @Override
  public void run(AddressConfiguration addressConfiguration, Environment environment)
      throws Exception {
    FilterRegistration.Dynamic cors =
        environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
        "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "POST,GET,DELETE,PUT,OPTIONS");
    cors.setInitParameter("allowCredentials", "true");

    environment.jersey().register(new ApiListingResourceJSON());
    environment.jersey().register(new ApiDeclarationProvider());
    environment.jersey().register(new ResourceListingProvider());

    environment.jersey().register(new AddressResource());
    environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    BeanConfig config = new BeanConfig();
    config.setTitle(addressConfiguration.getApplicationTitle());
    config.setDescription(addressConfiguration.getApplicationDescription());
    config.setTermsOfServiceUrl(addressConfiguration.getApplicationTosUrl());
    config.setContact(addressConfiguration.getApplicationContact());
    config.setLicense(addressConfiguration.getLicense());
    config.setVersion(addressConfiguration.getVersion());
    config.setBasePath(addressConfiguration.getDocsPath());
    config.setResourcePackage("com.sandeep.prototypes.address.resources");
    config.setScan(true);

  }
}
