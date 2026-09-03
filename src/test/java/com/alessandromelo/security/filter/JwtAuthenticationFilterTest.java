package com.alessandromelo.security.filter;

import com.alessandromelo.security.service.JwtService;
import com.alessandromelo.security.userprincipal.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;


    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("doFilterInternal() should call doFilter() when header authorization doesn't exists")
    void doFilterInternalShouldCallDoFilterWhenHeaderAuthorizationDoesNotExists() throws ServletException, IOException {
        //Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn(null);

        //Act:
        this.jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //Assert
        Mockito.verify(this.jwtService, Mockito.never()).extractEmail(Mockito.any());
        Mockito.verify(this.userDetailsService, Mockito.never()).loadUserByUsername(Mockito.any());
        Mockito.verify(this.jwtService, Mockito.never()).isAccessTokenValid(Mockito.any(), Mockito.any());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("doFilterInternal() should call doFilter() when header authorization doesn't start with Bearer")
    void doFilterInternalShouldCallDoFilterWhenHeaderAuthorizationDoesNotStartWithBearer() throws ServletException, IOException {
        //Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("fakeAuthorizationHeader");

        //Act:
        this.jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //Assert
        Mockito.verify(this.jwtService, Mockito.never()).extractEmail(Mockito.any());
        Mockito.verify(this.userDetailsService, Mockito.never()).loadUserByUsername(Mockito.any());
        Mockito.verify(this.jwtService, Mockito.never()).isAccessTokenValid(Mockito.any(), Mockito.any());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("doFilterInternal() should call doFilter() when subject token doesn't exists")
    void doFilterInternalShouldCallDoFilterWhenSubjectTokenDoesNotExists() throws ServletException, IOException {
        //Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer fakeToken");
        Mockito.when(this.jwtService.extractEmail("fakeToken")).thenReturn(null);

        //Act:
        this.jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //Assert
        Mockito.verify(this.userDetailsService, Mockito.never()).loadUserByUsername(Mockito.any());
        Mockito.verify(this.jwtService, Mockito.never()).isAccessTokenValid(Mockito.any(), Mockito.any());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(Mockito.any(), Mockito.any());
        Assertions.assertDoesNotThrow(() -> this.jwtAuthenticationFilter.doFilterInternal(request,response,filterChain));
    }

    @Test
    @DisplayName("doFilterInternal() should call doFilter() when there is someone authenticated in the SecurityContext")
    void doFilterInternalShouldCallDoFilterWhenThereIsSomeoneAuthenticatedInTheSecurityContext() throws ServletException, IOException {
        //Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        //Aqui estamos populando o SecurityContextHolder
        UserPrincipal userPrincipal = Mockito.mock(UserPrincipal.class);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer fakeToken");
        Mockito.when(this.jwtService.extractEmail("fakeToken")).thenReturn("fakeemail1234@gmail.com");

        //Act:
        this.jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //Assert
        Mockito.verify(this.userDetailsService, Mockito.never()).loadUserByUsername(Mockito.any());
        Mockito.verify(this.jwtService, Mockito.never()).isAccessTokenValid(Mockito.any(), Mockito.any());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("doFilterInternal() should call doFilter() when accessToken is invalid")
    void doFilterInternalShouldCallDoFilterWhenAccessTokenIsInvalid() throws ServletException, IOException {
        //Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer fakeToken");
        Mockito.when(this.jwtService.extractEmail("fakeToken")).thenReturn("fakeemail1234@gmail.com");
        Mockito.when(userDetailsService.loadUserByUsername("fakeemail1234@gmail.com")).thenReturn(Mockito.any());
        Mockito.when(jwtService.isAccessTokenValid("fakeToken", Mockito.any())).thenReturn(false);

        //Act:
        this.jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //Assert
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(Mockito.any(), Mockito.any());
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("doFilterInternal() should successfully set customer in the SecurityContextHolder")
    void doFilterInternalShouldSuccessfullySetCustomerInTheSecurityContextHolder() throws ServletException, IOException {
        //Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer fakeToken");
        Mockito.when(this.jwtService.extractEmail("fakeToken")).thenReturn("fakeemail1234@gmail.com");
        Mockito.when(userDetailsService.loadUserByUsername("fakeemail1234@gmail.com")).thenReturn(userDetails);
        Mockito.when(jwtService.isAccessTokenValid("fakeToken", userDetails)).thenReturn(true);

        //Act:
        this.jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //Assert
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(Mockito.any(), Mockito.any());
        Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}