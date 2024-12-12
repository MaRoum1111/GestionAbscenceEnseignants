package com.example.gestionabscenceenseignants.model;

public class Claim {
    private  String idClaim;
    private String cin;
    private String profName;
    private String date;          // Date 
    private String startTime;     // Heure de début
    private String endTime;       // Heure de fin
    private String claim;        // Réclamation
    private String classe;        // Classe
    private String claimDate;
    // No-argument constructor
    public Claim() {
        // Firebase requires this constructor
    }
    public Claim(String date, String startTime, String endTime, String claim, String classe,String claimDate) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.claim = claim;
        this.classe = classe;
        this.claimDate = claimDate;
    }
    public Claim(String idClaim, String cin,String profName, String date, String startTime, String endTime, String claim, String classe) {
        this.idClaim = idClaim;
        this.cin = cin;
        this.profName =profName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.claim = claim;
        this.classe = classe;

    }
    public Claim(String idClaim,String date, String startTime, String endTime, String claim, String classe,String claimDate) {
        this.idClaim = idClaim;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.claim = claim;
        this.classe = classe;
        this.claimDate = claimDate;

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

    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getIdClaim() {
        return idClaim;
    }

    public void setIdClaim(String idClaim) {
        this.idClaim = idClaim;
    }
}
