package com.juls.labs.websorting.controller;


import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.model.User;
import com.juls.labs.websorting.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/events")
public class EventController {

    @Autowired
    private final EventServiceImpl eventService;

    @Autowired
    @Qualifier("main-organizer")
    private final   User organizer;

    public EventController(EventServiceImpl eventService, @Qualifier("main-organizer") User organizer){
        this.eventService = eventService;
        this.organizer = organizer;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<EntityModel<Event>> createEvent(@RequestBody Event event){
        var newEvent = this.eventService.createEvent(event);
        newEvent.setOrganizer(organizer);

        EntityModel<Event> eventModel = EntityModel.of(newEvent,
                linkTo(methodOn(EventController.class).findEventById(String.valueOf(newEvent.getEventId()))).withSelfRel(),
                linkTo(methodOn(EventController.class).allEvents()).withRel("all")
                );

        return ResponseEntity.ok().body(eventModel);
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Event>> allEvents(){
        return ResponseEntity.ok(this.eventService.getAllEvents());
    }


    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Optional<Event>>  findEventById(@PathVariable String id){
        return ResponseEntity.ok(this.eventService.getEventById(Long.parseLong(id)));
    }

    @GetMapping("/upcoming")
    @ResponseBody
    public ResponseEntity<List<Event>> getUpcomingEvents(){
        return ResponseEntity.ok(this.eventService.getUpcomingEvents());
    }



}

