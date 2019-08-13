package com.endava.server.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Constraint;
import java.io.Serializable;

@Entity
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

}
