package com.bankbazaar.customerloginservice.vertx.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTransformer {
    public String toString(Object o) throws Exception {
        return new ObjectMapper().writeValueAsString(o);
    }

    public Object toObject(String s, Class clazz) throws Exception {
        return new ObjectMapper().readValue(s, clazz);
    }
}
