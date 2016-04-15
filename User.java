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
}
