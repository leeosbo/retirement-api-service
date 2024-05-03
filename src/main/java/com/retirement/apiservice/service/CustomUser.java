package com.retirement.apiservice.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.retirement.apiservice.entity.Authority;
import com.retirement.apiservice.entity.User;

public class CustomUser implements UserDetails {
    private User user;
    private Authority authorities;

    public CustomUser(User user, Authority authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public int getUserId() {
        return user.getUserId();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            return null;
        }

        ArrayList<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>();
        String[] list = authorities.getAuthority().split(";");
        for (String authority : list) {
            authList.add(new SimpleGrantedAuthority(authority));
        }

        return authList;
    };

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
