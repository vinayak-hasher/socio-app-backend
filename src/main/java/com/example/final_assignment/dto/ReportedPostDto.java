package com.example.final_assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportedPostDto {
    private Long postId;
    private int reportCount;
    private Set<String> reportedBy;
    private LocalDate reportedOn;
}
