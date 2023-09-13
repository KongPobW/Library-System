package com.example.librarysystem;

import com.example.librarysystem.utils.SceneManager;
import com.example.librarysystem.utils.DatabaseManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static SceneManager sceneManager;

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseManager.getConnection();
        sceneManager = new SceneManager(stage, getClass());

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("screen_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //Set properties of the stage
        stage.setTitle("Stamford Library");
        stage.setResizable(false);
        stage.setScene(scene);

        //Set the icon of application
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);

        //Show the screen to the user
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}