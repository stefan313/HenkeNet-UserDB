
import java.sql.Connection;



/**
 * Data structure storing Transaction Data.
 * @author Tobias
 */
public class Transaction {
    private String user;
    private int amountPayed;
    private String description;
    private String date;

    /**
     * Create a new Trancaction.
     * @param user The database user responsible for the Transaction
     * @param amountPayed Amount in EuroCENT payed.
     * @param description Verbal description.
     * @param date The date when this transaction was executed.
     */
    public Transaction(String user, int amountPayed, String description, String date) {
        this.user = user;
        this.amountPayed = amountPayed;
        this.description = description;
        this.date = date;
    }
    
    //Getters

    public String getUser() {
        return user;
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
