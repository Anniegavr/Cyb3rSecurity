package com.example.allaudits;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage stageM;

    public Stage getStage(){
        return this.stageM;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Audi🚘");
        stage.setScene(scene);
        stage.show();
        this.stageM = stage;
    }



    public static void main(String[] args) {
        launch();
    }
}
