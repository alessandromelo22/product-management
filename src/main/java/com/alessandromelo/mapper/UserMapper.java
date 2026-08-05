package com.alessandromelo.mapper;

import com.alessandromelo.entity.User;
import com.alessandromelo.security.userprincipal.UserPrincipal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //Entity -> UserPrincipal
    UserPrincipal toUserPrincipal(User user);
}
