package com.jvenki.microservices.jersey.tomcat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class HelloJerseyTomcatApplication {

    public static void main(String[] args) throws Exception {

        final String webappRoot = "HelloJerseyTomcat-9003";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(9003);

        StandardContext ctx = (StandardContext) tomcat.addContext("/", new File(webappRoot).getAbsolutePath());

        File additionWebInfClasses = new File(webappRoot + "/target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        Tomcat.addServlet(ctx, "jersey-servlet-container", createServletContainer());
        ctx.addServletMappingDecoded("/*", "jersey-servlet-container");

        tomcat.start();
        tomcat.getServer().await();
    }

    private static ServletContainer createServletContainer() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(Greeting.class);

        return new ServletContainer(new ResourceConfig(resources));
    }
}