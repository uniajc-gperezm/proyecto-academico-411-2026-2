package com.uniajc.modelo;

import java.time.LocalDateTime;

public class Grupo {

    private int id;
    private int idMateria;
    private int idDocente;
    private String classroom;
    private String schedule;
    private LocalDateTime createdAt;

    public Grupo() {
    }

    public Grupo(int id, int idMateria, int idDocente, String classroom, String schedule, LocalDateTime createdAt) {
        this.id = id;
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.classroom = classroom;
        this.schedule = schedule;
        this.createdAt = createdAt;
    }

    public Grupo(int idMateria, int idDocente, String classroom, String schedule) {
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.classroom = classroom;
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
