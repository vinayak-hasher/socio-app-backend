package com.example.final_assignment.services;

import com.example.final_assignment.dto.LoginDto;
import com.example.final_assignment.dto.LoginResponseDto;
import com.example.final_assignment.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
//    private final SessionService sessionService;

    public LoginResponseDto login(LoginDto loginDto){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        User user=(User) authentication.getPrincipal();

        if(user.getPasswordChangedAt().isBefore(LocalDateTime.now().minusHours(1))){
            System.out.println("Password Expired");
            throw new RuntimeException("Password Expired. Please change your password");
        }

        String jwtToken=jwtService.generateToken(user);

        return new LoginResponseDto(user.getId(),jwtToken,user.getRole());
    }

}
