package com.bankbazaar.customerloginservice.cxf;

import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class CustomerLoginServiceCXF {
    public static void main(String[] args) throws Exception {
        final String webappRoot = "CustomerLoginServiceApacheCXF-9008";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(9008);
        tomcat.addWebapp("/", new File(webappRoot + "/src/main/webapp").getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
    }
}
