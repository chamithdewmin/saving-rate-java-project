package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.*;
import com.savingRate.SavingRate.Utils.CustomAlert;
import com.savingRate.SavingRate.Utils.InvoiceNumberDAO;
import com.savingRate.SavingRate.Utils.InvoiceUtillty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;

public class InvoiceController {

    @FXML public TextField nameTxt;
    @FXML private TableColumn<InvoiceItem, String> typeColume;
    @FXML private Button RefreshBtn;
    @FXML private Button removeBtn;
    @FXML private TableView<InvoiceItem> invoiceTable;
    @FXML private TableColumn<InvoiceItem, LocalDate> dateColum;
    @FXML private TableColumn<InvoiceItem, String> desColum;
    @FXML private TableColumn<InvoiceItem, Double> costColum;
    @FXML private Button addBtn;
    @FXML private Button exportBtn;
    @FXML private ListView<InvoiceItem> listView;

    private final ObservableList<InvoiceItem> allItems = FXCollections.observableArrayList();
    private final ObservableList<InvoiceItem> selectedItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup table columns
        dateColum.setCellValueFactory(new PropertyValueFactory<>("date"));
        desColum.setCellValueFactory(new PropertyValueFactory<>("description"));
        costColum.setCellValueFactory(new PropertyValueFactory<>("value"));
        typeColume.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Style 'Type' column
        typeColume.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("type-complete", "type-pending");

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equalsIgnoreCase("Complete")) {
                        getStyleClass().add("type-complete");
                    } else if (item.equalsIgnoreCase("Pending")) {
                        getStyleClass().add("type-pending");
                    }
                }
            }
        });

        invoiceTable.setItems(allItems);
        listView.setItems(selectedItems);

        // Style list view
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(InvoiceItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDate() + " | " + item.getDescription() + " | Rs. " + item.getValue());
                }
            }
        });

        loadAllData();
    }

    private void loadAllData() {
        allItems.clear();

        // Load income items
        for (Income income : IncomeDAO.getAllIncome()) {
            allItems.add(new InvoiceItem(
                    income.getDate().toLocalDate(),
                    income.getDescription(),
                    income.getValue(),
                    false
            ));
        }

        // Load reminder (pending) items
        for (Reminder r : ReminderDAO.getAllReminders()) {
            if ("income".equalsIgnoreCase(r.getType())) {
                allItems.add(new InvoiceItem(
                        r.getDate(),
                        r.getDescription(),
                        r.getCost(),
                        true
                ));
            }
        }

        invoiceTable.refresh();
    }

    @FXML
    public void handleAdd(ActionEvent event) {
        InvoiceItem selected = invoiceTable.getSelectionModel().getSelectedItem();
        if (selected != null && !selectedItems.contains(selected)) {
            selectedItems.add(selected);
        }
    }

    @FXML
    public void handleRemove(ActionEvent event) {
        InvoiceItem selected = listView.getSelectionModel().getSelectedItem();
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
        LocalDate date = LocalDate.now();
        fileChooser.setInitialFileName("Logozo_Invoice_" + date + ".pdf");
        File file = fileChooser.showSaveDialog(invoiceTable.getScene().getWindow());

        if (file != null) {
            String invoiceNumber = InvoiceNumberDAO.getNextInvoiceNumber(); // ✅ auto-incremented invoice number
            InvoiceUtillty.exportUnifiedInvoice(file.getAbsolutePath(), selectedItems, invoiceNumber, date, clientName);
            CustomAlert.showSuccess("Invoice exported successfully!");
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        loadAllData();
    }
}
