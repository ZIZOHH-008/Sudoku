package org.example.sudoku.models;

/**
 * Defines a default method for logging game events.
 */
public interface IGameLog {

    /**
     * Prints a log message to the console.
     *
     * @param mensaje the event message to log
     */
    default void logEvent(String mensaje) {
        System.out.println("[LOG]: " + mensaje);
    }
}