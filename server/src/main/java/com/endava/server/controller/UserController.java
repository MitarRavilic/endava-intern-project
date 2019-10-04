package com.endava.server.controller;

import com.endava.server.dto.UserAccountDTOUserView;
import com.endava.server.dto.UserDTOfull;
import com.endava.server.dto.request.UserDTORegister;
import com.endava.server.model.User;
import com.endava.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users", method = { RequestMethod.GET, RequestMethod.POST })
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
       List<User> users = userService.getAllUsers();
       return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
       UserDTOfull userDTOfull = userService.getUser(userId);
       return ResponseEntity.ok(userDTOfull);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTORegister userDTO){
        userService.createUser(userDTO);
        return ResponseEntity.ok("User Created");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDTOfull userDTOfull) {
       userService.updateUser(userId, userDTOfull);
       return ResponseEntity.ok("User Updated");
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<?> getAccountsForUser(@PathVariable Long userId){
        List<UserAccountDTOUserView> dto = userService.getAllAccountsFromUser(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<?> getAccountsForCurrentUser(){
        List<UserAccountDTOUserView> dto = userService.getAllAcountsFromCurrentUser();
        return ResponseEntity.ok(dto);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/testrole")
//    public ResponseEntity<?> roleTestEndpoint(){
//        return ResponseEntity.ok("everything good");
//    }
}
