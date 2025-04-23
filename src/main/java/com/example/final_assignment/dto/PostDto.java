package com.example.final_assignment.dto;

import com.example.final_assignment.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String description;
    private Long likes;
    private Long comments;
    private String createdBy;
}
