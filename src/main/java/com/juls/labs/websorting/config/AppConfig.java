package com.juls.labs.websorting.config;

import com.juls.labs.websorting.controller.HelloController;
import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.model.Organizer;
import com.juls.labs.websorting.model.Participant;
import com.juls.labs.websorting.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "com.juls.labs.websorting.model")
@EnableWebMvc
public class AppConfig {

    @Bean
    public Event event(){
        return new Event();
    }

    @Bean
    public User participant(){
        Participant participant = new Participant();
        participant.setUserId(1L);
        participant.setUsername("Julius");
        participant.setPhone("0543358413");
        return participant;
    }

    @Bean
    public User Organzier(){
        Organizer organizer = new Organizer();
        organizer.setUserId(2L);
        organizer.setUsername("Gabriel");
        organizer.setPhone("02436=767688");
        return organizer;
    }

    @Bean
    public HelloController helloController(){
        return new HelloController();
    }



}
