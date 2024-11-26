package com.mobdeve.s21.grp4.mco_taftbites;

public class SignupLoginHelper {

    private String username;
    private String email;
    private String password;

    // Default constructor
    public SignupLoginHelper() {
    }

    // Parameterized constructor
    public SignupLoginHelper(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
