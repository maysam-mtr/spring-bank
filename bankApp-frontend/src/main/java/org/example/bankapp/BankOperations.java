package org.example.bankapp;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * BankOperations.java
 * A Class containing basic bank operations that are repeated throughout the application
 * Methods:
 * generatePassword()
 * getCard(String cardNumber, String cardHolder, String validDate)
 * getTransactionCell(String date, String sender, String receiver, String amount, String msg, String sign)
 * getSearchPane(String acc_nb, String username, String fn, String ln, String dob, String phone_nb,
 *                               String city, String country, String nb_of_accounts)
 */

public class BankOperations {

    /**
     * generate a random strong password
     * @return
     */
    public static String generatePassword(){
        //password length
        int pass_length = 10;

        //all characters
        final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final char[] uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray();
        final char[] numbers = "0123456789".toCharArray();
        final char[] symbols = "-_".toCharArray();
        final char[] allAllowed = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789-_".toCharArray();

        Random random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < pass_length-4; i++) {
            password.append(allAllowed[random.nextInt(allAllowed.length)]);
        }

        // replace chars in random positions
        password.insert(random.nextInt(password.length()), lowercase[random.nextInt(lowercase.length)]);
        password.insert(random.nextInt(password.length()), uppercase[random.nextInt(uppercase.length)]);
        password.insert(random.nextInt(password.length()), numbers[random.nextInt(numbers.length)]);
        password.insert(random.nextInt(password.length()), symbols[random.nextInt(symbols.length)]);

