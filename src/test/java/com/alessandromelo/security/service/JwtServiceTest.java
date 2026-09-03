package com.alessandromelo.security.service;

import com.alessandromelo.enums.UserRole;
import com.alessandromelo.security.builders.UserPrincipalBuilder;
import com.alessandromelo.security.exception.JwtTokenWithInvalidFormatException;
import com.auth0.jwt.JWT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;



    @BeforeEach //antes de cada metodo preenche o campo "secretString" com um valor fake e chama o metodo init() pra decodificar e atribuir a secretKey ao Algorithm
    void setUp(){
        ReflectionTestUtils.setField(this.jwtService, "secretString", "dGVzdGUtc2VjcmV0LWtleQ==");
        ReflectionTestUtils.invokeMethod(this.jwtService, "init"); // chama o metodo init() manualmente
    }

    @Test
    @DisplayName("generateAccessToken() should return a accessToken successfully")
    void generateAccessTokenShouldReturnAAccessTokenSuccessfully(){
        //Arrange:
        UserDetails userDetails = new UserPrincipalBuilder().withId(1L)
                .withEmail("maria244@gmail.com")
                .withPassword("123456")
                .withRole(UserRole.USER).build();

        //Act:
        String tokenReturned = this.jwtService.generateAccessToken(userDetails);

        //Assert:
        Assertions.assertAll(
                () -> Assertions.assertEquals("maria244@gmail.com", JWT.decode(tokenReturned).getSubject()),
                () -> Assertions.assertEquals("accessToken", JWT.decode(tokenReturned).getClaim("tokenType").asString())
        );
    }

    @Test
    @DisplayName("generateRefreshToken() should return a refreshToken successfully")
    void generateRefreshTokenShouldReturnARefreshTokenSuccessfully(){
        //Arrange:
        UserDetails userDetails = new UserPrincipalBuilder().withId(1L)
                .withEmail("maria244@gmail.com")
                .withPassword("123456")
                .withRole(UserRole.USER).build();

        //Act:
        String tokenReturned = this.jwtService.generateRefreshToken(userDetails);

        //Assert:
        Assertions.assertAll(
                () -> Assertions.assertEquals("maria244@gmail.com", JWT.decode(tokenReturned).getSubject()),
                () -> Assertions.assertEquals("refreshToken", JWT.decode(tokenReturned).getClaim("tokenType").asString())
        );
    }

    @Test
    @DisplayName("extractEmail() should return email successfully")
    void extractEmailShouldReturnEmailSuccessfully(){
        //Arrange
        UserDetails userDetails = new UserPrincipalBuilder()
                .withId(1L)
                .withEmail("maria244@gmail.com")
                .withPassword("123456")
                .withRole(UserRole.USER).build();

        String token = this.jwtService.generateAccessToken(userDetails);

        //Act
        String emailExtracted = this.jwtService.extractEmail(token);

        //Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("maria244@gmail.com", emailExtracted)
        );

    }

    @Test
    @DisplayName("extractEmail() should throw JwtTokenWithInvalidFormatException")
    void extractEmailShouldThrowJwtTokenWithInvalidFormatException(){
        //Arrange
        //Act
        //Assert
        Assertions.assertThrows(JwtTokenWithInvalidFormatException.class,
                () -> this.jwtService.extractEmail("invalidToken"));
    }

    @Test
    @DisplayName("isAccessTokenValid() should return true")
    void isAccessTokenValidShouldReturnTrue(){
        //Arrange
        UserDetails userDetails = new UserPrincipalBuilder()
                .withId(1L)
                .withEmail("maria244@gmail.com")
                .withPassword("123456")
                .withRole(UserRole.USER).build();

        String accessToken = this.jwtService.generateAccessToken(userDetails);

        //Act
        //Assert
        Assertions.assertTrue(this.jwtService.isAccessTokenValid(accessToken, userDetails));
    }

    @Test
    @DisplayName("isAccessTokenValid() should return false when refreshToken is passed")
    void isAccessTokenValidShouldReturnFalse(){
        //Arrange
        UserDetails userDetails = new UserPrincipalBuilder()
                .withId(1L)
                .withEmail("maria244@gmail.com")
                .withPassword("123456")
                .withRole(UserRole.USER).build();

        String refreshToken = this.jwtService.generateRefreshToken(userDetails);

        //Act
        //Assert
        Assertions.assertFalse(this.jwtService.isAccessTokenValid(refreshToken, userDetails));
    }


    @Test
    @DisplayName("isRefreshTokenValid() should return false when accessToken is passed")
    void isRefreshTokenValidShouldReturnFalse(){
        //Arrange
        UserDetails userDetails = new UserPrincipalBuilder()
                .withId(1L)
                .withEmail("maria244@gmail.com")
                .withPassword("123456")
                .withRole(UserRole.USER).build();

        String accessToken = this.jwtService.generateAccessToken(userDetails);

        //Act
        //Assert
        Assertions.assertFalse(this.jwtService.isRefreshTokenValid(accessToken, userDetails));
    }


    @Test
    @DisplayName("isRefreshTokenValid() should return false when the token subject doesn't match the provided user.")
    void isRefreshTokenValidShouldReturnFalseWhenTokenSubjectDoesNotMatchTheProvidedUser(){
        //Arrange
        UserDetails userDetails01 = new UserPrincipalBuilder() //cria o token com esse
                .withId(1L)
                .withEmail("maria244@gmail.com")
                .withPassword("123456")
                .withRole(UserRole.USER).build();

        UserDetails userDetails02 = new UserPrincipalBuilder()  //valida o token com esse
                .withId(2L)
                .withEmail("joazinhogames69@gmail.com")
                .withPassword("696969")
                .withRole(UserRole.USER).build();

        String refreshToken = this.jwtService.generateRefreshToken(userDetails01);

        //Act
        //Assert
        Assertions.assertFalse(this.jwtService.isRefreshTokenValid(refreshToken, userDetails02));
        //é criado um token a partir de do userDetails01 e é validado passando o userDetails02
    }

    @Test
    @DisplayName("isAccessTokenValid() should return false when the token subject doesn't match the provided user.")
    void isAccessTokenValidShouldReturnFalseWhenTokenSubjectDoesNotMatchTheProvidedUser(){
        //Arrange
        UserDetails userDetails01 = new UserPrincipalBuilder() //cria o token com esse
                .withId(1L)
                .withEmail("maria244@gmail.com")
                .withPassword("123456")
                .withRole(UserRole.USER).build();

        UserDetails userDetails02 = new UserPrincipalBuilder()  //valida o token com esse
                .withId(2L)
                .withEmail("joazinhogames69@gmail.com")
                .withPassword("696969")
                .withRole(UserRole.USER).build();

        String accessToken = this.jwtService.generateAccessToken(userDetails01);

        //Act
        //Assert
        Assertions.assertFalse(this.jwtService.isAccessTokenValid(accessToken, userDetails02));
        //é criado um token a partir de do userDetails01 e é validado passando o userDetails02
    }
}