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
	private int user_id;
	private boolean isModified;
	
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
	
	public void setUID(int uid) {
		user_id = uid;
	}
	
	// synchronise user object with database
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
