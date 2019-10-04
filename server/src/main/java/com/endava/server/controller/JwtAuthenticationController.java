package com.endava.server.controller;

import com.endava.server.dto.response.JwtResponse;
import com.endava.server.dto.request.UserDTOLogin;
import com.endava.server.dto.request.UserDTORegister;
import com.endava.server.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationController {


    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserDTOLogin login) throws Exception {

       JwtResponse token =  authenticationService.createAuthenticationToken(login);
       return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTORegister registerDTO) {
        authenticationService.register(registerDTO);
        String message = "successful registration";
        // add login
        return ResponseEntity.ok(message);
    }
}
