package com.example.final_assignment.controllers;

import com.example.final_assignment.dto.PostDto;
import com.example.final_assignment.dto.UpdateUserDto;
import com.example.final_assignment.dto.UserDto;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.repositories.UserRepo;
import com.example.final_assignment.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
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
        User user=userService.getUserById(userId);
        return modelMapper.map(user,UserDto.class);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){ return userService.deleteById(userId);}

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false) String search){
        return userService.getAllUsers(search);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UpdateUserDto userDto){
        UserDto userDto1= userService.updateUser(userId,userDto);
        return ResponseEntity.ok(userDto1);
    }
}
