package com.juls.labs.websorting.controller;


import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.model.User;
import com.juls.labs.websorting.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                linkTo(methodOn(EventController.class).getUpcomingEvents()).withRel("upcoming"),
                linkTo(methodOn(EventController.class).allEvents()).withRel("all")
                );

        return ResponseEntity.ok().body(eventModel);
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<CollectionModel<EntityModel<Event>>> allEvents(){
        List<Event>  eventList = this.eventService.getAllEvents();

        // Convert the list of Events into a list of EntityModels with links
        List<EntityModel<Event>> eventModels = eventList.stream()
                .map(event -> EntityModel.of(event,
                        linkTo(methodOn(EventController.class).findEventById(String.valueOf(event.getEventId()))).withRel("event_id"),
                        linkTo(methodOn(EventController.class).allEvents()).withSelfRel()))
                                .collect(Collectors.toList());

        CollectionModel<EntityModel<Event>> collectionModel = CollectionModel.of(eventModels,
                linkTo(methodOn(EventController.class).createEvent(new Event())).withRel("create"),
                linkTo(methodOn(EventController.class).getUpcomingEvents()).withRel("upcoming")
                );
        return ResponseEntity.ok().body(collectionModel);
    }


    @GetMapping("/event_id/{id}")
    @ResponseBody
    public ResponseEntity<Event> findEventById(@PathVariable String id) {
        try {
            Long eventId = Long.parseLong(id);
            Optional<Event> eventOptional = this.eventService.getEventById(eventId);

            return eventOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    @GetMapping("/upcoming")
    @ResponseBody
    public ResponseEntity<CollectionModel<EntityModel<Event>>> getUpcomingEvents(){

        List<Event> upcomingEvents = this.eventService.getUpcomingEvents();

        List<EntityModel<Event>> entityModels = upcomingEvents.stream()
                .map(event -> EntityModel.of(event,
                        linkTo(methodOn(EventController.class).getUpcomingEvents()).withSelfRel(),
                        linkTo(methodOn(EventController.class).findEventById(String.valueOf(event.getEventId()))).withRel("event_id"))
                ).collect(Collectors.toList());

        CollectionModel<EntityModel<Event>> collectionModel = CollectionModel.of(entityModels,
                linkTo(methodOn(EventController.class).createEvent(new Event())).withRel("create"),
                linkTo(methodOn(EventController.class).allEvents()).withRel("all"));

        return ResponseEntity.ok().body(collectionModel);
    }



    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<EntityModel<Event>> searchEvent(@RequestParam String name) {
        Optional<Event> optionalEvent = eventService.searchEvent(name);

        if (!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();

        EntityModel<Event> eventModel = EntityModel.of(event,
                linkTo(methodOn(EventController.class).findEventById(String.valueOf(event.getEventId()))).withSelfRel(),
                linkTo(methodOn(EventController.class).allEvents()).withRel("all"),
                linkTo(methodOn(EventController.class).createEvent(new Event())).withRel("create"));

        return ResponseEntity.ok(eventModel);
    }




}

