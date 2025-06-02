package com.savingRate.SavingRate.Model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Reminder {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty cost = new SimpleDoubleProperty();

    public Reminder(int id, String type, LocalDate date, String description, double cost) {
        this.id.set(id);
        this.type.set(type);
        this.date.set(date);
        this.description.set(description);
        this.cost.set(cost);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }
    public StringProperty typeProperty() { return type; }

    public LocalDate getDate() { return date.get(); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    public double getCost() { return cost.get(); }
    public void setCost(double cost) { this.cost.set(cost); }
    public DoubleProperty costProperty() { return cost; }
}