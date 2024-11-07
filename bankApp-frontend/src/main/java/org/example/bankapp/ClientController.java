package org.example.bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static org.example.bankapp.APIConnection.executeAPI;

public class ClientController implements Initializable {
    /**
     * Client account nb
     */
    public static Long clientId = 0L;
    public static String clientFirstName = "";
    public static String clientLastName = "";
    private ArrayList accountCodes;

    //Pane 1
    @FXML
    private Label welcome_msg;
    @FXML
    private Pane dashboard_pane;

    @FXML
    private Button nav_dashboard;

    @FXML
    private HBox cards_hbox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label client1_balance;

    @FXML
    private Label client1_income;

    @FXML
    private Label client1_expenses;

    @FXML
    private VBox client1_vbox;

    //Pane 2
    @FXML
    private ChoiceBox<String> client2_account_cb;

    @FXML
    private TextField client2_receiver_acc;

    @FXML
    private TextField client2_amount;

    @FXML
    private TextArea client2_transaction_msg;

    @FXML
    private Label client2_error_msg;

    @FXML
    private Button nav_transactions;

    @FXML
    private VBox transactions_pane;

    @FXML
    private VBox client2_transactions;

    @FXML
    private ScrollPane client2_sp;

    //Pane 3
    @FXML
    private Label client3_balance;

    @FXML
    private Label client3_acc_name;

    @FXML
    private Label client3_acc_nb;

    @FXML
    private Label client3_type;

    @FXML
    private Label client3_owners;

    @FXML
    private Label client3_date;

    @FXML
    private Label client3_card_holder;

    @FXML
    private Label client3_valid_date;

    @FXML
    private ListView<String> client3_list = new ListView<>();

    @FXML
    private BorderPane accounts_pane;

    @FXML
    private Button nav_accounts;

    private final Map<String, String[]> accountInfoMap = new HashMap<>();

    @FXML
    private Label client3_card_nb;

    //Pane 4
    @FXML
    private Pane profile_pane;

    @FXML
    private Button nav_profile;

    @FXML
    private Label client_fn;

    @FXML
    private Label client_ln;

    @FXML
    private Label client_username;

    @FXML
    private Label client_acc_nb;

    @FXML
    private Label client_pn;

    @FXML
    private Label client_nb_of_acc;

    @FXML
    private Label client_country;

    @FXML
    private Label client_city;

    @FXML
    private Label client_dob;

    public void resetAll() {
        // Hide all panes
        dashboard_pane.setVisible(false);
        transactions_pane.setVisible(false);
        accounts_pane.setVisible(false);
        profile_pane.setVisible(false);

        // Reset all navigation styles
        nav_dashboard.setStyle("-fx-background-color: #F3F6FA; -fx-text-fill: #000000");
        nav_transactions.setStyle("-fx-background-color: #F3F6FA; -fx-text-fill: #000000");
        nav_accounts.setStyle("-fx-background-color: #F3F6FA; -fx-text-fill: #000000");
        nav_profile.setStyle("-fx-background-color: #F3F6FA; -fx-text-fill: #000000");
    }

    public void onDashboardChoice(){

        resetAll();
        dashboard_pane.setVisible(true);
        nav_dashboard.setStyle("-fx-background-color: #14080E; -fx-text-fill: #FFFFFF");
        fillLatestTransactions();
    }

    public void onTransactionsChoice(){
        resetAll();
        transactions_pane.setVisible(true);
        nav_transactions.setStyle("-fx-background-color: #14080E; -fx-text-fill: #FFFFFF");
        fillTransactionList();
    }

    public void onAccountChoice(){
        resetAll();
        accounts_pane.setVisible(true);
        nav_accounts.setStyle("-fx-background-color: #14080E; -fx-text-fill: #FFFFFF");
        fillAccountsPage();
    }

    public void onProfileChoice(){
        resetAll();
        profile_pane.setVisible(true);
        nav_profile.setStyle("-fx-background-color: #14080E; -fx-text-fill: #FFFFFF");
    }
    /**
     * Pane 1
     */

