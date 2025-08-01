module com.savingRate.SavingRate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // ✅ Add these to fix the read errors
    requires static org.apache.poi.poi;
    requires static org.apache.poi.ooxml;
    requires mysql.connector.j;
    requires itextpdf;

    opens com.savingRate.SavingRate.Model to javafx.base;
    opens com.savingRate.SavingRate.Controller to javafx.fxml;

    exports com.savingRate.SavingRate;
    opens com.savingRate.SavingRate.Views to javafx.fxml;
    opens com.savingRate.SavingRate.Utils to javafx.base, javafx.fxml;
}
