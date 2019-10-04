package com.endava.server.dto;

import com.endava.server.model.Role;
import com.endava.server.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class UserDTOfull {

    @Nullable
    private Long id;

    private String username;

    private String password;

    @Nullable
    private String email;

    private Role role;


    public UserDTOfull(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
