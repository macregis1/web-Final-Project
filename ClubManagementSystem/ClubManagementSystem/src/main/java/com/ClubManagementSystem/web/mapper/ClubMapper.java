package com.ClubManagementSystem.web.mapper;

import com.ClubManagementSystem.web.dto.ClubDto;
import com.ClubManagementSystem.web.models.Club;

import java.util.stream.Collectors;

public class ClubMapper {
    public static Club mapToClub(ClubDto club) {
        Club clubDto = Club.builder()
                .id(club.getId())
                .title(club.getTitle())
                .photoUrl(club.getPhotoUrl())
                .content(club.getContent())
                .createdBy(club.getCreatedBy())
                .createdOn(club.getCreatedOn())
                .updatedOn(club.getUpdatedOn())
                .build();
        return  clubDto;
    }

    public static ClubDto mapToClubDto(Club club) {
        ClubDto clubDto = ClubDto.builder()
                .id(club.getId())
                .title(club.getTitle())
                .photoUrl(club.getPhotoUrl())
                .content(club.getContent())
                .createdBy(club.getCreatedBy())
                .createdOn(club.getCreatedOn())
                .updatedOn(club.getUpdatedOn())
                .events(club.getEvents().stream().map((event) -> EventMapper.mapToEventDto(event)).collect(Collectors.toList()))
                .build();
        return clubDto;
    }
}
