/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package henkenet.tool;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefan
 */
public class MainControl {

    private MainView mainView;
    private MysqlDataSource dataSource;

    Connection connection;

    MainControl(MainView view) {
        this.mainView = view;

        //zur DB verbinden
        dataSource = new MysqlDataSource();
        dataSource.setUser("test");
        dataSource.setPassword("testpw");
        dataSource.setServerName("shelldon");
        dataSource.setDatabaseName("radius");
        try {
            connection = dataSource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MainControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void commit() {
        try {
            //Daten aus mainView holen
            if (this.mainView.getTextFieldRoomNumber1().getText().length() != 2
                    || this.mainView.getTextFieldRoomNumber2().getText().length() != 2
                    || this.mainView.getTextFieldRoomNumber3().getText().length() != 2) {
                this.mainView.getTextAreaFehler().append("Raumnr reviewn!\n");
                return;
            }

            String vorname = this.mainView.getTextFieldVorname().getText(),
                    nachname = this.mainView.getTextFieldNachname().getText(),
                    email = this.mainView.getTextFieldEMail().getText(),
                    roomnumber = this.mainView.getTextFieldRoomNumber1().getText() + "-"
                    + this.mainView.getTextFieldRoomNumber2().getText() + "-"
                    + this.mainView.getTextFieldRoomNumber3().getText(),
                    username = this.mainView.getTextFieldUsername().getText(),
                    password = String.valueOf(this.mainView.getPasswordFied().getPassword()),
                    expyDate = "2016-04-16";
            //Daten checken
            if (!password.equals(String.valueOf(this.mainView.getPasswordFiedCheck().getPassword()))) {
                this.mainView.getTextAreaFehler().append("Passwords are not equal!\n");
                return;
            }
            if (password.equalsIgnoreCase("")
                    || username.equalsIgnoreCase("")) {
                this.mainView.getTextAreaFehler().append("Nich alles ausgefüllt!\n");
                return;
            }
            //Daten zur DB senden
            //Insert Username and Pw
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `users` (`username`, `password`, `room`, `real_name`, `email`, `expiry_date`) "
                    + "VALUES ('" + username + "', '" + password + "', '" + roomnumber + "', '" + nachname + ", " + vorname + "', "
                    + "'" + email + "', '" + expyDate + "');");
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
            this.mainView.getTextAreaFehler().append("------------------\nSUCESS\n--------------------\n");
        } catch (SQLException ex) {
            this.mainView.getTextAreaFehler().append("Dupiclate entry:\n" + ex.getLocalizedMessage() + "\n");
        }
    }

    void clearView() {
        //clear fields
        this.mainView.getTextFieldVorname().setText("");
        this.mainView.getTextFieldNachname().setText("");
        this.mainView.getTextFieldEMail().setText("");
        this.mainView.getTextFieldRoomNumber1().setText("");
        this.mainView.getTextFieldRoomNumber2().setText("");
        this.mainView.getTextFieldRoomNumber3().setText("");
        this.mainView.getTextFieldUsername().setText("");
        this.mainView.getPasswordFied().setText("");
        this.mainView.getPasswordFiedCheck().setText("");
    }

}
