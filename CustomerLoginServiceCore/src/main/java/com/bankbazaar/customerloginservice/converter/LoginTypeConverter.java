package com.bankbazaar.customerloginservice.converter;

import com.bankbazaar.customerloginservice.model.LoginType;
import com.bankbazaar.model.types.CustomerType;
import com.bankbazaar.util.Converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amala on 9/5/18.
 */
public class LoginTypeConverter implements Converter<LoginType, CustomerType> {
    private final Map<LoginType, CustomerType> loginTypeCustomerTypeMap = new HashMap<LoginType, CustomerType>(){{
        put(LoginType.BB_NATIVE_EMAIL, CustomerType.BANKBAZAAR);
        put(LoginType.BB_NATIVE_MOBILE, CustomerType.BBMOBILE);
        put(LoginType.FACEBOOK, CustomerType.FACEBOOK);
        put(LoginType.GOOGLE, CustomerType.GOOGLE);
        put(LoginType.LINKEDIN, CustomerType.LINKEDIN);
        put(LoginType.MOBILE_APP, CustomerType.DEVICE);
    }};

    private final Map<CustomerType, LoginType> customerTypeLoginTypeMap = new HashMap<CustomerType, LoginType>(){{
        put(CustomerType.BANKBAZAAR, LoginType.BB_NATIVE_EMAIL);
        put(CustomerType.BBMOBILE, LoginType.BB_NATIVE_MOBILE);
        put(CustomerType.FACEBOOK, LoginType.FACEBOOK);
        put(CustomerType.GOOGLE, LoginType.GOOGLE);
        put(CustomerType.LINKEDIN, LoginType.LINKEDIN);
        put(CustomerType.DEVICE, LoginType.MOBILE_APP);
    }};
    @Override
    public CustomerType convertToV1Model(LoginType loginType) {
        return loginTypeCustomerTypeMap.get(loginType);
    }

    @Override
    public LoginType convertToV2Model(CustomerType customerType) {
        return customerTypeLoginTypeMap.get(customerType);
    }
}
