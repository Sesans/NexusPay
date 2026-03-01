package com.nexuspay.common.infra;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public interface TokenValidator{

    UUID validateAndGetSubject(String token);

    Collection<? extends GrantedAuthority> getAuthorities(String token);
}