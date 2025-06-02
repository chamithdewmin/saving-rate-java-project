// ReminderController.java
package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.Reminder;
import com.savingRate.SavingRate.Model.ReminderDAO;
import com.savingRate.SavingRate.Utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class ReminderController {

    @FXML private TableView<Reminder> reminderTable;
    @FXML private TableColumn<Reminder, String> typeColumn;
    @FXML private TableColumn<Reminder, LocalDate> dateColumn;
    @FXML private TableColumn<Reminder, String> descriptionColumn;
    @FXML private TableColumn<Reminder, Double> costColumn;
    @FXML private Button incomeButton;
    @FXML private Button expenseButton;
    @FXML private Button refreshButton;
    @FXML private Button deleteBtn;
    @FXML private DatePicker date;
    @FXML private TextField description;
    @FXML private TextField cost;

    private final ObservableList<Reminder> reminders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());
        descriptionColumn.setCellValueFactory(data -> data.getValue().descriptionProperty());
        costColumn.setCellValueFactory(data -> data.getValue().costProperty().asObject());
        reminderTable.setItems(reminders);

        incomeButton.setOnAction(e -> handleAddReminder("Income"));
        expenseButton.setOnAction(e -> handleAddReminder("Expense"));
        refreshButton.setOnAction(e -> loadReminders());
        deleteBtn.setOnAction(e -> handleDeleteReminder());

        loadReminders();
    }

    private void handleAddReminder(String type) {
        try {
            LocalDate selectedDate = date.getValue();
            String desc = description.getText().trim();
            String costText = cost.getText().trim();

            if (selectedDate == null || desc.isEmpty() || costText.isEmpty()) {
                CustomAlert.showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill all reminder fields.");
                return;
            }

            double amount = Double.parseDouble(costText);
            Reminder reminder = new Reminder(0, type, selectedDate, desc, amount);
            ReminderDAO.saveReminder(reminder);
            CustomAlert.showSuccess(type + " Reminder Added Successfully!");
            clearReminderFields();
            loadReminders();

        } catch (NumberFormatException e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Cost must be a valid number.");
        } catch (Exception e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to Add Reminder: " + e.getMessage());
        }
    }

    private void handleDeleteReminder() {
        Reminder selected = reminderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CustomAlert.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a reminder to delete.");
            return;
        }

        String msg = "Type: " + selected.getType() +
                "\nDate: " + selected.getDate() +
                "\nDescription: " + selected.getDescription() +
                "\nCost: Rs " + selected.getCost();

        boolean confirmed = CustomAlert.showConfirmation("Delete Confirmation",
                "Are you sure you want to delete this reminder?\n\n" + msg);

        if (confirmed) {
            ReminderDAO.deleteReminder(selected.getId());
            reminders.remove(selected);
            CustomAlert.showSuccess("Reminder deleted successfully.");
        }
    }

    private void loadReminders() {
        reminders.setAll(ReminderDAO.getAllReminders());
    }

    private void clearReminderFields() {
        date.setValue(null);
        description.clear();
        cost.clear();
    }
}
