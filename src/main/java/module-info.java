module com.monthlysave.monthlysave {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.monthlysave.monthlysave to javafx.fxml;
    exports com.monthlysave.monthlysave;
}