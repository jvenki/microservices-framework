package com.bankbazaar.customerloginservice.spark.filters;

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * Created by amala on 4/5/18.
 */
public class ContentTypeFilter implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {
        if(response.status() == 200) {
            response.header("Content-type", "application/json");
        }
    }
}
