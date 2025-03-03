package com.security.feature.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class Users implements Serializable {

    @Id
    private int id;

    public Users() {
    }

    @Column(nullable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    private String role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthProvider() {
        return authProvider;
    }

    private String email;

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", authProvider='" + authProvider + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                '}';
    }

    public Users(int id, String username, String password, String role, String email, String authProvider) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.authProvider = authProvider;
    }

    public Users(int id, String username, String password, String role, String email, String authProvider, boolean isEmailVerified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.authProvider = authProvider;
        this.isEmailVerified = isEmailVerified;
    }

    private String authProvider;

    private boolean isEmailVerified;

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }
}
