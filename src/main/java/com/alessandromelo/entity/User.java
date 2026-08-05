package com.alessandromelo.entity;

import com.alessandromelo.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String uName;
    @Column(unique = true)
    @Email
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;


    public User() {
    }

    public User(String uName, String email, String password, UserRole role) {
        this.uName = uName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
