package com.sandeep.prototypes.person.circuitbreaker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.sandeep.prototypes.address.entity.Address;

/**
 * <p>
 * An HTTP GET request which is a Hystrix command which uses a background thread pool to make the
 * GET request synchronously. If the request fails, the call will return a cached value of the data
 * </p>
 * 
 * @author Sandeep Nayak
 *
 */
public class FailSafeGetRequest extends HystrixCommand<Address> {

  private static final String ADDRESS_CLIENT_POOL = "address-client-pool";
  private static final String ADDRESS_GET_COMMAND = "address-get";
  private static final String PERSON_GROUP = "person-group";
  private final HttpClient client;
  private final ResponseHandler<Address> responseHandler;
  private final HttpGet request;
  private static final ConcurrentMap<String, Address> cache =
      new ConcurrentHashMap<String, Address>();

  /**
   * <p>
   * A failsafe GET request which takes the client, request and response handler to use to send the
   * request and send a response
   * </p>
   * 
   * @param client the http client
   * @param request the request to make using the client
   * @param responseHandler the response handler
   */
  public FailSafeGetRequest(HttpClient client, HttpGet request,
      ResponseHandler<Address> responseHandler) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(PERSON_GROUP))
        .andCommandKey(HystrixCommandKey.Factory.asKey(ADDRESS_GET_COMMAND))
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(ADDRESS_CLIENT_POOL)));
    this.client = client;
    this.responseHandler = responseHandler;
    this.request = request;
  }

  @Override
  protected Address run() throws Exception {
    Address address = client.execute(request, responseHandler);
    return cacheAddress(request, address);
  }

  @Override
  protected Address getFallback() {
    return getFromCache(request);
  }

  private Address cacheAddress(HttpGet request, Address address) {
    cache.putIfAbsent(request.getRequestLine().getUri(), address);
    return address;
  }

  private Address getFromCache(HttpGet request) {
    return cache.get(request.getRequestLine().getUri());
  }

}
