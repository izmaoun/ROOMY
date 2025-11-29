package com.roomy.entities;

public class Admin {
    private int idAdmin;
    private String username;
    private String password;
    private String email;

    // Constructeurs
    public Admin() {}

    public Admin(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters et Setters
    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Administrateur{idAdmin=" + idAdmin +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}