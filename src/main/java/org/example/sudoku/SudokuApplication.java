package org.example.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class SudokuApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuApplication.class.getResource("welcome-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Sudoku Game");
        stage.setScene(scene);
        stage.sizeToScene(); // Ajusta la ventana al contenido del root
        stage.show();
    }

}
