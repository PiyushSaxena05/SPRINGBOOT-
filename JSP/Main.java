package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.jasper.servlet.JasperInitializer;
import org.apache.jasper.servlet.JspServlet;
import org.example.config.webConfig;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws LifecycleException {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        String baseDoc = new File("src/main/webapp").getAbsolutePath();
        Context context = tomcat.addContext("", baseDoc);

        // JSP Support
        context.addServletContainerInitializer(new JasperInitializer(), Set.of());

        Wrapper jspServlet = Tomcat.addServlet(context, "jsp", new JspServlet());
        jspServlet.setLoadOnStartup(3);
        context.addServletMappingDecoded("*.jsp", "jsp");
        context.addServletMappingDecoded("*.jspx", "jsp");

        // Spring Context
        AnnotationConfigWebApplicationContext springContext =
                new AnnotationConfigWebApplicationContext();

        springContext.register(webConfig.class);

        // Dispatcher Servlet
        DispatcherServlet dispatcherServlet =
                new DispatcherServlet(springContext);

        Wrapper dispatcher = Tomcat.addServlet(
                context,
                "dispatcherServlet",
                dispatcherServlet
        );

        dispatcher.setLoadOnStartup(1);
        context.addServletMappingDecoded("/", "dispatcherServlet");

        tomcat.start();

        System.out.println("Tomcat started at http://localhost:8080/");

        tomcat.getServer().await();
    }
}
