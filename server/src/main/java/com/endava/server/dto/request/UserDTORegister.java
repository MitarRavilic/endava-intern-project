package com.endava.server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@Getter
public class UserDTORegister {

    private String username;

    private String password;

    private String email;

}
