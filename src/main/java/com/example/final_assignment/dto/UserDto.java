package com.example.final_assignment.dto;

import com.example.final_assignment.entities.enums.Role;
import com.example.final_assignment.entities.enums.Visibility;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email,name;
    private Role role;
    private Visibility visibility;
}
