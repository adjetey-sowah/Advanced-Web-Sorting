package com.juls.labs.websorting.repository.impl;

import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.repository.EventRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements method of the event repository
 * @author Julius Adjetey Sowah
 */
@Repository
public class EventRepositoryImpl implements EventRepository {

    private Map<Long, Event> events = new ConcurrentHashMap<>();


    @Override
    public Event save(Event event) {
        events.put(event.getEventId(), event);
        return event;
    }

    @Override
    public Optional<Event> findById(Long eventId) {
        return Optional.ofNullable(events.get(eventId));
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<>(events.values());
    }

    @Override
    public void delete(Long eventId) {
        if(events.containsKey(eventId)){
            events.remove(eventId);
        }

    }

    @Override
    public Event update(Event event) {
        if(events.containsKey(event.getEventId())){
            events.put(event.getEventId(), event);
        }
        return event;
    }
}
