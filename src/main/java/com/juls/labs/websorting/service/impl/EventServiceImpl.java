package com.juls.labs.websorting.service.impl;

import com.juls.labs.websorting.model.Event;
import com.juls.labs.websorting.repository.EventRepository;
import com.juls.labs.websorting.repository.impl.EventRepositoryImpl;
import com.juls.labs.websorting.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class EventServiceImpl implements EventService {


    @Autowired
    private final  EventRepositoryImpl eventRepository;

    public EventServiceImpl(EventRepositoryImpl eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public Event createEvent(Event event) {
        event.setEventId(getAllEvents().size()+1L);
        event.setParticipants(new ArrayList<>());
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> getEventById(Long eventId) {
        return this.eventRepository.findById(eventId);
    }



    @Override
    public List<Event> getAllEvents() {
        return this.eventRepository.findAll();
    }

    @Override
    public Event updateEvent(Event event) {
        return this.eventRepository.update(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        this.eventRepository.delete(eventId);
    }

    @Override
    public Optional<Event> searchEvent(String eventName) {
        return this.eventRepository.getEventByName(eventName);
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return this.eventRepository.upcomingEvents();
    }

    @Override
    public List<Event> getEventByDate(LocalDate eventDate) {
        return this.eventRepository.getEventByDate(eventDate);
    }

}
