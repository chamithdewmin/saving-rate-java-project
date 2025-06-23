package com.savingRate.SavingRate.Model;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String priority;
    private boolean completed;
    private String remaining; // for UI only

    public Task(int id, String title, String description, LocalDate dueDate, String priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public String getRemaining() { return remaining; }

    // Setters
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setRemaining(String remaining) { this.remaining = remaining; }
}
