package com.juls.labs.websorting.service;


import com.juls.labs.websorting.exception.EventNotFoundException;
import com.juls.labs.websorting.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventService {

    Event createEvent(Event event);
    Optional<Event> getEventById(Long eventId) throws EventNotFoundException;
    List<Event> getAllEvents();
    Event updateEvent(Event event) throws EventNotFoundException;
    void deleteEvent(Long eventId) throws EventNotFoundException;
    Optional<Event> searchEvent(String eventName) throws EventNotFoundException;
    List<Event> getUpcomingEvents();
    List<Event> getEventByDate(LocalDate eventDate);

}
