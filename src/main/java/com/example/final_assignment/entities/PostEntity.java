package com.example.final_assignment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
//    private Long likes;
//    private Long comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<PostLikeEntity> likes=new HashSet<>();

    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments=new ArrayList<>();

    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Report> reports=new ArrayList<>();

}
