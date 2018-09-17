package com.houarizegai.generatetablefx.java.launch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/houarizegai/generatetablefx/resources/views/Main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("GenerateTableFX");
        stage.show();
    }
}
