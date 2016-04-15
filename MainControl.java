/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import java.util.logging.LogRecord;
import java.util.ArrayList;

/**
 *
 * @author Stefan
 */
public class MainControl {

    private MainView mainView;
    private LoginForm loginView;
    private MySQLDataLink dataSource;
    private final static Logger LOG = Logger.getLogger(MainControl.class.getName());
    
    User currentUser;
    
    MainControl(){
        // DEBUG
        LOG.setLevel(Level.INFO);
        loginView = new LoginForm(this);
        loginView.setVisible(true);
    }

    public static void main(String[] args){
        new MainControl();
    }
    
    void trylogin(){
        //zur DB verbinden
        dataSource = new MySQLDataLink(
                "shelldon",
                "radius",
                loginView.getTxtUser().getText(),
                String.valueOf(loginView.getTxtPassword().getPassword())
        );
        
        if (!(dataSource.connect())) return;

        loginView.setVisible(false);
        mainView = new MainView(this);
        enableEditSection(false);
        mainView.setVisible(true);
        LOG.addHandler(new StatusBar());

    }
        
    void commit() {
            //Daten aus mainView holen
            if (this.mainView.getTextFieldRoomNumber1().getText().length() != 2
                    || this.mainView.getTextFieldRoomNumber2().getText().length() != 2
                    || this.mainView.getTextFieldRoomNumber3().getText().length() != 2) {
                LOG.log(Level.WARNING, "[FAIL] Review room number.");
                return;
            }

            String vorname = this.mainView.getTextFieldVorname().getText(),
                    nachname = this.mainView.getTextFieldNachname().getText(),
                    email = this.mainView.getTextFieldEMail().getText(),
                    roomnumber = this.mainView.getTextFieldRoomNumber1().getText() + "-"
                    + this.mainView.getTextFieldRoomNumber2().getText() + "-"
                    + this.mainView.getTextFieldRoomNumber3().getText() + "-"
                    + this.mainView.getTextFieldRoomNumber4().getText(),
                    username = this.mainView.getTextFieldUsername().getText(),
                    password = String.valueOf(this.mainView.getPasswordField().getPassword()),
                    expyDate = "2016-04-16";
            //Daten checken
            if (!password.equals(String.valueOf(this.mainView.getPasswordFieldCheck().getPassword()))) {
                LOG.log(Level.WARNING, "[FAIL] Passwords do not match.");
                return;
            }
            if (password.equalsIgnoreCase("")
                    || username.equalsIgnoreCase("")) {
                LOG.log(Level.WARNING, "[FAIL] Password is empty.");
                return;
            }
            //Daten zur DB senden
            //Insert Username and Pw
            dataSource.insert(
                    new User(username, password, roomnumber, nachname, vorname, email, expyDate));
            LOG.info("[SUCCESS] Added user '" + username + "'");
    }

    
    void commitSearch() {
            ArrayList<User> result = dataSource.lookupUser(
                    mainView.getTextFieldUsernameSearch().getText());
            if (result.isEmpty()) {
                LOG.info("No hits.");
                return;
                // who the fuck programmiert eine suchfunktion,
                // haupteinstiegspunkt und einer der zentralen
                // bestandteile unseres projekts als void()?
                // tobi, WARUUUUUUUMMM????
            }
            
            for (User u : result)
                LOG.info("[SQL] Found user '" + u.username + "'");
            
            LOG.info("[SQL] Found " + result.size() + " users.");
            currentUser = result.get(0);
            
            mainView.getTextFieldEMailUpdate().setText(currentUser.email);
            mainView.getTextFieldGivenNameUpdate().setText(currentUser.givenname);
            mainView.getTextFieldSurNameUpdate().setText(currentUser.surname);
            mainView.getTextUserNameUpdate().setText(currentUser.username);
            mainView.getPasswordFieldUpdate().setText(currentUser.password);
            mainView.getPasswordFieldUpdateCheck().setText(currentUser.password);
            enableEditSection(true);
    }
    
    void commitUpdate() {
            //Daten aus mainView holen
            String user = mainView.getTextUserNameUpdate().getText(),
                    surname = mainView.getTextFieldSurNameUpdate().getText(),
                    givenname = mainView.getTextFieldGivenNameUpdate().getText(),
                    passwd = String.valueOf(mainView.getPasswordFieldUpdate().getPassword()),
                    passwdchk = String.valueOf(mainView.getPasswordFieldUpdateCheck().getPassword()),
                    email = mainView.getTextFieldEMailUpdate().getText();
            
            if(user.equals("") || passwd.equals("")){
                LOG.log(Level.WARNING, "[FAIL] " + "Please fill in username and password.");
                return;
            }
            
             if (!passwd.equals(passwdchk)) {
                LOG.log(Level.WARNING, "[FAIL] " + "Passwords do not match.");
                return;
            }
             
            currentUser.username = user;
            currentUser.surname = surname;
            currentUser.givenname = givenname;
            currentUser.password = passwd;
            currentUser.email = email;
            dataSource.update(currentUser);
            
            LOG.info("[SUCCESS] Updated user '" + currentUser.username + "'");
            enableEditSection(false);
    }
    
    void clearView() {
        //clear fields
        this.mainView.getTextFieldVorname().setText("");
        this.mainView.getTextFieldNachname().setText("");
        this.mainView.getTextFieldEMail().setText("");
        this.mainView.getTextFieldRoomNumber1().setText("");
        this.mainView.getTextFieldRoomNumber2().setText("");
        this.mainView.getTextFieldRoomNumber3().setText("");
        this.mainView.getTextFieldRoomNumber4().setText("");
        this.mainView.getTextFieldUsername().setText("");
        this.mainView.getPasswordField().setText("");
        this.mainView.getPasswordFieldCheck().setText("");
    }
    
    void enableEditSection(boolean enabled){
        this.mainView.getButtonCancelUpdate().setEnabled(enabled);
        this.mainView.getButtonCommitUpdate1().setEnabled(enabled);
        
        this.mainView.getTextFieldEMailUpdate().setEnabled(enabled);
        this.mainView.getTextFieldSurNameUpdate().setEnabled(enabled);
        this.mainView.getTextFieldGivenNameUpdate().setEnabled(enabled);
        this.mainView.getPasswordFieldUpdate().setEnabled(enabled);
        this.mainView.getPasswordFieldUpdateCheck().setEnabled(enabled);
        this.mainView.getTextUserNameUpdate().setEnabled(enabled);
    }
    
    public void closeConn(){
        dataSource.disconnect();
    }

    public class StatusBar extends StreamHandler {
        public void publish(LogRecord rec) {
            mainView.getStatusBar().setText(rec.getMessage());
            if (rec.getLevel().intValue() >= Level.SEVERE.intValue())
                mainView.getStatusBar().setForeground(Color.RED);
            else
                mainView.getStatusBar().setForeground(Color.BLACK);
        }
    }
}
