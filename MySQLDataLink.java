
import java.util.ArrayList;

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
        // STUB
        return -1;
    }
    public boolean update(User user) {
        // STUB
        return false;
    }
    
    public User getUser(String username) {
        // STUB
        return null;
    }
    public User getUser(int uid) {
        // STUB
        return null;
    }
    
    public ArrayList<User> lookupUser(String anyKey) {
        return null;
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
}
