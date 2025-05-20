package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.ExpenseDAO;
import com.savingRate.SavingRate.Model.Expences;
import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.IncomeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.time.LocalDate;
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
        loadIncomeData();
        loadExpenseData();
    }

    private void loadIncomeData() {
        List<Income> incomeList = IncomeDAO.getAllIncome();
        ObservableList<Income> incomeObservable = FXCollections.observableArrayList(incomeList);

        incomeDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        incomeDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        incomeValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        incomeTable.setItems(incomeObservable);
    }

    private void loadExpenseData() {
        List<Expences> expenseList = ExpenseDAO.getAllExpenses();
        ObservableList<Expences> expenseObservable = FXCollections.observableArrayList(expenseList);

        expenseDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        expenseDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        expenseCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        expenseValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

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
            IncomeDAO.deleteIncome(selectedIncome.getId());
            loadIncomeData();
        } else {
            showAlert("No Income Selected", "Please select an income record to delete.");
        }
    }

    @FXML
    private void handleDeleteExpense(ActionEvent event) {
        Expences selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
        if (selectedExpense != null) {
            ExpenseDAO.deleteExpense(selectedExpense.getId());
            loadExpenseData();
        } else {
            showAlert("No Expense Selected", "Please select an expense record to delete.");
        }
    }

    @FXML
    private void handleFilterByMonth(ActionEvent event) {
        LocalDate selectedDate = filterDatePicker.getValue();

        if (selectedDate == null) {
            showAlert("Invalid Date", "Please select a date to filter by month.");
            return;
        }

        int month = selectedDate.getMonthValue();
        int year = selectedDate.getYear();

        // Filter Income
        List<Income> incomeList = IncomeDAO.getAllIncome().stream()
                .filter(i -> {
                    LocalDate d = i.getDate().toLocalDate();
                    return d.getMonthValue() == month && d.getYear() == year;
                })
                .collect(Collectors.toList());

        // Filter Expenses
        List<Expences> expenseList = ExpenseDAO.getAllExpenses().stream()
                .filter(e -> {
                    LocalDate d = e.getDate().toLocalDate();
                    return d.getMonthValue() == month && d.getYear() == year;
                })
                .collect(Collectors.toList());

        incomeTable.setItems(FXCollections.observableArrayList(incomeList));
        expenseTable.setItems(FXCollections.observableArrayList(expenseList));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
