package com.example.final_assignment.services;

import com.example.final_assignment.dto.*;
import com.example.final_assignment.entities.PostEntity;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.entities.enums.Role;
import com.example.final_assignment.repositories.GroupRepository;
import com.example.final_assignment.repositories.PostRepository;
import com.example.final_assignment.repositories.UserRepo;
import com.example.final_assignment.utils.ExcelUserParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.hibernate.query.Page;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import java.awt.print.Pageable;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepo userRepo;
    private final PostRepository postRepo;
    private final GroupRepository groupRepo;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    public void addOrUpdateAdmin(SignUpDto dto) {
        User user = userRepo.findByEmail(dto.getEmail()).orElse(new User());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(Role.ADMIN);
        userRepo.save(user);
        log.info("Admin user saved/updated: {}", user.getEmail());
    }

    public Page<UserFollowerDto> getUsersByFollowersCount(Pageable pageable) {
        List<UserFollowerDto> list= userRepo.findAll().stream()
                .map(user -> UserFollowerDto.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .followerCount(user.getFollowers().size())
                        .build())
                .sorted(Comparator.comparingInt(UserFollowerDto::getFollowerCount).reversed())
                .collect(Collectors.toList());

        return paginate(list,pageable);
    }

    public Page<PostAnalyticsDto> getPostAnalytics(Pageable pageable) {
        List<PostAnalyticsDto> list= postRepo.findAll().stream()
                .map(post -> PostAnalyticsDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .likeCount(post.getLikes().size())
                        .commentCount(post.getComments().size())
                        .groupId(post.getGroup() != null ? post.getGroup().getId() : null)
                        .user(post.getCreatedBy().getEmail())
                        .build())
                .sorted(Comparator.comparingInt((PostAnalyticsDto p) -> p.getLikeCount() + p.getCommentCount()).reversed())
                .collect(Collectors.toList());

        return paginate(list,pageable);
    }

    public Page<ReportedPostDto> getReportedPostStats(Pageable pageable) {
        List<ReportedPostDto> list= postRepo.findAll().stream()
                .filter(post -> !post.getReports().isEmpty())
                .map(post -> ReportedPostDto.builder()
                        .postId(post.getId())
                        .reportCount(post.getReports().size())
                        .reportedBy(post.getReports().stream().map(r -> r.getUser().getEmail()).collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());

        return paginate(list,pageable);
    }

    public Page<GroupStatsDto> getGroupRankings(Pageable pageable) {
        List<GroupStatsDto> list= groupRepo.findAll().stream()
                .map(group -> GroupStatsDto.builder()
                        .groupId(group.getId())
                        .name(group.getName())
                        .memberCount(group.getMembers().size())
                        .build())
                .sorted(Comparator.comparingInt(GroupStatsDto::getMemberCount).reversed())
                .collect(Collectors.toList());

        return paginate(list,pageable);
    }

    public void actOnReportedPost(Long postId, String action) {
        PostEntity post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        switch (action.toLowerCase()) {
            case "delete" -> postRepo.delete(post);
            case "flag" -> log.warn("Post {} flagged for review", postId);
            case "ignore" -> log.info("Post {} ignored", postId);
            default -> throw new IllegalArgumentException("Invalid action: " + action);
        }
    }

    public void bulkUploadUsers(MultipartFile file) {
        try (InputStream input = file.getInputStream()) {
            List<User> users = ExcelUserParser.parse(input);
            users.forEach(user -> {
                if (user.getRole().equals(Role.ADMIN) && !user.getEmail().endsWith("@socio.com")) {
                    throw new IllegalArgumentException("Admin email must end with @socio.com");
                }
                user.setPassword(encoder.encode(user.getPassword()));
                userRepo.save(user);
            });
            log.info("Bulk upload completed: {} users", users.size());
        } catch (IOException e) {
            log.error("Failed to process bulk upload: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process file");
        }
    }

    private <T> Page<T> paginate(List<T> list, Pageable pageable){
        int start=(int) pageable.getOffset();
        int end=Math.min((start+pageable.getPageSize()),list.size());
        List<T> pageList= (start>=list.size())? new ArrayList<>():list.subList(start,end);
        return new PageImpl<>(pageList,pageable,list.size());
    }
}
