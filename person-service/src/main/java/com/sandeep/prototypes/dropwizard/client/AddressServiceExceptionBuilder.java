package com.sandeep.prototypes.dropwizard.client;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.client.HttpResponseException;

/**
 * An exception builder for address service exceptions
 * 
 * @author Sandeep Nayak
 *
 */
public class AddressServiceExceptionBuilder {
  private final Exception responseException;

  public AddressServiceExceptionBuilder(HttpResponseException responseException) {
    this.responseException = responseException;
  }

  public AddressServiceExceptionBuilder(IOException exception) {
    this.responseException = exception;
  }

  public AddressServiceException build() {
    if (responseException instanceof HttpResponseException) {
      HttpResponseException httpResponseException = (HttpResponseException) responseException;
      switch (httpResponseException.getStatusCode()) {
        case 404:
          return new AddressServiceException.AddressNotFoundException(
              responseException.getMessage());
        case 408:
          return new AddressServiceException.RequestTimeout(responseException.getMessage());
        case 503:
          return new AddressServiceException.AddressTemporarilyUnavailableException(
              responseException.getMessage());
        default:
          return new AddressServiceException.IOException(responseException);
      }
    } else if (responseException instanceof SocketTimeoutException) {
      return new AddressServiceException.RequestTimeout(responseException.getMessage());
    } else {
      return new AddressServiceException.IOException(responseException);
    }
  }
}
