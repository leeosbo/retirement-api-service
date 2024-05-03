package com.retirement.apiservice.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity(name = "retiree")
public class Retiree implements IUser {
    @Id
    @JsonProperty("user_id")
    private Integer userId;
    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "date_of_birth")
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @Column(name = "retirement_date")
    @JsonProperty("retirement_date")
    private Date retirementDate;
    @Column(name = "retirement_years")
    @JsonProperty("retirement_years")
    private int retirementYears;

    protected Retiree() {

    }

    public Retiree(int userId, User user, Date dateOfBirth, Date retirementDate, int retirementYears) {
        this.userId = userId;
        this.user = user;
        this.dateOfBirth = dateOfBirth;
        this.retirementDate = retirementDate;
        this.retirementYears = retirementYears;
    }

    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return user.getFirstName();
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return user.getLastName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getRetirementDate() {
        return retirementDate;
    }

    public int getRetirementYears() {
        return retirementYears;
    }

}
