package com.savingRate.SavingRate.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SmsSenderController {

    @FXML
    private TextField numberTxt;

    @FXML
    private TextArea massageTxt;

    // ✅ Replace with your actual credentials
    private final String USER_ID = "29693";
    private final String API_KEY = "iL42F9QCwoql7CKOy66h";
    private final String SENDER_ID = "NotifyDEMO"; // Or your approved sender ID

    @FXML
    private void sendSms() {
        String number = numberTxt.getText().trim();
        String message = massageTxt.getText().trim();

        if (number.isEmpty() || message.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both the phone number and message.");
            return;
        }

        try {
            // Prepare URL-encoded parameters
            String params = String.format("user_id=%s&api_key=%s&sender_id=%s&to=%s&message=%s",
                    URLEncoder.encode(USER_ID, "UTF-8"),
                    URLEncoder.encode(API_KEY, "UTF-8"),
                    URLEncoder.encode(SENDER_ID, "UTF-8"),
                    URLEncoder.encode(number, "UTF-8"),
                    URLEncoder.encode(message, "UTF-8")
            );

            URL url = new URL("https://app.notify.lk/api/v1/send");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Message sent successfully!");
                numberTxt.clear();
                massageTxt.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to send message. Response code: " + responseCode);
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
