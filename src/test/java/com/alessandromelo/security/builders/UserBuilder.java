package com.alessandromelo.security.builders;

import com.alessandromelo.entity.User;
import com.alessandromelo.enums.UserRole;


public class UserBuilder {

    private Long id = 1L;
    private String uName = "Maria";
    private String email = "mariazinha244@gmail.com";
    private String password = "123456";
    private UserRole role = UserRole.USER;

    public UserBuilder() {
    }

    public UserBuilder(Long id, String uName, String email, String password, UserRole role) {
        this.id = id;
        this.uName = uName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public UserBuilder withUName(String uName){
        this.uName = uName;
        return this;
    }

    public UserBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public UserBuilder withRole(UserRole role){
        this.role = role;
        return this;
    }

    public User build(){
        User user = new User();
        user.setId(this.id);
        user.setuName(this.uName);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setRole(this.role);

        return user;
    }
}
