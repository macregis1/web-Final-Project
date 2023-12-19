package com.ClubManagementSystem.web.service;

import com.ClubManagementSystem.web.dto.EventDto;

import java.util.List;

public interface EventService {
    void createEvent(Long clubId, EventDto eventDto);
    List<EventDto> findAllEvents();
    EventDto findByEventId(Long eventId);
    void updateEvent(EventDto eventDto);
    void deleteEvent(long eventId);
}
