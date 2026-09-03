package com.alessandromelo.security.builders;

import com.alessandromelo.enums.UserRole;
import com.alessandromelo.security.userprincipal.UserPrincipal;

public class UserPrincipalBuilder {

    //Deixei os atributos sem valores pq o teste que eu uso esse builde n verifica o userPrincipal em sí.
    private Long id;
    private String email;
    private String password;
    private UserRole role;

    public UserPrincipalBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public UserPrincipalBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public UserPrincipalBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public UserPrincipalBuilder withRole(UserRole role){
        this.role = role;
        return this;
    }

    public UserPrincipal build(){
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(this.id);
        userPrincipal.setEmail(this.email);
        userPrincipal.setPassword(this.password);
        userPrincipal.setRole(this.role);
        return userPrincipal;
    }
}
