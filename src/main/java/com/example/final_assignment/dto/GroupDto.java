package com.example.final_assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {
    private Long id;
    private String name;
    private String createdBy;
    private Set<String> members;
    private LocalDateTime createdAt;
}
