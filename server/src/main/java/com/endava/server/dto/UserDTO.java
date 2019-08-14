package com.endava.server.dto;

import com.endava.server.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDTO {

  //  private Long id;

    private String username;

    private String email;

    private String password;

    public UserDTO(User user) {
    //    this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
