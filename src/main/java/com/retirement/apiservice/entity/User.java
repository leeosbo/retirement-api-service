package com.retirement.apiservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Entity(name = "users")
public class User implements IUser {
    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Integer userId;
    @Column(name = "first_name")
    @JsonProperty("first_name")
    @Pattern(regexp = "[a-zA-Z]*", message = "value must be string/text with no spaces")
    private String firstName;
    @Column(name = "last_name")
    @JsonProperty("last_name")
    @Pattern(regexp = "[a-zA-Z]*", message = "value must be string/text with no spaces")
    private String lastName;
    @Email
    private String email;
    private String password;
    private Boolean enabled;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Retiree retiree;

    protected User() {

    }

    public User(Integer userId, String firstName, String lastName, String email, String password, Boolean enabled) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

}
