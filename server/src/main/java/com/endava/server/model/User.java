package com.endava.server.model;

import com.endava.server.dto.UserDTO;
import com.endava.server.dto.UserDTORegister;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "username", name = "uniqueUsernameConstraint"),
@UniqueConstraint(columnNames = "email", name = "uniqueEmailConstraint")})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @NotEmpty
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<UserAccount> accounts;


    public User(UserDTO userDTO){
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
    }

    public User(UserDTORegister userDTO) {
        this.username = userDTO.getUsername();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
    }

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
        this.accounts = new HashSet<>();
        this.setRole(Role.USER);
    }

    public Optional<UserAccount> getUserAccountWithCurrency(String currencyCode){
       return this.accounts.stream().filter(account -> account.getCurrencyCode().equals(currencyCode)).findFirst();
    }
    public void addAccount(String currencyCode) {

        this.accounts.add(new UserAccount(this, currencyCode));
    }
}
