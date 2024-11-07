package com.example.bankingApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Entity
public class User {
    @Id
    @Column(name="user_id", nullable = false)
    @NotNull
    private Long userId; //user id
    @Column(name = "username", nullable = false)
    @NotEmpty(message = "user name cannot be empty")
    private String username; //username

    @Column(name="user_password", nullable = false)
    @NotEmpty
    @Size(min = 8, message = "password should have at least 8 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password; //user password

    @Column(name="user_first_name", nullable = false)
    @NotEmpty
    private String firstName; //user first name
    @Column(name="user_last_name", nullable = false)
    @NotEmpty
    private String lastName; //user last name
    @Column(name="user_dob", nullable = false)
    @NotEmpty
    private LocalDate dob; //user date of birth
    @Column(name="user_phone_nb", nullable = false)
    @NotEmpty
    private String phoneNb; //user phone number
    @Column(name="user_country", nullable = false)
    @NotEmpty
    private String country; //user country
    @Column(name="user_city", nullable = false)
    @NotEmpty
    private String city; //user city
    @Column(name="user_role", nullable = false)
    @NotNull
    @Min(0)
    @Max(2)
    private int role; //enum role of user

    @OneToMany(mappedBy = "user")
    private List<AccountHolder> accounts = new ArrayList<>(); //list of accounts owned by user

    @OneToMany(mappedBy = "cardOwner")
    private List<DebitCard> cards = new ArrayList<>(); //list of cards owned by the user

    //default constructor
    public User(){}

    /**
     * Arg constructor
     * @param userId
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param dob
     * @param phoneNb
     * @param country
     * @param city
     * @param role
     */
    public User(Long userId, String username, String password, String firstName, String lastName,
                LocalDate dob, String phoneNb, String country, String city, int role) {
        setUserId(userId);
        setUsername(username);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setDob(dob);
        setPhoneNb(phoneNb);
        setCountry(country);
        setCity(city);
        setRole(role);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhoneNb() {
        return phoneNb;
    }

    public void setPhoneNb(String phoneNb) {
        this.phoneNb = phoneNb;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNbOfAccounts() {
        return accounts.size();
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        if (role == 0 || role == 1 || role == 2) {
            this.role = role;
        }else {
            throw new IllegalArgumentException("Invalid number, role should be 0, 1, or 2");
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob='" + dob + '\'' +
                ", phoneNb='" + phoneNb + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", role=" + role +
                '}';
    }
}
