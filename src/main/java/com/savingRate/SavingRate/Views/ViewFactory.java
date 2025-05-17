package com.savingRate.SavingRate.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
    private final ObjectProperty<ViewOption> menuItem = new SimpleObjectProperty<>();
    private AnchorPane dashboardView;
    private AnchorPane incomeExpensesView;
    private AnchorPane settingView;

    public ObjectProperty<ViewOption> getMenuItem() {
        return menuItem;
    }

    public AnchorPane showDashboardView() {
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Xaml/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane showIncomeExpensesView() {
        if (incomeExpensesView == null) {
            try {
                incomeExpensesView = new FXMLLoader(getClass().getResource("/Xaml/addRate.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return incomeExpensesView;
    }

    public AnchorPane showSettingView() {
        if (settingView == null) {
            try {
                settingView = new FXMLLoader(getClass().getResource("/Xaml/Setting.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return settingView;
    }

    public void fullWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Xaml/MenuFull.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Monthly Save");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}