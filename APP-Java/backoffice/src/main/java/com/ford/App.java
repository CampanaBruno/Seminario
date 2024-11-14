package com.ford;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("IniciarSesion"), 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(root);
        scene.getWindow().setWidth(width);
        scene.getWindow().setHeight(height);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
