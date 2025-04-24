package com.example.final_assignment.dto;

import com.example.final_assignment.entities.enums.Role;
import com.example.final_assignment.entities.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email,name;
    private Role role;
    private Visibility visibility;
    private List<String> followers;
    private List<String> following;
}
