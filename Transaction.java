
import java.sql.Connection;



/**
 * Data structure storing Transaction Data.
 * @author Tobias
 */
public class Transaction {
    private String dbuser;
    private int amountPayed;
    private String description;
    private String date;
    private User account;

    /**
     * Create a new Trancaction.
     * @param dbuser The database user responsible for the transaction.
     * @param account The account affected by this transaction.
     * @param amountPayed Amount in EuroCENT payed.
     * @param description Verbal description.
     * @param date The date when this transaction was executed.
     */
    public Transaction(String dbuser, User account, int amountPayed, String description, String date) {
        this.dbuser = dbuser;
        this.account = account;
        this.amountPayed = amountPayed;
        this.description = description;
        this.date = date;
    }
    
    //Getters

    public String getDbUser() {
        return dbuser;
    }
    
    public User getAccount() {
        return account;
    }

    public int getAmountPayed() {
        return amountPayed;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
    
    /**
     * Commit the Transaction using the specified connection.
     * @param connection The database link to use.
     * @return True, if the connection was executed successfully. False if an error occured.
     */
    public boolean commit(Connection connection){
        //TODO
        return true;
    }
    
}
