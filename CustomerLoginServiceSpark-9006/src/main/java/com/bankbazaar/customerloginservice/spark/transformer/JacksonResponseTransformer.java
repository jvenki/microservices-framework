package com.bankbazaar.customerloginservice.spark.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

/**
 * Created by amala on 4/5/18.
 */
public class JacksonResponseTransformer implements ResponseTransformer {
    @Override
    public String render(Object o) throws Exception {
        return new ObjectMapper().writeValueAsString(o);
    }
}
