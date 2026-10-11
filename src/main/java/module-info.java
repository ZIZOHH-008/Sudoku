module org.example.sudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.sudoku to javafx.fxml;
    exports org.example.sudoku;
    exports org.example.sudoku.controllers;
    opens org.example.sudoku.controllers to javafx.fxml;
}