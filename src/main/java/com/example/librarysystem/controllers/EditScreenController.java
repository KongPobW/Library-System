package com.example.librarysystem.controllers;

import com.example.librarysystem.Application;
import com.example.librarysystem.classes.Book;
import com.example.librarysystem.classes.MessageBox;
import com.example.librarysystem.classes.User;
import com.example.librarysystem.utils.BookManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditScreenController {

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
    private TextArea DescriptionField;
    @FXML
    private javafx.scene.control.DatePicker DatePicker;

    private static User currentUser;
    private static int bookID;
    private static Book bookSelected;
    public static String messageError = "No Error";

    @FXML
    private String getDate() {
        LocalDate myDate = DatePicker.getValue();
        if (myDate == null) return "";
        return myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @FXML
    private void onConfirmEditBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("Confirm Edit Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Confirm Edit Button Clicked");

        if (TitleField.getText().equals("") || IsbnField.getText().equals("") || AuthorField.getText().equals("")
                || PublisherField.getText().equals("") || CategoryField.getText().equals("")
                || LanguageField.getText().equals("") || getDate().equals("") || LengthField.getText().equals("")
                || DescriptionField.getText().equals("")) {
            MessageBox.warningMessageBox("Please fill up all information");
            System.out.println("Please fill up all information");
        } else {

            BookManager.editBook(bookID, IsbnField.getText(), TitleField.getText(), DescriptionField.getText(), CategoryField.getText(), LanguageField.getText(), getDate(), LengthField.getText(), AuthorField.getText(), PublisherField.getText());

            if (messageError.equals("No Error")) {

                FXMLLoader loader1 = Application.sceneManager.getLoader("screen_main.fxml");
                Scene scene1 = new Scene(loader1.load());
                MainScreenController mainScreenController = loader1.getController();
                mainScreenController.passUserToMain(currentUser);
                Application.sceneManager.changeScene(scene1);

            } else if (messageError.equals("Publisher and Author not found")) {

                FXMLLoader loader2 = Application.sceneManager.getLoader("book_edit.fxml");
                Scene scene2 = new Scene(loader2.load());
                EditScreenController editScreenController = loader2.getController();
                editScreenController.passDataToEdit(currentUser, bookSelected);
                Application.sceneManager.changeScene(scene2);
            }
        }
    }

    @FXML
    private void onBackBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("Back To Main Screen Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Back To Main Screen Button Clicked");

        FXMLLoader loader = Application.sceneManager.getLoader("screen_main.fxml");

        Scene scene = new Scene(loader.load());

        MainScreenController mainScreenController = loader.getController();
        mainScreenController.passUserToMain(currentUser);

        Application.sceneManager.changeScene(scene);
    }

    public void passDataToEdit(User user, Book selectedBook) {
        currentUser = user;
        NameDisplayLabel.setText(user.getFirstName());

        if (selectedBook != null) {
            TitleField.setText(selectedBook.getTitle());
            IsbnField.setText(selectedBook.getISBN());
            AuthorField.setText(selectedBook.getAuthorName());
            PublisherField.setText(selectedBook.getPublisherName());
            CategoryField.setText(selectedBook.getCategory());
            LanguageField.setText(selectedBook.getLanguage());
            DatePicker.setValue(selectedBook.getPublicationDate().toLocalDate());
            LengthField.setText(selectedBook.getLength());
            DescriptionField.setText(selectedBook.getDescription());

            bookID = selectedBook.getBookID();
            bookSelected = selectedBook;
        }
    }
}