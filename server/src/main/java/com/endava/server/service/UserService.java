package com.endava.server.service;

import com.endava.server.dto.UserAccountDTOUserView;
import com.endava.server.dto.UserDTO;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserRepository;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public List<User> getAllUsers(){
       return userRepository.findAll();
    }

    public UserDTO getUser(Long userId){
        return new UserDTO(userRepository.findById(userId).orElseThrow(() -> new  ResourceNotFoundException("User", "userId", userId)));
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO){
       return new UserDTO(userRepository.save(new User(userDTO)));
    }


    @Transactional
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return new UserDTO(user);
    }

    public void deleteUser(Long userId) {
       User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
       userRepository.delete(user);
    }

    public List<UserAccountDTOUserView> getAllAccountsFromUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        Set<UserAccount> accounts = user.getAccounts();
        ModelMapper mm = new ModelMapper();
        List<UserAccountDTOUserView> dto = accounts.stream().map(acc -> mm.map(acc, UserAccountDTOUserView.class)).collect(Collectors.toList());
        return dto;
    }
}
