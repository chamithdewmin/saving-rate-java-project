package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.Model;
import com.savingRate.SavingRate.Views.ViewOption;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    public Button reminderBtn;
    public Button invoiceBtn;
    public Button taskBtn;
    public Button smsBtn;
    @FXML
    private Button dashboardButton;

    @FXML
    private Button AddRateButton;

    @FXML
    private Button SettingButton;

    @FXML
    private void initialize() {
        dashboardButton.setOnAction(e -> Model.getInstance().getViewFactory().getMenuItem().set(ViewOption.DASHBOARD));
        AddRateButton.setOnAction(e -> Model.getInstance().getViewFactory().getMenuItem().set(ViewOption.ADD_RATE));
        SettingButton.setOnAction(e -> Model.getInstance().getViewFactory().getMenuItem().set(ViewOption.READ_DATA));
        reminderBtn.setOnAction(e -> Model.getInstance().getViewFactory().getMenuItem().set(ViewOption.REMINDER));
        invoiceBtn.setOnAction(e -> Model.getInstance().getViewFactory().getMenuItem().set(ViewOption.INVOICE));
        taskBtn.setOnAction(e -> Model.getInstance().getViewFactory().getMenuItem().set(ViewOption.TASK));
        smsBtn.setOnAction(e -> Model.getInstance().getViewFactory().getMenuItem().set(ViewOption.SMS));



    }
}