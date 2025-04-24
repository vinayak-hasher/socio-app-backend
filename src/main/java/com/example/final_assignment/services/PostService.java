package com.example.final_assignment.services;

import com.example.final_assignment.dto.*;
import com.example.final_assignment.entities.*;
import com.example.final_assignment.repositories.PostRepository;
import jakarta.transaction.Transactional;
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
                .likes(savedPost.getLikes().size())
                .comments(savedPost.getComments().stream()
                        .map(Comment::getContent)
                        .collect(Collectors.toList()))
                .sharedFromUser(savedPost.getOriginalUser()!=null ? savedPost.getOriginalUser().getName():null)
                .sharedFromPostId(savedPost.getOriginalPost()!=null ?savedPost.getOriginalPost().getId():null)
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
                        .likes(post.getLikes().size())
                        .comments(post.getComments().stream()
                                .map(Comment::getContent)
                                .collect(Collectors.toList()))
                        .sharedFromUser(post.getOriginalUser()!=null ? post.getOriginalUser().getName():null)
                        .sharedFromPostId(post.getOriginalPost()!=null ?post.getOriginalPost().getId():null)
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

    public void likePost(Long postId, User user){
        PostEntity post=postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("Post Not Found"));

        boolean alreadyLiked= post.getLikes().stream()
                .anyMatch(like->like.getUser().equals(user));

        if(!alreadyLiked){
            post.getLikes().add(PostLikeEntity.builder().post(post).user(user).build());
            postRepository.save(post);
        }
    }

    public void commentPost(Long postId, String content, User user){
        PostEntity post=postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("Post Not Found"));

        post.getComments().add(Comment.builder().post(post).user(user).content(content).build());
        postRepository.save(post);

    }

    public void reportPost(Long postId, String reason, User user){
        PostEntity post=postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("Post Not Found"));

        boolean alreadyReported= post.getReports().stream()
                .anyMatch(report->report.getUser().equals(user));

        if(!alreadyReported) {
            post.getReports().add(Report.builder().post(post).user(user).reason(reason).build());
            postRepository.save(post);
        }
    }

    @Transactional
    public void sharePost(Long postId, User user){
        PostEntity original =postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("Post Not Found"));

        PostEntity shared= PostEntity.builder()
                .title("[Shared]"+original.getTitle())
                .description(original.getDescription())
                .createdBy(user)
                .originalPost(original)
                .originalUser(original.getCreatedBy())
                .build();

        postRepository.save(shared);
    }
}

