/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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

    /**
     * Controller class, which contais the programm logic of the tool.
     * This class handels the different forms and passes events to the database
     * which occur.
     */
    public String dbuser;

    //Strings set in initialisation
    private String keyStorePath, keyStorePassword,
            trustStorePath, trustStorePassword;

    //Static Content
    static private String configurationPath = "configuration.txt";

    //Forms
    private MainView mainView;
    private LoginForm loginView;

    private EditCreateForm ecView;
    private TransactionForm transactionView;

    private MySQLDataLink dataSource;
    private final static Logger LOG = Logger.getLogger("*");

    MainControl() {
        // DEBUG
        LOG.setLevel(Level.INFO);
        LOG.addHandler(new StatusBar());

        // Parses Required SSL Configuration, if it fails it prints it to stdout
        // TODO: refactor into Method
        InputStreamReader inputStreamReader = null;
        BufferedReader configBuffer = null;
        try {
            inputStreamReader = new InputStreamReader(
                    new FileInputStream(configurationPath));
            configBuffer = new BufferedReader(inputStreamReader);
            String line = configBuffer.readLine();
            while (line != null) {
                if (line.startsWith("keyStorePath=")) {
                    keyStorePath = line.substring(line.indexOf("=") + 1);
                } else if (line.startsWith("keyStorePassword=")) {
                    keyStorePassword = line.substring(line.indexOf("=") + 1);
                } else if (line.startsWith("trustStorePath=")) {
                    trustStorePath = line.substring(line.indexOf("=") + 1);
                } else if (line.startsWith("trustStorePassword=")) {
                    trustStorePassword = line.substring(line.indexOf("=") + 1);
                }

                line = configBuffer.readLine();
            }

        } catch (Exception e) {
            System.out.println("Config (SSL) parsing Failed! Exception: " + e.getMessage());
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (configBuffer != null) {
                    configBuffer.close();
                }
            } catch (Exception e) {
                System.err.println("Exception: " + e.getMessage());
            }
        }

        loginView = new LoginForm(this);
        loginView.setVisible(true);
    }

    /**
     *  Main Method. Start of program.
     *  @param args Command-Line args.
     */
    public static void main(String[] args) {
        new MainControl();
    }

    void trylogin() {
        //zur DB verbinden

        /*
        dataSource = new MySQLDataLink(
                "shelldon",
                "radius",
                loginView.getTxtUser().getText(),
                String.valueOf(loginView.getTxtPassword().getPassword())
        );
         */
        
        // TODO server name und datenbank name in die config auslagern! #gegenHardcode!
        
        //neuer Konstruktor initialize muss vorher ausgeführt werden!
	// ueberpruefung dass auch alles gesetzt wurde!
	if (!(trustStorePath == null || trustStorePassword == null))
	{
		if(keyStorePath == null || keyStorePassword == null)
		{
			// falls kein client key vorliegt
			dataSource = new MySQLDataLink("shelldon",
					"radius",
					loginView.getTxtUser().getText(),
					String.valueOf(loginView.getTxtPassword().getPassword()),
					trustStorePath,
					trustStorePassword);
		} else {
			// mit client keys
			dataSource = new MySQLDataLink("shelldon",
					"radius",
					loginView.getTxtUser().getText(),
					String.valueOf(loginView.getTxtPassword().getPassword()),
					keyStorePath,
					keyStorePassword,
					trustStorePath,
					trustStorePassword);
		}
	} else {
		//		geh kaputt
		LOG.severe("Faulty Config");
		System.exit(1);
	}

	if (!(dataSource.connect())) {
		LOG.severe("Failed to connect.");
		return;
	}

	dbuser = loginView.getTxtUser().getText();
	loginView.setVisible(false);
	mainView = new MainView(this);
	mainView.setVisible(true);

    }
    /**
     * Disable main view and show the edit/create Form.
     * This is the first step of Creating / Deleting an account.
     * @param selected The User currently selected, can be null.
     */
    void showECForm(User selected) {
	    this.mainView.setEnabled(false);
	    this.ecView = new EditCreateForm(this, selected);
	    this.ecView.setVisible(true);
    }

    List<User> doSearch(String searchText) {
	    return dataSource.lookupUser(searchText);
    }
    
    /**
     * Init a creation.
     * Show the Transaction view and disable main.
     * @param u The User-Object with data.
     */
    void initCreate(User u) {
	    //Transaktionsfenster öffnen
	    mainView.setEnabled(false);
	    transactionView = new TransactionForm(this, TransactionForm.TransactionType.CREATE,
			    u, dbuser);
	    transactionView.setVisible(true);
    }
    
    /**
     * Create a new Account.    
     * @param u User containing account data.
     * @param comment Comment on the action
     * @param amount Amount received in Cents.
     */
    void commitCreate(User u, String comment, int amount) {
	    //Daten zur DB senden
	    //Insert Username and Pw
	    if (dataSource.insert(u, comment, amount) != -1) {
		    LOG.info("[SUCCESS] Added user '" + u.getUsername() + "'");
	    } else {
		    LOG.log(Level.SEVERE, "[FAIL] Failed to insert user '" + u.getUsername() + "'");
	    }
	    enableMain();
    }
    
    /**
     * Init an update action.
     * Show the Update Form and disable the Main View.
     * @param u The User to update.
     */
    void initUpdate(User u) {
	    mainView.setEnabled(false);
	    transactionView = new TransactionForm(this, TransactionForm.TransactionType.UPDATE,
			    u, dbuser);
	    transactionView.setVisible(true);

    }
    /**
     * Update a User record.    
     * @param u The User to update. 
     * @param comment Comment on the Action
     * @param amount Amount received in cents.
     */
    void commitUpdate(User u, String comment, int amount) {
	    if (dataSource.update(u, comment, amount) != -1) {
		    LOG.info("[SUCCESS] Updated user '" + u.getUsername() + "'");
	    } else {
		    LOG.log(Level.SEVERE, "[FAIL] Failed to update user '" + u.getUsername() + "'");
	    }
	    enableMain();
    }
    /** 
     * Start a deleting action.
     * Show Transaction Form and disable Main Form.
     * @param u The User to be deleted.
     */
    void initDelete(User u) {
	    mainView.setEnabled(false);
	    transactionView = new TransactionForm(this, TransactionForm.TransactionType.DELETE,
			    u, dbuser);
	    transactionView.setVisible(true);
    }
    /**
     * Delete a user record.
     * @param u The User to be deleted.
     * @param comment Comment on the action.
     * @param amount Amount received in Cents.
     */
    void commitDelete(User u, String comment, int amount) {
	    dataSource.delete(u, comment, amount);
	    LOG.info("[SUCCESS] Deleted user '" + u.getUsername() + "'");
	    enableMain();
    }
    /**
     * Start an extending action.
     * Shows the transaction form and disables the Main View.
     * @param u The User to be extended.
     */
    void initExtend(User u) {
	    mainView.setEnabled(false);
	    transactionView = new TransactionForm(this, TransactionForm.TransactionType.EXTEND_VALIDITY,
			    u, dbuser);
	    transactionView.setVisible(true);
    }
    /**
     * Extend a users validity.
     * @param u The user.
     * @param comment Comment to the action.
     * @param amount Money recieved in cents.
     */
    void commitExtend(User u, String comment, int amount) {
	    if (dataSource.update(u, comment, amount) != -1) {
		    LOG.info("[SUCCESS] Extended user '" + u.getUsername() + "'");
	    } else {
		    LOG.log(Level.SEVERE, "[FAIL] Failed to extend user '" + u.getUsername() + "'");
	    }
	    enableMain();
    }
    /**
     * Shows the History View Form.
     * @param u The selected user.
     */
    void showTransactionHistory(User u) {
	    new TransactionHistoryView(dataSource, u).setVisible(true);
    }

    /**
     * Closes the connection to the data source.
     */
    public void closeConn() {
	    dataSource.disconnect();
    }

    /**
     * Re-Enables the Main view. (e.g. after a sub-form is closed and
     * Control is passed back to Main.
     */
    public void enableMain() {
	    this.mainView.setEnabled(true);
	    mainView.setVisible(true);
	    mainView.setState(Frame.NORMAL);
	    mainView.updateBrowserView();
    }

    /**
     * Get next expiration date for "Extend Validity".
     * From January to March this will be Apr 30 in the same year.
     * From April to September this will be Oct 31 in the same year.
     * From October to December this will be Apr 30 next year.
     * @return String representation of the next expiration date.
     */
    public String getNextExpDate() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        String ret;
        //Sommersemester, validate until october
        if (month >= 3 && month <= 8) {
            ret = c.get(Calendar.YEAR) + "-10-31";
        } else {
            //Wintersemester, valdiate until april
            if(month > 8){
                ret = (c.get(Calendar.YEAR) + 1) + "-04-30";
            } else {
                ret = (c.get(Calendar.YEAR)) + "-04-30";
            }
        }
        return ret;
    }

     /**
     * Get the first expiration date for a newly create account.
     * In month between March and Aug. it is set to Apr 30.
     * Else to Oct. 31.
     * @return The expiration date
     */
    public String getThisExpDate(){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        String ret;
        //Sommersemester, set to april.
        if (month >= 2 && month <= 7) {
            ret = c.get(Calendar.YEAR) + "-04-31";
        } else {
            //Wintersemester, set to october.
            if(month > 7){
                ret = (c.get(Calendar.YEAR)) + "-10-31";
            } else {
                //Last years october
                ret = (c.get(Calendar.YEAR)-1) + "-10-31";
            }
        }
        return ret;
    }
    
    /**
     * Status Bar down on the Main Form.
     */
    public class StatusBar extends StreamHandler {
        /**
         * Publish a logging message (show it on the status bar)
         * @param rec Message to show.
         */
        public void publish(LogRecord rec) {
            if (mainView != null) {
                mainView.getStatusBar().setText(rec.getMessage());
                if (rec.getLevel().intValue() >= Level.SEVERE.intValue()) {
                    mainView.getStatusBar().setForeground(Color.RED);
                } else {
                    mainView.getStatusBar().setForeground(Color.BLACK);
                }
            }

            if (loginView != null) {
                loginView.getLblStatusBar().setText("Try again.");
                // message unhelpful. try again.
            }

        }
    }
}
