package com.juls.labs.websorting.controller;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.model.Organizer;
import com.juls.labs.websorting.model.User;
import com.juls.labs.websorting.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Event> createEvent(@RequestBody Event event){
        var newEvent = this.eventService.createEvent(event);
        newEvent.setOrganizer(organizer);
        return ResponseEntity.ok().body(newEvent);
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Event>> allEvents(){
        return ResponseEntity.ok(this.eventService.getAllEvents());
    }

    @GetMapping("/{eventId}")
    @ResponseBody
    public ResponseEntity<Optional<Event>>  getEventById(@PathVariable Long eventId){
        return ResponseEntity.ok(this.eventService.getEventById(eventId));
    }



}
