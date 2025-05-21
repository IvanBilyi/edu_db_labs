package com.example.model;

import java.util.UUID;

public class StudyGroup {
    private UUID id;
    private String name;
    private int year;

    public StudyGroup() {}

    public StudyGroup(UUID id, String name, int year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}