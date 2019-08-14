package com.endava.server.service;

import com.endava.server.dto.UserDTO;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.User;
import com.endava.server.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserDTO getUser(Long userId){
        return new UserDTO(userRepository.findById(userId).orElseThrow(() -> new  ResourceNotFoundException("User", "userId", userId)));
    }

    public UserDTO testDataFlow(UserDTO userDTO){
        return userDTO;
    }

    public UserDTO createUser(UserDTO userDTO){
       return new UserDTO(userRepository.save(new User(userDTO)));
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);
        return userDTO; // probably wrong
    }



}
