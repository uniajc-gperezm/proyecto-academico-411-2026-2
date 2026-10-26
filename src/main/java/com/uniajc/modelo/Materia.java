package com.uniajc.modelo;

import java.time.LocalDateTime;


public class Materia {

    
    private int id;

    
    private String name;

    
    private int credits;

    
    private LocalDateTime createdAt;

    
    public Materia() {
    }

    
    public Materia(int id, String name, int credits, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.createdAt = createdAt;
    }

   
    public Materia(String name, int credits) {
        this.name = name;
        this.credits = credits;
    }

   
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}