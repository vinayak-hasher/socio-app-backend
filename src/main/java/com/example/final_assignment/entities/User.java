package com.example.final_assignment.entities;

import com.example.final_assignment.entities.enums.Role;
import com.example.final_assignment.entities.enums.Visibility;
import com.example.final_assignment.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role =Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility =Visibility.PUBLIC;

    @Column(name="password_last_changed")
    private LocalDateTime passwordChangedAt= LocalDateTime.now();

    @PrePersist
    @PreUpdate
    private void validateRoleAndEmail(){
        if(role==Role.ADMIN && !email.endsWith("@socio.com")){
            log.error("Email of Admin must end with domain @socio.com");
            throw new IllegalArgumentException("Email is not Valid for admin role");
        }
    }

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name="user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers= new HashSet<>();


    @ManyToMany(mappedBy = "followers",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<User> following= new HashSet<>();



    public User(Long id, String email, String password){
        this.id=id;
        this.email=email;
        this.password=password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> authorities= new HashSet<>();
        authorities.addAll(PermissionMapping.getAuthoritiesForRole(role));
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));

        return authorities;
    }

    @Override
    public String getPassword(){return this.password;}

    @Override
    public String getUsername(){return this.email;}
}
