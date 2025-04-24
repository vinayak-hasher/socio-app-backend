package com.example.final_assignment.controllers;

import com.example.final_assignment.dto.CreatePostDto;
import com.example.final_assignment.dto.PostDto;
import com.example.final_assignment.dto.UpdatePostDto;
import com.example.final_assignment.dto.UserDto;
import com.example.final_assignment.entities.PostEntity;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.services.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private ModelMapper modelMapper;

    @PostMapping("/create-post")
    @PreAuthorize("isAuthenticated()")
    public PostDto createPost(@RequestBody CreatePostDto createPostDto, @AuthenticationPrincipal User user){
        return postService.createPost(createPostDto, user);
    }

    @GetMapping("/{postId}")
    public PostDto getPostById(@PathVariable Long postId){
        PostEntity post=postService.getPostById(postId);
        return modelMapper.map(post,PostDto.class);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId){ return postService.deletePostById(postId);}

    @GetMapping
    public List<PostDto> getAllPosts(@RequestParam(required = false) String search){
        return postService.getAllPosts(search);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updateUser(@PathVariable Long postId, @RequestBody UpdatePostDto updatePostDto){
        PostDto postDto= postService.updatePost(postId,updatePostDto);
        return ResponseEntity.ok(postDto);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @AuthenticationPrincipal User user){
        postService.likePost(postId,user);
        return ResponseEntity.ok("Post Liked");
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> commentPost(@PathVariable Long postId,
                                           @RequestBody String content,
                                           @AuthenticationPrincipal User user){
        postService.commentPost(postId, content,user);
        return ResponseEntity.ok("Comment Posted");
    }

    @PostMapping("/{postId}/report")
    public ResponseEntity<String> reportPost(@PathVariable Long postId,
                                           @RequestBody String reason,
                                           @AuthenticationPrincipal User user){
        postService.reportPost(postId,reason,user);
        return ResponseEntity.ok("Post Reported");
    }

    @PostMapping("/{postId}/share")
    public ResponseEntity<String> sharePost(@PathVariable Long postId, @AuthenticationPrincipal User user){
        postService.sharePost(postId,user);
        return ResponseEntity.ok("Post Shared sucessfully");
    }

}
