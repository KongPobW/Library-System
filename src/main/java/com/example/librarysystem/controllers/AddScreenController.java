package com.example.librarysystem.controllers;

import com.example.librarysystem.Application;
import com.example.librarysystem.classes.MessageBox;
import com.example.librarysystem.classes.User;
import com.example.librarysystem.utils.BookManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddScreenController {

    @FXML
    private Text NameDisplayLabel;
    @FXML
    private TextField TitleField;
    @FXML
    private TextField IsbnField;
    @FXML
    private TextField AuthorField;
    @FXML
    private TextField PublisherField;
    @FXML
    private TextField CategoryField;
    @FXML
    private TextField LanguageField;
    @FXML
    private TextField LengthField;
    @FXML
    private javafx.scene.control.DatePicker DatePicker;
    @FXML
    private TextArea DescriptionField;

    private static User currentUser;
    public static String messageError = "No Error";

    @FXML
    private String getDate() {
        LocalDate myDate = DatePicker.getValue();
        if (myDate == null) return "";
        return myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @FXML
    private void onConfirmAddBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("Confirm Add Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Confirm Add Button Clicked");

        if (TitleField.getText().equals("") || IsbnField.getText().equals("") || AuthorField.getText().equals("")
                || PublisherField.getText().equals("") || CategoryField.getText().equals("")
                || LanguageField.getText().equals("") || getDate().equals("") || LengthField.getText().equals("")
                || DescriptionField.getText().equals("")) {
            MessageBox.warningMessageBox("Please fill up all information");
            System.out.println("Please fill up all information");
        } else {

            BookManager.addBook(IsbnField.getText(), TitleField.getText(), DescriptionField.getText(), CategoryField.getText(), LanguageField.getText(), getDate(), LengthField.getText(), AuthorField.getText(), PublisherField.getText());

            if (messageError.equals("No Error")) {

                FXMLLoader loader1 = Application.sceneManager.getLoader("ScreenMain.fxml");

                Scene scene = new Scene(loader1.load());

                MainScreenController mainScreenController = loader1.getController();
                mainScreenController.passUserToMain(currentUser);

                Application.sceneManager.changeScene(scene);

            } else if (messageError.equals("Please type existing author and publisher in database")) {

                FXMLLoader loader2 = Application.sceneManager.getLoader("BookAdd.fxml");

                Scene scene = new Scene(loader2.load());

                AddScreenController addScreenController = loader2.getController();
                addScreenController.passDataToAdd(currentUser, TitleField.getText(), IsbnField.getText(), AuthorField.getText(), PublisherField.getText(), CategoryField.getText(), LanguageField.getText(), LengthField.getText(), DatePicker, DescriptionField.getText(), "aupu");

                Application.sceneManager.changeScene(scene);

            } else if (messageError.equals("ISBN is duplicated with another ISBN in database")) {

                FXMLLoader loader3 = Application.sceneManager.getLoader("BookAdd.fxml");

                Scene scene = new Scene(loader3.load());

                AddScreenController addScreenController = loader3.getController();
                addScreenController.passDataToAdd(currentUser, TitleField.getText(), IsbnField.getText(), AuthorField.getText(), PublisherField.getText(), CategoryField.getText(), LanguageField.getText(), LengthField.getText(), DatePicker, DescriptionField.getText(), "isbn");

                Application.sceneManager.changeScene(scene);
            }
        }
    }

    @FXML
    private void onBackBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("Back To Main Screen Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Back To Main Screen Button Clicked");

        FXMLLoader loader = Application.sceneManager.getLoader("ScreenMain.fxml");

        Scene scene = new Scene(loader.load());

        MainScreenController mainScreenController = loader.getController();
        mainScreenController.passUserToMain(currentUser);

        Application.sceneManager.changeScene(scene);
    }

    public void passUserToAdd(User user) {
        currentUser = user;
        NameDisplayLabel.setText(user.getFirstName());
    }

    private void passDataToAdd(User user, String title, String isbn, String authorName, String publisherName, String category, String language, String length, DatePicker datePicker, String description, String error) {
        passUserToAdd(user);

        if (error.equals("aupu")) {
            TitleField.setText(title);
            IsbnField.setText(isbn);
            CategoryField.setText(category);
            LanguageField.setText(language);
            DatePicker.setValue(datePicker.getValue());
            DescriptionField.setText(description);
            LengthField.setText(length);
        } else { //isbn
            TitleField.setText(title);
            AuthorField.setText(authorName);
            PublisherField.setText(publisherName);
            CategoryField.setText(category);
            LanguageField.setText(language);
            DatePicker.setValue(datePicker.getValue());
            DescriptionField.setText(description);
            LengthField.setText(length);
        }
    }
}