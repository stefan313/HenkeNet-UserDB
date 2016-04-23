
import java.util.logging.*;



/**
 * Data structure storing Transaction Data.
 * @author Tobias
 */
public class Transaction {
    private final String dbuser;
    private final int amountPaid;
    private final String description;
    private final String date;
    private final User account;
    
    private final static Logger LOG = Logger.getLogger("*"); 

    /**
     * Create new Transaction.
     * @param dbuser The database user responsible for the transaction.
     * @param account The account affected by this transaction.
     * @param amountPaid Amount in EuroCENT payed.
     * @param description Verbal description.
     * @param date The date when this transaction was executed.
     */
    public Transaction(String dbuser, User account, int amountPaid, String description, String date) {        
        this.dbuser = dbuser;
        this.account = account;
        this.amountPaid = amountPaid;
        this.description = description;
        this.date = date;
    }
    
    public Transaction(User account, int amountPaid, String description) {
        this.account = account;
        this.amountPaid = amountPaid;
        this.description = description;
        this.dbuser = null;
        this.date = null;
    }

    public String getDbUser() {
        if (this.date == null) return "";
        return dbuser;
    }
    
    public User getAccount() {
        return account;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        if (this.date == null) return "";
        return date;
    }
    
    /**
     * Commit the Transaction using the specified connection.
     * @param dataLink The database link to use.
     * @return True, if the connection was executed successfully. False if an error occurred.
     * 
     * commit() only calls the corresponding method in dataLink, which may
     * or may not ignore fields such as 'dbuser' or 'date'.
     * (MySQLDataLink always ignores 'dbuser' and 'timestamp')
     */
    public boolean commit(DataLink dataLink){
        return dataLink.commitTransaction(this);
    }
    
}
