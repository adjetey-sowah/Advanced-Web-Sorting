package com.juls.labs.websorting.repository;

import com.juls.labs.websorting.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Event save(Event event);
    Optional<Event> findById(Long eventId);
    List<Event> findAll();
    void delete(Long eventId);
    Event update(Event event);

}
