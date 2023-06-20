package com.example.librarysystem.controllers;

import com.example.librarysystem.Application;
import com.example.librarysystem.classes.Book;
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
import java.util.HashMap;
import java.util.Locale;

public class ConfirmBorrowController {

    @FXML
    private TableView<Book> BookTable;
    @FXML
    private TextField SearchField;
    @FXML
    private Text NameDisplayLabel;
    @FXML
    private TableColumn<Book, ImageView> Thumbnail;
    @FXML
    private TableColumn<Book, String> Title;
    @FXML
    private TableColumn<Book, String> Description;

    private User currentUser;
    private ObservableList<Book> books;

    @FXML
    public void initialize() {
        BookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Thumbnail.setCellValueFactory(new PropertyValueFactory<Book, ImageView>("Thumbnail"));
        Title.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        Description.setCellValueFactory(new PropertyValueFactory<Book, String>("Description"));

        BookTable.setItems(books);
    }

    @FXML
    private void onBackMainScreenBtnClicked() throws IOException {
        if (MessageBox.informationMessageBox("Back To Main Screen Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Back To Main Screen Button Clicked");

        FXMLLoader loader = Application.sceneManager.getLoader("ScreenMain.fxml");

        Scene scene = new Scene(loader.load());

        MainScreenController mainScreenController = loader.getController();
        mainScreenController.passUserToMain(currentUser);

        Application.sceneManager.changeScene(scene);
    }

    private void removeBooksFromList(ObservableList<Book> selectedBooks) {
        HashMap<String, String> selectedBookISBNs = new HashMap<>();

        for (Book book : selectedBooks) {
            selectedBookISBNs.put(book.getISBN(), "Yes");
        }

        ObservableList<Book> newBooks = FXCollections.observableArrayList();
        for (Book book : books) {
            if (selectedBookISBNs.get(book.getISBN()) == null) {
                newBooks.add(book);
            }
        }

        books = newBooks;
        BookTable.setItems(books);
    }

    @FXML
    private void onConfirmBorrowBtnClicked() {
        if (MessageBox.informationMessageBox("Confirm Borrow Button Clicked", MessageBox.TypeMessageBox.GET_RESULT)) return;
        System.out.println("Confirm Borrow Button Clicked");

        ObservableList<Book> selectedBooks = BookTable.getSelectionModel().getSelectedItems();

        BookManager.borrowBooks(currentUser, selectedBooks);

        removeBooksFromList(selectedBooks);
    }

    @FXML
    private void onRemoveFromListBtnClicked() {
        if (books.size() == 0) return;
        removeBooksFromList(BookTable.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void onSearchBtnClicked() {
        initSearch();
    }

    @FXML
    private void initSearch() {
        String searchTxt = SearchField.getText();

        if (searchTxt.equals("")) {
            BookTable.setItems(books);
        } else {
            ObservableList<Book> filteredData = FXCollections.observableArrayList();

            for (Book book : books) {
                if (book.getTitle().toLowerCase(Locale.ROOT).contains(searchTxt.toLowerCase(Locale.ROOT))) {
                    filteredData.add(book);
                }
            }

            BookTable.setItems(filteredData);
        }
    }

    public void receiveData(User user, ObservableList<Book> books) {
        this.currentUser = user;
        this.books = books;

        NameDisplayLabel.setText(user.getFirstName());
        BookTable.setItems(books);

        for (Book book : books) {
            System.out.printf("Received %s with ID %s in receiveData (ConfirmBorrow)%n", book.getTitle(), book.getBookID());
        }
    }
}