package com.alessandromelo.security.filter;

import com.alessandromelo.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    /**
     *
     * @param request
     * @param response
     * @param filterChain representa a cadeia de filtros. Usado para passar a requisição para o proximo filtro.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //retira o header
        String header = request.getHeader("Authorization");

        //se o header Authorization for nulo ou se o header Athorization não tiver o Token,
        //quer dizer que não tem nada pra validar, então deixamos passar sem autenticar
        //caso a rota exija login o AuthorizationFilter barra depois
        if(header == null || !(header.startsWith("Bearer "))){

            filterChain.doFilter(request, response); //passa pra pro proximo filtro da cadeia
            return;
        }

        String token = header.substring(7); //remove o "Bearer " e deixa apenas o Token
        String email = this.jwtService.extractEmail(token);

        //validamos o token so se o subject (email) existir & Não tiver ninguem autenticado no contexto (evitar reprocessar)
        if(email != null & SecurityContextHolder.getContext().getAuthentication() == null){

            //recuperamos o userPrincipal
            UserDetails userPrincipal = this.userDetailsService.loadUserByUsername(email);

            //validação do token
            if (this.jwtService.isTokenValid(token, userPrincipal)){

                //se for valido settamos o user no contexto de autenticação
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        //passa pro proximo filtro da cadeia
        filterChain.doFilter(request, response);
    }
}
