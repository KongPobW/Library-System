package com.example.librarysystem.controllers;

import com.example.librarysystem.Application;
import com.example.librarysystem.classes.Book;
import com.example.librarysystem.classes.BookStatus;
import com.example.librarysystem.classes.MessageBox;
import com.example.librarysystem.classes.User;
import com.example.librarysystem.utils.BookManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class MainScreenController {

    @FXML
    private TableView<Book> BookTable;
    @FXML
    private Text NameDisplayLabel;
    @FXML
    private TextField SearchField;
    @FXML
    private TableColumn<Book, String> Status;
    @FXML
    private TableColumn<Book, ImageView> Thumbnail;
    @FXML
    private TableColumn<Book, String> Description;
    @FXML
    private TableColumn<Book, String> Title;

    private static User currentUser;
    private ObservableList<Book> data;

    @FXML
    public void initialize() {
        BookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Thumbnail.setCellValueFactory(new PropertyValueFactory<Book, ImageView>("Thumbnail"));
        Title.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        Description.setCellValueFactory(new PropertyValueFactory<Book, String>("Description"));
        Status.setCellValueFactory(new PropertyValueFactory<Book, String>("Status"));
    }

    @FXML
    private void onSearchBtnClicked() {
        initSearch();
    }

    @FXML
    private void initSearch() {
        String searchTxt = SearchField.getText();

        if (searchTxt.equals("")) {
            BookTable.setItems(data);
        } else {
            ObservableList<Book> filteredData = FXCollections.observableArrayList();

            for (Book book : data) {
                if (book.getTitle().toLowerCase(Locale.ROOT).contains(searchTxt.toLowerCase(Locale.ROOT))) {
                    filteredData.add(book);
                }
            }

            BookTable.setItems(filteredData);
        }
    }

    @FXML
    private void onLogoutBtnClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you confirm to log out?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation to Log out");
        addDialogIconTo(alert);
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

        if (result == ButtonType.YES) {
            System.out.println(currentUser.getFirstName() + " confirms to log out");
            FXMLLoader loader = Application.sceneManager.getLoader("screen_login.fxml");
            Scene scene;

            try {
                scene = new Scene(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Application.sceneManager.changeScene(scene);

        } else {
            System.out.println("Cancel to Log out");
        }
    }

    @FXML
    private void onAddBtnClicked() throws IOException {
        FXMLLoader loader = Application.sceneManager.getLoader("book_add.fxml");

        Scene scene = new Scene(loader.load());

        AddScreenController addScreenController = loader.getController();
        addScreenController.passUserToAdd(currentUser);

        Application.sceneManager.changeScene(scene);
    }

    @FXML
    private void onEditBtnClicked() {
        try {
            Book selectedBook = BookTable.getSelectionModel().getSelectedItems().get(0);

            FXMLLoader loader = Application.sceneManager.getLoader("book_edit.fxml");

            Scene scene = new Scene(loader.load());

            EditScreenController editScreenController = loader.getController();
            editScreenController.passDataToEdit(currentUser, selectedBook);

            Application.sceneManager.changeScene(scene);
        } catch (Exception e) {
            MessageBox.warningMessageBox("Please choose the book that you want to edit");
            System.out.println("Please choose the book that you want to edit");
        }
    }

    @FXML
    private void onRemoveBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("Remove Book Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Remove Book Button Clicked");

        ObservableList<Book> selectedBooks = BookTable.getSelectionModel().getSelectedItems();

        FXMLLoader loader = Application.sceneManager.getLoader("confirm_remove.fxml");

        Scene scene = new Scene(loader.load());

        ConfirmRemoveController confirmRemoveController = loader.getController();
        confirmRemoveController.receiveData(currentUser, selectedBooks);

        Application.sceneManager.changeScene(scene);
    }

    @FXML
    private void onMyBookBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("My Book Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("My Book Button Clicked");

        FXMLLoader loader = Application.sceneManager.getLoader("mybook.fxml");

        Scene scene = new Scene(loader.load());

        MyBookScreenController myBookScreenController = loader.getController();
        myBookScreenController.passUserToMyBook(currentUser);

        Application.sceneManager.changeScene(scene);
    }

    @FXML
    private void onBorrowBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("Borrow Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Borrow Button Clicked");

        ObservableList<Book> selectedBooks = BookTable.getSelectionModel().getSelectedItems();

        for (Book book : selectedBooks) {
            if (book.getStatus() == BookStatus.UNAVAILABLE) {
                MessageBox.warningMessageBox("Please check the books selected are available");
                System.out.println("Please check the books selected are available");
                return;
            }
        }

        FXMLLoader loader = Application.sceneManager.getLoader("confirm_borrow.fxml");

        Scene scene = new Scene(loader.load());

        ConfirmBorrowController confirmBorrowController = loader.getController();
        confirmBorrowController.receiveData(currentUser, selectedBooks);

        Application.sceneManager.changeScene(scene);
    }

    private static void addDialogIconTo(Alert alert) {
        final Image APPLICATION_ICON = new Image("logout.jpg");
        Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(APPLICATION_ICON);
    }

    public void passUserToMain(User user) {
        currentUser = user;
        NameDisplayLabel.setText(user.getFirstName());
        data = FXCollections.observableArrayList(BookManager.getAllBooks());
        BookTable.setItems(data);
    }
}