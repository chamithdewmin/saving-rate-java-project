package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.ExpenseDAO;
import com.savingRate.SavingRate.Model.Expences;
import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.IncomeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.List;

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

    @FXML private Button refreshButton;

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
        loadIncomeData();
        loadExpenseData();
    }
}
