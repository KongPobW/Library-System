package com.example.librarysystem.controllers;

import com.example.librarysystem.Application;
import com.example.librarysystem.classes.MessageBox;
import com.example.librarysystem.utils.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ResetPassController {

    @FXML
    private TextField EmailField;
    @FXML
    private PasswordField NewPassField;
    @FXML
    private PasswordField VerifyPassField;

    @FXML
    private void onResetBtnClicked() {

        try {

            System.out.println("Reset Password Button Clicked");

            if (NewPassField.getText().equals("") || VerifyPassField.getText().equals("")) {
                MessageBox.warningMessageBox("Please fill up all information");
                System.out.println("Please fill up all information");
            } else {

                if (NewPassField.getText().equals(VerifyPassField.getText())) {
                    String errorMessage = UserManager.resetPassword(EmailField.getText(), NewPassField.getText());
                    System.out.println(errorMessage);

                    if (errorMessage.equals("Please input your email correctly")) {
                        MessageBox.warningMessageBox("Please input your email correctly");
                        EmailField.setText("");
                    }

                } else {
                    MessageBox.warningMessageBox("Password not match");
                    System.out.println("Password not match");

                    NewPassField.setText("");
                    VerifyPassField.setText("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackBtnClicked() throws IOException {
        System.out.println("Back To Login Button Clicked");

        FXMLLoader loader = Application.sceneManager.getLoader("screen_login.fxml");

        Scene scene = new Scene(loader.load());

        Application.sceneManager.changeScene(scene);
    }
}