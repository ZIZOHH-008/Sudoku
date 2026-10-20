package org.example.sudoku.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.sudoku.models.AlertBox;
import org.example.sudoku.models.IAlertBox;
import org.example.sudoku.models.IGameLog;

import java.io.IOException;





public class WelcomeController implements IGameLog {

    @FXML
    public void enterGame(ActionEvent event) throws IOException {
        logEvent("UI Event: Clicked Start Game button");

        AlertBox alertBox = new AlertBox();
        boolean response = alertBox.showConfirmBox(
                "Start Game",
                "¡Hi there!",
                "¿Do you want to start a game?");



        if (response){
            logEvent("UI Event: Load Game View");
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/sudoku/game-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.show();
        }

        else{
            logEvent("UI Event: Cancel game");
            System.out.println("No quisiste jugar");
        }
    }




    @FXML
    private void exitGame(ActionEvent event) {
        logEvent("UI Event: Clicked Exit Game button");
        Stage stage = (Stage) ((Node) event.getSource()) //Elemento
                .getScene() //Escena
                .getWindow(); //Ventana
        stage.close();
    }

}
