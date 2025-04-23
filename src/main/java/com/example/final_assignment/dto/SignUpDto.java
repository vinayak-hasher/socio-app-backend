package com.example.final_assignment.dto;

import com.example.final_assignment.entities.enums.Permission;
import com.example.final_assignment.entities.enums.Role;
import com.example.final_assignment.entities.enums.Visibility;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {
    private String email,name,password;
    private Role role;
    private Visibility visibility;
}
