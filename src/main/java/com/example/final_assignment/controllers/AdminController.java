package com.example.final_assignment.controllers;

import com.example.final_assignment.dto.*;
import com.example.final_assignment.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users")
    public ResponseEntity<String> addOrUpdateAdmin(@RequestBody SignUpDto dto) {
        log.info("Admin request to add/update user with email: {}", dto.getEmail());
        adminService.addOrUpdateAdmin(dto);
        return ResponseEntity.ok("Admin user added/updated successfully");
    }

    @GetMapping("/users/by-followers")
    public ResponseEntity<List<UserFollowerDto>> getUsersByFollowers() {
        log.info("Fetching users grouped by join date and sorted by follower count");
        return ResponseEntity.ok(adminService.getUsersByFollowersCount());
    }

    @GetMapping("/posts/analytics")
    public ResponseEntity<List<PostAnalyticsDto>> getPostAnalytics() {
        log.info("Fetching post analytics");
        return ResponseEntity.ok(adminService.getPostAnalytics());
    }

    @GetMapping("/posts/reported")
    public ResponseEntity<List<ReportedPostDto>> getReportedPosts() {
        log.info("Fetching all reported posts grouped by metadata");
        return ResponseEntity.ok(adminService.getReportedPostStats());
    }

    @GetMapping("/groups/ranking")
    public ResponseEntity<List<GroupStatsDto>> getGroupStats() {
        log.info("Fetching group rankings by members and posts");
        return ResponseEntity.ok(adminService.getGroupRankings());
    }

    @PutMapping("/posts/{postId}/action")
    public ResponseEntity<String> actOnReportedPost(@PathVariable Long postId,
                                                    @RequestParam String action) {
        log.info("Admin taking action '{}' on post ID: {}", action, postId);
        adminService.actOnReportedPost(postId, action);
        return ResponseEntity.ok("Action performed: " + action);
    }

//    @PostMapping("/users/bulk-upload")
//    public ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file) {
//        log.info("Starting bulk upload of users from file: {}", file.getOriginalFilename());
//        adminService.bulkUploadUsers(file);
//        return ResponseEntity.ok("Bulk user upload started");
//    }
}

