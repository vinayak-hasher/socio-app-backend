package com.example.final_assignment.dto;

import com.example.final_assignment.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String description;
    private String createdBy;
    private int likes;
    private List<String> comments;

}
