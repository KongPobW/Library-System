package com.example.librarysystem.utils;

import com.example.librarysystem.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    public final Stage ROOT_STAGE;
    private final Class<? extends Application> CLASS;
    private final Map<String, Scene> scenes = new HashMap<>();

    public SceneManager(Stage rootStage, Class<? extends Application> aClass) {
        if (rootStage == null || aClass == null) throw new IllegalArgumentException();
        this.ROOT_STAGE = rootStage;
        this.CLASS = aClass;
    }

    public FXMLLoader getLoader(String fxml) {
        return new FXMLLoader(CLASS.getResource(fxml));
    }

    public void changeScene(String fxml) {
        Scene scene = scenes.computeIfAbsent(fxml, u -> {
            try {
                Pane pane = FXMLLoader.load(CLASS.getResource(fxml));
                return new Scene(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ROOT_STAGE.setScene(scene);
    }

    public void changeScene(Scene scene) {
        ROOT_STAGE.setScene(scene);
    }
}