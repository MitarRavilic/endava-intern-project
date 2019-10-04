package com.endava.server.dto.response;

import lombok.Getter;

@Getter
public class JwtResponse {

    private final String jwttoken;

    public JwtResponse(String jwttoken) { this.jwttoken = jwttoken;}
}
