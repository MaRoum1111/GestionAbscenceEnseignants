package com.example.gestionabscenceenseignants.model;

public class Absence {
    private String profName;
    private String cin;
    private String idAbsence;
    private String idAgent; // Ajout de l'attribut idAgent
    private String date;          // Date de l'absence
    private String startTime;     // Heure de début
    private String endTime;       // Heure de fin
    private String Classe;        // Raison de l'absence
    private String status;        // Statut de l'absence (justifiée/non justifiée)
    private String Salle;   // Nom de la matière
    private int absenceCount;
    // No-argument constructor
    public Absence() {
        // Firebase requires this constructor
    }

    // Constructeur avec idAgent
    public Absence(String profName, String date, String startTime, String endTime, String Classe, String status, String Salle, String cin) {
        this.profName = profName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.Classe = Classe;
        this.status = status;
        this.Salle = Salle;
        this.cin = cin;
    }

    // Constructeur avec idAbsence
    public Absence(String idAbsence, String profName, String date, String startTime, String endTime, String Classe, String status, String Salle, String cin) {
        this.idAbsence = idAbsence;
        this.profName = profName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.Classe = Classe;
        this.status = status;
        this.Salle = Salle;
        this.cin = cin;
    }

    // Getters et Setters pour chaque champ
    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
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

    public String getSalle() {
        return Salle;
    }

    public void setSalle(String Salle) {
        this.Salle = Salle;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClasse() {
        return Classe;
    }

    public void setClasse(String Classe) {
        this.Classe= Classe;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(int absenceCount) {
        this.absenceCount = absenceCount;
    }

    public String getIdAbsence() {
        return idAbsence;
    }

    public void setIdAbsence(String idAbsence) {
        this.idAbsence = idAbsence;
    }

    // Getter et Setter pour idAgent
    public String getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(String idAgent) {
        this.idAgent = idAgent;
    }
}
