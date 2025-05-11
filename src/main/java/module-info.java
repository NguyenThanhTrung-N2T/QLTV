module com.main.qltv {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires jdk.sctp;
    requires java.sql;

    opens com.main.qltv to javafx.fxml;
    opens com.main.qltv.controller to javafx.fxml;
    exports com.main.qltv;
}