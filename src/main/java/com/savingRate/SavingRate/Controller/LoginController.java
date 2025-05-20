package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.Model;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class LoginController {

    @FXML
    public TextField usernaemTxt;

    @FXML
    public PasswordField PasswordTxt;

    @FXML
    public Button LoginBtn;

    @FXML
    public CheckBox showPassword;

    private TextField visiblePassword;

    @FXML
    public void initialize() {
        setupShowPasswordToggle();

        LoginBtn.setOnAction(event -> handleLogin());

        // Handle Enter key for login
        usernaemTxt.setOnKeyPressed(this::handleEnterKey);
        PasswordTxt.setOnKeyPressed(this::handleEnterKey);
        // Also apply it to the visible password field
        visiblePassword.setOnKeyPressed(this::handleEnterKey);
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    private void handleLogin() {
        String username = usernaemTxt.getText();
        String password = showPassword.isSelected() ? visiblePassword.getText() : PasswordTxt.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in both username and password.");
            return;
        }

        if (username.equals("user") && password.equals("123")) {
            // Open main window
            Model.getInstance().getViewFactory().fullWindow();
            // Close login window
            LoginBtn.getScene().getWindow().hide();
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void setupShowPasswordToggle() {
        visiblePassword = new TextField();
        visiblePassword.setLayoutX(PasswordTxt.getLayoutX());
        visiblePassword.setLayoutY(PasswordTxt.getLayoutY());
        visiblePassword.setPrefWidth(PasswordTxt.getPrefWidth());
        visiblePassword.setPrefHeight(PasswordTxt.getPrefHeight());
        visiblePassword.setPromptText("Password");
        visiblePassword.setVisible(false);
        visiblePassword.setManaged(false);

        visiblePassword.textProperty().bindBidirectional(PasswordTxt.textProperty());

        AnchorPane parent = (AnchorPane) PasswordTxt.getParent();
        parent.getChildren().add(visiblePassword);

        showPassword.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                visiblePassword.setVisible(true);
                visiblePassword.setManaged(true);
                PasswordTxt.setVisible(false);
                PasswordTxt.setManaged(false);
            } else {
                visiblePassword.setVisible(false);
                visiblePassword.setManaged(false);
                PasswordTxt.setVisible(true);
                PasswordTxt.setManaged(true);
            }
        });
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
