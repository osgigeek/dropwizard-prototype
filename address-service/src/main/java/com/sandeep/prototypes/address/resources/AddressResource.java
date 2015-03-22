package com.sandeep.prototypes.address.resources;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.sandeep.prototypes.address.entity.Address;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/address")
@Consumes("application/json")
@Api(value = "/address", description = "Address Management API")
public class AddressResource {
  private final ConcurrentMap<Integer, Address> addresses;
  private final AtomicInteger successCount;
  private static final DynamicStringProperty error_404 = DynamicPropertyFactory.getInstance()
      .getStringProperty("404", "{\"error\":\"Not found\"}");
  private static final DynamicStringProperty error_503 = DynamicPropertyFactory.getInstance()
      .getStringProperty("503", "{\"error\":\"Service Unavailable\"}");

  public AddressResource() {
    addresses = new ConcurrentHashMap<Integer, Address>();
    successCount = new AtomicInteger();
  }

  @GET
  @Timed
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  @ApiOperation(value = "Find address by id",
      notes = "Returns a address when id > 1 or nonintegers will simulate API error conditions",
      response = Address.class)
  @ApiResponses(value = {@ApiResponse(code = 503, message = "Address service unavailable"),
      @ApiResponse(code = 404, message = "Address not found")})
  public Response getAddress(@PathParam("id") Integer id) {
    // Here is the response semantics
    // 1 = 200
    // 2 = 404
    // 3 = Timeout
    // 4 = 200
    // 5 = 200
    // 6 = 503
    // 7 = 503
    // 8 = 404
    // 9 = 200
    // 10 = 200
    Address address = addresses.get(id);
    int counter = successCount.incrementAndGet();
    if (address != null) {
      if (counter == 3) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(address).build();
      }
      if (counter == 2 || counter == 8) {
        return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
            .entity(error_404.get()).build();
      }
      if (counter > 5 && counter < 8) {
        return Response.status(Status.SERVICE_UNAVAILABLE).entity(error_503.get())
            .type(MediaType.APPLICATION_JSON).build();
      }
      if (counter > 10) {
        successCount.set(0);
      }
      // Should be true for 1, 4, 5, 9 and 10
      return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(address).build();
    }
    return Response.status(Status.NOT_FOUND).entity(error_503.get())
        .type(MediaType.APPLICATION_JSON).build();
  }

  @POST
  @ApiOperation(value = "Add a new address")
  @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createAddress(Address address) {
    addresses.putIfAbsent(address.getId(), address);
    return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(address).build();
  }
}
