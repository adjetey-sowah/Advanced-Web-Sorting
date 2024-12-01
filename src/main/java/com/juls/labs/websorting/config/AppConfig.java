package com.juls.labs.websorting.config;

import com.juls.labs.websorting.controller.EventController;
import com.juls.labs.websorting.controller.HelloController;
import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.model.Organizer;
import com.juls.labs.websorting.model.Participant;
import com.juls.labs.websorting.model.User;
import com.juls.labs.websorting.repository.impl.EventRepositoryImpl;
import com.juls.labs.websorting.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "com.juls.labs.websorting")
@EnableWebMvc
public class AppConfig {

    @Bean
    public Event event(){
        return new Event();
    }

    @Bean(name = "part")
    public User participant(){
        Participant participant = new Participant();
        participant.setUserId(1L);
        participant.setUsername("Julius");
        participant.setPhone("0543358413");
        return participant;
    }

    @Bean
    @Qualifier("main-organizer")
    public User organzier(){
        Organizer organizer = new Organizer();
        organizer.setUserId(2L);
        organizer.setUsername("Gabriel");
        organizer.setPhone("0243767688");
        return organizer;
    }

    @Bean
    public HelloController helloController(){
        return new HelloController();
    }

    @Bean
    public EventRepositoryImpl eventRepository(){
        return new EventRepositoryImpl();
    }

    @Bean
    public EventServiceImpl eventService(){
        return new EventServiceImpl();
    }

    @Bean
    public EventController eventController(){
        return new EventController(eventService(), organzier());
    }



}
