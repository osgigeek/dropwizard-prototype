package com.sandeep.prototypes.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.sandeep.prototypes.dropwizard.config.PersonConfiguration;
import com.sandeep.prototypes.dropwizard.guice.PersonGuiceModule;
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

  private static final String PROTOTYPE_APPLICATION = "Prototype Application";

  @Override
  public String getName() {
    return PROTOTYPE_APPLICATION;
  }

  /**
   * Main entry point into the application
   * 
   * @param args the arguments to the application
   * @throws Exception we do not handle exceptions just throw them back
   */
  public static void main(String args[]) throws Exception {
    new PersonApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<PersonConfiguration> bootStrapConfiguration) {
    GuiceBundle<PersonConfiguration> guiceBundle =
        GuiceBundle
            .<PersonConfiguration>newBuilder()
            .addModule(new PersonGuiceModule())
            .enableAutoConfig("com.sandeep.prototypes.dropwizard",
                "com.sandeep.prototypes.dropwizard.client",
                "com.sandeep.prototypes.dropwizard.core",
                "com.sandeep.prototypes.dropwizard.config",
                "com.sandeep.prototypes.dropwizard.resources",
                "com.sandeep.prototypes.dropwizard.guice")
            .setConfigClass(PersonConfiguration.class).build();
    bootStrapConfiguration.addBundle(guiceBundle);
  }

  @Override
  public void run(PersonConfiguration configuration, Environment environment) throws Exception {
    // We need CORS enabled so that swagger can access the documentation endpoint and we can
    // retrieve the swagger compliant json
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
    config.setResourcePackage("com.sandeep.prototypes.dropwizard.resources");
    config.setScan(true);

  }
}
