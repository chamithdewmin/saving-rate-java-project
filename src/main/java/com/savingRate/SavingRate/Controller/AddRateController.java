package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.ExpenseDAO;
import com.savingRate.SavingRate.Model.Expences;
import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.IncomeDAO;
import com.savingRate.SavingRate.Utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;

public class AddRateController {

    @FXML private TextField income_description;
    @FXML private TextField income_value;
    @FXML private DatePicker income_Date;

    @FXML private TextField expens_description;
    @FXML private TextField expens_value;
    @FXML private DatePicker expens_date;
    @FXML private ComboBox<String> categoryCombo;

    @FXML private ListView<String> expens_list;
    @FXML private Button search_button;
    @FXML private TextField search_box;
    @FXML private ComboBox<String> search_list;
    @FXML private TableView<?> table;

    @FXML
    public void initialize() {
        categoryCombo.getItems().addAll("Needs", "Wants");
    }

    @FXML
    public void handleAddIncome() {
        try {
            if (income_description.getText().isEmpty() ||
                    income_value.getText().isEmpty() ||
                    income_Date.getValue() == null) {
                CustomAlert.showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill all income fields.");
                return;
            }

            Income income = new Income();
            income.setDate(Date.valueOf(income_Date.getValue()));
            income.setDescription(income_description.getText());
            income.setValue(Double.parseDouble(income_value.getText()));

            IncomeDAO.insertIncome(income);
            CustomAlert.showSuccess("Income Added Successfully!");
            clearIncomeFields();
        } catch (Exception e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to Add Income: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddExpense() {
        try {
            if (expens_description.getText().isEmpty() ||
                    expens_value.getText().isEmpty() ||
                    expens_date.getValue() == null ||
                    categoryCombo.getValue() == null) {
                CustomAlert.showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill all expense fields.");
                return;
            }

            Expences expense = new Expences();
            expense.setDate(Date.valueOf(expens_date.getValue()));
            expense.setDescription(expens_description.getText());
            expense.setValue(Double.parseDouble(expens_value.getText()));
            expense.setCategory(categoryCombo.getValue());

            ExpenseDAO.insertExpense(expense);
            expens_list.getItems().add(expense.getDescription() + " - " + expense.getCategory());
            CustomAlert.showSuccess("Expense Added Successfully!");
            clearExpenseFields();
        } catch (Exception e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to Add Expense: " + e.getMessage());
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
}
