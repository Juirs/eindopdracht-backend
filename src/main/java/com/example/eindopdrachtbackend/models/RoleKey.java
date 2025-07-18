package com.example.eindopdrachtbackend.models;

import java.io.Serializable;
import java.util.Objects;

public class RoleKey implements Serializable {
    private String username;
    private String role;

    public RoleKey() {}

    public RoleKey(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Override equals and hashCode for composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleKey roleKey = (RoleKey) o;
        return Objects.equals(username, roleKey.username) && Objects.equals(role, roleKey.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role);
    }
}
