module com.monthlysave.monthlysave {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.savingRate.SavingRate to javafx.fxml;
    exports com.savingRate.SavingRate;
}