package com.juls.labs.websorting.repository.impl;

import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.repository.EventRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This class implements method of the event repository
 * @author Julius Adjetey Sowah
 */
@Repository
public class EventRepositoryImpl implements EventRepository {

    private final Map<Long, Event> events = new ConcurrentHashMap<>();

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
            return event;
        }
        throw new NoSuchElementException("Event not found");
    }

    @Override
    public List<Event> getEventByDate(LocalDate eventDate) {
        return events.values()
                .stream()
                .filter(e -> e.getDate()
                        .isEqual(eventDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> upcomingEvents() {
        return events.values()
                .stream()
                .filter(e -> e.getDate()
                        .isAfter(LocalDate.now()))
                .limit(6)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Event> getEventByName(String eventName) {
        return events.values()
                .stream()
                .filter(e -> e.getEventName().toLowerCase().contains(eventName.toLowerCase()))
                .findFirst();
    }
}
