package org.example.sudoku.models;

public interface IAlertBox {

    boolean showConfirmBox(String title, String header, String message);

    void showInformationBox(String title, String header, String message);
}