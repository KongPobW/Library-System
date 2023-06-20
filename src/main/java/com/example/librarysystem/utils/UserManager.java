package com.example.librarysystem.utils;

import com.example.librarysystem.Application;
import com.example.librarysystem.classes.MessageBox;
import com.example.librarysystem.classes.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserManager {

    private static String passwordSalter(String password) {
        return password;
    }

    public static User login(String email, String password) {
        Connection con = DatabaseManager.getConnection();

        String query = "SELECT * FROM library.user INNER JOIN library.user_details ON user_details.Email = user.Email WHERE user.Email = ? AND Password = ?;";

        try {
            assert con != null;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, passwordSalter(password));

            ResultSet rs = stmt.executeQuery();

            User foundUser = null;

            while (rs.next()) {
                int userId = rs.getInt("UserID");
                String userEmail = rs.getString("Email");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");

                foundUser = new User(userId, userEmail, "HIDDEN", firstName, lastName);
            }

            System.out.println(foundUser);

            con.close();

            return foundUser;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String signup(String fname, String lname, String email, String password) {
        Connection con = DatabaseManager.getConnection();

        String query1 = "INSERT INTO library.user (Email, Password) VALUES (?, ?);";
        String query2 = "INSERT INTO library.user_details (Email, FirstName, LastName) VALUES (?, ?, ?);";
        String query3 = "SELECT * FROM library.user WHERE Email = ?;";

        try {
            assert con != null;
            PreparedStatement stmtt = con.prepareStatement(query3);
            stmtt.setString(1, email);

            ResultSet rs = stmtt.executeQuery();
            String errorMessage = "No Error";

            if (rs.next()) {
                errorMessage = "Email has been used. Please use another email";
            } else {
                PreparedStatement stat = con.prepareStatement(query2);
                stat.setString(1, email);
                stat.setString(2, fname);
                stat.setString(3, lname);

                PreparedStatement stmt = con.prepareStatement(query1);
                stmt.setString(1, email);
                stmt.setString(2, password);

                stmt.executeUpdate();
                stat.executeUpdate();

                MessageBox.informationMessageBox("Successfully sign up", MessageBox.TypeMessageBox.NON_RESULT);
                System.out.println("Successfully sign up");

                FXMLLoader loader = Application.sceneManager.getLoader("ScreenLogin.fxml");

                Scene scene = new Scene(loader.load());

                Application.sceneManager.changeScene(scene);
            }

            con.close();

            return errorMessage;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String resetPassword(String email, String password) {
        Connection con = DatabaseManager.getConnection();

        String query1 = "UPDATE library.user SET Password = ? WHERE Email = ?;";
        String query2 = "SELECT * FROM library.user WHERE Email = ?;";

        try {
            assert con != null;
            PreparedStatement stmtt = con.prepareStatement(query2);
            stmtt.setString(1, email);

            ResultSet rs = stmtt.executeQuery();
            String errorMessage = "No Error";

            if (rs.next()) {
                PreparedStatement stmt = con.prepareStatement(query1);
                stmt.setString(1, password);
                stmt.setString(2, email);

                stmt.executeUpdate();

                MessageBox.informationMessageBox("Successfully reset password", MessageBox.TypeMessageBox.NON_RESULT);
                System.out.println("Successfully reset password");

                FXMLLoader loader = Application.sceneManager.getLoader("ScreenLogin.fxml");

                Scene scene = new Scene(loader.load());

                Application.sceneManager.changeScene(scene);
            } else {
                errorMessage = "Please input your email correctly";
            }

            con.close();

            return errorMessage;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}