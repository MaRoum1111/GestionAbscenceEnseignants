package com.example.gestionabscenceenseignants.model;

public class User {
    private String cin;
    private String name;
    private String email;
    private String password;
    private String role;
    private String photo; // Photo de profil

    public User() {
        // Ce constructeur est vide, mais il est nécessaire pour Firestore.
    }

    // Constructeur
    public User(String cin, String name, String email, String password, String role, String photo) {
        this.cin = cin;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.photo = photo;
    }

    public User(String cin, String email, String role) {
        this.cin = cin;
        this.email = email;
        this.role = role;
    }

    // Getters et Setters
    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
