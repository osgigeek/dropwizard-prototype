package com.sandeep.prototypes.address;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.fasterxml.jackson.annotation.JsonInclude;
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
  public void initialize(Bootstrap<AddressConfiguration> bootstrapConfig) {}

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
