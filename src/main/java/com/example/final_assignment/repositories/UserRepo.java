package com.example.final_assignment.repositories;

import com.example.final_assignment.dto.UserDto;
import com.example.final_assignment.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name,String email);
    Optional<User> findByEmail(String email);
}
