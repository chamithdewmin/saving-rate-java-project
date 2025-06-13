package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.*;
import com.savingRate.SavingRate.Utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class ReminderController {

    public Label total_income;
    public Label total_expence;
    public Button addBtn;
    @FXML private TableView<Reminder> reminderTable;
    @FXML private TableColumn<Reminder, String> typeColumn;
    @FXML private TableColumn<Reminder, LocalDate> dateColumn;
    @FXML private TableColumn<Reminder, String> descriptionColumn;
    @FXML private TableColumn<Reminder, Double> costColumn;
    @FXML private Button incomeButton;
    @FXML private Button expenseButton;
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
        deleteBtn.setOnAction(e -> handleDeleteReminder());
        addBtn.setOnAction(e -> handleMoveToRegular());

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

    private void handleMoveToRegular() {
        Reminder selected = reminderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CustomAlert.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a reminder to transfer.");
            return;
        }

        String summary = "Type: " + selected.getType() +
                "\nDate: " + selected.getDate() +
                "\nDescription: " + selected.getDescription() +
                "\nCost: Rs " + selected.getCost();

        boolean confirmed = CustomAlert.showConfirmation("Confirm Transfer", "Are you sure you want to move this to regular "+selected.getType()+"?\n\n" + summary);

        if (!confirmed) return;

        try {
            if ("Income".equalsIgnoreCase(selected.getType())) {
                Income income = new Income();
                income.setDate(Date.valueOf(selected.getDate()));
                income.setDescription(selected.getDescription());
                income.setValue(selected.getCost());
                IncomeDAO.insertIncome(income);
            } else if ("Expense".equalsIgnoreCase(selected.getType())) {
                Expences expense = new Expences();
                expense.setDate(Date.valueOf(selected.getDate()));
                expense.setDescription(selected.getDescription());
                expense.setValue(selected.getCost());
                expense.setCategory("Wants"); // default category
                ExpenseDAO.insertExpense(expense);
            }
            ReminderDAO.deleteReminder(selected.getId());
            reminders.remove(selected);
            CustomAlert.showSuccess("Reminder successfully transferred to " + selected.getType() + ".");
            loadReminders();
        } catch (Exception e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to transfer reminder: " + e.getMessage());
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
            loadReminders();
        }
    }

    private void loadReminders() {
        reminders.setAll(ReminderDAO.getAllReminders());

        double totalIncome = 0;
        double totalExpense = 0;

        for (Reminder reminder : reminders) {
            if ("Income".equalsIgnoreCase(reminder.getType())) {
                totalIncome += reminder.getCost();
            } else if ("Expense".equalsIgnoreCase(reminder.getType())) {
                totalExpense += reminder.getCost();
            }
        }

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "US"));
        total_income.setText("Rs " + formatter.format(totalIncome));
        total_expence.setText("Rs " + formatter.format(totalExpense));
    }

    private void clearReminderFields() {
        date.setValue(null);
        description.clear();
        cost.clear();
    }
}