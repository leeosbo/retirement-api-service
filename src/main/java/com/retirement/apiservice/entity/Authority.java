package com.retirement.apiservice.entity;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "authorities")
public class Authority implements GrantedAuthority {
    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Integer userId;
    private String authority;

    protected Authority() {
    }

    public Authority(int userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
