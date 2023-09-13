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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Date;
import java.util.Locale;

public class MyBookScreenController {

    @FXML
    private TableView<Book> BookTable;
    @FXML
    private Text NameDisplayLabel;
    @FXML
    private TextField SearchField;
    @FXML
    private TableColumn<Book, ImageView> Thumbnail;
    @FXML
    private TableColumn<Book, String> Title;
    @FXML
    private TableColumn<Book, Date> BorrowDate;
    @FXML
    private TableColumn<Book, Date> ExpireDate;
    @FXML
    private TableColumn<Book, String> DateRemaining;
    @FXML
    private TableColumn<Book, BookStatus> Status;

    private static User currentUser;
    private ObservableList<Book> data;

    @FXML
    public void initialize() {
        BookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Thumbnail.setCellValueFactory(new PropertyValueFactory<Book, ImageView>("Thumbnail"));
        DateRemaining.setCellValueFactory(new PropertyValueFactory<Book, String>("DateRemaining"));
        Title.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        BorrowDate.setCellValueFactory(new PropertyValueFactory<Book, Date>("BorrowDate"));
        ExpireDate.setCellValueFactory(new PropertyValueFactory<Book, Date>("ExpireDate"));
        Status.setCellValueFactory(new PropertyValueFactory<Book, BookStatus>("Status"));
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
    private void onBackBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("Back To Main Screen Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Back To Main Screen Button Clicked");

        FXMLLoader loader = Application.sceneManager.getLoader("screen_main.fxml");

        Scene scene = new Scene(loader.load());

        MainScreenController mainScreenController = loader.getController();

        mainScreenController.passUserToMain(currentUser);

        Application.sceneManager.changeScene(scene);
    }

    @FXML
    private void onReturnBtnClicked() {
        if (MessageBox.informationMessageBox("Return Book Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Return Book Button Clicked");
        ObservableList<Book> selectedBooks = BookTable.getSelectionModel().getSelectedItems();

        for (Book book : selectedBooks) {
            if (book.getStatus() == BookStatus.RETURNED) {
                MessageBox.warningMessageBox("Please check the books selected are borrowing");
                System.out.println("Please check the books selected are borrowing");
                return;
            }
        }

        BookManager.returnBooks(selectedBooks);

        data = FXCollections.observableArrayList(BookManager.getBorrowingBooks(currentUser.getUserID()));
        BookTable.setItems(data);
    }

    @FXML
    private void onCurrentBorrowBtnClicked() {
        if (MessageBox.informationMessageBox("Current Borrow Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Current Borrow Button Clicked");
        data = FXCollections.observableArrayList(BookManager.getBorrowingBooks(currentUser.getUserID()));
        BookTable.setItems(data);
    }

    @FXML
    private void onBorrowHistoryBtnClicked() {
        if (MessageBox.informationMessageBox("Borrow History Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Borrow History Button Clicked");
        data = FXCollections.observableArrayList(BookManager.getBorrowedBooks(currentUser.getUserID()));
        BookTable.setItems(data);
    }

    public void passUserToMyBook(User user) {
        currentUser = user;
        NameDisplayLabel.setText(user.getFirstName());
        data = FXCollections.observableArrayList(BookManager.getBorrowingBooks(currentUser.getUserID()));
        BookTable.setItems(data);
    }
}