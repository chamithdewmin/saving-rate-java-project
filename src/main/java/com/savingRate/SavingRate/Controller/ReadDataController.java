package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.ExpenseDAO;
import com.savingRate.SavingRate.Model.Expences;
import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.IncomeDAO;
import com.savingRate.SavingRate.Utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReadDataController {

    @FXML private TableView<Income> incomeTable;
    @FXML private TableColumn<Income, Date> incomeDateCol;
    @FXML private TableColumn<Income, String> incomeDescCol;
    @FXML private TableColumn<Income, Double> incomeValueCol;

    @FXML private TableView<Expences> expenseTable;
    @FXML private TableColumn<Expences, Date> expenseDateCol;
    @FXML private TableColumn<Expences, String> expenseDescCol;
    @FXML private TableColumn<Expences, String> expenseCategoryCol;
    @FXML private TableColumn<Expences, Double> expenseValueCol;

    @FXML private Button deleteIncomeBtn;
    @FXML private Button deleteExpenseBtn;
    @FXML private Button refreshButton;
    @FXML private DatePicker filterDatePicker;

    @FXML
    public void initialize() {
        incomeDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        incomeDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        incomeValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        expenseDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        expenseDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        expenseCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        expenseValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        loadIncomeData();
        loadExpenseData();
    }

    private void loadIncomeData() {
        List<Income> incomeList = IncomeDAO.getAllIncome().stream()
                .sorted(Comparator.comparing(Income::getDate))
                .collect(Collectors.toList());

        ObservableList<Income> incomeObservable = FXCollections.observableArrayList(incomeList);
        incomeTable.setItems(incomeObservable);
    }

    private void loadExpenseData() {
        List<Expences> expenseList = ExpenseDAO.getAllExpenses().stream()
                .sorted(Comparator.comparing(Expences::getDate))
                .collect(Collectors.toList());

        ObservableList<Expences> expenseObservable = FXCollections.observableArrayList(expenseList);
        expenseTable.setItems(expenseObservable);
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        filterDatePicker.setValue(null);
        loadIncomeData();
        loadExpenseData();
    }

    @FXML
    private void handleDeleteIncome(ActionEvent event) {
        Income selectedIncome = incomeTable.getSelectionModel().getSelectedItem();
        if (selectedIncome != null) {
            String msg = "Date: " + selectedIncome.getDate() +
                    "\nDescription: " + selectedIncome.getDescription() +
                    "\nValue: " + selectedIncome.getValue();

            boolean confirmed = CustomAlert.showConfirmation("Delete Confirmation",
                    "Are you sure you want to delete this income?\n\n" + msg);

            if (confirmed) {
                IncomeDAO.deleteIncome(selectedIncome.getId());
                loadIncomeData();
                CustomAlert.showSuccess("Income deleted successfully.");
            }
        } else {
            CustomAlert.showAlert("No Income Selected", "Please select an income record to delete.");
        }
    }

    @FXML
    private void handleDeleteExpense(ActionEvent event) {
        Expences selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
        if (selectedExpense != null) {
            String msg = "Date: " + selectedExpense.getDate() +
                    "\nDescription: " + selectedExpense.getDescription() +
                    "\nCategory: " + selectedExpense.getCategory() +
                    "\nValue: " + selectedExpense.getValue();

            boolean confirmed = CustomAlert.showConfirmation("Delete Confirmation",
                    "Are you sure you want to delete this expense?\n\n" + msg);

            if (confirmed) {
                ExpenseDAO.deleteExpense(selectedExpense.getId());
                loadExpenseData();
                CustomAlert.showSuccess("Expense deleted successfully.");
            }
        } else {
            CustomAlert.showAlert("No Expense Selected", "Please select an expense record to delete.");
        }
    }

    @FXML
    private void handleFilterByMonth(ActionEvent event) {
        LocalDate selectedDate = filterDatePicker.getValue();

        if (selectedDate == null) {
            CustomAlert.showAlert("Invalid Date", "Please select a date to filter by month.");
            return;
        }

        int month = selectedDate.getMonthValue();
        int year = selectedDate.getYear();

        List<Income> filteredIncome = IncomeDAO.getAllIncome().stream()
                .filter(i -> {
                    LocalDate d = i.getDate().toLocalDate();
                    return d.getMonthValue() == month && d.getYear() == year;
                })
                .sorted(Comparator.comparing(Income::getDate))
                .collect(Collectors.toList());

        List<Expences> filteredExpense = ExpenseDAO.getAllExpenses().stream()
                .filter(e -> {
                    LocalDate d = e.getDate().toLocalDate();
                    return d.getMonthValue() == month && d.getYear() == year;
                })
                .sorted(Comparator.comparing(Expences::getDate))
                .collect(Collectors.toList());

        incomeTable.setItems(FXCollections.observableArrayList(filteredIncome));
        expenseTable.setItems(FXCollections.observableArrayList(filteredExpense));
    }
}
