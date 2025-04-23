package com.example.final_assignment.dto;

import com.example.final_assignment.entities.enums.Visibility;
import lombok.Data;

@Data
public class UpdateUserDto {
    private String email;
    private String name;
    private Visibility visibility;
}
