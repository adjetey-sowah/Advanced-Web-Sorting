package com.juls.labs.websorting.service;


import com.juls.labs.websorting.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventService {

    Event createEvent(Event event);
    Optional<Event> getEventById(Long eventId);
    List<Event> getAllEvents();
    Event updateEvent(Event event);
    void deleteEvent(Long eventId);
    Optional<Event> searchEvent(String eventName);
    List<Event> getUpcomingEvents();
    List<Event> getEventByDate(LocalDate eventDate);

}
