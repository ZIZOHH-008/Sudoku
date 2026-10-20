package org.example.sudoku.models;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;


public class AlertBox implements IAlertBox {

        @Override
        public boolean showConfirmBox(String title, String header, String message){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);

            Optional<ButtonType> response = alert.showAndWait();

            if(response.isPresent() && response.get() == ButtonType.OK){ //Si tenes una respuesta y es Ok
                return true;
            }
            return false;
        }

}


