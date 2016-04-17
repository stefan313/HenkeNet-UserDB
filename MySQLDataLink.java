/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nick
 */

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.*;

public class MySQLDataLink implements DataLink {
    
    private MysqlDataSource db;
    private Connection link;
    
    public MySQLDataLink(String server, String dbname, String user, String pw) {
        db = new MysqlDataSource();
        db.setServerName(server);
        db.setDatabaseName(dbname);
        db.setUser(user);
        db.setPassword(pw);
    }
    
    public boolean connect() {
        try {
            link = db.getConnection();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(MySQLDataLink.class.getName()).log(Level.SEVERE, "[SQL][FAIL] Failed to connect.", e);
            return false;
        }
    }
    
    public int insert(User user) {
        try { // or dont..
            String statement = prepareInsertStatement(user);
            System.out.println("[SQL] " + statement + "\n");
            PreparedStatement preparedStatement = link.prepareStatement(statement);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 1; // FIX: return new uid!
        } catch (SQLException e) {
            System.out.println("And Fail.");
            return -1337;
        }
    }
    public int insert(User user, String comment, int amountReceivedInCents) {
        // TODO
        return -1;
    }
    public int insert(User user, String comment) {
        return insert(user, comment, 0);
    }
    public int update(User user) {
        try { // motherfucker
	String statement = prepareUpdateStatement(user);
	System.out.println("[SQL] " + statement + "\n");
        PreparedStatement preparedStatement = link.prepareStatement(statement);
        preparedStatement.executeUpdate();
        preparedStatement.close();	
            return user.getUID();
            // heh xD
        }catch (SQLException e) {
            System.out.println ("And Fail.");
            return (!(user.isModified = true));
            // heh xD
        }
    }
    
    public int update(User user, String comment) {
        return update(user, comment, 0);
    }
    
    public int update(User user, String comment, int amountReceivedInCents) {
        // TODO
        return -1;
    }
    
    public User getUser(String username) {
        // STUB
        return null;
    }
    public User getUser(int uid) {
        // STUB
        return null;
    }
    
    @Override
    public ArrayList<User> lookupUser(String anyKey) {
        String statement = "SELECT * FROM users WHERE username LIKE '%"
                + anyKey + "%' LIMIT 100;";
        try {
        PreparedStatement search = link.prepareStatement(statement);
        ResultSet result = search.executeQuery();
        
        ArrayList<User> foundUsers = new ArrayList<>();
        while (result.next())
            foundUsers.add(
                    new User(
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("room"),
                            result.getString("surname"),
                            result.getString("givenname"),
                            result.getString("email"),
                            result.getString("expiration_date")
                    ).setUID(Integer.parseInt(result.getString("id")))
            );
        return foundUsers;
        } catch (SQLException e) {
            System.out.println("[SQL][FAIL]" + e.getMessage());
            return null;
        }
    }
    
    // AUA GANZ BOESE KACKE
    public Connection getConnection() {
        return link;
    }
    
    public void disconnect() {
        try {
            link.close();
        } catch (SQLException e) {
            Logger.getLogger(MySQLDataLink.class.getName()).log(Level.SEVERE, "[SQL][FAIL] Failed to terminate connection.", e);
        }
    }
    	
	
	
	private String prepareInsertStatement(User u) {
        String statement = "INSERT INTO users SET ";
        
        statement += "username='"+u.username+"',";
        statement += "password='"+u.password+"',";
        statement += "room='"+u.room+"',";
        statement += "surname='"+u.surname+"',";
        statement += "givenname='"+u.givenname+"',";
        statement += "email='"+u.email+"',";
        statement += "expiration_date='"+u.expirationDate+"';";
        
        return statement;
	}
	
	private String prepareUpdateStatement(User u) {
        String statement = "UPDATE users SET ";
        
        statement += "username='"+u.username+"',";
        statement += "password='"+u.password+"',";
        statement += "room='"+u.room+"',";
        statement += "surname='"+u.surname+"',";
        statement += "givenname='"+u.givenname+"',";
        statement += "email='"+u.email+"',";
        
        statement += "expiration_date=" +
        		(u.expirationDate == null
        				? "NULL"
        				: "'" + u.expirationDate + "'"
        				);
        
        statement += " WHERE id="+u.user_id+";";
		
        return statement;
	}
}
