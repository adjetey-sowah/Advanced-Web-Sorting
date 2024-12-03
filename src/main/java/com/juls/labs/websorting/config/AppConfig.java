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
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@ComponentScan(basePackages = "com.juls.labs.websorting")
@EnableWebMvc
public class AppConfig {

    @Autowired
    private   EventServiceImpl eventService;

    @Bean
    @Scope(scopeName = "prototype")
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
    @Qualifier("co-organizer")
    public User coOrganizer() {
        Organizer organizer = new Organizer();
        organizer.setUserId(3L);
        organizer.setUsername("Grace");
        organizer.setPhone("0243767699");
        return organizer;
    }

    @Bean
    @Qualifier("event-planner")
    public User eventPlanner() {
        Organizer organizer = new Organizer();
        organizer.setUserId(4L);
        organizer.setUsername("Samuel");
        organizer.setPhone("0243767700");
        return organizer;
    }

    @Bean
    @Qualifier("guest-speaker")
    public User guestSpeaker() {
        Organizer organizer = new Organizer();
        organizer.setUserId(5L);
        organizer.setUsername("Nina");
        organizer.setPhone("0243767711");
        return organizer;
    }

    @Bean
    @Qualifier("volunteer")
    public User volunteer() {
        Organizer organizer = new Organizer();
        organizer.setUserId(6L);
        organizer.setUsername("James");
        organizer.setPhone("0243767722");
        return organizer;
    }

    @Bean

    public Event events(){
        Event event = new  Event("Matilda's Wedding","Agona Swedru", LocalDate.of(2024,12,20),organzier());
        eventService.createEvent(event);
        return  event;
    }

    @Bean
    public Event event2() {
        Event event = new Event("Tech Conference 2024", "Accra International Conference Center", LocalDate.of(2024, 11, 10),  organzier());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public Event event3() {
       Event event =  new Event("Music Concert", "Kumasi Sports Stadium", LocalDate.of(2024, 10, 5), organzier());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public Event event4() {
        Event event =  new Event("Wedding Anniversary Celebration", "Labadi Beach Hotel", LocalDate.of(2024, 9, 15), guestSpeaker());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public Event event5() {
        Event event = new Event("Christmas Gala", "Accra Mall", LocalDate.of(2024, 12, 25), coOrganizer());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public Event event6() {
        Event event = new Event("New Year Party", "East Legon", LocalDate.of(2025, 1, 1), eventPlanner());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public Event event7() {
        Event event = new Event("Charity Ball", "Movenpick Hotel", LocalDate.of(2024, 12, 30), organzier());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public Event event8() {
        Event event = new Event("Summer Beach Party", "Labadi Beach", LocalDate.of(2024, 8, 15), eventPlanner());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public Event event9() {
        Event event = new Event("Fashion Show", "Accra National Theatre", LocalDate.of(2024, 7, 25), volunteer());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public Event event10() {
        Event event = new Event("Annual Business Conference", "Hilton Hotel", LocalDate.of(2024, 10, 20), guestSpeaker());
        eventService.createEvent(event);
        return event;
    }

    @Bean
    public HelloController helloController(){
        return new HelloController();
    }
}
