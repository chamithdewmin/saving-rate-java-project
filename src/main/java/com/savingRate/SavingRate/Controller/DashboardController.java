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
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML public Label income_value;
    @FXML public Label expenses_value;
    @FXML public Label saved_value;
    @FXML public Label total_needs;
    @FXML public Label total_wants;
    @FXML public LineChart<String, Number> line_chart;
    @FXML public DatePicker date;
    @FXML public PieChart PieChart;
    @FXML public RadioButton viewAllRadio;

    @FXML
    public void initialize() {
        // Set default date and load current month data
        date.setValue(LocalDate.now());
        loadDashboardData(LocalDate.now(), false);
    }

    private void loadDashboardData(LocalDate selectedDate, boolean viewAllMonths) {
        List<Income> incomes = IncomeDAO.getAllIncome();
        List<Expences> expenses = ExpenseDAO.getAllExpenses();

        List<Income> filteredIncomes = incomes.stream()
                .filter(i -> {
                    LocalDate d = i.getDate().toLocalDate();
                    return viewAllMonths
                            ? d.getYear() == selectedDate.getYear()
                            : (d.getMonth() == selectedDate.getMonth() && d.getYear() == selectedDate.getYear());
                })
                .collect(Collectors.toList());

        List<Expences> filteredExpenses = expenses.stream()
                .filter(e -> {
                    LocalDate d = e.getDate().toLocalDate();
                    return viewAllMonths
                            ? d.getYear() == selectedDate.getYear()
                            : (d.getMonth() == selectedDate.getMonth() && d.getYear() == selectedDate.getYear());
                })
                .collect(Collectors.toList());

        double totalIncome = filteredIncomes.stream().mapToDouble(Income::getValue).sum();
        double totalExpense = filteredExpenses.stream().mapToDouble(Expences::getValue).sum();
        double saved = totalIncome - totalExpense;

        double needs = filteredExpenses.stream()
                .filter(e -> "Needs".equalsIgnoreCase(e.getCategory()))
                .mapToDouble(Expences::getValue)
                .sum();

        double wants = filteredExpenses.stream()
                .filter(e -> "Wants".equalsIgnoreCase(e.getCategory()))
                .mapToDouble(Expences::getValue)
                .sum();

        income_value.setText(String.format("%.2f", totalIncome));
        expenses_value.setText(String.format("%.2f", totalExpense));
        saved_value.setText(String.format("%.2f", saved));
        total_needs.setText(String.format("%.2f", needs));
        total_wants.setText(String.format("%.2f", wants));

        PieChart.getData().clear();
        PieChart.getData().add(new PieChart.Data("Total Income", totalIncome));
        PieChart.getData().add(new PieChart.Data("Total Expenses", totalExpense));
        PieChart.getData().add(new PieChart.Data("Saved", saved));

        populateLineChart(filteredIncomes, filteredExpenses);
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
    public void handleRefresh() {
        LocalDate selectedDate = date.getValue();
        if (selectedDate != null) {
            boolean viewAll = viewAllRadio.isSelected();
            loadDashboardData(selectedDate, viewAll);
        } else {
            loadDashboardData(LocalDate.now(), false);
        }
    }

    @FXML
    public void handleExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Finzo_Monthly_Report.pdf");

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            LocalDate selectedDate = date.getValue();
            boolean viewAll = viewAllRadio.isSelected();

            List<Income> filteredIncomes = IncomeDAO.getAllIncome().stream()
                    .filter(i -> {
                        LocalDate d = i.getDate().toLocalDate();
                        return viewAll
                                ? d.getYear() == selectedDate.getYear()
                                : d.getMonth() == selectedDate.getMonth() && d.getYear() == selectedDate.getYear();
                    })
                    .collect(Collectors.toList());

            List<Expences> filteredExpenses = ExpenseDAO.getAllExpenses().stream()
                    .filter(e -> {
                        LocalDate d = e.getDate().toLocalDate();
                        return viewAll
                                ? d.getYear() == selectedDate.getYear()
                                : d.getMonth() == selectedDate.getMonth() && d.getYear() == selectedDate.getYear();
                    })
                    .collect(Collectors.toList());

            ExportUtility.exportToPDF(file.getAbsolutePath(), filteredIncomes, filteredExpenses, selectedDate, viewAll);
        }
    }

    @FXML
    public void openAddRateWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Xaml/addRate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Rate");
            stage.setScene(new Scene(loader.load(), 1200, 800));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
