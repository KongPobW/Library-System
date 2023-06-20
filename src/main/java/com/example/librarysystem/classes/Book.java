package com.example.librarysystem.classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Date;

public class Book {

    private int BookID;
    private SimpleStringProperty ISBN;
    private SimpleObjectProperty<ImageView> Thumbnail;
    private SimpleStringProperty Title;
    private SimpleStringProperty Description;
    private SimpleStringProperty Category;
    private SimpleStringProperty Language;
    private int Length;
    private SimpleObjectProperty<Date> PublicationDate;
    private int AuthorID;
    private SimpleIntegerProperty AID; //author id
    private SimpleStringProperty AuthorName;
    private int PublisherID;
    private SimpleIntegerProperty PID; //publisher id
    private SimpleStringProperty PublisherName;
    private int BorrowID;
    private int BorrowerUserID;
    private SimpleObjectProperty<Date> BorrowDate;
    private SimpleObjectProperty<Date> ExpireDate;
    private SimpleIntegerProperty DateRemaining;
    private SimpleObjectProperty<BookStatus> Status;

    public Book(int bookID, String ISBN, String thumbnail, String title, String description, String category, String language, int length, Date publicationDate, int authorID, String authorName, int publisherID, String publisherName, int borrowerUserID, BookStatus status) {
        BookID = bookID;
        this.ISBN = new SimpleStringProperty(ISBN);
        Thumbnail = new SimpleObjectProperty<>(new ImageView(new Image(thumbnail)));
        Title = new SimpleStringProperty(title);
        Description = new SimpleStringProperty(description);
        Category = new SimpleStringProperty(category);
        Language = new SimpleStringProperty(language);
        Length = length;
        PublicationDate = new SimpleObjectProperty<>(publicationDate);
        AuthorID = authorID;
        AID = new SimpleIntegerProperty(authorID);
        AuthorName = new SimpleStringProperty(authorName);
        PublisherID = publisherID;
        PID = new SimpleIntegerProperty(publisherID);
        PublisherName = new SimpleStringProperty(publisherName);
        BorrowerUserID = borrowerUserID;
        Status = new SimpleObjectProperty<>(status);
    }

    public Book(int bookID, String thumbnail, String title, int borrowID, Date borrowDate, Date expireDate, int dateRemaining, BookStatus status) {
        BookID = bookID;
        Thumbnail = new SimpleObjectProperty<>(new ImageView(new Image(thumbnail)));
        Title = new SimpleStringProperty(title);
        BorrowID = borrowID;
        BorrowDate = new SimpleObjectProperty<>(borrowDate);
        ExpireDate = new SimpleObjectProperty<>(expireDate);
        DateRemaining = new SimpleIntegerProperty(dateRemaining);
        Status = new SimpleObjectProperty<>(status);
    }

    public int getBookID() {
        return BookID;
    }

    public void setBookID(int bookID) {
        BookID = bookID;
    }

    public String getISBN() {
        return ISBN.get();
    }

    public SimpleStringProperty ISBNProperty() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN.set(ISBN);
    }

    public ImageView getThumbnail() {
        return Thumbnail.get();
    }

    public SimpleObjectProperty<ImageView> thumbnailProperty() {
        return Thumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        this.Thumbnail.set(thumbnail);
    }

    public String getTitle() {
        return Title.get();
    }

    public SimpleStringProperty titleProperty() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title.set(title);
    }

    public String getDescription() {
        return Description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description.set(description);
    }

    public String getCategory() {
        return Category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category.set(category);
    }

    public String getLanguage() {
        return Language.get();
    }

    public SimpleStringProperty languageProperty() {
        return Language;
    }

    public void setLanguage(String language) {
        this.Language.set(language);
    }

    public String getLength() {
        return String.valueOf(Length);
    }

    public void setLength(int length) {
        Length = length;
    }

    public Date getPublicationDate() {
        return PublicationDate.get();
    }

    public SimpleObjectProperty<Date> publicationDateProperty() {
        return PublicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.PublicationDate.set(publicationDate);
    }

    public int getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(int authorID) {
        AuthorID = authorID;
    }

    public int getAID() {
        return AID.get();
    }

    public SimpleIntegerProperty AIDProperty() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID.set(AID);
    }

    public String getAuthorName() {
        return AuthorName.get();
    }

    public SimpleStringProperty authorNameProperty() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        this.AuthorName.set(authorName);
    }

    public int getPublisherID() {
        return PublisherID;
    }

    public void setPublisherID(int publisherID) {
        PublisherID = publisherID;
    }

    public int getPID() {
        return PID.get();
    }

    public SimpleIntegerProperty PIDProperty() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID.set(PID);
    }

    public String getPublisherName() {
        return PublisherName.get();
    }

    public SimpleStringProperty publisherNameProperty() {
        return PublisherName;
    }

    public void setPublisherName(String publisherName) {
        this.PublisherName.set(publisherName);
    }

    public int getBorrowID() {
        return BorrowID;
    }

    public void setBorrowID(int borrowID) {
        BorrowID = borrowID;
    }

    public int getBorrowerUserID() {
        return BorrowerUserID;
    }

    public void setBorrowerUserID(int borrowerUserID) {
        BorrowerUserID = borrowerUserID;
    }

    public Date getBorrowDate() {
        return BorrowDate.get();
    }

    public SimpleObjectProperty<Date> borrowDateProperty() {
        return BorrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.BorrowDate.set(borrowDate);
    }

    public Date getExpireDate() {
        return ExpireDate.get();
    }

    public SimpleObjectProperty<Date> expireDateProperty() {
        return ExpireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.ExpireDate.set(expireDate);
    }

    public int getDateRemaining() {
        return DateRemaining.get();
    }

    public SimpleIntegerProperty dateRemainingProperty() {
        return DateRemaining;
    }

    public void setDateRemaining(int dateRemaining) {
        this.DateRemaining.set(dateRemaining);
    }

    public BookStatus getStatus() {
        return Status.get();
    }

    public SimpleObjectProperty<BookStatus> statusProperty() {
        return Status;
    }

    public void setStatus(BookStatus status) {
        this.Status.set(status);
    }
}