package org.example.bankapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Views {
    public static Stage stage1;

    public void showLoginStage(Stage stage){
        if(stage1 != null){
            stage1.close();
        }
        try {
            stage1 = stage;
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage.setResizable(false);

            stage.setTitle("Spring Bank");

            Image icon = new Image(getClass().getResourceAsStream("Images/icon.png"));
            stage.getIcons().add(icon);

            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void showStage(String fxmlFile){
        stage1.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Spring Bank");

            Image icon = new Image(getClass().getResourceAsStream("Images/icon.png"));
            stage.getIcons().add(icon);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage1 = stage;
            stage.show();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
