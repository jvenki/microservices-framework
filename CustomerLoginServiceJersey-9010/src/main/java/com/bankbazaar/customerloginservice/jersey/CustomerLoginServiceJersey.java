package com.bankbazaar.customerloginservice.jersey;

import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class CustomerLoginServiceJersey {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(9010);
        tomcat.addWebapp("/", new File("CustomerLoginServiceJersey-9010/src/main/webapp").getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
    }
}
