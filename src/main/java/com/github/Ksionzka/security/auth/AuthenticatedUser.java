package com.github.Ksionzka.security.auth;

import com.github.Ksionzka.persistence.entity.UserEntity;
import lombok.Data;

@Data
public class AuthenticatedUser {
    private UserEntity user;
    private String bearerToken;
}
