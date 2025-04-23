package com.example.final_assignment.repositories;

import com.example.final_assignment.entities.PostEntity;
import com.example.final_assignment.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {
    List<PostEntity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    Optional<PostEntity> findById(Long postId);

}
