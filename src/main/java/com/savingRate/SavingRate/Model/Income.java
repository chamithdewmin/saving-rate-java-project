package com.savingRate.SavingRate.Model;

import java.sql.Date;

public class Income {
    private int id;
    private Date date;
    private String description;
    private double value;

    // Constructor, Getters & Setters

    public Income(int id, Date date, String description, double value) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.value = value;
    }

    public Income() {

    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
