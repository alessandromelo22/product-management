package com.alessandromelo.security.service;

import com.alessandromelo.entity.User;
import com.alessandromelo.enums.UserRole;
import com.alessandromelo.exception.user.EmailAlreadyExistsException;
import com.alessandromelo.exception.user.EmailNotFoundException;
import com.alessandromelo.repository.UserRepository;
import com.alessandromelo.security.builders.*;
import com.alessandromelo.security.dto.*;
import com.alessandromelo.security.exception.InvalidRefreshTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> usernamePasswordAuthenticationTokenCaptor;

    @Captor
    private ArgumentCaptor<UserDetails> userDetailsCaptor;


    @Test
    @DisplayName("register() should throw EmailAlreadyExistsException")
    void registerShouldThrowEmailAlreadyExistsException(){
        //Arrange
        RegisterRequestDto requestDto = new RegisterRequestDtoBuilder().build();

        Mockito.when(this.userRepository.existsByEmail("mariazinha244@gmail.com")).thenReturn(true);

        //Act
        //Assert
        Assertions.assertThrows(EmailAlreadyExistsException.class,
                () -> this.authService.register(requestDto));

        Mockito.verify(this.userRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    @DisplayName("register() should save a user successfully")
    void registerShouldSaveAUserSuccessfully(){
        //Arrange
        RegisterRequestDto requestDto = new RegisterRequestDtoBuilder().build();

        Mockito.when(this.userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        Mockito.when(this.passwordEncoder.encode(requestDto.getPassword())).thenReturn("encrypted_password");
        //Act
        this.authService.register(requestDto);

        //Assert
        Mockito.verify(this.userRepository).save(this.userCaptor.capture());
        User captured = this.userCaptor.getValue();

        Assertions.assertAll(
                () -> Assertions.assertEquals("Maria", captured.getuName()),
                () -> Assertions.assertEquals("mariazinha244@gmail.com", captured.getEmail()),
                () -> Assertions.assertEquals("encrypted_password", captured.getPassword()),
                () -> Assertions.assertEquals(UserRole.USER, captured.getRole())
        );
    }

    @Test
    @DisplayName("login() should return tokens when credentials are valid")
    void loginShouldReturnTokensWhenCredentialsAreValid(){
        //Arrange
        LoginRequestDto requestDto = new LoginRequestDtoBuilder().build();
        UserDetails userDetails = new UserPrincipalBuilder().build();
        Authentication auth = Mockito.mock(Authentication.class);

        Mockito.when(this.authenticationManager
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        Mockito.when(auth.getPrincipal()).thenReturn(userDetails);
        Mockito.when(this.jwtService.generateAccessToken(userDetails)).thenReturn("fake_accessToken");
        Mockito.when(this.jwtService.generateRefreshToken(userDetails)).thenReturn("fake_refreshToken");

        //Act
        LoginResponseDto result = this.authService.login(requestDto);

        //Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("fake_accessToken", result.getAccessToken()),
                () -> Assertions.assertEquals("fake_refreshToken", result.getRefreshToken())
        );

        Mockito.verify(this.authenticationManager).authenticate(this.usernamePasswordAuthenticationTokenCaptor.capture());
        UsernamePasswordAuthenticationToken captured = this.usernamePasswordAuthenticationTokenCaptor.getValue();

        Assertions.assertAll(
                () -> Assertions.assertEquals(requestDto.getEmail(), captured.getPrincipal()),
                () -> Assertions.assertEquals(requestDto.getPassword(), captured.getCredentials())
        );
    }

    @Test
    @DisplayName("login() should throw EmailNotFoundException")
    void loginShouldThrowEmailNotFoundException(){
        //Arrange
        LoginRequestDto requestDto = new LoginRequestDtoBuilder().build();

        Mockito.when(this.authenticationManager
                        .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new EmailNotFoundException("emailfake@gmail.com"));

        //Act
        //Assert
        Assertions.assertThrows(EmailNotFoundException.class,
                () -> this.authService.login(requestDto));
    }


    @Test
    @DisplayName("")
    void refreshShouldThrowsInvalidRefreshTokenException(){
        //Arrange
        RefreshRequestDto refreshRequestDto = new RefreshRequestDtoBuilder().build();
        UserDetails userDetails = Mockito.mock(UserDetails.class); //Mockei dessa forma pq irei usar esse mock apenas para criar a stub

        Mockito.when(this.jwtService.extractEmail(refreshRequestDto.getRefreshToken())).thenReturn("emailfake@gmail.com");
        Mockito.when(this.userDetailsService.loadUserByUsername("emailfake@gmail.com")).thenReturn(userDetails);
        Mockito.when(this.jwtService.isRefreshTokenValid(refreshRequestDto.getRefreshToken(), userDetails)).thenReturn(false);

        //Act
        //Assert
        Assertions.assertThrows(InvalidRefreshTokenException.class,
                () -> this.authService.refresh(refreshRequestDto));
    }

    //Deixa eu ver se eu entendi, eu devo criar um objeto de verdade usando new e tals apenas se eu vou querer validar posteriormente aquele objeto


    @Test
    @DisplayName("refresh() should return a new accessToken successfully")
    void refreshShouldReturnANewAccessTokenSuccessfully(){
        //Arrange
        RefreshRequestDto refreshRequestDto = new RefreshRequestDtoBuilder().build();// ver se é valido trocar esse mock pelo mock usando Mockito.mock()
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(this.jwtService.extractEmail(refreshRequestDto.getRefreshToken())).thenReturn("emailfake@gmail.com");
        Mockito.when(this.userDetailsService.loadUserByUsername("emailfake@gmail.com")).thenReturn(userDetails);
        Mockito.when(this.jwtService.isRefreshTokenValid(refreshRequestDto.getRefreshToken(), userDetails)).thenReturn(true);
        Mockito.when(this.jwtService.generateAccessToken(userDetails)).thenReturn("newAccessTokenFake");

        //Act
        RefreshResponseDto result = this.authService.refresh(refreshRequestDto);

        //Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("newAccessTokenFake", result.getNewAccessToken())
        );

        Mockito.verify(this.jwtService).generateAccessToken(this.userDetailsCaptor.capture());
        UserDetails captured = this.userDetailsCaptor.getValue();

        Assertions.assertAll(
                () -> Assertions.assertEquals(userDetails.getUsername(), captured.getUsername()),
                () -> Assertions.assertEquals(userDetails.getPassword(), captured.getPassword())
        );
    }
}