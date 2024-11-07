package org.example.bankapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.bankapp.APIConnection.executeAPI;

public class AdminController implements Initializable {
    //Pane 1: Create Client
    @FXML
    private Button nav_create_client;

    @FXML
    private AnchorPane create_client;

    @FXML
    private TextField client1_fn;

    @FXML
    private TextField client1_ln;

    @FXML
    private TextField  client1_phone_nb;

    @FXML
    private TextField client1_username;

    @FXML
    private TextField  client1_password;

    @FXML
    private DatePicker client1_dob;

    @FXML
    private TextField client1_city;

    @FXML
    private  TextField client1_country;

    @FXML
    private CheckBox client1_generate_username;

    @FXML
    private CheckBox client1_generate_password;

    @FXML
    private Label client1_error_message;

    //Pane 2: Create Account
    @FXML
    private TextField client2_card_holder;
    @FXML
    private TextField client3_acc_nb2;

    @FXML
    private Label client3_error2;

    @FXML
    private Label client3_error3;

    @FXML
    private AnchorPane create_account_pane;

    @FXML
    private Label admin2_msg_err;

    @FXML
    private Button nav_create_account;

    @FXML
    private Label admin2_error_msg;

    @FXML
    private ChoiceBox<String> admin2_account_choicebox;

    @FXML
    private ChoiceBox<String> admin2_branch_choicebox;

    @FXML
    private TextField admin2_deposit_nb;

    @FXML
    private TextField admin2_acc_nb;

    @FXML
    private TextField admin2_acc_nb2;

    @FXML
    private TextField admin2_acc_nb3;

    @FXML
    private TextField client2_account_name;

    //Pane 3: client list
    @FXML
    private VBox search_vbox;

    @FXML
    private ScrollPane client_scrollpane;

    @FXML
    private AnchorPane clients_list_pane;
    @FXML
    private Button nav_client_list;

    @FXML
    private TextField search_txt;

    @FXML
    private Label search_lbl;

    @FXML
    private TableView tableview;
    private ObservableList<ObservableList> data;

    //Pane 4: deposit
    @FXML
    private AnchorPane deposit_pane;

    @FXML
    private Button nav_deposit;

    @FXML
    private TextField admin4_acc_nb;

    @FXML
    private TextField admin4_amount;

    @FXML
    private TextField admin4_acc_nb2;

    @FXML
    private TextField admin4_amount2;

    @FXML
    private Label admin4_err_msg;

    @FXML
    private Label admin4_err_msg2;

    /**
     * methods for switching between panes
     */

    /**
     * resetAll(): resets all panes to invisible and buttons to unselected
     */
    public void resetAll() {
        // Hide all panes
        create_client.setVisible(false);
        create_account_pane.setVisible(false);
        clients_list_pane.setVisible(false);
        deposit_pane.setVisible(false);

        // Reset all navigation styles
        nav_create_client.setStyle("-fx-background-color: #F3F6FA; -fx-text-fill: #000000");
        nav_create_account.setStyle("-fx-background-color: #F3F6FA; -fx-text-fill: #000000");
        nav_client_list.setStyle("-fx-background-color: #F3F6FA; -fx-text-fill: #000000");
        nav_deposit.setStyle("-fx-background-color: #F3F6FA; -fx-text-fill: #000000");
    }

    /**
     * methods to make the selected pane visible and set navigation style
     */
    public void onCreateClientChoice(){

        resetAll();
        create_client.setVisible(true);
        nav_create_client.setStyle("-fx-background-color: #0f6124; -fx-text-fill: #FFFFFF");

        //set error message to null
        client1_error_message.setText("");
    }

    public void onCreateAccountChoice(){
        resetAll();
        create_account_pane.setVisible(true);
        nav_create_account.setStyle("-fx-background-color: #0f6124; -fx-text-fill: #FFFFFF");

        //set error message to null
        admin2_error_msg.setText("");
    }

    public void onClientListChoice(){
        resetAll();
        clients_list_pane.setVisible(true);
        nav_client_list.setStyle("-fx-background-color: #0f6124; -fx-text-fill: #FFFFFF");

        //set error message to null and search result layout to invisible
        search_lbl.setText("");
        client_scrollpane.setVisible(false);

        //fill table
        fillTable();
    }

