package com.example.final_assignment.services;

import com.example.final_assignment.dto.SignUpDto;
import com.example.final_assignment.dto.UpdateUserDto;
import com.example.final_assignment.dto.UserDto;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.entities.enums.Role;
import com.example.final_assignment.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.Resolution;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userRepo.findByEmail(username)
                .orElseThrow(()->new BadCredentialsException("User Not found with the given email"));
    }

    public UserDto toDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .visibility(user.getVisibility())
                .role(user.getRole())
                .followers(user.getFollowers().stream().map(User::getName).collect(Collectors.toList()))
                .following(user.getFollowing().stream().map(User::getName).collect(Collectors.toList()))
                .build();

    }

    public List<UserDto> getAllUsers(String search){
        List<User> users;
        if(search!=null && !search.isEmpty()){
            users=userRepo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search,search);
        }
        else{
            users=userRepo.findAll();
        }
        return users
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public User getUserById(Long userId){
        return userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found with the given id"));
    }

    public UserDto updateUser(Long userId, UpdateUserDto userDto){
        User exisitingUser= userRepo.findById(userId).orElseThrow(()->new RuntimeException("User Not exists"));
        exisitingUser.setName(userDto.getName());
        exisitingUser.setEmail(userDto.getEmail());
        exisitingUser.setVisibility(userDto.getVisibility());
        User updatedUser=userRepo.save(exisitingUser);
        log.info("User updated in the database");
        return modelMapper.map(updatedUser,UserDto.class);

    }

    public ResponseEntity<Void> deleteById(Long userId){
        if(userRepo.existsById(userId)){
            userRepo.deleteById(userId);
            log.info("user Deleted with id :"+userId);
            return ResponseEntity.ok().build();
        }
        log.error("No User found with the id: "+userId);
        return ResponseEntity.notFound().build();
    }

//    public User getUserByEmail(String email){return userRepo.findByEmail(email).orElse(null);}

    public UserDto signUp(SignUpDto signUpDto){
        Optional<User> user= userRepo.findByEmail(signUpDto.getEmail());
        if(user.isPresent()){
            log.error("User already exists with the mail:"+signUpDto.getEmail());
            throw new BadCredentialsException("User with email already exists"+signUpDto.getEmail());
        }

        User toBeCreatedUser= modelMapper.map(signUpDto, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));
        User saveduser=userRepo.save(toBeCreatedUser);
        log.info("user created success at service level and saved to DB");
        return modelMapper.map(saveduser,UserDto.class);
    }

    public User save(User user){return userRepo.save(user);}

    @Transactional
    public void followUser(Long userId,User user){
        User targetUser= getUserById(userId);
        User currentUser=userRepo.findById(user.getId())
                .orElseThrow(()->new RuntimeException("Current user not found"));
        if(targetUser.equals(currentUser)) throw new RuntimeException("You can't follow yourself");
        if(targetUser.getFollowers().contains(currentUser)) throw new RuntimeException("You already follows this user");


        targetUser.getFollowers().add(currentUser);
        currentUser.getFollowing().add(targetUser);

        userRepo.save(targetUser);
        userRepo.save(currentUser);

        log.info("Follow success");
    }

    @Transactional
    public void unfollowUser(Long userId,User user){
        User targetUser= getUserById(userId);
        User currentUser=userRepo.findById(user.getId())
                .orElseThrow(()->new RuntimeException("Current user not found"));

        if(targetUser.equals(currentUser)) throw new RuntimeException("You can't unfollow yourself");

        targetUser.getFollowers().remove(currentUser);
        currentUser.getFollowing().remove(targetUser);

        userRepo.save(targetUser);
        userRepo.save(currentUser);

        log.info("Unfollow success");
    }


//    public void importUsersFromFile(MultipartFile file){
//        try{
//            List<User> users;
//
//            if(file.getOriginalFilename().endsWith(".csv")){
//                users=CsvUserParser.parse(file.getInputStream());
//            }else if(file.getOriginalFilename().endsWith(".xlsx")){
//                users=ExcelUserParser.parse(file.getInputStream());
//            }
//            else{
//                throw new IllegalArgumentException("Unsupported File Format");
//            }
//
//
//            for (User user: users){
//                if(user.getRole()== Role.ADMIN && !user.getEmail().endsWith("@socio.com")){
//                    throw new IllegalArgumentException("Admin email must end with error");
//                }
//                user.setPassword(passwordEncoder.encode(user.getPassword()));
//                userRepo.save(user);
//            }
//        }
//        catch (IOException e){
//            throw new RuntimeException("Failed to process file",e);
//        }
//    }
}

