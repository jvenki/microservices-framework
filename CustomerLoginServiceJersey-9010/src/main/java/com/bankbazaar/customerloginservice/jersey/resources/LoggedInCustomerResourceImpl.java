package com.bankbazaar.customerloginservice.jersey.resources;

import com.bankbazaar.customerloginservice.CustomerLoginService;
import com.bankbazaar.customerloginservice.resource.LoggedInCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("/customer")
public class LoggedInCustomerResourceImpl implements LoggedInCustomer {
    @Autowired
    CustomerLoginService customerLoginService;

    public GetLoggedInCustomerResponse getLoggedInCustomer(String userName, String loginType) {
        return GetLoggedInCustomerResponse.respond200WithApplicationJson(customerLoginService.getCustomer(userName, loginType));
    }

    public PostLoggedInCustomerResponse postLoggedInCustomer(com.bankbazaar.customerloginservice.model.LoggedInCustomer loggedInCustomer) {
        return null;
    }

    public GetLoggedInCustomerByIdResponse getLoggedInCustomerById(int i) {
        return LoggedInCustomer.GetLoggedInCustomerByIdResponse.respond200WithApplicationJson(customerLoginService.getCustomer(i));
    }
}