    public void onDepositChoice(){
        resetAll();
        deposit_pane.setVisible(true);
        nav_deposit.setStyle("-fx-background-color: #0f6124; -fx-text-fill: #FFFFFF");

        //set fields back to null
        admin4_err_msg.setText("");
        admin4_err_msg2.setText("");
        search_txt.setText("");
        admin4_amount.setText("");
        admin4_amount2.setText("");
        admin4_acc_nb.setText("");
        admin4_acc_nb2.setText("");
        client3_error2.setText("");
        client3_error3.setText("");
        client2_card_holder.setText("");
        client3_acc_nb2.setText("");
    }

    /**
     * Pane 1 methods
     */

    /**
     * onCreateClientClick(): creates a client and inserts him into database
     */
    public void onCreateClientClick(){

        //check if required fields are empty
        if (client1_username.getText().isBlank() || client1_password.getText().isBlank() ||
                client1_fn.getText().isBlank() || client1_ln.getText().isBlank() ||
                String.valueOf(client1_dob.getValue()).isBlank() || client1_phone_nb.getText().isBlank() ||
                client1_country.getText().isBlank() || client1_city.getText().isBlank()){

            //display error message
            client1_error_message.setText("Fields cannot be empty");
            client1_error_message.setTextFill(Color.RED);

        }else{
            try {
                Pair<Integer, String> response = executeAPI("POST", "http://localhost:8080/api/user/createUser",
                        "{\n" +
                                "    \"username\": \"" + client1_username.getText() + "\" ,\n" +
                                "    \"password\": \"" + client1_password.getText() + "\",\n" +
                                "    \"firstName\": \"" + client1_fn.getText() + "\",\n" +
                                "    \"lastName\": \"" + client1_ln.getText() + "\",\n" +
                                "    \"dob\": \"" + client1_dob.getValue() + "\",\n" +
                                "    \"phoneNb\": \"" + client1_phone_nb.getText() + "\",\n" +
                                "    \"country\": \"" + client1_country.getText() + "\",\n" +
                                "    \"city\": \"" + client1_city.getText() +"\",\n" +
                                "    \"role\": 1\n}");

                if (response.getKey() == HttpURLConnection.HTTP_CREATED) {
                    client1_error_message.setText("Client Created Successfully");
                    client1_error_message.setTextFill(Color.GREEN);
                }else {
                    JSONObject res = new JSONObject(response.getValue());
                    client1_error_message.setText(res.getString("message"));
                    client1_error_message.setTextFill(Color.RED);
                }
            }catch (Exception e){
                client1_error_message.setText("Error creating client");
                client1_error_message.setTextFill(Color.RED);
            }
        }

    }

    /**
     * clearPane1Fields(): method to clear content of input fields in the first page
     */
    public void clearPane1Fields(){
        client1_username.clear();
        client1_password.clear();
        client1_fn.clear();
        client1_ln.clear();
        client1_dob.setValue(null);
        client1_phone_nb.clear();
        client1_country.clear();
        client1_city.clear();
        client1_generate_password.setDisable(false);
        client1_generate_username.setDisable(false);
        client1_generate_username.setSelected(false);
        client1_generate_password.setSelected(false);
    }

    /**
     * generateUsername(): method to generate custom username depending on first name and last name of the client
     */
    public void generateUsername(){
        String username = "";

        if(client1_generate_username.isSelected()){

            if (!client1_fn.getText().isBlank() && !client1_ln.getText().isBlank()){
                //take the first letter of the first name plus the last name plus random 3-digit number
                username += "" + client1_fn.getText().charAt(0) + client1_ln.getText() + ((int) (Math.random() * 900) + 100);

                client1_username.setText(username);
                client1_generate_username.setDisable(true);

            }else {
                //tell the user to enter required fields
                client1_error_message.setText("Please enter first name and last name to generate username");
                client1_error_message.setTextFill(Color.RED);
            }
        }

    }

