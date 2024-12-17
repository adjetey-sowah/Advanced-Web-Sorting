package com.juls.labs.websorting.controller;


import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.model.User;
import com.juls.labs.websorting.service.EventService;
import com.juls.labs.websorting.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/events")
public class EventController {

    @Autowired
    private final EventService eventService;

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
                        linkTo(methodOn(EventController.class).findEventById(String.valueOf(event.getEventId()))).withRel("Find event by id"),
                        linkTo(methodOn(EventController.class).searchEvent(event.getEventName())).withRel("Search Event"),
                        linkTo(methodOn(EventController.class).updateEvent(event.getEventId(),event)).withRel("Update Event"),
                        linkTo(methodOn(EventController.class).deleteEvent(event.getEventId())).withSelfRel())
                )

                                .collect(Collectors.toList());
        CollectionModel<EntityModel<Event>> collectionModel = CollectionModel.of(eventModels,
                linkTo(methodOn(EventController.class).createEvent(new Event())).withRel("create"),
                linkTo(methodOn(EventController.class).getUpcomingEvents()).withRel("upcoming"),
                linkTo(methodOn(EventController.class).allEvents()).withRel("All Events")
                );
        return ResponseEntity.ok().body(collectionModel);
    }

    @GetMapping("/event_id/{id}")
    @ResponseBody
    public ResponseEntity<EntityModel<Event>> findEventById(@PathVariable String id) {
        try {
            Long eventId = Long.parseLong(id);
            Optional<Event> eventOptional = this.eventService.getEventById(eventId);

            return eventOptional.map(event -> {
                EntityModel<Event> eventResource = EntityModel.of(event);
                eventResource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).findEventById(id)).withSelfRel());
                return ResponseEntity.ok(eventResource);
            }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
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

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        CollectionModel<Object> responseModel = CollectionModel.of(Collections.emptyList());
        // Add links to the response model
        responseModel.add(linkTo(methodOn(EventController.class).allEvents()).withRel("all"));
        responseModel.add(linkTo(methodOn(EventController.class).createEvent(new Event())).withRel("create"));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseModel);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<EntityModel<Event>> updateEvent(
            @PathVariable Long id,
            @RequestBody Event updatedEvent) {
        updatedEvent.setEventId(id); // Ensure the ID is set
        Event event = eventService.updateEvent(updatedEvent);

        EntityModel<Event> eventModel = EntityModel.of(event,
                linkTo(methodOn(EventController.class).findEventById(String.valueOf(id))).withSelfRel(),
                linkTo(methodOn(EventController.class).allEvents()).withRel("Get all Events"),
                linkTo(methodOn(EventController.class).getUpcomingEvents()).withRel("Update an Event"),
                linkTo(methodOn(EventController.class).getEventByDate(event.getDate())).withRel("Get event by date"),
                linkTo(methodOn(EventController.class).createEvent(new Event())).withRel("Create new Event"));

        return ResponseEntity.ok(eventModel);
    }

    @GetMapping("event")
    @ResponseBody
    public ResponseEntity<CollectionModel<EntityModel<Event>>> getEventByDate(@RequestParam(name="eventDate") LocalDate eventDate){

        try {
            List<Event> eventList = this.eventService.getEventByDate(eventDate);

            List<EntityModel<Event>> entityModels = eventList.stream()
                    .map(event -> EntityModel.of(event,
                            linkTo(methodOn(EventController.class).findEventById(String.valueOf(event.getEventId()))).withRel("Find event by id"),
                            linkTo(methodOn(EventController.class).deleteEvent(event.getEventId())).withRel("Delete Event"),
                            linkTo(methodOn(EventController.class).updateEvent(event.getEventId(), event)).withRel("Update Event"),
                            linkTo(methodOn(EventController.class).getEventByDate(eventDate)).withSelfRel(),
                            linkTo(methodOn(EventController.class).searchEvent(event.getEventName())).withRel("Search Event")
                            )).collect(Collectors.toList());

            CollectionModel<EntityModel<Event>> collectionModel = CollectionModel.of(entityModels,
                    linkTo(methodOn(EventController.class).allEvents()).withRel("All Events"),
                    linkTo(methodOn(EventController.class).getUpcomingEvents()).withRel("Upcoming Events")
                    );

            return ResponseEntity.ok().body(collectionModel);
        }

        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}

