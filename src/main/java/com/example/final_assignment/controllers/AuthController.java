package com.example.final_assignment.controllers;

import com.example.final_assignment.dto.*;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.repositories.UserRepo;
import com.example.final_assignment.services.AuthService;
import com.example.final_assignment.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto){
        UserDto userDto=userService.signUp(signUpDto);
        log.info("User Signup sucess");
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login (@RequestBody LoginDto loginDto, HttpServletResponse response){
        LoginResponseDto loginResponseDto= authService.login(loginDto);

        Cookie cookie= new Cookie("jwtToken",loginResponseDto.getJwtToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("User Log in success");
        return ResponseEntity.ok(loginResponseDto);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto, @AuthenticationPrincipal User user){
        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(),user.getPassword())){
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepo.save(user);

        log.info("Password Changed successfully");
        return ResponseEntity.ok("Password changed successfully");
    }
}
