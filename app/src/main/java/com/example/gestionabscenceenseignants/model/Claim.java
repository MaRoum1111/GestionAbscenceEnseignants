package com.example.gestionabscenceenseignants.model;

public class Claim {
    private String cin;
    private String date;          // Date 
    private String startTime;     // Heure de début
    private String endTime;       // Heure de fin
    private String claim;        // Réclamation
    private String classe;        // Classe
    // No-argument constructor
    public Claim() {
        // Firebase requires this constructor
    }
    
    public Claim(String cin, String date, String startTime, String endTime, String claim, String classe) {
        this.cin = cin;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.claim = claim;
        this.classe = classe;
    }
    
    // Getters et Setters pour chaque champ
    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe= classe;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }
}