    /**
     * generatePassword(): method to generate a random strong password
     */
    public void generatePassword(){
        if (client1_generate_password.isSelected()){
            client1_password.setText(BankOperations.generatePassword());
            client1_generate_password.setDisable(true);
        }
    }

    /**
     * Pane 2 methods
     */

    /**
     * onAddClientClick(): to display addition text fields for account owners
     */
    public void onAddClientClick(){
        admin2_acc_nb2.setVisible(true);
        admin2_acc_nb3.setVisible(true);
    }

    /**
     *onCreateAccountClick(): method to create an account for client(s) and creates debit cards for them
     */
    public void onCreateAccountClick() {
        //check if required fields are empty
        if (admin2_account_choicebox.getValue() == null || admin2_branch_choicebox.getValue() == null
                || admin2_acc_nb.getText().isBlank() || admin2_deposit_nb.getText().isBlank()
                || client2_account_name.getText().isBlank()){
            //display error message
            admin2_error_msg.setText("Fields cannot be empty");
            admin2_error_msg.setTextFill(Color.RED);

        }else{
            try {

                Pair<Integer, String> response = executeAPI("POST", "http://localhost:8080/api/account/createAccount",
                        "{\n" +
                                "   \"account\": { \n" +
                                "       \"accountType\": \"" + admin2_account_choicebox.getValue() + "\",\n" +
                                "       \"accountName\": \"" + client2_account_name.getText() + "\",\n" +
                                "       \"branch\": \"" + admin2_branch_choicebox.getValue() + "\",\n" +
                                "       \"balance\": " + admin2_deposit_nb.getText() + "\n" +
                                "   },\n" +
                                "   \"userIds\": [" + admin2_acc_nb.getText() +
                                ((admin2_acc_nb2.getText().isBlank())? "":"," + admin2_acc_nb2.getText()) +
                                ((admin2_acc_nb3.getText().isBlank())? "":"," + admin2_acc_nb3.getText()) + "]\n" +
                                "}");

                if (response.getKey() == HttpURLConnection.HTTP_CREATED) {
                    admin2_error_msg.setText("Account Successfully Created");
                    admin2_error_msg.setTextFill(Color.GREEN);
                }else {
                    JSONObject res = new JSONObject(response.getValue());
                    admin2_msg_err.setText(res.getString("message"));
                    admin2_msg_err.setTextFill(Color.RED);
                }

                //clear fields
                clearPage2Fields();

            }catch (Exception e){
                admin2_msg_err.setText("Error creating account. Try Again!");
                admin2_msg_err.setTextFill(Color.RED);
            }
        }
    }
    public void createCard(){
        if (client2_card_holder.getText().isBlank() || client3_acc_nb2.getText().isBlank()){
            client3_error2.setText("Fields cannot be empty");
            client3_error2.setTextFill(Color.RED);
        }else {
            try {
                Pair<Integer, String> response = executeAPI("POST",
                        "http://localhost:8080/api/card/addDebitCard/" + client2_card_holder.getText() + "/" +
                                client3_acc_nb2.getText());
                if (response.getKey() == HttpURLConnection.HTTP_CREATED) {
                    client3_error2.setText("Card Created Successfully!");
                    client3_error2.setTextFill(Color.GREEN);
                }else {
                    JSONObject res = new JSONObject(response.getValue());
                    client3_error2.setText(res.getString("message"));
                    client3_error2.setTextFill(Color.RED);
                }

            } catch (Exception e) {
                client3_error2.setText("Internal Error. Try Again");
                client3_error2.setTextFill(Color.RED);
            }
        }
    }

    public void updateCard(){
        if (client2_card_holder.getText().isBlank() || client3_acc_nb2.getText().isBlank()){
            client3_error2.setText("Fields cannot be empty");
            client3_error2.setTextFill(Color.RED);
        }else {
            try {
                Pair<Integer, String> response = executeAPI("PUT",
                        "http://localhost:8080/api/card/updateExpirationDate/" + client2_card_holder.getText() + "/" +
                                client3_acc_nb2.getText());
                if (response.getKey() == HttpURLConnection.HTTP_OK) {
                    client3_error2.setText("Card Exp date updated!");
                    client3_error2.setTextFill(Color.GREEN);
                }else {
                    JSONObject res = new JSONObject(response.getValue());
                    client3_error2.setText(res.getString("message"));
                    client3_error2.setTextFill(Color.RED);
                }

            } catch (Exception e) {
                client3_error2.setText("Internal Error. Try Again");
                client3_error2.setTextFill(Color.RED);
            }
        }
    }

