package com.github.Ksionzka.security.jwt;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.Role;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

@ContextConfiguration(classes = {JwtRequestFilter.class})
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
class JwtRequestFilterTest {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link JwtRequestFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)}
     */
    @Test
    void testDoFilterInternal() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter((ServletRequest) any(), (ServletResponse) any());
        jwtRequestFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter((ServletRequest) any(), (ServletResponse) any());
    }

    /**
     * Method under test: {@link JwtRequestFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)}
     */
    @Test
    void testDoFilterInternal4() throws IOException, ServletException {
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("https://example.org/example");
        when(defaultMultipartHttpServletRequest.getParameterMap()).thenReturn(new HashMap<>());
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter((ServletRequest) any(), (ServletResponse) any());
        jwtRequestFilter.doFilterInternal(defaultMultipartHttpServletRequest, response, filterChain);
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
        verify(defaultMultipartHttpServletRequest).getParameterMap();
        verify(filterChain).doFilter((ServletRequest) any(), (ServletResponse) any());
    }

    /**
     * Method under test: {@link JwtRequestFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)}
     */
    @Test
    void testDoFilterInternal5() throws JWTVerificationException, IOException, ServletException {
        when(jwtService.validateTokenAndRetrieveUserId((String) any())).thenReturn(123L);

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);
        when(userRepository.getOrThrowById((Long) any())).thenReturn(userEntity);
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getRemoteAddr()).thenReturn("42 Main St");
        when(defaultMultipartHttpServletRequest.getSession(anyBoolean())).thenReturn(new MockHttpSession());
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("Bearer ");
        when(defaultMultipartHttpServletRequest.getParameterMap()).thenReturn(new HashMap<>());
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter((ServletRequest) any(), (ServletResponse) any());
        jwtRequestFilter.doFilterInternal(defaultMultipartHttpServletRequest, response, filterChain);
        verify(jwtService).validateTokenAndRetrieveUserId((String) any());
        verify(userRepository).getOrThrowById((Long) any());
        verify(defaultMultipartHttpServletRequest).getRemoteAddr();
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
        verify(defaultMultipartHttpServletRequest).getSession(anyBoolean());
        verify(filterChain).doFilter((ServletRequest) any(), (ServletResponse) any());
    }
}

