package com.netinfo.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utility {

    public static void reportError(String msg) {
	Alert alert = new Alert(AlertType.ERROR);
	alert.setContentText(msg);
	alert.showAndWait();
    }
}
