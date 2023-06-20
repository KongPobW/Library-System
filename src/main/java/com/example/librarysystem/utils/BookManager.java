package com.example.librarysystem.utils;

import com.example.librarysystem.classes.Book;
import com.example.librarysystem.classes.BookStatus;
import com.example.librarysystem.classes.MessageBox;
import com.example.librarysystem.classes.User;
import com.example.librarysystem.controllers.AddScreenController;
import com.example.librarysystem.controllers.EditScreenController;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class BookManager {

    private static final String INSERT_BORROW_BOOK_QUERY1 = "INSERT INTO library.borrows (BorrowedBookID, BorrowerUserID) VALUES (?, ?);";
    private static final String INSERT_BORROW_BOOK_QUERY2 = "INSERT INTO library.borrow_details (BorrowID, BorrowEndDate, BorrowedDate) VALUES (?, ?, ?);";
    private static final String DELETE_BOOK_QUERY1 = "DELETE FROM library.book_details WHERE ISBN = ?;";
    private static final String DELETE_BOOK_QUERY2 = "DELETE FROM library.book WHERE BookID = ?;";
    private static final String UPDATE_BOOK_QUERY1 = "UPDATE library.book SET AuthorID = ?, PublishID = ? WHERE BookID = ?;";
    private static final String UPDATE_BOOK_QUERY2 = "UPDATE library.book_details SET Title = ?, Description = ?, Category = ?, Language = ?, PublishDate = ?, Length = ? WHERE ISBN = ?;";
    private static final String INSERT_BOOK_QUERY1 = "INSERT INTO library.book (AuthorID, PublishID, ISBN) VALUES (?, ?, ?);";
    private static final String INSERT_BOOK_QUERY2 = "INSERT INTO library.book_details (ISBN, Title, Description, Category, Language, PublishDate, Length) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_END_DATE_QUERY = "UPDATE library.borrow_details SET BorrowEndDate = ? WHERE BorrowID = ?;";
    private static final String GET_ALL_BOOKS_QUERY = "SELECT *, IF(EXISTS(SELECT borrows.*, borrow_details.* FROM borrows LEFT JOIN borrow_details ON borrow_details.BorrowID = borrows.BorrowID WHERE borrows.BorrowedBookID = book.BookID AND borrow_details.BorrowEndDate > curdate()), false, true) AS Available FROM book LEFT JOIN book_details ON book.ISBN = book_details.ISBN INNER JOIN author ON book.AuthorID = author.AuthorID INNER JOIN publisher ON publisher.publishID = book.PublishID;";
    private static final String GET_ALL_BOOKS_BORROWED_QUERY = "SELECT *, DATEDIFF(borrow_details.BorrowEndDate, curdate()) AS RemainingDays, DATEDIFF(borrow_details.BorrowEndDate, curdate()) <= 0 AS Returned FROM borrows INNER JOIN borrow_details INNER JOIN book INNER JOIN book_details ON borrows.BorrowID = borrow_details.BorrowID AND borrows.BorrowedBookID = book.BookID AND book_details.ISBN = book.ISBN WHERE borrows.BorrowerUserID = ?;";
    private static final String GET_ALL_BOOKS_BORROWING_QUERY = "SELECT *, DATEDIFF(borrow_details.BorrowEndDate, curdate()) AS RemainingDays, DATEDIFF(borrow_details.BorrowEndDate, curdate()) <= 0 AS Returned FROM borrows INNER JOIN borrow_details INNER JOIN book INNER JOIN book_details ON borrows.BorrowID = borrow_details.BorrowID AND book.BookID = borrows.BorrowedBookID AND book_details.ISBN = book.ISBN WHERE borrows.BorrowerUserID = ? AND borrow_details.BorrowEndDate > curdate();";
    private static final String GET_AUTHOR_ID_QUERY = "SELECT AuthorID FROM library.author WHERE aName = ?;";
    private static final String GET_PUBLISHER_ID_QUERY = "SELECT PublishID FROM library.publisher WHERE pName = ?;";
    private static final String GET_ISBN_QUERY = "SELECT ISBN FROM library.book WHERE ISBN = ?;";
    private static final String DELETE_BORROW_BOOK_QUERY1 = "DELETE FROM library.borrows WHERE BorrowedBookID = ?;";
    private static final String DELETE_BORROW_BOOK_QUERY2 = "DELETE FROM library.borrow_details WHERE BorrowID = ?;";
    private static final String GET_BORROW_ID_QUERY = "SELECT BorrowID FROM library.borrows WHERE BorrowedBookID = ?;";

    public static ArrayList<Book> getAllBooks() {
        Connection con = DatabaseManager.getConnection();
        try {
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(GET_ALL_BOOKS_QUERY);

            ResultSet rs = stmt.executeQuery();

            ArrayList<Book> booksFound = new ArrayList<Book>();

            while (rs.next()) {
                int bookID = rs.getInt("BookID");
                String authorName = rs.getString("aName");
                String publisherName = rs.getString("pName");
                String bookTitle = rs.getString("Title");
                String bookISBN = rs.getString("ISBN");
                int bookAuthorID = rs.getInt("AuthorID");
                int bookPublisherID = rs.getInt("PublishID");
                String bookDescription = rs.getString("Description");
                String bookCategory = rs.getString("Category");
                String bookLanguage = rs.getString("Language");
                Date bookPublicationDate = rs.getDate("PublishDate");
                int bookLength = rs.getInt("Length");

                boolean bookIsAvailable = rs.getBoolean("Available");

                String bookThumbnail = "https://images.unsplash.com/photo-1509021436665-8f07dbf5bf1d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=200&q=80";

                BookStatus bookStatus = bookIsAvailable ? BookStatus.AVAILABLE : BookStatus.UNAVAILABLE;

                Book foundBook = new Book(bookID, bookISBN, bookThumbnail, bookTitle, bookDescription, bookCategory, bookLanguage, bookLength, bookPublicationDate, bookAuthorID, authorName, bookPublisherID, publisherName, 0, bookStatus);
                System.out.printf("Got book %s with ID %s from database%n", foundBook.getTitle(), foundBook.getBookID());

                booksFound.add(foundBook);
            }

            con.close();

            return booksFound;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Book> getBorrowedBooks(int userID) {
        Connection con = DatabaseManager.getConnection();
        try {
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(GET_ALL_BOOKS_BORROWED_QUERY);
            stmt.setInt(1, userID);

            ResultSet rs = stmt.executeQuery();

            ArrayList<Book> booksUserBorrowed = new ArrayList<Book>();

            while (rs.next()) {
                int borrowID = rs.getInt("BorrowID");
                int bookID = rs.getInt("BorrowedBookID");
                String bookThumbnail = "https://images.unsplash.com/photo-1509021436665-8f07dbf5bf1d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=200&q=80";
                String bookTitle = rs.getString("Title");
                Date borrowDate = rs.getDate("BorrowedDate");
                Date borrowEndDate = rs.getDate("BorrowEndDate");
                int remainingDays = rs.getInt("RemainingDays");

                boolean bookIsReturned = rs.getBoolean("Returned");
                BookStatus bookStatus = bookIsReturned ? BookStatus.RETURNED : BookStatus.BORROWING;

                Book booksBorrowed = new Book(bookID, bookThumbnail, bookTitle, borrowID, borrowDate, borrowEndDate, remainingDays, bookStatus);
                System.out.printf("Got book %s that borrowed and is borrowing of user from database%n", booksBorrowed.getTitle());

                booksUserBorrowed.add(booksBorrowed);
            }

            con.close();

            return booksUserBorrowed;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Book> getBorrowingBooks(int userID) {
        Connection con = DatabaseManager.getConnection();
        try {
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(GET_ALL_BOOKS_BORROWING_QUERY);
            stmt.setInt(1, userID);

            ResultSet rs = stmt.executeQuery();

            ArrayList<Book> booksUserBorrowing = new ArrayList<Book>();

            while (rs.next()) {
                int borrowID = rs.getInt("BorrowID");
                int bookID = rs.getInt("BorrowedBookID");
                String bookThumbnail = "https://images.unsplash.com/photo-1509021436665-8f07dbf5bf1d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=200&q=80";
                String bookTitle = rs.getString("Title");
                Date borrowDate = rs.getDate("BorrowedDate");
                Date borrowEndDate = rs.getDate("BorrowEndDate");
                int remainingDays = rs.getInt("RemainingDays");

                boolean bookIsReturned = rs.getBoolean("Returned");
                BookStatus bookStatus = bookIsReturned ? BookStatus.RETURNED : BookStatus.BORROWING;

                Book booksBorrowing = new Book(bookID, bookThumbnail, bookTitle, borrowID, borrowDate, borrowEndDate, remainingDays, bookStatus);
                System.out.printf("Got book %s that is borrowing of user from database%n", booksBorrowing.getTitle());

                booksUserBorrowing.add(booksBorrowing);
            }

            con.close();

            return booksUserBorrowing;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean borrowBooks(User user, ObservableList<Book> books) {
        Connection con = DatabaseManager.getConnection();
        assert con != null;

        try {

            long millisInWeek = 86400000 * 7;

            for (Book book : books) {

                System.out.printf("Attempting to borrow \"%s\", with ID %s%n", book.getTitle(), book.getBookID());

                PreparedStatement stmt = con.prepareStatement(INSERT_BORROW_BOOK_QUERY1, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, book.getBookID());
                stmt.setInt(2, user.getUserID());

                int rs1 = stmt.executeUpdate();

                if (rs1 >= 1) {

                    try (ResultSet generate_id = stmt.getGeneratedKeys()) {

                        if (generate_id.next()) {
                            PreparedStatement stmtt = con.prepareStatement(INSERT_BORROW_BOOK_QUERY2);
                            stmtt.setInt(1, generate_id.getInt(1));
                            stmtt.setDate(2, new Date(System.currentTimeMillis() + millisInWeek));
                            stmtt.setDate(3, new Date(System.currentTimeMillis()));

                            stmtt.executeUpdate();
                        }

                    }

                    System.out.printf("Successfully borrowed \"%s\", with ID %s%n", book.getTitle(), book.getBookID());
                    book.setStatus(BookStatus.UNAVAILABLE);

                } else {
                    MessageBox.warningMessageBox("Failed borrowed " + book.getTitle() + ", with ID " + book.getBookID());
                    System.out.println("Failed borrowed " + book.getTitle() + ", with ID " + book.getBookID());
                }
            }

            MessageBox.informationMessageBox("Successfully borrowed books", MessageBox.TypeMessageBox.NON_RESULT);
            System.out.println("Successfully borrowed books");

            con.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void returnBooks(ObservableList<Book> selectedBooks) {

        try {

            for (Book book : selectedBooks) {

                System.out.printf("Attempting to return \"%s\", with ID %s%n", book.getTitle(), book.getBookID());

                Connection con = DatabaseManager.getConnection();
                assert con != null;
                PreparedStatement stmt = con.prepareStatement(UPDATE_END_DATE_QUERY);
                Date todayDate = new Date(System.currentTimeMillis());
                stmt.setDate(1, todayDate);
                stmt.setInt(2, book.getBorrowID());

                int rs = stmt.executeUpdate();

                if (rs < 1) {
                    MessageBox.warningMessageBox("Failed returned " + book.getTitle() + ", with ID " + book.getBookID());
                    System.out.println("Failed returned " + book.getTitle() + ", with ID " + book.getBookID());
                    return;
                } else {
                    System.out.printf("Successfully returned \"%s\", with ID %s%n", book.getTitle(), book.getBookID());
                    book.setStatus(BookStatus.AVAILABLE);
                }

                MessageBox.informationMessageBox("Successfully returned books", MessageBox.TypeMessageBox.NON_RESULT);
                System.out.println("Successfully returned books");

                con.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void addBook(String isbn, String title, String description, String category, String language, String publicationDate, String length, String authorName, String publisherName) {

        try {

            int lengthBook = Integer.parseInt(length);

            Connection con = DatabaseManager.getConnection();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(GET_AUTHOR_ID_QUERY);
            stmt.setString(1, authorName);

            PreparedStatement stat = con.prepareStatement(GET_PUBLISHER_ID_QUERY);
            stat.setString(1, publisherName);

            PreparedStatement check = con.prepareStatement(GET_ISBN_QUERY);
            check.setString(1, isbn);

            ResultSet rs1 = stmt.executeQuery();
            ResultSet rs2 = stat.executeQuery();
            ResultSet resultSet = check.executeQuery();

            if (resultSet.next()) {
                MessageBox.warningMessageBox("ISBN is duplicated with another ISBN in database");
                AddScreenController.messageError = "ISBN is duplicated with another ISBN in database";
                System.out.println(AddScreenController.messageError);
                return;
            }

            if (rs1.next() && rs2.next()) {
                int author_id = rs1.getInt("AuthorID");
                int publisher_id = rs2.getInt("PublishID");

                PreparedStatement stmtt = con.prepareStatement(INSERT_BOOK_QUERY1);
                stmtt.setInt(1, author_id);
                stmtt.setInt(2, publisher_id);
                stmtt.setString(3, isbn);

                int rs3 = stmtt.executeUpdate();

                PreparedStatement statt = con.prepareStatement(INSERT_BOOK_QUERY2);
                statt.setString(1, isbn);
                statt.setString(2, title);
                statt.setString(3, description);
                statt.setString(4, category);
                statt.setString(5, language);
                statt.setString(6, publicationDate);
                statt.setInt(7, lengthBook);

                int rs4 = statt.executeUpdate();

                if (rs3 < 1 || rs4 < 1) {
                    MessageBox.warningMessageBox("Failed added the book");
                    System.out.println("Failed added the book");
                } else {
                    MessageBox.informationMessageBox("Successfully added book " + title + ", with " + isbn, MessageBox.TypeMessageBox.NON_RESULT);
                    System.out.println(AddScreenController.messageError);
                    System.out.printf("Successfully added book \"%s\", with %s%n", title, isbn);
                }

            } else {
                MessageBox.warningMessageBox("Please type existing author and publisher in database");
                AddScreenController.messageError = "Please type existing author and publisher in database";
                System.out.println(AddScreenController.messageError);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void editBook(int bookID, String isbn, String title, String description, String category, String language, String publicationDate, String length, String authorName, String publisherName) {

        try {

            int lengthBook = Integer.parseInt(length);

            Connection con = DatabaseManager.getConnection();
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(GET_AUTHOR_ID_QUERY);
            stmt.setString(1, authorName);

            PreparedStatement stat = con.prepareStatement(GET_PUBLISHER_ID_QUERY);
            stat.setString(1, publisherName);

            ResultSet rs1 = stmt.executeQuery();
            ResultSet rs2 = stat.executeQuery();

            if (rs1.next() && rs2.next()) {

                int author_id = rs1.getInt("AuthorID");
                int publisher_id = rs2.getInt("PublishID");

                PreparedStatement stmtt = con.prepareStatement(UPDATE_BOOK_QUERY1);
                stmtt.setInt(1, author_id);
                stmtt.setInt(2, publisher_id);
                stmtt.setInt(3, bookID);

                int rs3 = stmtt.executeUpdate();

                PreparedStatement statt = con.prepareStatement(UPDATE_BOOK_QUERY2);
                statt.setString(1, title);
                statt.setString(2, description);
                statt.setString(3, category);
                statt.setString(4, language);
                statt.setString(5, publicationDate);
                statt.setInt(6, lengthBook);
                statt.setString(7, isbn);

                int rs4 = statt.executeUpdate();

                if (rs3 < 1 || rs4 < 1) {
                    MessageBox.warningMessageBox("Failed edited the book");
                    System.out.println("Failed edited the book");
                } else {
                    MessageBox.informationMessageBox("Successfully edited book " + title + ", with " + isbn, MessageBox.TypeMessageBox.NON_RESULT);
                    System.out.println(EditScreenController.messageError);
                    System.out.printf("Successfully edited book \"%s\", with %s%n", title, isbn);
                }

            } else {
                MessageBox.warningMessageBox("Publisher and Author not found");
                EditScreenController.messageError = "Publisher and Author not found";
                System.out.println(EditScreenController.messageError);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean removeBooks(ObservableList<Book> books) {
        Connection con = DatabaseManager.getConnection();
        assert con != null;

        try {

            for (Book book : books) {

                System.out.printf("Attempting to remove \"%s\", with ID %s%n", book.getTitle(), book.getBookID());

                PreparedStatement stmt = con.prepareStatement(DELETE_BORROW_BOOK_QUERY1);
                stmt.setInt(1, book.getBookID());

                PreparedStatement getBorrowID = con.prepareStatement(GET_BORROW_ID_QUERY);
                getBorrowID.setInt(1, book.getBookID());

                ResultSet resultSet = getBorrowID.executeQuery();

                while (resultSet.next()) {
                    int borrowID = resultSet.getInt("BorrowID");

                    PreparedStatement stmtt = con.prepareStatement(DELETE_BORROW_BOOK_QUERY2);
                    stmtt.setInt(1, borrowID);

                    stmt.executeUpdate();
                    stmtt.executeUpdate();
                }

                PreparedStatement stat = con.prepareStatement(DELETE_BOOK_QUERY1);
                stat.setString(1, book.getISBN());

                PreparedStatement statt = con.prepareStatement(DELETE_BOOK_QUERY2);
                statt.setInt(1, book.getBookID());

                int rs1 = stat.executeUpdate();
                int rs2 = statt.executeUpdate();

                if (rs1 > 0 && rs2 > 0) {
                    System.out.printf("Successfully removed \"%s\", with ID %s%n", book.getTitle(), book.getBookID());
                } else {
                    MessageBox.warningMessageBox("Failed removed " + book.getTitle() + ", with ID " + book.getBookID());
                    System.out.println("Failed removed " + book.getTitle() + ", with ID " + book.getBookID());
                }
            }

            MessageBox.informationMessageBox("Successfully removed books", MessageBox.TypeMessageBox.NON_RESULT);
            System.out.println("Successfully removed books");

            con.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}