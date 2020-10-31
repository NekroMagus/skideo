package net.skideo.controller;


import net.skideo.dto.UserDto;
import net.skideo.dto.UserProfileDto;
import net.skideo.model.enums.RoleFootball;
import net.skideo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

@RestController
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserProfileDto getProfile() {
        return userService.getProfile();
    }

    @PutMapping("/profile")
    public ResponseEntity editProfile(@RequestBody UserDto newUser) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.editUser(newUser, login);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/roleFootball")
    public ResponseEntity getRoleFootball() {
        return ResponseEntity.ok(Arrays.toString(RoleFootball.values()));
    }

}