package com.uniajc.modelo;

import java.time.LocalDateTime;


public class Docente {

    
    private int id;

    
    private String name;

    
    private String specialty;

    
    private String email;

    
    private LocalDateTime createdAt;

    
    public Docente() {
    }

    
    public Docente(int id, String name, String specialty, String email, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.createdAt = createdAt;
    }

    
    public Docente(String name, String specialty, String email) {
        this.name = name;
        this.specialty = specialty;
        this.email = email;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}