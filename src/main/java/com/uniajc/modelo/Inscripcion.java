package com.uniajc.modelo;

import java.time.LocalDateTime;

public class Inscripcion {

    private int id;
    private int idEstudiante;
    private int idGrupo;
    private double finalGrade;
    private String status;
    private LocalDateTime createdAt;

    public Inscripcion() {
    }

    public Inscripcion(int id, int idEstudiante, int idGrupo, double finalGrade, String status, LocalDateTime createdAt) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.finalGrade = finalGrade;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Inscripcion(int idEstudiante, int idGrupo, double finalGrade, String status) {
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.finalGrade = finalGrade;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}