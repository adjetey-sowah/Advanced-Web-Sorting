package com.juls.labs.websorting.repository;

import com.juls.labs.websorting.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface EventRepository {

    Event save(Event event);
    Optional<Event> findById(Long eventId);
    List<Event> findAll();
    void delete(Long eventId);
    Event update(Event event);
    List <Event> getEventByDate(LocalDate localDate);
    List <Event> upcomingEvents();
    Optional <Event> getEventByName(String eventName);

}
