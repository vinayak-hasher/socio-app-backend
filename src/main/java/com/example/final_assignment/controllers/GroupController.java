package com.example.final_assignment.controllers;

import com.example.final_assignment.dto.GroupDto;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto dto,
                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(groupService.createGroup(dto, user));
    }

    @PutMapping("/{groupId}/add-member/{userId}")
    public ResponseEntity<GroupDto> addMemberToGroup (@PathVariable Long groupId,
                                                      @PathVariable Long userId){
        return ResponseEntity.ok(groupService.addMemberToGroup(groupId,userId));
    }

    @PutMapping("/{groupId}/remove-member/{userId}")
    public ResponseEntity<GroupDto> removeMemberFromGroup (@PathVariable Long groupId,
                                                      @PathVariable Long userId){
        return ResponseEntity.ok(groupService.removeMemberFromGroup(groupId,userId));
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAllGroups(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(groupService.getAllGroups(search));
    }

    @GetMapping("/my")
    public ResponseEntity<List<GroupDto>> getMyGroups(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(groupService.getMyGroups(user));
    }
}

