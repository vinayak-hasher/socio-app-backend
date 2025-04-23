package com.example.final_assignment.services;

import com.example.final_assignment.dto.*;
import com.example.final_assignment.entities.PostEntity;
import com.example.final_assignment.entities.User;
import com.example.final_assignment.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto createPost(CreatePostDto postDto, User user){
        System.out.println("Create Post");
        PostEntity PostToBeCreated=modelMapper.map(postDto,PostEntity.class);
        PostToBeCreated.setCreatedBy(user);
        PostEntity savedPost= postRepository.save(PostToBeCreated);
        return PostDto.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .description(savedPost.getDescription())
                .createdBy(savedPost.getCreatedBy().getName())
                .likes(savedPost.getLikes())
                .comments(savedPost.getComments())
                .build();
    }

    public List<PostDto> getAllPosts(String search){
        List<PostEntity> posts;
        if(search!=null && !search.isEmpty()){
            posts=postRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search,search);
        }
        else{
            posts=postRepository.findAll();
        }
        return posts
                .stream()
                .map(post->PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .createdBy(post.getCreatedBy().getName())
                        .likes(post.getLikes())
                        .comments(post.getComments())
                        .build())
                .collect(Collectors.toList());
    }

    public PostEntity getPostById(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("Post not found with the given id"));
    }

    public PostDto updatePost(Long postId, UpdatePostDto postDto){
        PostEntity existingPost= postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post Not Found"));
        existingPost.setTitle(postDto.getTitle());
        existingPost.setDescription(postDto.getDescription());
        PostEntity updatedPost=postRepository.save(existingPost);
        return modelMapper.map(updatedPost,PostDto.class);

    }

    public ResponseEntity<Void> deletePostById(Long postId){
        if(postRepository.existsById(postId)){
            postRepository.deleteById(postId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
