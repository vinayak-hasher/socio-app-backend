package com.example.final_assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.PrimitiveIterator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostAnalyticsDto {
    private Long id;
    private String title;
    private int likeCount;
    private int commentCount;
    private String user;
    private Long groupId;
    private LocalDate createdAt;
}
