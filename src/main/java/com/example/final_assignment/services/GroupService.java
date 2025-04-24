package com.example.final_assignment.services;

import com.example.final_assignment.dto.CreateGroupDto;
import com.example.final_assignment.dto.GroupDto;
import com.example.final_assignment.entities.UserGroup;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.repositories.GroupRepository;
import com.example.final_assignment.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepo userRepository;

    public GroupDto createGroup(CreateGroupDto dto, User creator) {
        log.info("Entering service layer:"+creator.getName());
        UserGroup userGroup = UserGroup.builder()
                .name(dto.getName())
                .createdBy(creator)
                .members(new HashSet<>(List.of(creator)))
                .build();
//        userGroup.getMembers().add(creator);
        log.info("Group Created at Service level");
        return toDto(groupRepository.save(userGroup));
    }

    public GroupDto addMemberToGroup(Long groupId,Long userId){
        UserGroup group=groupRepository.findById(groupId)
                .orElseThrow(()->new RuntimeException("Group Not Found"));

        User user =userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));

        group.getMembers().add(user);
        log.info("Member added");
        return toDto(groupRepository.save(group));
    }

    public GroupDto removeMemberFromGroup(Long groupId,Long userId){
        UserGroup group=groupRepository.findById(groupId)
                .orElseThrow(()->new RuntimeException("Group Not Found"));

        User user =userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));

        if(!user.equals(group.getCreatedBy()))  group.getMembers().remove(user);
        log.info("User Removed");
        return toDto(groupRepository.save(group));
    }

    public List<GroupDto> getAllGroups(String search) {
        List<UserGroup> userGroups = (search != null && !search.isEmpty())
                ? groupRepository.findByNameContainingIgnoreCase(search)
                : groupRepository.findAll();
        return userGroups.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<GroupDto> getMyGroups(User user) {
        List<UserGroup> userGroups = groupRepository.findByCreatedByOrMembersContains(user, user);
        return userGroups.stream().map(this::toDto).collect(Collectors.toList());
    }

    private GroupDto toDto(UserGroup userGroup) {
        return GroupDto.builder()
                .id(userGroup.getId())
                .name(userGroup.getName())
                .createdBy(userGroup.getCreatedBy().getName())
                .members(userGroup.getMembers().stream().map(User::getName).collect(Collectors.toSet()))
                .build();
    }
}

