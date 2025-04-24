package com.example.final_assignment.services;

import com.example.final_assignment.dto.GroupPostDto;
import com.example.final_assignment.entities.PostEntity;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.entities.UserGroup;
import com.example.final_assignment.repositories.GroupRepository;
import com.example.final_assignment.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupPostService {

    private final PostRepository postRepository;
    private final GroupRepository groupRepository;

    public GroupPostDto createGroupPost(Long groupId, GroupPostDto dto, User user) {
        UserGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getMembers().contains(user)) {
            log.error("Group post creation failed");
            throw new RuntimeException("Only group members can post.");
        }

        PostEntity post = PostEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .group(group)
                .createdBy(user)
                .build();

        log.info("Group Post Creation Sucess");
        return toDto(postRepository.save(post));
    }

    public GroupPostDto updateGroupPost(Long groupId, Long postId, GroupPostDto dto, User user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getGroup().getId().equals(groupId)) {
            log.error("Error updating as post not belongs to group");
            throw new RuntimeException("Post does not belong to this group.");
        }

        if (!post.getCreatedBy().equals(user)) {
            log.error("Error updating the post as you are not the creator");
            throw new RuntimeException("Only the creator can update this post.");
        }

        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        log.info("Group Post Update Sucessful");
        return toDto(postRepository.save(post));
    }

    private GroupPostDto toDto(PostEntity post) {
        return GroupPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .groupId(post.getGroup().getId())
                .createdBy(post.getCreatedBy().getName())
                .build();
    }
}
