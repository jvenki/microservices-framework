package com.bankbazaar.customerloginservice.spark;

import com.bankbazaar.customerloginservice.CustomerLoginService;
import com.bankbazaar.customerloginservice.spark.filters.ContentTypeFilter;
import com.bankbazaar.customerloginservice.spark.transformer.JacksonResponseTransformer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static spark.Spark.after;
import static spark.Spark.get;

public class CustomerLoginServiceSpark {
    public static void main(String [] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("sampleCustomerLoginServiceContext.xml", "*/applicationContext.xml");
        com.bankbazaar.customerloginservice.spark.Server.startServer(9006);
        setupRoutes(ctx);
    }

    private static void setupRoutes(ApplicationContext ctx) {
        CustomerLoginService customerLoginService = ctx.getBean(CustomerLoginService.class);
        get("/customer/:id", (req, res) -> customerLoginService.getCustomer(Integer.valueOf(req.params(":id"))), new JacksonResponseTransformer());
        after("*", new ContentTypeFilter());
    }
}
