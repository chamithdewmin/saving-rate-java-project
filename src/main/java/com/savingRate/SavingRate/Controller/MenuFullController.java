package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.Model;
import com.savingRate.SavingRate.Views.ViewOption;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuFullController implements Initializable {

    @FXML
    private BorderPane menuParent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getMenuItem().addListener((obs, oldVal, newVal) -> {
            switch (newVal) {
                case DASHBOARD -> menuParent.setCenter(Model.getInstance().getViewFactory().showDashboardView());
                case ADD_RATE -> menuParent.setCenter(Model.getInstance().getViewFactory().showIncomeExpensesView());
                case READ_DATA -> menuParent.setCenter(Model.getInstance().getViewFactory().showSettingView());
            }
        });

        // Set default view
        Model.getInstance().getViewFactory().getMenuItem().set(ViewOption.DASHBOARD);
    }
}