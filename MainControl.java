/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefan
 */
public class MainControl {

    private MainView mainView;
    private LoginForm loginView;
    private MysqlDataSource dataSource;
    private final static Logger LOG = Logger.getLogger(MainControl.class.getName());

    //id of the account which is edited
    private int accountIDupdate;
    
    Connection connection;
    
    User currentUser;
    
    MainControl(){
        //Show Login Form
        loginView = new LoginForm(this);
        loginView.setVisible(true);
        
        
    }

    public static void main(String[] args){
        new MainControl();
    }
    
    void trylogin(){
        //zur DB verbinden
        dataSource = new MysqlDataSource();
        dataSource.setUser(loginView.getTxtUser().getText());
        dataSource.setPassword(String.valueOf(loginView.getTxtPassword().getPassword()));
        dataSource.setServerName("shelldon");
        dataSource.setDatabaseName("radius");
        try {
            connection = dataSource.getConnection();
            loginView.setVisible(false);
            mainView = new MainView(this);
            enableEditSection(false);
            mainView.setVisible(true);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
        
    void commit() {
        try {
            //Daten aus mainView holen
            if (this.mainView.getTextFieldRoomNumber1().getText().length() != 2
                    || this.mainView.getTextFieldRoomNumber2().getText().length() != 2
                    || this.mainView.getTextFieldRoomNumber3().getText().length() != 2) {
                //TODO: this.mainView.getTextAreaFehler().append("Review room number!\n");
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
                //TODO: this.mainView.getTextAreaFehler().append("Passwords are not equal!\n");
                return;
            }
            if (password.equalsIgnoreCase("")
                    || username.equalsIgnoreCase("")) {
                //TODO: this.mainView.getTextAreaFehler().append("Nicht alles ausgefüllt!\n");
                return;
            }
            //Daten zur DB senden
            //Insert Username and Pw
            new User(username, password, roomnumber, nachname, vorname, email, expyDate).insert(connection);
            //TODO: this.mainView.getTextAreaFehler().append("--------------------\nSUCCESS\n--------------------\n");
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "[FAIL] " + ex.getLocalizedMessage(), ex);
        }
    }

    
    void commitSearch() {
        try {
            //Daten aus mainView holen
            String user = mainView.getTextFieldUsernameSearch().getText(),
                    name = mainView.getTextFieldNachnameSearch().getText(),
                    roomnr = mainView.getTextFieldRoomSearch().getText();
            
            if(user.equals("") && name.equals("") && roomnr.equals("")){
                //TODO: this.mainView.getTextAreaFehler().append("Please fill at least on search field.\n");
                return;
            }
            
            
            String statement = "SELECT * FROM users WHERE ";
            boolean first = true;
            if(user.length()>1){
                statement += "username='"+user+"'";
                first = false;
            }
           
            if(name.length()>0){
                if(!first){
                    statement += " AND ";
                } 
                statement += "(surname LIKE '%" + name +"%' OR givenname LIKE '%" + name + "%')";
                first = false;
            }
             
            if(roomnr.length()>0){
                if(!first){
                    statement += " AND ";
                } 
                statement += "room='" + roomnr +"'";
            }
            
            //Terminate statemnt
            statement += ";";
            
            //Daten zur DB senden
            //Insert Username and Pw
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            ResultSet set = preparedStatement.executeQuery();
            int count = 0;
            
            while(set.next()){
            	currentUser = new User(
            			set.getString("username"),
            			set.getString("password"),
            			set.getString("room"),
            			set.getString("surname"),
            			set.getString("givenname"),
            			set.getString("email"),
            			set.getString("expiration_date")
            			);
                currentUser.setUID(Integer.parseInt(set.getString("id")));
                count++;
            }
            if(count==0){
                //TODO: this.mainView.getTextAreaFehler().append("No entry found.\n");
                return;
            }
            
             if(count>1){
                //TODO: this.mainView.getTextAreaFehler().append(count +" entries found. Please fill in more search parameters.\n");
                return;
            }
            
            preparedStatement.close();
            //TODO: this.mainView.getTextAreaFehler().append("--------------------\nSUCCESS\n--------------------\n");
            
            mainView.getTextFieldEMailUpdate().setText(currentUser.email);
            mainView.getTextFieldRealNameUpdate().setText(currentUser.givenname + " " + currentUser.surname);
            mainView.getTextUserNameUpdate().setText(currentUser.username);
            mainView.getPasswordFieldUpdate().setText(currentUser.password);
            mainView.getPasswordFieldUpdateCheck().setText(currentUser.password);
            enableEditSection(true);
            
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "[FAIL] " + ex.getLocalizedMessage(), ex);
        }
    }
    
    void commitUpdate() {
        try {
            //Daten aus mainView holen
            String user = mainView.getTextUserNameUpdate().getText(),
                    name = mainView.getTextFieldRealNameUpdate().getText(),
                    passwd = String.valueOf(mainView.getPasswordFieldUpdate().getPassword()),
                    passwdchk = String.valueOf(mainView.getPasswordFieldUpdateCheck().getPassword()),
                    email = mainView.getTextFieldEMailUpdate().getText();
            
            if(user.equals("") || passwd.equals("")){
                //TODO: this.mainView.getTextAreaFehler().append("Please fill in username and password.\n");
                return;
            }
            
             if (!passwd.equals(passwdchk)) {
                //TODO: this.mainView.getTextAreaFehler().append("Passwords are not equal!\n");
                return;
            }
             
            currentUser.username = user;
            currentUser.surname = name;
            currentUser.password = passwd;
            currentUser.email = email;
            currentUser.update(connection);
            
            //TODO: this.mainView.getTextAreaFehler().append("--------------------\nSUCCESS\n--------------------\n");
            enableEditSection(false);
            
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "[FAIL] " + ex.getLocalizedMessage(), ex);
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
        this.mainView.getTextFieldRoomNumber4().setText("");
        this.mainView.getTextFieldUsername().setText("");
        this.mainView.getPasswordField().setText("");
        this.mainView.getPasswordFieldCheck().setText("");
    }
    
    void enableEditSection(boolean enabled){
        this.mainView.getButtonCancelUpdate().setEnabled(enabled);
        this.mainView.getButtonCommitUpdate1().setEnabled(enabled);
        
        this.mainView.getTextFieldEMailUpdate().setEnabled(enabled);
        this.mainView.getTextFieldRealNameUpdate().setEnabled(enabled);
        this.mainView.getPasswordFieldUpdate().setEnabled(enabled);
        this.mainView.getPasswordFieldUpdateCheck().setEnabled(enabled);
        this.mainView.getTextUserNameUpdate().setEnabled(enabled);
    }
    
    public void closeConn(){
        try {
            connection.close();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
