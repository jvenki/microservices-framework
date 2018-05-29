package com.bankbazaar.customerloginservice.springboot;

import com.bankbazaar.customerloginservice.CustomerLoginService;
import com.bankbazaar.customerloginservice.model.LoggedInCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerLoginServiceController {

    @Autowired
    @Qualifier("customerLoginService")
    private CustomerLoginService customerLoginService;

    @RequestMapping(value="/customer/{id}", method=RequestMethod.GET)
    public LoggedInCustomer getCustomer(@PathVariable("id") int customerId){
        return customerLoginService.getCustomer(customerId);
    }
}