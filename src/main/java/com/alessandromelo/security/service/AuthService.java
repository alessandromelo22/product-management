package com.alessandromelo.security.service;

import com.alessandromelo.dto.security.LoginRequestDto;
import com.alessandromelo.dto.security.LoginResponseDto;
import com.alessandromelo.dto.security.RegisterRequestDto;
import com.alessandromelo.entity.User;
import com.alessandromelo.exception.user.EmailAlreadyExistsException;
import com.alessandromelo.repository.UserRepository;
import com.alessandromelo.security.userprincipal.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     *Recebe o email e senha que são validados pelo metodo {@code authenticate()}
     * @param loginRequestDto email e senha do user
     * @return {@code LoginResponseDto} contendo o JWT token
     */
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword());

        //verificação pra ver se o user existe no banco e se a senha bate
        var auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserPrincipal authenticatedUser = (UserPrincipal) auth.getPrincipal();

        return new LoginResponseDto(this.jwtService.generateToken(authenticatedUser));
    }


    public void register(RegisterRequestDto registerRequestDto) {

        boolean emailAlreadyExists = this.userRepository.existsByEmail(registerRequestDto.getEmail());

        if(emailAlreadyExists){
            throw new EmailAlreadyExistsException();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequestDto.getPassword());

        this.userRepository.save(new User(registerRequestDto.getuName(), registerRequestDto.getEmail(),
                encryptedPassword, registerRequestDto.getRole()));
    }
}
