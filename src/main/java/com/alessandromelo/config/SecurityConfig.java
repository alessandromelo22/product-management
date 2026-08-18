package com.alessandromelo.config;

import com.alessandromelo.security.exceptionhandler.JsonAccessDeniedHandler;
import com.alessandromelo.security.exceptionhandler.JsonAuthEntryPoint;
import com.alessandromelo.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JsonAuthEntryPoint jsonAuthEntryPoint;
    private final JsonAccessDeniedHandler jsonAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JsonAuthEntryPoint jsonAuthEntryPoint, JsonAccessDeniedHandler jsonAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jsonAuthEntryPoint = jsonAuthEntryPoint;
        this.jsonAccessDeniedHandler = jsonAccessDeniedHandler;
    }

    /**
     * <p>Permite a gente customizar as configurações de segurança da aplicação.</p>
     *
     * desativa a proteção padrão csrf:
     * <blockquote><pre>
     *  .csrf(csrf -> csrf.disable())
     * </pre></blockquote><p>
     *
     *
     *
     * define a aplicação como sendo {@code STATELESS}:
     * <blockquote><pre>
     *  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
     * </pre></blockquote><p>
     *
     *
     *
     * define as regras de autorização das requisições HTTP da aplicação:
     * <blockquote><pre>
     *  .authorizeHttpRequests(authorizeRequests -> authorizeRequests
     *          .requestMatchers(HttpMethod.GET, "/products/**").hasRole("ADMIN")
     *          .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
     *          .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
     *          .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
     *          .anyRequest().hasRole("ADMIN"))
     * </pre></blockquote><p>
     *
     *
     * define tratamento personalizado para {@link org.springframework.security.core.AuthenticationException}
     *  e {@link org.springframework.security.access.AccessDeniedException}:
     * <blockquote><pre>
     *  .exceptionHandling(ex -> ex
     *          .authenticationEntryPoint(this.jsonAuthEntryPoint)
     *          .accessDeniedHandler(this.jsonAccessDeniedHandler))
     * </pre></blockquote><p>
     *
     *
     * adiciona o {@link JwtAuthenticationFilter} dentro da {@code SecurityFilterChain},
     * mais especificamente antes do {@link UsernamePasswordAuthenticationFilter}:
     *
     * <blockquote><pre>
     *  .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
     * </pre></blockquote><p>
     *
     *
     *
     *
     * @param httpSecurity builder usado para customizar a configuração de segurança da aplicação
     * @return {@code SecurityFilterChain} representa a cadeia de filtros do Spring Security
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()//Deixar acesso apenas para ADMIN
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll() //deixar acesso apenas para o cliente usar (navegador)
                        .anyRequest().hasRole("ADMIN"))

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this.jsonAuthEntryPoint)
                        .accessDeniedHandler(this.jsonAccessDeniedHandler))

                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /**
     * Usado para autenticar o user no login
     *
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean usado para encriptar e decriptar a senha do user
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define hierarquia de roles.
     * @return
     */
    @Bean //precisa ser static
    public static RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("OWNER").implies("ADMIN")
                .role("ADMIN").implies("USER")
                .build();
    }



}
