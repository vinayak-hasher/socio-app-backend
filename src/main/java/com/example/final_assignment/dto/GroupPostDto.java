package com.example.final_assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPostDto {
    private Long id;
    private String title;
    private String description;
    private Long groupId;
    private String createdBy;
    private LocalDateTime createdAt;
}
