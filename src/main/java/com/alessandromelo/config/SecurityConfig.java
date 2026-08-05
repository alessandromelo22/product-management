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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, RoleHierarchy roleHierarchy) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() //Deixar acesso apenas para ADMIN
                        .anyRequest().hasRole("ADMIN"))

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this.jsonAuthEntryPoint)
                        .accessDeniedHandler(this.jsonAccessDeniedHandler))

                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //precisa ser static
    public static RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("OWNER").implies("ADMIN")
                .role("ADMIN").implies("USER")
                .build();
    }



}
