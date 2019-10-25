package com.endava.server.service;

import com.endava.server.dto.UserAccountDTOUserView;
import com.endava.server.dto.UserDTOfull;
import com.endava.server.dto.request.UserDTORegister;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.Role;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public List<User> getAllUsers(){
       return userRepository.findAll();
    }

    public UserDTOfull getUser(Long userId){
        return new UserDTOfull(userRepository.findById(userId).orElseThrow(() -> new  ResourceNotFoundException("User", "userId", userId)));
    }

    public UserDTOfull getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return new UserDTOfull(user);
    }

    @Transactional
    public void createUser(UserDTORegister userDTO){
       User user = new User();
       user.setUsername(userDTO.getUsername());
       user.setEmail(userDTO.getEmail());
       user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
       user.setRole(Role.USER);
        userRepository.save(user);
    }


    @Transactional
    public void updateUser(Long userId, UserDTOfull userDTOfull) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        user.setUsername(userDTOfull.getUsername());
        user.setEmail(userDTOfull.getEmail());
    }

    @Transactional
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

    public List<UserAccountDTOUserView> getAllAcountsFromCurrentUser() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.
               findByUsername(username)
               .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        Set<UserAccount> accounts = user.getAccounts();
        ModelMapper mm = new ModelMapper();
        List<UserAccountDTOUserView> dto = accounts.stream().map(acc -> mm.map(acc, UserAccountDTOUserView.class)).sorted(Comparator.comparing(UserAccountDTOUserView::getBalance)).collect(Collectors.toList());
        return dto;
    }
}
