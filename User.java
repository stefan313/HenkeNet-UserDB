/**
 * class User
 * 
 * User implements a single HenkeNet user account. 
 * 
 */

// temporary until implementation of dataLink
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author nick
 *
 */
public class User {

	public String username;
	public String password;
	public String surname;
	public String givenname;
	public String room;
	public String email;
	public String expirationDate;
	
	// this is only referring to a MySQL record id! 
	public int user_id;
	public boolean isModified;
	
	public User(String u, String p, String r, String s, String g, String e, String d) {
		username = u;
		password = p;
		surname = s;
		givenname = g;
		room = r;
		email = e;
		expirationDate = d;
		
		isModified = true;
	}

	public User(String uname) {
		// TODO: add getter function
	}
	
	public User(int uid) {
		// TODO: add getter function
	}
                
	public User setUID(int uid) {
		user_id = uid;
                return this;
                // fick-boeser hack
	}
	
    /**
     * Main search function. This function returns all users whose username,
     * roomnumber (or real name?) contains the given search text.
     * (This can also be implemented as constructor, but constructors normally
     * just initialize objects ;) )
     * @param connection The data base connection.
     * @param searchText Text from the search function.
     * @return A List containing all Users matching the specified criteria.
     */
    public static List<User> get(Connection connection, String searchText){
        
        LinkedList<User> ret = new LinkedList<User>();

        // Diese Funktion gibts unter
        // ArrayList<User> MySQLDataLink.lookupUser(String)
        //TODO
        return ret;
    }
        
	// synchronise user object with database

    /**
     *
     * @param connection
     * @throws SQLException
     */
	public void update(Connection connection) throws SQLException {
            String statement = prepareUpdateStatement();
            System.out.println("[SQL] " + statement + "\n");
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.executeUpdate();
            preparedStatement.close();	
        
		isModified = false;
	}

        
	public void insert(Connection connection) throws SQLException {
            String statement = prepareInsertStatement();
            System.out.println("[SQL] " + statement + "\n");
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.executeUpdate();
            preparedStatement.close();	
        
		isModified = false;
	}
        
        /**
         * Insert this user as new user into the database.
         * @param connection The data base connection.
         * @param t Transaction for this action. Must not be null.
         */
        public void insert(Connection connection, Transaction t){
            //TODO
            t.commit(connection);
        }

        /**
         * Extend the validity of an account to the given new expiration data.
         * @param connection The data base connection.
         * @param expyDate The new expiration date to be set.
         * @param t Transaction for this action. Must not be null.
         */
        public void extendValidity(Connection connection, String expyDate,
                Transaction t){
            //TODO
            t.commit(connection);
        }

        /**
         * Deletes a user from the Database, if uids are equal
         * @param connection The data base connection.
         * @param t Transaction. Can be null, if none occured. 
         *      (e.g. a user just moved out, ...)
         */
        public void delete(Connection connection, Transaction t){
            //TODO
            if(t!=null){
                t.commit(connection);
            }
        }
        
        //Private Helper Methods
	private String prepareInsertStatement() {
        String statement = "INSERT INTO users SET ";
        
        statement += "username='"+username+"',";
        statement += "password='"+password+"',";
        statement += "room='"+room+"',";
        statement += "surname='"+surname+"',";
        statement += "givenname='"+givenname+"',";
        statement += "email='"+email+"',";
        statement += "expiration_date='"+expirationDate+"';";
        
        return statement;
	}
	
	private String prepareUpdateStatement() {
        String statement = "UPDATE users SET ";
        
        statement += "username='"+username+"',";
        statement += "password='"+password+"',";
        statement += "room='"+room+"',";
        statement += "surname='"+surname+"',";
        statement += "givenname='"+givenname+"',";
        statement += "email='"+email+"',";
        
        statement += "expiration_date=" +
        		(expirationDate == null
        				? "NULL"
        				: "'" + expirationDate + "'"
        				);
        
        statement += " WHERE id="+user_id+";";
		
        return statement;
	}
}
