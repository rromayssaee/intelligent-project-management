package com.example.intellipm.dto;

public class DashboardStats {

    private long projets;
    private long taches;
    private long utilisateurs;
    private long equipes;

    public DashboardStats(long projets, long taches, long utilisateurs, long equipes) {
        this.projets = projets;
        this.taches = taches;
        this.utilisateurs = utilisateurs;
        this.equipes = equipes;
    }

    public long getProjets() {
        return projets;
    }

    public long getTaches() {
        return taches;
    }

    public long getUtilisateurs() {
        return utilisateurs;
    }

    public long getEquipes() {
        return equipes;
    }
}