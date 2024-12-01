package com.juls.labs.websorting;

import com.juls.labs.websorting.config.AppConfig;
import com.juls.labs.websorting.model.Participant;
import com.juls.labs.websorting.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


public class WebInitializer implements WebApplicationInitializer {


    @Override
    public void onStartup(jakarta.servlet.ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(com.juls.labs.websorting.config.AppConfig.class);
        context.setServletContext(servletContext);

        ServletRegistration.Dynamic dispacther = servletContext.addServlet(
                "SpringDispatcher", new DispatcherServlet(context)
        );

        dispacther.setLoadOnStartup(1);
        dispacther.addMapping("/");

    }
}
