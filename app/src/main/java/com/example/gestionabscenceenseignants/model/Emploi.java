package com.example.gestionabscenceenseignants.model;

public class Emploi {
    private String nom;
    private String jour;
    private String heure;

    // Constructeur vide requis pour Firebase
    public Emploi() {}

    public Emploi(String nom, String jour, String heure) {
        this.nom = nom;
        this.jour = jour;
        this.heure = heure;
    }

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getJour() { return jour; }
    public void setJour(String jour) { this.jour = jour; }
    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }
}

