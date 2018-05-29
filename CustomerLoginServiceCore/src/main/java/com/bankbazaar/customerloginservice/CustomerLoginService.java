package com.bankbazaar.customerloginservice;

import com.bankbazaar.customerloginservice.converter.LoggedInCustomerConverter;
import com.bankbazaar.customerloginservice.model.LoggedInCustomer;
import com.bankbazaar.dao.CustomerDao;
import com.bankbazaar.model.types.CustomerType;
import com.bankbazaar.model.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by amala on 8/5/18.
 */
@Component
public class CustomerLoginService {
    @Autowired
    LoggedInCustomerConverter loggedInCustomerConverter;

    @Autowired
    CustomerDao customerDao;

    public LoggedInCustomer getCustomer(String userName, String loginType) {
        Customer customer = customerDao.getUser(userName, CustomerType.getCustomerType(loginType));
        return loggedInCustomerConverter.convertToV2Model(customer);
    }

    public LoggedInCustomer getCustomer(int id) {
        Customer customer = customerDao.get((long) id);
        return loggedInCustomerConverter.convertToV2Model(customer);
    }
}
