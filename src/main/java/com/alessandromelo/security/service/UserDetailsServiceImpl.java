package com.alessandromelo.security.service;

import com.alessandromelo.entity.User;
import com.alessandromelo.exception.user.EmailNotFoundException;
import com.alessandromelo.mapper.UserMapper;
import com.alessandromelo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    //Metodo usado pelo springSecurity quando alguem tenta se autenticar a nossa aplicação
    //É aqui que o Spring security faz a consulta pelos usuarios (seja banco, vindo do google, etc...)
    //No nosso caso os usuarios estão vindo do banco
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        return this.userMapper.toUserPrincipal(user);
    }

    //Nesse metodo eu poderia ter apenas criado um metodo no repository retornando direto um UserPrincipal
    //porem não seria boa pratica a camada responsavel por conversar com os dados do banco, entidades persistidas
    //conhecer conceitos da camada de Security (o UserPrincipal no caso)
}
