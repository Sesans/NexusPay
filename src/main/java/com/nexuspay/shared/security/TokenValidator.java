package com.nexuspay.shared.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public interface TokenValidator{

    UUID validateAndGetSubject(String token);

    Collection<? extends GrantedAuthority> getAuthorities(String token);
}