        return password.toString();
    }

    /**
     * method to create a debit card layout
     * @param cardNumber
     * @param cardHolder
     * @param validDate
     * @return Pane that is a debit card layout
     */
    public Pane getCard(Long cardNumber, String cardHolder, String validDate){
        //style of pane
        Pane pane = new Pane();
        pane.setPrefHeight(200.0);
        pane.setPrefWidth(300.0);
        pane.setStyle("-fx-background-radius: 10px; -fx-background-color: #14080E;");

        //card number layout
        Label client3_card_nb = new Label(cardNumber.toString());
        client3_card_nb.setLayoutX(21.0);
        client3_card_nb.setLayoutY(26.0);
        client3_card_nb.setPrefHeight(45.0);
        client3_card_nb.setPrefWidth(206.0);
        client3_card_nb.setTextFill(Color.web("#faf8f8"));
        client3_card_nb.setFont(new Font(30.0));
        client3_card_nb.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));

        //cardholder layout
        Label cardHolderLabel = new Label("CARD HOLDER");
        cardHolderLabel.setLayoutX(21.0);
        cardHolderLabel.setLayoutY(108.0);
        cardHolderLabel.setTextFill(Color.web("#faf9f9"));
        cardHolderLabel.setFont(new Font(10.0));

        Label client3_card_holder = new Label(cardHolder);
        client3_card_holder.setLayoutX(14.0);
        client3_card_holder.setLayoutY(123.0);
        client3_card_holder.setPrefHeight(35.0);
        client3_card_holder.setPrefWidth(122.0);
        client3_card_holder.setTextFill(Color.web("#faf8f8"));
        client3_card_holder.setFont(new Font(18.0));
        client3_card_holder.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));

        //valid thru layout
        Label validThruLabel = new Label("VALID THRU");
        validThruLabel.setLayoutX(200.0);
        validThruLabel.setLayoutY(108.0);
        validThruLabel.setTextFill(Color.web("#faf9f9"));
        validThruLabel.setFont(new Font(10.0));

        Label client3_valid_date = new Label(validDate);
        client3_valid_date.setLayoutX(200.0);
        client3_valid_date.setLayoutY(123.0);
        client3_valid_date.setPrefHeight(35.0);
        client3_valid_date.setPrefWidth(122.0);
        client3_valid_date.setTextFill(Color.web("#faf8f8"));
        client3_valid_date.setFont(new Font(18.0));
        client3_valid_date.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));

        //image logo of the bank
        ImageView imageView = new ImageView();
        imageView.setFitHeight(60.0);
        imageView.setFitWidth(68.0);
        imageView.setLayoutX(215.0);
        imageView.setLayoutY(23.0);
        imageView.setImage(new Image(getClass().getResourceAsStream("/org/example/bankapp/Images/img_1.png")));

        pane.getChildren().addAll(client3_card_nb, cardHolderLabel, client3_card_holder, validThruLabel, client3_valid_date, imageView);
        return pane;
    }

    /**
     * mthod to get transaction info row layout
     * @param date
     * @param sender
     * @param receiver
     * @param amount
     * @param msg
     * @param sign
     * @return Pane that is a transaction info row layout
     */
    public Pane getTransactionCell(String date, Long sender, Long receiver, double amount, String msg, String sign){
        //pane style
        Pane pane = new Pane();
        pane.setPrefHeight(77.0);
        pane.setPrefWidth(503.0);
        pane.setStyle("-fx-border-color: #EEEEEE; -fx-background-radius: 10px; -fx-border-radius: 10px; " +
                "-fx-background-color: #EEEEEE;");

        //date of transaction layout
        Label dateLabel = new Label(date);
        dateLabel.setLayoutX(14.0);
        dateLabel.setLayoutY(30.0);
        pane.getChildren().add(dateLabel);

        //sender account layout
        Label senderLabel = new Label(sender.toString());
        senderLabel.setLayoutX(134.0);
        senderLabel.setLayoutY(30.0);
        senderLabel.setStyle("-fx-font-weight: BOLD");
        pane.getChildren().add(senderLabel);

        //receiver account layout
        Label receiverLabel = new Label(receiver.toString());
        receiverLabel.setLayoutX(273.0);
        receiverLabel.setLayoutY(30.0);
        receiverLabel.setStyle("-fx-font-weight: BOLD");
        pane.getChildren().add(receiverLabel);

        //amount layout
        Label amountLabel = new Label("$" + ((amount < 0)? amount*(-1):amount));
        amountLabel.setLayoutX(446.0);
        amountLabel.setLayoutY(30.0);
        amountLabel.setFont(new Font(14.0));
        pane.getChildren().add(amountLabel);

        //message button layout
        Button button = new Button("!");
        button.setLayoutX(382.0);
        button.setLayoutY(24.0);
        button.setStyle("-fx-background-color: #14080E;");
        button.setTextFill(Color.web("#f4f0f0"));
        button.setFont(Font.font("System", FontWeight.BOLD, 14.0));
        pane.getChildren().add(button);

        //display transaction message as dialogue
        button.setOnMouseClicked(e->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText(msg);
            alert.show();
        });

        //separator
        Line line = new Line();
        line.setEndX(-100.0);
        line.setEndY(33.0);
        line.setLayoutX(338.0);
        line.setLayoutY(23.0);
        line.setStartX(-100.0);
        line.setStroke(Color.web("#a49f9f"));
        pane.getChildren().add(line);

        //sign if its withdrawal or deposit
        Label type = new Label(sign);
        type.setLayoutX(427.0);
        type.setLayoutY(25.0);
        if (sign.equals("+")){
            type.setTextFill(Color.GREEN);
            amountLabel.setTextFill(Color.GREEN);
        }else {
            type.setTextFill(Color.RED);
            amountLabel.setTextFill(Color.RED);
        }

        type.setFont(new Font(20.0));
        senderLabel.setStyle("-fx-font-weight: BOLD");
        pane.getChildren().add(type);

        return pane;
    }

    public static Pane getSearchPane(String acc_nb, String username, String fn, String ln, String dob, String phone_nb,
                              String city, String country, String nb_of_accounts) {
        Pane searchResult = new Pane();
        searchResult.setPrefSize(732.0, 75.0);
        searchResult.setStyle("-fx-background-color: #53B052; -fx-background-radius: 10px;");

        Label[] labels = new Label[18];
        String[] labelNames = {"Account nb", "Username", "First Name", "Last Name", "DOB", "Phone nb", "City", "Country",
                "Nb of Accounts", acc_nb, username, fn, ln, dob, phone_nb, city, country, nb_of_accounts};
        double[] layoutXs = {14.0, 108.0, 200.0, 276.0, 368.0, 438.0, 528.0, 582.0, 640.0, 14.0, 108.0, 197.0, 276.0,
                350.0, 444.0, 528.0, 593.0, 681.0};
        double[] layoutYs = {6.0, 5.0, 5.0, 6.0, 5.0, 6.0, 5.0, 5.0, 5.0, 37.0, 38.0, 38.0, 37.0, 37.0, 38.0, 38.0,
                37.0, 37.0};

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(labelNames[i]);
            labels[i].setLayoutX(layoutXs[i]);
            labels[i].setLayoutY(layoutYs[i]);
            labels[i].setTextFill(Color.web("#0f6124"));
            if (i >= 9) {
                labels[i].setPrefSize(73.0, 18.0);
                labels[i].setFont(Font.font("System", FontWeight.BOLD, 12.0));
            } else {
                labels[i].setTextFill(Color.web("#f4f3f3"));
            }
            searchResult.getChildren().add(labels[i]);
        }
        return searchResult;
    }
}
