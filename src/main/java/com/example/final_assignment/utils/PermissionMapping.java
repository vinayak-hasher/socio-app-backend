package com.example.final_assignment.utils;

import com.example.final_assignment.entities.enums.Permission;
import com.example.final_assignment.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.final_assignment.entities.enums.Permission.*;
import static com.example.final_assignment.entities.enums.Role.ADMIN;
import static com.example.final_assignment.entities.enums.Role.USER;

public class PermissionMapping {

    private static final Map<Role, Set<Permission>> map= Map.of(
            USER,Set.of(USER_VIEW,POST_VIEW),
            ADMIN, Set.of(USER_DELETE,USER_CREATE,POST_DELETE)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());

    }
}
