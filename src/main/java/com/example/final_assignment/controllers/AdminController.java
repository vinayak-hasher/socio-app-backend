package com.example.final_assignment.controllers;

import com.example.final_assignment.dto.*;
import com.example.final_assignment.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.hibernate.query.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;

//import java.awt.print.Pageable;
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
    public ResponseEntity<Page<UserFollowerDto>> getUsersByFollowers(Pageable pageable) {
        log.info("Fetching users grouped by join date and sorted by follower count");
        return ResponseEntity.ok(adminService.getUsersByFollowersCount(pageable));
    }

    @GetMapping("/posts/analytics")
    public ResponseEntity<Page<PostAnalyticsDto>> getPostAnalytics(Pageable pageable) {
        log.info("Fetching post analytics");
        return ResponseEntity.ok(adminService.getPostAnalytics(pageable));
    }

    @GetMapping("/posts/reported")
    public ResponseEntity<Page<ReportedPostDto>> getReportedPosts(Pageable pageable) {
        log.info("Fetching all reported posts grouped by metadata");
        return ResponseEntity.ok(adminService.getReportedPostStats(pageable));
    }

    @GetMapping("/groups/ranking")
    public ResponseEntity<Page<GroupStatsDto>> getGroupStats(Pageable pageable) {
        log.info("Fetching group rankings by members and posts");
        return ResponseEntity.ok(adminService.getGroupRankings(pageable));
    }

    @PutMapping("/posts/{postId}/action")
    public ResponseEntity<String> actOnReportedPost(@PathVariable Long postId,
                                                    @RequestParam String action) {
        log.info("Admin taking action '{}' on post ID: {}", action, postId);
        adminService.actOnReportedPost(postId, action);
        return ResponseEntity.ok("Action performed: " + action);
    }

    @PostMapping(value = "/users/bulk-upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload users from Excel file")
    @ApiResponse(responseCode = "200", description = "Upload done")
    public ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file) {
        log.info("Starting bulk upload of users from file: {}", file.getOriginalFilename());
        adminService.bulkUploadUsers(file);
        return ResponseEntity.ok("Bulk user upload started");
    }
}

