package com.alessandromelo.security.service;

import com.alessandromelo.security.dto.RefreshRequestDto;
import com.alessandromelo.security.exception.JwtTokenWithInvalidFormatException;
import com.alessandromelo.security.filter.JwtAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    /**
     * Cadeia de bytes (array) que será usado pelo Algoritmo, porém antes deve ser decodificada da {@code Base64}
     */
    @Value("${SECRET_KEY}")
    private String secretString;

    /**
     * Algoritmo usado para criar a <b>signature</b>  do Token. Precisa de uma secretKey (cadeia de bytes secreta)
     */
    private Algorithm algorithm;

    /**
     * Validade do accessToken e refreshToken
     */
    //ver de colocar esses valores em variaveis de ambiente
    private static final long ACCESS_TOKEN_EXPIRATION = 900_000; //15 minutos
    private static final long REFRESH_TOKEN_EXPIRATION = 604_800_000; // 7 dias

    /**
     * Definem os tipos de tokens
     */
    private static final String ACCESS_TOKEN_TYPE = "accessToken";
    private static final String REFRESH_TOKEN_TYPE = "refreshToken";

    /**
     * Chamado no construção do bean, decodifica a {@link #secretString} que está na {@code Base64} para a
     * cadeia de bytes (array de bytes) que será usado pelo {@link #algorithm}
     */
    @PostConstruct
    private void init(){
        byte[] keyBytes = Base64.getDecoder().decode(this.secretString);
        this.algorithm = Algorithm.HMAC256(keyBytes);
    }

    /**
     * Gera um {@code accessToken}
     * @param user usado na construção do token
     * @return {@code String} sendo o {@code accessToken}
     */
   public String generateAccessToken(UserDetails user){
        return this.generateToken(user, ACCESS_TOKEN_EXPIRATION, ACCESS_TOKEN_TYPE);
   }

    /**
     * Gera um {@code refreshToken}
     * @param user usado na construção do token
     * @return {@code String} sendo o {@code refreshToken}
     */
    public String generateRefreshToken(UserDetails user){
        return this.generateToken(user, REFRESH_TOKEN_EXPIRATION, REFRESH_TOKEN_TYPE);
    }


    /**
     * Chamado pelos metodos {@link #generateAccessToken(UserDetails)} e {@link #generateRefreshToken(UserDetails)} 
     *(dependendo do {@code tokenType}) para gerar o token depois do User ser validado.
     * 
     * @param user usado para criar o payload do token
     * @param expirationMs validade do token
     * @param tokenType tipo do token a ser criado
     * @return {@code String} JWT token
     */
    private String generateToken(UserDetails user, long expirationMs, String tokenType){

        return JWT.create()
                .withSubject(user.getUsername())// a qual User pertence esse token (identificador do user: email, id, etc.)
                .withClaim("tokenType", tokenType)
                .withIssuedAt(new Date()) //data de criação
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs)) //data de expiração
                .sign(this.algorithm); // algoritmo + secretKey que será usado na assinatura
    }

    /**
     * Extrai o email (subject) do token. Usado pelo {@link JwtAuthenticationFilter} e 
     * {@link AuthService#refresh(RefreshRequestDto)} pra saber QUEM buscar no banco.
     * 
     * @param token
     * @return {@code String} subject do token (email)
     * @throws JwtTokenWithInvalidFormatException quando o token possui um formato invalido
     *
     */
    public String extractEmail(String token){
        try{
            return JWT.decode(token).getSubject();
        }catch (JWTDecodeException ex){
            throw new JwtTokenWithInvalidFormatException();
        }
    }

    /**
     *  Chama o metodo {@link #isTokenValid(String, UserDetails, String)} para validar o {@code accessToken}
     * @param accessToken que sera validado
     * @param user usado para verificar se o token enviado bate com o user
     * @return {@code true} se o token for valido ou {@code false} se o token for inválido
     */
    public boolean isAccessTokenValid(String accessToken, UserDetails user){
        return this.isTokenValid(accessToken, user, ACCESS_TOKEN_TYPE);
    }

    /**
     *  Chama o metodo {@link #isTokenValid(String, UserDetails, String)} para validar o {@code refreshToken}
     * @param refreshToken que sera validado
     * @param user usado para verificar se o token enviado bate com o user
     * @return {@code true} se o token for valido ou {@code false} se o token for inválido
     */
    public boolean isRefreshTokenValid(String refreshToken, UserDetails user){
        return this.isTokenValid(refreshToken, user, REFRESH_TOKEN_TYPE);
    }

    /**
     * Chamado pelo {@link #isAccessTokenValid(String, UserDetails)} para validar {@code accessToken} e
     * {@link #isRefreshTokenValid(String, UserDetails)} para validar {@code refreshToken}
     * (assinatura + expiração + dono do token)
     *
     * @param token que será validado
     * @param user usado para verificar se o token enviado bate com o usuario
     * @param tokenType indica qual tipo de token que sera validado ({@code accessToken} ou {@code refreshToken})
     * @return {@code true} se o token for valido ou {@code false} se o token for inválido
     */
    private boolean isTokenValid(String token, UserDetails user, String tokenType){

        try{
            JWT.require(this.algorithm) // monta o verificador com o MESMO algoritmo/chave usado pra assinar
                    .withSubject(user.getUsername())// exige que o "dono" do token bata com o usuário carregado do banco
                    .withClaim("tokenType", tokenType)
                    .build()
                    .verify(token);

            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
