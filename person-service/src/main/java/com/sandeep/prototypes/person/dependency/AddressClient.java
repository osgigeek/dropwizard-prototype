package com.sandeep.prototypes.person.dependency;

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
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.sandeep.prototypes.address.entity.Address;

/**
 * <p>
 * A client class for Address Service. Ideally this is provided by the Address Service but since
 * this is a prototype its here to avoid spending too much time on separating builds and projects
 * </p>
 * 
 * @author Sandeep Nayak
 *
 */
public class AddressClient {

  private final HttpClient client;
  // Its ok to cache this handle statically as the handle will refresh the underlying property when
  // its updated
  private DynamicStringProperty addressUrl = DynamicPropertyFactory.getInstance()
      .getStringProperty("addressUrl", "http://localhost:9100/address/%s");

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

  /**
   * <p>
   * Constructs an address client with an http client injected via Guice
   * </p>
   * 
   * @param client
   */
  @Inject
  public AddressClient(HttpClient client) {
    this.client = client;
  }

  public Address getAddress(int id) throws AddressServiceException {
    // We simply use the addressUrl handle here and get will give us the latest configuration
    HttpGet getAddress = new HttpGet(String.format(addressUrl.get(), id));
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
