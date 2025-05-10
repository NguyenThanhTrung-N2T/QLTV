package com.main.qltv;

import com.sun.nio.sctp.MessageInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class QLTVController {

    @FXML Button Login;
    @FXML Button Register;
    @FXML Button Exit;

    @FXML
    public void Exit(){
        Platform.exit();
    }

}