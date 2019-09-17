package com.endava.server.dto;

import lombok.Getter;

@Getter
public class JwtResponse {

    private final String jwttoken;

    public JwtResponse(String jwttoken) { this.jwttoken = jwttoken;}
}
