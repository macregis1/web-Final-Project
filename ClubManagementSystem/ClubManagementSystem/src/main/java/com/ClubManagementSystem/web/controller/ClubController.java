package com.ClubManagementSystem.web.controller;

import com.ClubManagementSystem.web.dto.ClubDto;
import com.ClubManagementSystem.web.models.Club;
import com.ClubManagementSystem.web.models.UserEntity;
import com.ClubManagementSystem.web.security.SecurityUtil;
import com.ClubManagementSystem.web.service.ClubService;
import com.ClubManagementSystem.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ClubController {
    private ClubService clubService;
    private UserService userService;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String sender;
    @Autowired
    public ClubController(ClubService clubService, UserService userService) {
        this.userService = userService;
        this.clubService = clubService;
    }

    @GetMapping("/clubs")
    public String listClubs(Model model) {
        UserEntity user = new UserEntity();
        List<ClubDto> clubs = clubService.findAllClubs();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("clubs", clubs);
        return "clubs-list";
    }

    @GetMapping("/clubs/{clubId}")
    public String clubDetail(@PathVariable("clubId") long clubId, Model model) {
        UserEntity user = new UserEntity();
        ClubDto clubDto = clubService.findClubById(clubId);
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("club", clubDto);
        return "clubs-detail";
    }

    @GetMapping("/clubs/new")
    public String createClubForm(Model model) {
        Club club = new Club();
        model.addAttribute("club", club);
        return "clubs-create";
    }

    @GetMapping("/clubs/{clubId}/delete")
    public String deleteClub(@PathVariable("clubId")Long clubId) {
        clubService.delete(clubId);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/search")
    public String searchClubs(@RequestParam(value = "query") String query, Model model) {
        List<ClubDto> clubs;
        if (query != null && !query.isEmpty()) {
            clubs = clubService.searchClubs(query);
        } else {
            clubs = clubService.findAllClubs();
        }
        model.addAttribute("clubs", clubs);
        return "clubs-list";
    }
    @PostMapping("/clubs/new")
    public String saveClub(@Valid @ModelAttribute("club") ClubDto clubDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("club", clubDto);
            return "clubs-create";
        }
        String username = SecurityUtil.getSessionUser();
//        if (username != null) {
//            UserEntity user = userService.findByUsername(username);
//            clubDto.setCreatedBy(user); // Set the 'created_by' field
//        }
        clubService.saveClub(clubDto);

//        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            UserEntity user = userService.findByUsername(username);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender); // Set your email here
            mailMessage.setTo(user.getEmail());
            mailMessage.setText("Hello " + user.getUsername() + ",\n\nThank you for creating a club!\n\nBest regards,\nYour Club Management System");
            mailMessage.setSubject("Club Creation Confirmation");

            javaMailSender.send(mailMessage);
        }

        return "redirect:/clubs";
    }


    @GetMapping("/clubs/{clubId}/edit")
    public String editClubForm(@PathVariable("clubId") Long clubId, Model model) {
        ClubDto club = clubService.findClubById(clubId);
        model.addAttribute("club", club);
        return "clubs-edit";
    }
    @PostMapping("/clubs/{clubId}/edit")
    public String updateClub(@PathVariable("clubId") Long clubId,
                             @Valid @ModelAttribute("club") ClubDto club,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("club", club);
            return "clubs-edit";
        }
        club.setId(clubId);
        clubService.updateClub(club);
        return "redirect:/clubs";
    }
}
