package com.ClubManagementSystem.web.repository;

import com.ClubManagementSystem.web.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
