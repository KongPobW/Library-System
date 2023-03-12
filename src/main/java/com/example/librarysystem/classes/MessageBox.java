package com.example.librarysystem.classes;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class MessageBox {

    public enum TypeMessageBox {
        GET_RESULT, NON_RESULT
    }

    public static boolean informationMessageBox(String text, TypeMessageBox type) {
        if (type == TypeMessageBox.NON_RESULT) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
            alert.initStyle(StageStyle.UTILITY);
            alert.showAndWait();
        } else { //TypeMessageBox.GET_RESULT
            Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
            alert.initStyle(StageStyle.UTILITY);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            return result == ButtonType.NO;
        }
        return false;
    }

    public static void warningMessageBox(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
}