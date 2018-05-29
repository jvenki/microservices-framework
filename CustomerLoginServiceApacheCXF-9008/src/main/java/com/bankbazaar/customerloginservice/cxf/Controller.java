package com.bankbazaar.customerloginservice.cxf;

import com.bankbazaar.customerloginservice.CustomerLoginService;
import com.bankbazaar.customerloginservice.model.LoggedInCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.ws.rs.*;

@Path("/")
@Produces({"application/json"})
@Consumes({"application/json"})
public class Controller {

    @Autowired
    @Qualifier("customerLoginService")
    private CustomerLoginService customerLoginService;

    @Path("/hello/{id}")
    @GET
    public String sayHelloToCustomer(@PathParam("id") int customerId) {
        return "Hello " + (customerLoginService.getCustomer(customerId).getUserName());
    }

    @Path("/customer/{id}")
    @GET
    public LoggedInCustomer getCustomer(@PathParam("id") int customerId){
        return customerLoginService.getCustomer(customerId);
    }
}