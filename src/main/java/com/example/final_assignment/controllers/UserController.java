package com.example.final_assignment.controllers;

import com.example.final_assignment.dto.PostDto;
import com.example.final_assignment.dto.UpdateUserDto;
import com.example.final_assignment.dto.UserDto;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.repositories.UserRepo;
import com.example.final_assignment.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
//
//    @PostMapping("/import")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> importUsers(@RequestParam("file")MultipartFile file){
//        userService.importUsersFromFile(file);
//        return ResponseEntity.ok("User import Started");
//    }

//    @GetMapping
//    public List<UserDto> getAllusers() {return userService.getAllUsers();}

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId){
            User user= userService.getUserById(userId);
            return userService.toDto(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        log.info("Deleting user with Id :"+ userId);
        return userService.deleteById(userId);}

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false) String search){
        return userService.getAllUsers(search);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UpdateUserDto userDto){
        UserDto userDto1= userService.updateUser(userId,userDto);
        log.info("Updating user with id : "+ userId);
        return ResponseEntity.ok(userDto1);
    }

    @PostMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> followUser(@PathVariable Long userId, @AuthenticationPrincipal User user){
        userService.followUser(userId,user);
        return ResponseEntity.ok("Followed user sucessfully");
    }

    @PostMapping("/{userId}/unfollow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId, @AuthenticationPrincipal User user){
        userService.unfollowUser(userId,user);
        return ResponseEntity.ok("Unfollowed user sucessfully");
    }
}
