package com.example.gestionabscenceenseignants.model;

public class Absence {
    private String profName;
    private String cin;
    private String idAbsence;
    private String idAgent; // Ajout de l'attribut idAgent
    private String date;          // Date de l'absence
    private String startTime;     // Heure de début
    private String endTime;       // Heure de fin
    private String reason;        // Raison de l'absence
    private String status;        // Statut de l'absence (justifiée/non justifiée)
    private String subjectName;   // Nom de la matière
    private int absenceCount;

    // No-argument constructor
    public Absence() {
        // Firebase requires this constructor
    }

    // Constructeur avec idAgent
    public Absence(String profName, String date, String startTime, String endTime, String reason, String status, String subjectName, String cin) {
        this.profName = profName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.subjectName = subjectName;
        this.cin = cin;
    }

    // Constructeur avec idAbsence
    public Absence(String idAbsence, String profName, String date, String startTime, String endTime, String reason, String status, String subjectName, String cin) {
        this.idAbsence = idAbsence;
        this.profName = profName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.subjectName = subjectName;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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
