package com.example.final_assignment.repositories;

import com.example.final_assignment.entities.UserGroup;
import com.example.final_assignment.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByNameContainingIgnoreCase(String search);
    List<UserGroup> findByCreatedByOrMembersContains(User creator, User member);
}

