package com.example.librarysystem.controllers;

import com.example.librarysystem.Application;
import com.example.librarysystem.classes.MessageBox;
import com.example.librarysystem.classes.User;
import com.example.librarysystem.utils.UserManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginScreenController {

    private boolean debounce = false;

    @FXML
    private Text LoginMessage;
    @FXML
    private TextField EmailField;
    @FXML
    private TextField PasswordField;

    @FXML
    private void onLoginBtnClicked() {
        try {
            System.out.println("Login Button Clicked");

            //This is so that login cannot be spam clicked
            if (debounce) return;
            debounce = true;

            Task task = new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        LoginMessage.setText("Logging in...");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            task.setOnSucceeded(taskFinishEvent -> {
                User maybeFoundUser = UserManager.login(EmailField.getText(), PasswordField.getText());
                if (EmailField.getText().equals("") || PasswordField.getText().equals("")) {
                    debounce = false;

                    MessageBox.warningMessageBox("Please fill up all information");
                    System.out.println("Please fill up all information");

                    LoginMessage.setText("Please Login");

                } else {

                    if (maybeFoundUser == null) {
                        debounce = false;

                        MessageBox.warningMessageBox("Please input email and password correctly");
                        System.out.println("Please input email and password correctly");

                        LoginMessage.setText("Please Login");
                        EmailField.setText("");
                        PasswordField.setText("");

                        return;
                    }

                    MessageBox.informationMessageBox("Successfully login", MessageBox.TypeMessageBox.NON_RESULT);
                    System.out.println(maybeFoundUser.getFirstName() + " successfully login");

                    FXMLLoader loader = Application.sceneManager.getLoader("ScreenMain.fxml");
                    Scene scene;
                    try {
                        scene = new Scene(loader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    MainScreenController mainScreenController = loader.getController();
                    mainScreenController.passUserToMain(maybeFoundUser);

                    Application.sceneManager.changeScene(scene);
                }
            });

            new Thread(task).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onForgetPassLinkClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("HPL forget password clicked");

        if (debounce) return;
        debounce = true;

        FXMLLoader loader = Application.sceneManager.getLoader("ScreenResetPass.fxml");

        Scene scene = new Scene(loader.load());

        Application.sceneManager.changeScene(scene);

        debounce = false;
    }

    @FXML
    private void onSignupLinkClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("HPL signup clicked");

        if (debounce) return;
        debounce = true;

        FXMLLoader loader = Application.sceneManager.getLoader("ScreenSignUp.fxml");

        Scene scene = new Scene(loader.load());

        Application.sceneManager.changeScene(scene);

        debounce = false;
    }
}