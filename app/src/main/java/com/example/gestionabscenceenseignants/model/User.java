package com.example.gestionabscenceenseignants.model;

import android.net.Uri;

public class User {
    private String uid;
    private String name;
    private String email;
    private String password;
    private String role;
    private Uri photo; // Photo de profil

    public User() {
        // Ce constructeur est vide, mais il est nécessaire pour Firestore.
    }

    // Constructeur
    public User(String uid, String name, String email, String password, String role, Uri photo) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.photo = photo;
    }

    public User(String uid, String email, String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }

    // Getters et Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }
}
