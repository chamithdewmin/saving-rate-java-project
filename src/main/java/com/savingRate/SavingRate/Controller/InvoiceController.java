package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.IncomeDAO;
import com.savingRate.SavingRate.Utils.CustomAlert;
import com.savingRate.SavingRate.Utils.InvoiceUtillty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

public class InvoiceController {
    @FXML public TextField nameTxt;
    @FXML private Button RefreshBtn;
    @FXML private Button removeBtn;
    @FXML private TableView<Income> invoiceTable;
    @FXML private TableColumn<Income, Date> dateColum;
    @FXML private TableColumn<Income, String> desColum;
    @FXML private TableColumn<Income, Double> costColum;
    @FXML private Button addBtn;
    @FXML private Button exportBtn;
    @FXML private ListView<Income> listView;

    private final ObservableList<Income> incomeList = FXCollections.observableArrayList();
    private final ObservableList<Income> selectedItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        dateColum.setCellValueFactory(new PropertyValueFactory<>("date"));
        desColum.setCellValueFactory(new PropertyValueFactory<>("description"));
        costColum.setCellValueFactory(new PropertyValueFactory<>("value"));

        incomeList.addAll(IncomeDAO.getAllIncome());
        invoiceTable.setItems(incomeList);

        listView.setItems(selectedItems);
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Income item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDate() + " | " + item.getDescription() + " | Rs. " + item.getValue());
                }
            }
        });
    }

    @FXML
    public void handleAdd(ActionEvent event) {
        Income selected = invoiceTable.getSelectionModel().getSelectedItem();
        if (selected != null && !selectedItems.contains(selected)) {
            selectedItems.add(selected);
        }
    }

    @FXML
    public void handleRemove(ActionEvent event) {
        Income selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedItems.remove(selected);
        } else {
            CustomAlert.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item from the list to remove.");
        }
    }

    @FXML
    public void handleExport(ActionEvent event) {
        if (selectedItems.isEmpty()) {
            CustomAlert.showAlert(Alert.AlertType.WARNING, "No Items", "Please add at least one item to export.");
            return;
        }

        String clientName = nameTxt.getText();
        if (clientName == null || clientName.isBlank()) {
            CustomAlert.showAlert(Alert.AlertType.WARNING, "Missing Client Name", "Please enter the client name.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Invoice PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Logozo_Invoice.pdf");
        File file = fileChooser.showSaveDialog(invoiceTable.getScene().getWindow());

        if (file != null) {
            String invoiceNumber = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            LocalDate date = LocalDate.now();
            InvoiceUtillty.exportIncomeInvoice(file.getAbsolutePath(), selectedItems, invoiceNumber, date, clientName);
            CustomAlert.showSuccess("Invoice exported successfully!");
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        incomeList.clear();
        incomeList.addAll(IncomeDAO.getAllIncome());
        invoiceTable.refresh();
    }
}