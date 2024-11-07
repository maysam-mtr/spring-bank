package org.example.bankapp;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;


public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Views view = new Views();
        view.showLoginStage(stage);

    }

    public static void main(String[] args) {
        launch();
    }

}