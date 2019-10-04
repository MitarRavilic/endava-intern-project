package com.endava.server.service;

import com.endava.server.dto.JwtResponse;
import com.endava.server.dto.UserDTO;
import com.endava.server.dto.UserDTOLogin;
import com.endava.server.dto.UserDTORegister;
import com.endava.server.model.Role;
import com.endava.server.model.User;
import com.endava.server.repository.UserRepository;
import com.endava.server.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements UserDetailsService{

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.getUserByUsername(username);
        ArrayList<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), role);
    }

    private void authenticate(UserDTOLogin userDTOLogin) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTOLogin.getUsername(), userDTOLogin.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public void register(UserDTORegister userDTORegister) {
        userService.createUser(userDTORegister);
    }

    public JwtResponse createAuthenticationToken(UserDTOLogin userDTOLogin) throws Exception {
        authenticate(userDTOLogin);
        final UserDetails userDetails = loadUserByUsername(userDTOLogin.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new  JwtResponse(token);
    }

}

