package com.bankbazaar.customerloginservice.converter;

import com.bankbazaar.customerloginservice.model.LoggedInCustomer;
import com.bankbazaar.model.user.Customer;
import com.bankbazaar.util.BeanPropertyMappedConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amala on 16/4/18.
 */
@Component
public class LoggedInCustomerConverter extends BeanPropertyMappedConverter<LoggedInCustomer, Customer>{

    @Override
    protected List<PropertyMap> getPropertyMappingRules() {
        ArrayList<PropertyMap> propertyMaps = new ArrayList<PropertyMap>();
        propertyMaps.add(new PropertyMap("id", "id"));
        propertyMaps.add(new PropertyMap("userName", "email"));
        propertyMaps.add(new PropertyMap("loginType", "customerType", new LoginTypeConverter()));
        return propertyMaps;
    }

    @Override
    protected Customer getEmptyV1Object() {
        return new Customer();
    }

    @Override
    protected LoggedInCustomer getEmptyV2Object() {
        return new LoggedInCustomer();
    }
}
