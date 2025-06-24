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

    // ✅ Your working credentials from Postman
    private final String USER_ID = "295";
    private final String API_KEY = "ba048971-6e14-4358-87b6-b2add09a6734";
    private final String SENDER_ID = "SMSlenzDEMO";

    @FXML
    private void sendSms() {
        String number = numberTxt.getText().trim();
        String message = massageTxt.getText().trim();

        if (number.isEmpty() || message.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both the phone number and message.");
            return;
        }

        try {
            // Format as x-www-form-urlencoded
            String params = String.format("user_id=%s&api_key=%s&sender_id=%s&contact=%s&message=%s",
                    URLEncoder.encode(USER_ID, "UTF-8"),
                    URLEncoder.encode(API_KEY, "UTF-8"),
                    URLEncoder.encode(SENDER_ID, "UTF-8"),
                    URLEncoder.encode(number, "UTF-8"),
                    URLEncoder.encode(message, "UTF-8")
            );

            URL url = new URL("https://smslenz.lk/api/send-sms");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Message sent successfully!");
                numberTxt.clear();
                massageTxt.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to send SMS. Response code: " + responseCode);
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Exception", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}