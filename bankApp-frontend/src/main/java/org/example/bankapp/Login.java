package org.example.bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.bankapp.APIConnection.executeAPI;

/**
 * Login.java
 * A class that handles the login operations and opens appropriate stage
 * Methods:
 * onLoginClick()
 * validateLogin()
 */

public class Login implements Initializable {

    @FXML
    private Label lblLoginMessage;

    @FXML
    private TextField txtLoginUsername;

    @FXML
    private TextField txtLoginPassword;

    @FXML
    private ChoiceBox<String> loginChoice;

    String[] cbElements = {"Client", "Admin"};

    /**
     * onLoginClick(): method to log in to the application
     */
    @FXML
    protected void onLoginClick(){

        //check if required fields are empty
        if(!txtLoginUsername.getText().isBlank() && !txtLoginPassword.getText().isBlank()){
            validateLogin();
        }
        else{
            lblLoginMessage.setText("Username and password cannot be empty");
            lblLoginMessage.setTextFill(Color.RED);
        }

    }

    /**
     * validateLogin(): method to verify login credentials and open the client/admin stage if valid
     */
    public void validateLogin() {
        int role = 0;
        Pair<Integer, String> response;

        try {
            if (loginChoice.getValue().equals("Client")) {
                role = 1;
                response = executeAPI("GET", "http://localhost:8080/api/user/authenticateUser/" +
                        txtLoginUsername.getText() + "/" + txtLoginPassword.getText() + "/" + role);
            } else {
                role = 0;
                response = executeAPI("GET", "http://localhost:8080/api/user/authenticateUser/" +
                        txtLoginUsername.getText() + "/" + txtLoginPassword.getText() + "/" + role);
            }

            if (response.getKey() == HttpURLConnection.HTTP_OK) {
                JSONObject user = new JSONObject(response.getValue());
                if (loginChoice.getValue().equals("Client")) {
                    // Fill user info in the client controller
                    ClientController.clientId = user.getLong("userId");
                    ClientController.clientFirstName = user.getString("firstName");
                    ClientController.clientLastName = user.getString("lastName");

                    // Show client stage
                    Views view = new Views();
                    Views.stage1.close();
                    view.showStage("client.fxml");
                } else if (loginChoice.getValue().equals("Admin")) { // If admin exists
                    // Show admin stage
                    Views.stage1.close();
                    Views view = new Views();
                    view.showStage("admin.fxml");
                }
            } else if (response.getKey() == HttpURLConnection.HTTP_NOT_FOUND) {
                lblLoginMessage.setText("Invalid username or password");
                lblLoginMessage.setTextFill(Color.RED);
            } else {
                lblLoginMessage.setText("Error logging in. Please try again.");
                lblLoginMessage.setTextFill(Color.RED);
            }
        } catch (Exception e) {
            lblLoginMessage.setText("An unexpected error occurred. Please try again");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set choice box values
        loginChoice.getItems().addAll(cbElements);
        loginChoice.setValue("Client");
    }
}
