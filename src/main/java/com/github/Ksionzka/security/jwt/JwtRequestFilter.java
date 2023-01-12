package com.github.Ksionzka.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.Ksionzka.exception.RestException;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.auth.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .or(() -> this.findAuthTokenInQuery(request))
                .map(token -> {
                    try {
                        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
                        authenticatedUser.setBearerToken(token);

                        Long userId = this.jwtService.validateTokenAndRetrieveUserId(token);
                        authenticatedUser.setUser(this.userRepository.getOrThrowById(userId));
                        return authenticatedUser;
                    } catch (IllegalArgumentException e) {
                        throw RestException.of(HttpStatus.UNAUTHORIZED, "Unauthorized operation");
                    } catch (JWTVerificationException e) {
                        throw RestException.of(HttpStatus.UNAUTHORIZED, "Token expired");
                    }
                })
                .ifPresent(authenticatedUser -> {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        authenticatedUser.getUser(), null, authenticatedUser.getUser().getAuthorities());

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
        }

        chain.doFilter(request, response);
    }

    private Optional<String> findAuthTokenInQuery(HttpServletRequest request) {
        return request.getParameterMap()
            .entrySet()
            .stream()
            .filter(stringEntry -> Objects.equals(stringEntry.getKey(), "authToken"))
            .map(Map.Entry::getValue)
            .flatMap(valueArray -> Optional.ofNullable(valueArray).stream().flatMap(Arrays::stream))
            .findFirst();
    }
}
