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

public class SignUpScreenController {

    @FXML
    private TextField FnameField;
    @FXML
    private TextField LnameField;
    @FXML
    private TextField EmailField;
    @FXML
    private PasswordField PassField;
    @FXML
    private PasswordField VerifyPassField;

    @FXML
    private void onSignUpBtnClicked() {

        try {

            System.out.println("SignUp Button Clicked");

            if (FnameField.getText().equals("") || LnameField.getText().equals("") || EmailField.getText().equals("") || PassField.getText().equals("") || VerifyPassField.getText().equals("")) {
                MessageBox.warningMessageBox("Please fill up all information");
                System.out.println("Please fill up all information");
            } else if (checkEmailCondition()) {
                MessageBox.warningMessageBox("Please input email format correctly");
                EmailField.setText("");
            } else {

                if (PassField.getText().equals(VerifyPassField.getText())) {
                    String errorMessage = UserManager.signup(FnameField.getText(), LnameField.getText(), EmailField.getText(), PassField.getText());
                    System.out.println(errorMessage);

                    if (errorMessage.equals("Email has been used. Please use another email")) {
                        MessageBox.warningMessageBox("Email has been used. Please use another email");
                        EmailField.setText("");
                    }

                } else {
                    MessageBox.warningMessageBox("Password not match");
                    System.out.println("Password not match");

                    PassField.setText("");
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

    private boolean checkEmailCondition() {
        String get = EmailField.getText();
        char a = '@';
        char dot = '.';
        String message = "";
        int count = 0;
        int atPosition = 0, dotPosition = 0, flag = 0;

        for (int i = 0; i < get.length(); i++) {

            if (get.charAt(i) == a) {
                count++;
                atPosition = i;

                if (count >= 2) {
                    flag = 1;
                    break;
                }
            }

            if (get.charAt(i) == dot) {
                dotPosition = i;
            }
        }

        if (atPosition < 1 || dotPosition < atPosition + 2 || dotPosition + 2 >= get.length() || flag == 1) {
            message = "Not valid email address";
            System.out.println(message);
            return true;
        } else {
            message = "Valid email address";
            System.out.println(message);
            return false;
        }
    }
}