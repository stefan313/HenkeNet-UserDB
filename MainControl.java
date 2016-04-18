/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Frame;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import java.util.logging.LogRecord;
import java.util.List;


/**
 *
 * @author Stefan, Tobias
 */
public class MainControl {
    
    public String dbuser;
    
    //Forms
    private MainView mainView;
    private LoginForm loginView;
    
    private EditCreateForm ecView;
    private TransactionForm transactionView;
    
    private MySQLDataLink dataSource;
    private final static Logger LOG = Logger.getLogger("*");

    
    MainControl(){
        // DEBUG
        LOG.setLevel(Level.INFO);
        LOG.addHandler(new StatusBar());
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
        
        if (!(dataSource.connect())){
            LOG.severe("Failed to connect.");
            return;
        }
        
        dbuser = loginView.getTxtUser().getText();
        loginView.setVisible(false);
        mainView = new MainView(this);
        mainView.setVisible(true);

    }
    
    void showECForm(User selected){
        this.mainView.setEnabled(false);
        this.ecView = new EditCreateForm(this, selected);
        this.ecView.setVisible(true);
    }
    
    List<User> doSearch(String searchText){
        return dataSource.lookupUser(searchText);
    }
    void initCreate(User u) {
            //Transaktionsfenster öffnen
            mainView.setEnabled(false);
            transactionView = new TransactionForm(this, TransactionForm.TransactionType.CREATE,  
                    u, dbuser);
            transactionView.setVisible(true);
    }
    
    void commitCreate(User u, String comment, int amount) {
        //Daten zur DB senden
        //Insert Username and Pw
        dataSource.insert(u, comment, amount);
        LOG.info("[SUCCESS] Added user '" + u.username + "'");
        mainView.setEnabled(true);
        
    }

    
    void initUpdate(User u) {
        mainView.setEnabled(false);
        transactionView = new TransactionForm(this, TransactionForm.TransactionType.UPDATE,  
                u, dbuser);
        transactionView.setVisible(true);
        
    }
    
    void commitUpdate(User u, String comment, int amount) {
        dataSource.update(u, comment, amount);
        LOG.info("[SUCCESS] Updated user '" + u.username + "'");
        enableMain();  
    }
    
    void initDelete(User u) {
        mainView.setEnabled(false);
        transactionView = new TransactionForm(this, TransactionForm.TransactionType.DELETE,  
                u, dbuser);
        transactionView.setVisible(true);
    }
    
    void commitDelete(User u, String comment, int amount) {
        dataSource.delete(u, comment, amount);
        LOG.info("[SUCCESS] Deleted user '" + u.username + "'");
        enableMain();  
    }
    
     void initExtend(User u) {
        mainView.setEnabled(false);
        transactionView = new TransactionForm(this, TransactionForm.TransactionType.EXTEND_VALIDITY,  
                u, dbuser);
        transactionView.setVisible(true);
    }
    
    void commitExtend(User u, String comment, int amount) {
        dataSource.update(u, comment, amount);
        LOG.info("[SUCCESS] Extended validity of user '" + u.username + "'");
        enableMain();
    }
    
    public void closeConn(){
        dataSource.disconnect();
    }

    public void enableMain(){
        this.mainView.setEnabled(true);
        mainView.setVisible(true);
        mainView.setState(Frame.NORMAL);
    }
    

    
    public String getNextExpDate(){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        String ret;
        //Sommersemester, validate until october
        if(month>=3 && month <= 9){
            ret = c.get(Calendar.YEAR) +"-10-31";
        }else{
            if(month <3){
                ret = c.get(Calendar.YEAR) +"-04-31";
            }else{
                ret = (c.get(Calendar.YEAR)+1) +"-04-31";
            } 
        }
        
        return ret;    
    }
    
    public class StatusBar extends StreamHandler {
        public void publish(LogRecord rec) {
            if(mainView!=null){
                mainView.getStatusBar().setText(rec.getMessage());
                if (rec.getLevel().intValue() >= Level.SEVERE.intValue())
                mainView.getStatusBar().setForeground(Color.RED);
            else
                mainView.getStatusBar().setForeground(Color.BLACK);
            }
            
            if(loginView!=null){
                loginView.getLblStatusBar().setText("Try again.");
            }
            
        }
    }
        
    
}
