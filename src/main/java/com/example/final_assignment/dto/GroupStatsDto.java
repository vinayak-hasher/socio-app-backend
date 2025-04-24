package com.example.final_assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupStatsDto {
    private Long groupId;
    private String name;
    private int memberCount;
    private int postCount;
}
