package com.alessandromelo.security.service;

import com.alessandromelo.dto.security.LoginRequestDto;
import com.alessandromelo.exception.security.JwtTokenWithInvalidFormatException;
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
     * Validade do Token
     */
    //@Value("${EXPIRATION_TIME:900000}")
    private Long expirationMs = 900_000L;


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
     * Chamado pelo {@link AuthService#login(LoginRequestDto)} para gerar o token depois do User ser validado.
     * @param user usado para criar o payload do Token
     * @return {@code String} JWT token
     */
    public String generateToken(UserDetails user){

        return JWT.create()
                .withSubject(user.getUsername()) // a qual User pertence esse token (identificador do user: email, id, etc.)
                .withIssuedAt(new Date()) //data de criação
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs)) //data de expiração
                .sign(this.algorithm); // algoritmo + secretKey que será usado na assinatura
    }

    /**
     * Extrai o email do token. Usado pelo {@link JwtAuthenticationFilter} pra saber QUEM buscar no banco.
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
     * Chamado pelo {@link JwtAuthenticationFilter} para validar o token enviado (assinatura + expiração + dono do token)
     *
     * @param token que será validado
     * @param user usado para verificar se o token enviado bate com o usuario
     * @return {@code true} se o Token for valido ou {@code false} se o Token for invalido
     */
    public boolean isTokenValid(String token, UserDetails user){

        try{
            JWT.require(this.algorithm) // monta o verificador com o MESMO algoritmo/chave usado pra assinar
                    .withSubject(user.getUsername()) // exige que o "dono" do token bata com o usuário carregado do banco
                    .build()
                    .verify(token);

            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
