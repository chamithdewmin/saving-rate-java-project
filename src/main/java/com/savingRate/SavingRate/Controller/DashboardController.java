package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.ExpenseDAO;
import com.savingRate.SavingRate.Model.Expences;
import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.IncomeDAO;
import com.savingRate.SavingRate.Utils.ExportUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class DashboardController {

    @FXML public PieChart pie_cart;
    @FXML public Label income_value;
    @FXML public Label expenses_value;
    @FXML public Label saved_value;
    @FXML public Label total_needs;
    @FXML public Label total_wants;
    @FXML public LineChart<String, Number> line_chart;
    @FXML public DatePicker date;

    @FXML
    public void initialize() {
        loadDashboardData();
    }

    private void loadDashboardData() {
        List<Income> incomes = IncomeDAO.getAllIncome();
        List<Expences> expenses = ExpenseDAO.getAllExpenses();

        double totalIncome = incomes.stream().mapToDouble(Income::getValue).sum();
        double totalExpense = expenses.stream().mapToDouble(Expences::getValue).sum();
        double saved = totalIncome - totalExpense;

        double needs = expenses.stream()
                .filter(e -> "Needs".equalsIgnoreCase(e.getCategory()))
                .mapToDouble(Expences::getValue)
                .sum();

        double wants = expenses.stream()
                .filter(e -> "Wants".equalsIgnoreCase(e.getCategory()))
                .mapToDouble(Expences::getValue)
                .sum();

        income_value.setText(String.format("%.2f", totalIncome));
        expenses_value.setText(String.format("%.2f", totalExpense));
        saved_value.setText(String.format("%.2f", saved));
        if (total_needs != null) total_needs.setText(String.format("%.2f", needs));
        if (total_wants != null) total_wants.setText(String.format("%.2f", wants));

        pie_cart.getData().clear();
        pie_cart.getData().add(new PieChart.Data("Total Income", totalIncome));
        pie_cart.getData().add(new PieChart.Data("Total Expenses", totalExpense));
        pie_cart.getData().add(new PieChart.Data("Saved", saved));

        populateLineChart(incomes, expenses);
    }

    private void populateLineChart(List<Income> incomes, List<Expences> expenses) {
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        for (Income income : incomes) {
            incomeSeries.getData().add(new XYChart.Data<>(income.getDate().toString(), income.getValue()));
        }

        for (Expences expense : expenses) {
            expenseSeries.getData().add(new XYChart.Data<>(expense.getDate().toString(), expense.getValue()));
        }

        line_chart.getData().clear();
        line_chart.getData().addAll(incomeSeries, expenseSeries);
    }

    @FXML
    public void handleExportExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("MonthlyBudget.xlsx");

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            ExportUtility.exportToExcel(file.getAbsolutePath());
        }
    }

    @FXML
    public void handleExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("MonthlyBudget.pdf");

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            ExportUtility.exportToPDF(file.getAbsolutePath());
        }
    }

    @FXML
    public void openAddRateWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Xaml/addRate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Rate");
            stage.setScene(new Scene(loader.load(), 1024, 768));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRefresh() {
        loadDashboardData();
    }
}