    /**
     * clearPage2Fields():method to clear page 2 fields
     */
    public void clearPage2Fields(){
        client2_account_name.setText("");
        admin2_deposit_nb.setText("");
        admin2_acc_nb.setText("");
        admin2_acc_nb2.setText("");
        admin2_acc_nb3.setText("");
        admin2_acc_nb2.setVisible(false);
        admin2_acc_nb3.setVisible(false);
        admin2_account_choicebox.setValue("");
        admin2_branch_choicebox.setValue("");
    }

    /**
     * Page 3: Clients list
     */

    /**
     * onSearchClick(): method to get a certain client based on username
     */
    public void onSearchClick(){
        //set labels and result layout to invisible
        search_lbl.setVisible(false);
        //search_result.setVisible(false);
        client_scrollpane.setVisible(false);
        search_vbox.getChildren().clear();

        if(!search_txt.getText().isBlank()){ //if the search text field not empty
            try {
                Pair<Integer, String> response = executeAPI("GET",
                        "http://localhost:8080/api/user/searchForUser/" + search_txt.getText());
                if (response.getKey() == HttpURLConnection.HTTP_OK) {
                    JSONArray jsonArray = new JSONArray(response.getValue());
                    if (jsonArray.isEmpty()) {
                        System.out.println("The JSONArray is empty");
                        search_lbl.setText("No Results");
                        search_lbl.setVisible(true);
                    }else {
                        Pane pane;
                        for (int i = 0; i < jsonArray.length(); i++) {

                            pane = BankOperations.getSearchPane(String.valueOf(jsonArray.getJSONObject(i).getLong("userId")),
                                    jsonArray.getJSONObject(i).getString("username"),
                                    jsonArray.getJSONObject(i).getString("firstName"),
                                    jsonArray.getJSONObject(i).getString("lastName"),
                                    jsonArray.getJSONObject(i).getString("dob"),
                                    jsonArray.getJSONObject(i).getString("phoneNb"),
                                    jsonArray.getJSONObject(i).getString("city"),
                                    jsonArray.getJSONObject(i).getString("country"),
                                    String.valueOf(jsonArray.getJSONObject(i).getInt("nbOfAccounts")));

                            pane.setMinHeight(pane.getPrefHeight());
                            search_vbox.getChildren().add(pane);

                            client_scrollpane.setVisible(true);
                            search_vbox.setSpacing(10);
                            client_scrollpane.setFitToWidth(true);

                        }
                    }
                }else {
                    search_lbl.setText("Internal error. Try Again!");
                    search_lbl.setVisible(true);
                }

            } catch (Exception e) {
                search_lbl.setText("Internal error");
                search_lbl.setVisible(true);
            }
        }
    }

