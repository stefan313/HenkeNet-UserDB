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

    //id of the account which is edited
    private int accountIDupdate;
    
    Connection connection;
    

    
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
            Logger.getLogger(MainControl.class.getName()).log(Level.SEVERE, null, ex);
            loginView.gettxtStatus().setText("ERROR:" + ex.getMessage());
        }
    }
        
    void commit() {
        try {
            //Daten aus mainView holen
            if (this.mainView.getTextFieldRoomNumber1().getText().length() != 2
                    || this.mainView.getTextFieldRoomNumber2().getText().length() != 2
                    || this.mainView.getTextFieldRoomNumber3().getText().length() != 2) {
                this.mainView.getTextAreaFehler().append("Review room number!\n");
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
                this.mainView.getTextAreaFehler().append("Passwords are not equal!\n");
                return;
            }
            if (password.equalsIgnoreCase("")
                    || username.equalsIgnoreCase("")) {
                this.mainView.getTextAreaFehler().append("Nicht alles ausgefüllt!\n");
                return;
            }
            //Daten zur DB senden
            //Insert Username and Pw
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `users` (`username`, `password`, `room`, `surname`, `givenname`, `email`, `expiration_date`) "
                    + "VALUES ('" + username + "', '" + password + "', '" + roomnumber + "', " + ((nachname.length()==0 || vorname.length() ==0) ? "NULL": "'" + nachname + "', '" + vorname + "'") + ", "
                    +  (email.length()==0 ? "NULL": "'" +email + "'") + ", '" + expyDate + "');");
            preparedStatement.executeUpdate();

            preparedStatement.close();
            this.mainView.getTextAreaFehler().append("--------------------\nSUCCESS\n--------------------\n");
        } catch (SQLException ex) {
            this.mainView.getTextAreaFehler().append("Dupiclate entry:\n" + ex.getLocalizedMessage() + "\n");
        }
    }

    
    void commitSearch() {
        try {
            //Daten aus mainView holen
            String user = mainView.getTextFieldUsernameSearch().getText(),
                    name = mainView.getTextFieldNachnameSearch().getText(),
                    roomnr = mainView.getTextFieldRoomSearch().getText();
            
            if(user.equals("") && name.equals("") && roomnr.equals("")){
                this.mainView.getTextAreaFehler().append("Please fill at least on search field.\n");
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
            
            String username = "", mail="", passwrd="", fullname="";
            while(set.next()){
                accountIDupdate = Integer.parseInt(set.getString("id"));
                //this.mainView.getTextAreaFehler().append("Found id"+ accountIDupdate+".\n");
                mail =set.getString("email");
                passwrd = set.getString("password");
                fullname = set.getString("surname, givenname");
                username = set.getString("username");
                count++;
            }
            if(count==0){
                this.mainView.getTextAreaFehler().append("No entry found.\n");
                return;
            }
            
             if(count>1){
                this.mainView.getTextAreaFehler().append(count +" entries found. Please fill in more search parameters.\n");
                return;
            }
             
            preparedStatement.close();
            this.mainView.getTextAreaFehler().append("--------------------\nSUCCESS\n--------------------\n");
            
            mainView.getTextFieldEMailUpdate().setText(mail);
            mainView.getTextFieldRealNameUpdate().setText(fullname);
            mainView.getTextUserNameUpdate().setText(username);
            mainView.getPasswordFieldUpdate().setText(passwrd);
            mainView.getPasswordFieldUpdateCheck().setText(passwrd);
            enableEditSection(true);
            
        } catch (SQLException ex) {
            this.mainView.getTextAreaFehler().append("ERROR:\n" + ex.getLocalizedMessage() + "\n");
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
                this.mainView.getTextAreaFehler().append("Please fill in username and password.\n");
                return;
            }
            
             if (!passwd.equals(passwdchk)) {
                this.mainView.getTextAreaFehler().append("Passwords are not equal!\n");
                return;
            }
            
            
            String statement = "UPDATE users SET ";
            
            statement += "username='"+user+"',";
            statement += "surname=" + (name.length()==0 ? "NULL" : "'"+name+"'")+",";
            statement += "password='"+passwd+"',";
            statement += "email="+(email.length()==0 ? "NULL" : "'" +email+"'");
            
            statement += " WHERE id="+accountIDupdate+";";
           
            
            //Daten zur DB senden
            //Insert Username and Pw
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            
            this.mainView.getTextAreaFehler().append("--------------------\nSUCCESS\n--------------------\n");
            enableEditSection(false);
            
        } catch (SQLException ex) {
            this.mainView.getTextAreaFehler().append("ERROR:\n" + ex.getLocalizedMessage() + "\n");
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
            Logger.getLogger(MainControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
