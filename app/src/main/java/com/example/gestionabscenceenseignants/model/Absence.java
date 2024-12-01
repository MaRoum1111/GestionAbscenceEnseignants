package com.example.gestionabscenceenseignants.model;

public class Absence {
    private String profName;      // Nom du professeur
    private String date;          // Date de l'absence
    private String startTime;     // Heure de début
    private String endTime;       // Heure de fin
    private String reason;        // Raison de l'absence
    private String status;        // Statut de l'absence (justifiée/non justifiée)
    private String subjectName;   // Nom de la matière
    private int absenceCount;     // Nombre d'absences

    // No-argument constructor
    public Absence() {
        // Firebase requires this constructor
    }

    // Constructeur
    public Absence(String profName, String date, String startTime, String endTime, String reason, String status, String subjectName, int absenceCount) {
        this.profName = profName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.subjectName = subjectName;
        this.absenceCount = absenceCount;
    }

    public Absence(String profName, String date, String startTime, String endTime, String reason, String status, String subjectName) {
        this.profName = profName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.subjectName = subjectName;
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
}