    public void fillDasboard(){

        try {
            Pair<Integer, String> response = executeAPI("GET",
                    "http://localhost:8080/api/card/getCardsByUserId/" + clientId);
            JSONArray cardArray = new JSONArray(response.getValue());
            if (response.getKey() == HttpURLConnection.HTTP_OK){
                welcome_msg.setText("Hello, " + clientFirstName);
                BankOperations o = new BankOperations();
                Pane pane;
                for (int i = 0; i < cardArray.length(); i++){
                    pane = o.getCard(cardArray.getJSONObject(i).getJSONObject("card_account").getLong("accountNb"),
                            clientFirstName + " " + clientLastName,
                            cardArray.getJSONObject(i).getString("expirationDate"));
                    cards_hbox.getChildren().add(pane);
                }

            }
            fillStatistics();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        cards_hbox.setSpacing(15);
        scrollPane.setFitToHeight(true);
    }

    public void fillStatistics(){
        try {
            //total balance
            Pair<Integer, String> response = executeAPI("GET",
                    "http://localhost:8080/api/account/getUserTotalBalance/" + clientId);
            if (response.getKey() == HttpURLConnection.HTTP_OK){
                client1_balance.setText(String.valueOf(response.getValue()));
            }else{
                client1_balance.setText("Error");
            }

            Pair<Integer, String> response2 = executeAPI("GET",
                    "http://localhost:8080/api/transaction/getTotalIncome/" + clientId);
            if (response2.getKey() == HttpURLConnection.HTTP_OK){
                client1_income.setText(String.valueOf(response2.getValue()));
            }else{
                client1_income.setText("Error");
            }

            Pair<Integer, String> response3 = executeAPI("GET",
                    "http://localhost:8080/api/transaction/getTotalExpenses/" + clientId);
            if (response3.getKey() == HttpURLConnection.HTTP_OK){
                client1_expenses.setText(String.valueOf(response3.getValue()));
            }else{
                client1_expenses.setText("Error");
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public void fillLatestTransactions(){
        BankOperations o = new BankOperations();
        String type;
        try {
            Pair<Integer, String> response = executeAPI("GET",
                    "http://localhost:8080/api/transaction/getLatestTransactions/" + clientId);

            if (response.getKey() == HttpURLConnection.HTTP_OK){
                JSONArray transactions = new JSONArray(response.getValue());
                for (int i = 0; i < transactions.length(); i++){
                    JSONObject transaction = transactions.getJSONObject(i);
                    if (transaction.getJSONObject("senderAccount").getLong("accountNb") ==
                            (transaction.getJSONObject("receiverAccount").getLong("accountNb"))){
                        if (transaction.getDouble("value") > 0){
                            type = "+";
                        }else {
                            type = "-";
                        }
                        client1_vbox.getChildren().add(o.getTransactionCell(transaction.getString("date"),
                                transaction.getJSONObject("senderAccount").getLong("accountNb"),
                                transaction.getJSONObject("receiverAccount").getLong("accountNb"),
                                transaction.getDouble("value"),
                                transaction.getString("message"),
                                type));
                    }else {
                        client1_vbox.getChildren().add(o.getTransactionCell(transaction.getString("date"),
                                transaction.getJSONObject("senderAccount").getLong("accountNb"),
                                transaction.getJSONObject("receiverAccount").getLong("accountNb"),
                                transaction.getDouble("value"),
                                transaction.getString("message"),
                                (accountCodes.contains(transaction.getJSONObject("senderAccount").getLong("accountNb")))? "-":"+"));
                    }

                }

            }else{
                throw new Exception("Error loading transactions");
            }
            client1_vbox.setSpacing(10);
            client1_vbox.setPadding(new Insets(10));
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    /**
     * Pane 2
     */

    public void onSendMoneyClick(){

        if (client2_account_cb.getValue().isBlank() || client2_amount.getText().isBlank() ||
                client2_receiver_acc.getText().isBlank()){
            client2_error_msg.setText("Fields cannot be empty");
            client2_error_msg.setTextFill(Color.RED);
        } else if (Double.valueOf(client2_amount.getText()) <= 0) {
            client2_error_msg.setText("Invalid Amount");
            client2_error_msg.setTextFill(Color.RED);
        } else if (client2_account_cb.getValue().equals(client2_receiver_acc.getText())){
            client2_error_msg.setText("You can't send money to from and to same account!");
            client2_error_msg.setTextFill(Color.RED);
        }else{
            Pair<Integer, String> response = executeAPI("POST",
                    "http://localhost:8080/api/transaction/makeTransaction/" + client2_account_cb.getValue() +
                            "/" + client2_receiver_acc.getText() + "/" + Double.valueOf(client2_amount.getText()),
                    (client2_transaction_msg.getText().isBlank())? "No message":client2_transaction_msg.getText());
            if (response.getKey() == HttpURLConnection.HTTP_CREATED) {
                client2_error_msg.setText("Transaction Successful!");
                client2_error_msg.setTextFill(Color.GREEN);
            }else {
                JSONObject json = new JSONObject(response.getValue());
                client2_error_msg.setText(json.getString("message"));
                client2_error_msg.setTextFill(Color.RED);
            }
        }
    }

    public void fillTransactionList(){
        BankOperations o = new BankOperations();
        String type;

        try {
            Pair<Integer, String> response = executeAPI("GET",
                    "http://localhost:8080/api/transaction/getTransactionsByUserId/" + clientId);

            if (response.getKey() == HttpURLConnection.HTTP_OK){
                JSONArray transactions = new JSONArray(response.getValue());
                for (int i = 0; i < transactions.length(); i++){
                    JSONObject transaction = transactions.getJSONObject(i);
                    if (transaction.getJSONObject("senderAccount").equals(transaction.getJSONObject("receiverAccount"))){
                        if (transaction.getDouble("value") > 0){
                            type = "+";
                        }else {
                            type = "-";
                        }
                        client2_transactions.getChildren().add(o.getTransactionCell(transaction.getString("date"),
                                transaction.getJSONObject("senderAccount").getLong("accountNb"),
                                transaction.getJSONObject("receiverAccount").getLong("accountNb"),
                                transaction.getDouble("value"),
                                transaction.getString("message"),
                                type));
                    }else {
                        client2_transactions.getChildren().add(o.getTransactionCell(transaction.getString("date"),
                                transaction.getJSONObject("senderAccount").getLong("accountNb"),
                                transaction.getJSONObject("receiverAccount").getLong("accountNb"),
                                transaction.getDouble("value"),
                                transaction.getString("message"),
                                (accountCodes.contains(transaction.getJSONObject("senderAccount").getLong("accountNb")))? "-":"+"));
                    }

                }

            }else{
                throw new Exception("Error loading transactions");
            }
            client2_transactions.setSpacing(10);
            client2_transactions.setPadding(new Insets(10));
            client2_sp.setFitToWidth(true);

        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    /**
     * Pane 3: Accounts Page
     */
    public void fillAccountsPage(){
            try {
                Pair<Integer, String> response = executeAPI("GET",
                        "http://localhost:8080/api/account/getAccountsByUser/" + clientId);
                if (response.getKey() == HttpURLConnection.HTTP_OK) {
                    JSONArray accounts = new JSONArray(response.getValue());
                    for (int i = 0; i < accounts.length(); i++) {
                        JSONObject account = accounts.getJSONObject(i);
                        Long accountCode = account.getLong("accountNb");

                        Pair<Integer, String> res2 = executeAPI("GET",
                                "http://localhost:8080/api/accountholder/getUsersByAccount/" + accountCode);
                        ArrayList<String> accountOwnersNames = new ArrayList<>();
                        if (res2.getKey() == HttpURLConnection.HTTP_OK) {
                            JSONArray owners = new JSONArray(res2.getValue());
                            for (int j = 0; j < owners.length(); j++) {
                                accountOwnersNames.add((owners.getJSONObject(j).getString("firstName") + " " +
                                        owners.getJSONObject(j).getString("lastName")));
                            }
                        }

                        Pair<Integer, String> res3 = executeAPI("GET",
                                "http://localhost:8080/api/card/getCardsByAccountNb/" + accountCode);
                        String cardExpirationDate = null;
                        if (res3.getKey() == HttpURLConnection.HTTP_OK) {
                            JSONArray debitCards = new JSONArray(res3.getValue());
                            if (debitCards.length() > 0) {
                                JSONObject debitCard = debitCards.getJSONObject(0);
                                cardExpirationDate = debitCard.getString("expirationDate");
                            }
                        }

                        String[] accountInfo = new String[9];
                        accountInfo[0] = account.getString("accountName");
                        accountInfo[1] = account.getString("accountType");
                        accountInfo[2] = String.valueOf(account.getDouble("balance"));
                        accountInfo[3] = account.getString("creationDate");
                        accountInfo[4] = accountOwnersNames.get(0);
                        accountInfo[5] = cardExpirationDate;
                        for (int j = 0; j < accountOwnersNames.size(); j++) {
                            accountInfo[j + 6] = accountOwnersNames.get(j);
                        }

                        client3_list.getItems().add(String.valueOf(accountCode));
                        accountInfoMap.put(String.valueOf(accountCode), accountInfo);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        client3_list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label l = new Label(item);
                    l.setTextFill(Color.WHITE);
                    l.setStyle("-fx-font-weight: BOLD; -fx-font-size: 14");
                    VBox vbox = new VBox(l);
                    vbox.setPadding(new Insets(10));
                    vbox.setStyle("-fx-background-radius: 10px; -fx-background-color: #0d1366;");
                    setGraphic(vbox);
                }
            }
        });

        client3_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Get account information from the Map
            String[] accountInfo = accountInfoMap.get(newValue);
            client3_acc_name.setText(accountInfo[0]);
            client3_acc_nb.setText(newValue);
            client3_type.setText(accountInfo[1]);
            client3_balance.setText("$" + accountInfo[2] + " USD");
            client3_owners.setText(accountInfo[6] + ((accountInfo[7] == null)? "":(", " + accountInfo[7])) +
                    ((accountInfo[8] == null)? "":(", " + accountInfo[8])));
            client3_date.setText(accountInfo[3]);

            //card info
            if (accountInfo[5] != null) {
                client3_card_nb.setText(newValue);
                client3_card_holder.setText(accountInfo[4]);
                client3_valid_date.setText(accountInfo[5]);
            }else {
                client3_card_nb.setText("No Card");
                client3_card_holder.setText("");
                client3_valid_date.setText("");
            }
        });

        accounts_pane.setLeft(client3_list);
    }

    /**
     * Pane 4: Profile Page
     */
    public void fillUserInfo(){
        try {
            Pair<Integer, String> response = executeAPI("GET",
                    "http://localhost:8080/api/user/getUserById/" + clientId);
            JSONObject user = new JSONObject(response.getValue());
            if (response.getKey() == HttpURLConnection.HTTP_OK){
                client_fn.setText(user.getString("firstName"));
                client_ln.setText(user.getString("lastName"));
                client_city.setText(user.getString("city"));
                client_country.setText(user.getString("country"));
                client_dob.setText(user.getString("dob"));
                client_acc_nb.setText(String.valueOf(user.getLong("userId")));
                client_nb_of_acc.setText(String.valueOf(user.getInt("nbOfAccounts")));
                client_pn.setText(user.getString("phoneNb"));
                client_username.setText(user.getString("username"));
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }

    /**
     * Logout Function
     */
    public void Logout(){
        Views.stage1.close();
        Views views = new Views();
        views.showLoginStage(new Stage());
    }

    public ArrayList getAccountCodes(){
        ArrayList<Long> accounts = new ArrayList<>();
        try {
            Pair<Integer, String> res2 = executeAPI("GET",
                    "http://localhost:8080/api/account/getAccountsByUser/" + clientId);

            if (res2.getKey() == HttpURLConnection.HTTP_OK) {
                JSONArray accountsArray = new JSONArray(res2.getValue());
                for (int i = 0; i < accountsArray.length(); i++) {
                    accounts.add(accountsArray.getJSONObject(i).getLong("accountNb"));
                }
            } else {
                throw new Exception("Error loading accounts");
            }
            return accounts;
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }

    }

    public void fillAccNames(){
        for (int i = 0; i < accountCodes.size(); i++) {
            client2_account_cb.getItems().add(String.valueOf(accountCodes.get(i)));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountCodes = getAccountCodes();
        fillUserInfo();
        onDashboardChoice();
        fillDasboard();
        fillAccNames();
    }
}
