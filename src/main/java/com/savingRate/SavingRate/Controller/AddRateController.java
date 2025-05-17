package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.ExpenseDAO;
import com.savingRate.SavingRate.Model.Expences;
import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.IncomeDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;

public class AddRateController {

    @FXML public TextField income_description;
    @FXML public TextField income_value;
    @FXML public DatePicker income_Date;

    @FXML public TextField expens_description;
    @FXML public TextField expens_value;
    @FXML public DatePicker expens_date;
    @FXML public ComboBox<String> categoryCombo;

    @FXML public ListView<String> expens_list;
    public Button search_button;
    public TextField search_box;
    public ComboBox search_list;
    public TableView table;

    @FXML
    public void initialize() {
        categoryCombo.getItems().addAll("Needs", "Wants");
    }

    @FXML
    public void handleAddIncome() {
        try {
            if (income_description.getText().isEmpty() || income_value.getText().isEmpty() || income_Date.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Please fill all income fields.");
                return;
            }
            Income income = new Income();
            income.setDate(Date.valueOf(income_Date.getValue()));
            income.setDescription(income_description.getText());
            income.setValue(Double.parseDouble(income_value.getText()));

            IncomeDAO.insertIncome(income);
            showAlert(Alert.AlertType.INFORMATION, "Income Added Successfully!");
            clearIncomeFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to Add Income: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddExpense() {
        try {
            if (expens_description.getText().isEmpty() || expens_value.getText().isEmpty() || expens_date.getValue() == null || categoryCombo.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Please fill all expense fields.");
                return;
            }
            Expences expense = new Expences();
            expense.setDate(Date.valueOf(expens_date.getValue()));
            expense.setDescription(expens_description.getText());
            expense.setValue(Double.parseDouble(expens_value.getText()));
            expense.setCategory(categoryCombo.getValue());

            ExpenseDAO.insertExpense(expense);
            expens_list.getItems().add(expense.getDescription() + " - " + expense.getCategory());
            showAlert(Alert.AlertType.INFORMATION, "Expense Added Successfully!");
            clearExpenseFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to Add Expense: " + e.getMessage());
        }
    }

    private void clearIncomeFields() {
        income_description.clear();
        income_value.clear();
        income_Date.setValue(null);
    }

    private void clearExpenseFields() {
        expens_description.clear();
        expens_value.clear();
        expens_date.setValue(null);
        categoryCombo.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
