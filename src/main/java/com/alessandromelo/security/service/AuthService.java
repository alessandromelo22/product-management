package com.alessandromelo.security.service;

import com.alessandromelo.entity.User;
import com.alessandromelo.security.dto.*;
import com.alessandromelo.security.exception.InvalidRefreshTokenException;
import com.alessandromelo.exception.user.EmailAlreadyExistsException;
import com.alessandromelo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService,
                       @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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


    /**
     *Recebe o email e senha que são validados pelo metodo {@code authenticate()}
     * @param loginRequestDto email e senha do user
     * @return {@code LoginResponseDto} contendo o {@code accessToken} e o {@code refreshToken}
     */
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword());

        //verificação pra ver se o user existe no banco e se a senha bate
        // (internamente chama o metodo loadUserByUsername())
        var auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetails authenticatedUser = (UserDetails) auth.getPrincipal();

        String accessToken = this.jwtService.generateAccessToken(authenticatedUser);
        String refreshToken = this.jwtService.generateRefreshToken(authenticatedUser);

        return new LoginResponseDto(accessToken, refreshToken);
    }


    /**
     * Recebe o {@code refreshToken} valida e gera um novo {@code accessToken}
     *
     * @param refreshRequestDto contem o {@code refreshToken}
     * @return {@code RefreshResponseDto} - contendo o novo {@code accessToken}
     */
    public RefreshResponseDto refresh(RefreshRequestDto refreshRequestDto){

        String email = this.jwtService.extractEmail(refreshRequestDto.getRefreshToken());
        UserDetails user = this.userDetailsService.loadUserByUsername(email);

        if(!(this.jwtService.isRefreshTokenValid(refreshRequestDto.getRefreshToken(), user))){
            throw new InvalidRefreshTokenException();
        }

        String newAccessToken = this.jwtService.generateAccessToken(user);
        return new RefreshResponseDto(newAccessToken);
    }

}
