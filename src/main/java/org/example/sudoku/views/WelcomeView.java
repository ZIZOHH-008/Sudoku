package org.example.sudoku.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeView extends Stage {

    public WelcomeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/sudoku/welcome-view.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root);

        setTitle("Sudoku Game");
        setScene(scene);
        sizeToScene();
    }

    public static WelcomeView getInstance() throws IOException {
        if (WelcomeViewHolder.INSTANCE == null) {
            WelcomeViewHolder.INSTANCE = new WelcomeView();
        }
        return WelcomeViewHolder.INSTANCE;
    }

    public static void deleteInstance() {
        WelcomeViewHolder.INSTANCE = null;
    }

    private static class WelcomeViewHolder {
        private static WelcomeView INSTANCE;
    }
}