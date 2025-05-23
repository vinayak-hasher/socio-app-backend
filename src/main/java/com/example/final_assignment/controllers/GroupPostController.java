package com.example.final_assignment.controllers;

import com.example.final_assignment.dto.GroupPostDto;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.services.GroupPostService;
import com.example.final_assignment.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups/{groupId}/posts")
@RequiredArgsConstructor
@Slf4j
public class GroupPostController {

    private final GroupPostService groupPostService;

    @PostMapping
    public ResponseEntity<GroupPostDto> createPost(@PathVariable Long groupId,
                                                   @RequestBody GroupPostDto dto,
                                                   @AuthenticationPrincipal User user) {
        log.info("Creating Post for the Group");
        return ResponseEntity.ok(groupPostService.createGroupPost(groupId, dto, user));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<GroupPostDto> updatePost(@PathVariable Long groupId,
                                                   @PathVariable Long postId,
                                                   @RequestBody GroupPostDto dto,
                                                   @AuthenticationPrincipal User user) {
        log.info("Editing post for the Group");
        return ResponseEntity.ok(groupPostService.updateGroupPost(groupId, postId, dto, user));
    }
}

