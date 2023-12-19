package com.ClubManagementSystem.web.service;

import com.ClubManagementSystem.web.dto.ClubDto;
import com.ClubManagementSystem.web.models.Club;

import java.util.List;

public interface ClubService {
    List<ClubDto> findAllClubs();
    Club saveClub(ClubDto clubDto);
    ClubDto findClubById(Long clubId);
    void updateClub(ClubDto club);
    void delete(Long clubId);
    List<ClubDto> searchClubs(String query);
}
