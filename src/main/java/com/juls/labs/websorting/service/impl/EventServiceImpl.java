package com.juls.labs.websorting.service.impl;

import com.juls.labs.websorting.exception.EventNotFoundException;
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
    public Optional<Event> getEventById(Long eventId) throws EventNotFoundException {
        return Optional.ofNullable(this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with Id does not exist: " + eventId)));
    }



    @Override
    public List<Event> getAllEvents() {
        return this.eventRepository.findAll();
    }

    private boolean eventExist(Long eventId) throws EventNotFoundException {
        return this.getEventById(eventId).isPresent();
    }

    @Override
    public Event updateEvent(Event event) throws EventNotFoundException {
        if (eventExist(event.getEventId())){
            throw new EventNotFoundException("Event does not exist");
        }
        return this.eventRepository.update(event);
    }

    @Override
    public void deleteEvent(Long eventId) throws EventNotFoundException {
        if (eventExist(eventId)){
            throw new EventNotFoundException("Event does not exist");
        }
        this.eventRepository.delete(eventId);
    }

    @Override
    public Optional<Event> searchEvent(String eventName) throws EventNotFoundException {
        return Optional.ofNullable(this.eventRepository.getEventByName(eventName))
                .orElseThrow(() -> new EventNotFoundException("Event Not found"));
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return this.eventRepository.upcomingEvents();
    }

    @Override
    public List<Event> getEventByDate(LocalDate eventDate) {
        if(eventDate == null){
            throw new IllegalArgumentException("Specify a date value");
        }
        return this.eventRepository.getEventByDate(eventDate);
    }

}