    /**
     * fillTable(): method to fill the table with the list of clients
     */
    public void fillTable(){
        // Clear existing data and columns
        tableview.getItems().clear();
        tableview.getColumns().clear();

        data = FXCollections.observableArrayList();
        try {
            //get all clients
            Pair<Integer, String> response = executeAPI("GET", "http://localhost:8080/api/user/getAllClients");
            if (response.getKey() == HttpURLConnection.HTTP_OK) {

                JSONArray jsonArray = new JSONArray(response.getValue());

                // Iterate over the keys of the first JSONObject in the array to create the columns
                if (jsonArray.length() > 0) {
                    JSONObject firstObject = jsonArray.getJSONObject(0);
                    for (String key : firstObject.keySet()) {
                        TableColumn<JSONObject, String> column = new TableColumn<>(key);
                        column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(key).toString()));
                        tableview.getColumns().add(column);
                    }
                }else {
                    tableview.setPlaceholder(new Label("No Clients"));
                }

                // Add the JSONObjects in the JSONArray as items in the TableView
                ObservableList<JSONObject> data = FXCollections.observableArrayList();
                for (int i = 0; i < jsonArray.length(); i++) {
                    data.add(jsonArray.getJSONObject(i));
                }
                tableview.setItems(data);
            }else {
                tableview.setPlaceholder(new Label("Internal Error!"));
            }

        }catch(Exception e){
            tableview.setPlaceholder(new Label("Internal Error!"));
        }
    }

    /**
     * Page 3: Deposit
     */

    /**
     * onDepositClick():method to deposit money into a client's accocunt
     */
    public void onDepositClick(){
        //check if fields are empty
        if (admin4_acc_nb.getText().isBlank() || admin4_amount.getText().isBlank()){
            admin4_err_msg.setText("Fields Cannot be empty");
            admin4_err_msg.setTextFill(Color.RED);
        } else if (Double.valueOf(admin4_amount.getText()) <= 0) {//check if amount is negative number
            admin4_err_msg.setText("Invalid deposit amount");
            admin4_err_msg.setTextFill(Color.RED);
        } else{

            try {
                Pair<Integer, String> response = executeAPI("POST",
                        "http://localhost:8080/api/transaction/makeTransaction/" + admin4_acc_nb.getText() +
                                "/" + admin4_acc_nb.getText() + "/" + admin4_amount.getText(),
                        "In Person Deposit to the Bank");
                if (response.getKey() == HttpURLConnection.HTTP_CREATED) {
                    admin4_err_msg.setText("Deposit Successful!");
                    admin4_err_msg.setTextFill(Color.GREEN);
                }else {
                    JSONObject json = new JSONObject(response.getValue());
                    admin4_err_msg.setText(json.getString("message"));
                    admin4_err_msg.setTextFill(Color.RED);
                }
            } catch (Exception e) {
                admin4_err_msg.setText("Internal Error. Try Again!");
                admin4_err_msg.setTextFill(Color.RED);
            }
        }
    }

    /**
     * onWithdrawClick(): method to withdraw money from a client account
     */
    public void onWithdrawClick(){
        //check if fields are empty
        if (admin4_acc_nb2.getText().isBlank() || admin4_amount2.getText().isBlank()){
            admin4_err_msg2.setText("Fields Cannot be empty");
            admin4_err_msg2.setTextFill(Color.RED);
        } else if (Double.valueOf(admin4_amount2.getText()) <= 0) { //if amount is negative
            admin4_err_msg2.setText("Invalid deposit amount");
            admin4_err_msg2.setTextFill(Color.RED);
        } else{
            try {
                Pair<Integer, String> response = executeAPI("POST",
                        "http://localhost:8080/api/transaction/makeTransaction/" + admin4_acc_nb2.getText() +
                                "/" + admin4_acc_nb2.getText() + "/" + Double.parseDouble(admin4_amount2.getText())*(-1),
                        "In Person Deposit to the Bank");
                if (response.getKey() == HttpURLConnection.HTTP_CREATED) {
                    admin4_err_msg2.setText("Withdrawal Successful");
                    admin4_err_msg2.setTextFill(Color.GREEN);
                }else {
                    JSONObject json = new JSONObject(response.getValue());
                    admin4_err_msg2.setText(json.getString("message"));
                    admin4_err_msg2.setTextFill(Color.RED);
                }

            } catch (Exception e) {
                admin4_err_msg2.setText("Internal Error");
                admin4_err_msg2.setTextFill(Color.RED);
            }
        }
    }

    /**
     * Logout(): go back to login view
     */
    public void Logout(){
        Views.stage1.close();
        Views views = new Views();
        views.showLoginStage(new Stage());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set values of choice boxes
        admin2_account_choicebox.getItems().addAll("CHECKING", "SAVINGS");
        admin2_branch_choicebox.getItems().addAll("Beirut", "Sayda", "Tripoli", "Aley");

        //set create client page as first page displayed
        onCreateClientChoice();

    }
}
