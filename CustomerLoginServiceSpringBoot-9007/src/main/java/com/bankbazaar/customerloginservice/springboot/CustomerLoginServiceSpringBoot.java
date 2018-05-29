package com.bankbazaar.customerloginservice.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jmx.support.RegistrationPolicy;

@SpringBootApplication
@ImportResource("classpath:sampleCustomerLoginServiceContext.xml")
@EnableMBeanExport(registration= RegistrationPolicy.IGNORE_EXISTING)
public class CustomerLoginServiceSpringBoot {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(CustomerLoginServiceSpringBoot.class, args);
    }
}