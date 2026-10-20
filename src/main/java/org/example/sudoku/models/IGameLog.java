package org.example.sudoku.models;

public interface IGameLog {

    default void logEvent(String mensaje) {
        System.out.println("[LOG]: " + mensaje);
    }
}

