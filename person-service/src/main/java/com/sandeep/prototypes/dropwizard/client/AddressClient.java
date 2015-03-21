package com.sandeep.prototypes.dropwizard.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sandeep.prototypes.address.entity.Address;

public class AddressClient {

  private final HttpClient client;
  private final String addressUrl;

  private static class AddressResponseHandler implements ResponseHandler<Address> {

    public Address handleResponse(HttpResponse response) throws ClientProtocolException,
        IOException {
      BasicResponseHandler handler = new BasicResponseHandler();
      String message = handler.handleResponse(response);
      Address address = null;
      if (!StringUtils.isEmpty(message)) {
        ObjectMapper mapper = new ObjectMapper();
        address = mapper.readValue(message.getBytes(StandardCharsets.UTF_8), Address.class);
      }
      return address;
    }

  }

  @Inject
  public AddressClient(@Named("addressUrl") String addressUrl, HttpClient client) {
    this.client = client;
    this.addressUrl = addressUrl;
  }

  public Address getAddress(int id) throws AddressServiceException {
    HttpGet getAddress = new HttpGet(String.format(addressUrl, id));
    Address address;
    try {
      address = client.execute(getAddress, new AddressResponseHandler());
    } catch (HttpResponseException e) {
      throw new AddressServiceExceptionBuilder(e).build();
    } catch (IOException e) {
      throw new AddressServiceExceptionBuilder(e).build();
    } finally {
      getAddress.releaseConnection();
    }
    return address;
  }
}
