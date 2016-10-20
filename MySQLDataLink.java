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
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.*;

public class MySQLDataLink implements DataLink {

    // zusätzlicher contraint, weil lol
    final private MysqlDataSource db;
    private Connection link;
    private PreparedStatement userBrowser;

    private final static Logger LOG = Logger.getLogger("*");

    /**
     * *
     * Beschreibt einen DataLink (ohne!! RequireSSL und UseSSL)
     *
     * @param server der benutzte Server
     * @param dbname die benutze Datenbank
     * @param user der username
     * @param pw des usernames passwort
     */
    @Deprecated
    public MySQLDataLink(String server, String dbname, String user, String pw) {
        db = new MysqlDataSource();
        db.setServerName(server);
        db.setDatabaseName(dbname);
        db.setUser(user);
        db.setPassword(pw);
        // sollte auch ohne jegliche truststores funktionieren --> TODO test! #13

        db.setRequireSSL(true);
        db.setUseSSL(true);
    }

    /**
     * *
     *
     * Mit einem KeyStore des Benutzers
     * @param server der benutzte Server
     * @param dbname die benutze Datenbank
     * @param user der username
     * @param pw des usernames passwort
     * @param keyStorePath der lokal erzeugte Keystore mit dem privaten
     * Schlüssel des Users
     * @param keyStorePassword das Passwort dazu
     * @param trustStorePath der mitgelieferte trust store mit dem SSL Cert des
     * Servers
     * @param trustStorePassword das Passwort dazu
     */
    public MySQLDataLink( String server,  String dbname,  String user,
             String pw,  String keyStorePath,  String keyStorePassword,
             String trustStorePath,  String trustStorePassword) {
        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
        db = new MysqlDataSource();
        db.setServerName(server);
        db.setDatabaseName(dbname);
        db.setUser(user);
        db.setPassword(pw);
        db.setRequireSSL(true);
        db.setUseSSL(true);
    }
    
    /**
     * *
     *
     * Enthält keine KeyStores!!! SSL cert vom server aus only
     * @param server der benutzte Server
     * @param dbname die benutze Datenbank
     * @param user der username
     * @param pw des usernames passwort
     * @param trustStorePath der mitgelieferte trust store mit dem SSL Cert des
     * Servers
     * @param trustStorePassword das Passwort dazu
     */
    public MySQLDataLink( String server,  String dbname,  String user,
             String pw,
             String trustStorePath,  String trustStorePassword) {
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
        db = new MysqlDataSource();
        db.setServerName(server);
        db.setDatabaseName(dbname);
        db.setUser(user);
        db.setPassword(pw);
        db.setRequireSSL(true);
        db.setUseSSL(true);
    }

    public boolean connect() {
        try {
            link = db.getConnection();
            return true;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SQL] Failed to connect.", e);
            return false;
        }
    }

    public void disconnect() {
        try {
            link.close();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SQL] Failed to terminate connection.", e);
        }
    }

    public int insert(User user) {
        String sql = prepareInsertStatement(user);
        try (Statement statement = link.createStatement()) {
            LOG.info("[SQL] << " + sql);
            statement.executeUpdate(sql);

            // get id of inserted user
            ResultSet result = statement.executeQuery(sql = "SELECT LAST_INSERT_ID();");
            result.next();
            // TODO bei so dirty hacks bitte ein bisschen kommentar funktion benutzen!!!!
            user.setUser_id(result.getInt(1));

            statement.close();
            return user.getUser_id();

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SQL] + Failed to insert user '" + user.getUsername()
                    + "'\n" + "[SQL] | Error message: '" + e.getLocalizedMessage()
                    + "'\n" + "[SQL] | Statement: '" + sql + "'");
            return -1;
        }
    }

    public int insert(User user, String comment) {
        return insert(user, comment, 0);
    }

    public int insert(User user, String comment, int amountReceivedInCents) {
        user.setUser_id(insert(user));
        if (!commitTransaction(new Transaction(user, amountReceivedInCents, comment))) {
            return -1;
        }
        return user.getUser_id();
    }

    public int update(User user) {
        String sql = prepareUpdateStatement(user);
        try (Statement statement = link.createStatement()) {
            LOG.info("[SQL] << " + sql);
            statement.executeUpdate(sql);
            statement.close();
            return user.getUser_id();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SQL] + Failed to update user '" + user.getUsername()
                    + "'\n" + "[SQL] | Error message: '" + e.getLocalizedMessage()
                    + "'\n" + "[SQL] | Statement: '" + sql + "'");
            return -1;
        }
    }
   
    public int update(User user, String comment) {
        return update(user, comment, 0);
    }
    
    public int update(User user, String comment, int amountReceivedInCents) {
        if (!commitTransaction(new Transaction(user, amountReceivedInCents, comment))) {
            return -1;
        }
        return update(user);
    }

    public boolean commitTransaction(Transaction t) {
        if (t.getAccount().getUser_id() == 0) {
            return false;
        }
        String sql = prepareTransactionStatement(t);
        LOG.info("[SQL] << " + sql);
        try (Statement statement = link.createStatement()) {
            statement.executeUpdate(sql);
            statement.close();
            return true;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SQL] + Failed to commit transaction (" + t.getAccount().getUsername()
                    + ", " + t.getAmountPaid() + ", '" + t.getDescription()
                    + "'\n" + "[SQL] | Error message: '" + e.getLocalizedMessage()
                    + "'\n" + "[SQL] | Statement: '" + sql + "'");
            return false;
        }
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
        try {
            if (userBrowser == null) {
                initBrowser();
            }
            ResultSet result = queryBrowser(anyKey);
            ArrayList<User> foundUsers = new ArrayList<>();
            while (result.next()) {
                foundUsers.add(
                        new User(
                                Integer.parseInt(result.getString("id")),
                                result.getString("username"),
                                "",
                                result.getString("room"),
                                result.getString("surname"),
                                result.getString("givenname"),
                                result.getString("email"),
                                result.getString("expiration_date")
                        )
                );
            }
            return foundUsers;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SQL][FAIL]" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ArrayList<Transaction> lookupTransactions(User user) {
        try {
            Statement st = link.createStatement();
            ResultSet result = st.executeQuery(prepareLookupTransactionStatement(user));
            ArrayList<Transaction> foundTransactions = new ArrayList<>();
            while (result.next()) {
                foundTransactions.add(
                        new Transaction(
                                result.getString("operator").replaceAll("@%", ""),
                                user,
                                result.getInt("amount_paid"),
                                result.getString("comment"),
                                result.getString("timestamp")));
            }
            return foundTransactions;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SQL][FAIL]" + e.getMessage(), e);
            return null;
        }
    }

    public boolean delete(User user) {
        String sql = prepareDeleteStatement(user);
        try (Statement state = link.createStatement()) {
            // returns ob state wie erwarted war //TODO erlaeutern
            return state.executeUpdate(sql) == 1;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SQL] + Failed to delete user '" + user.getUsername()
                    + "'\n" + "[SQL] | Error message: '" + e.getLocalizedMessage()
                    + "'\n" + "[SQL] | Statement: '" + sql + "'");
            return false;
        }
    }

    public boolean delete(User user, String comment) {
        return delete(user, comment, 0);
    }

    public boolean delete(User user, String comment, int amountReceivedInCents) {
        // returns ob beide Aktionen erfolgreich waren
        return commitTransaction(new Transaction(user, amountReceivedInCents, comment))
                && delete(user);
    }

    private boolean initBrowser() throws SQLException {
        // TODO: TEST
        this.userBrowser = link.prepareStatement("SELECT id, username, room, surname, givenname, email, expiration_date FROM `users` WHERE"
                + "`username` LIKE ? OR `room` LIKE ? OR `surname` LIKE ? "
                + "OR `givenname` LIKE ? ORDER BY `username` ASC;");
        return true;
    }

    private ResultSet queryBrowser(String anyKey) throws SQLException {
        userBrowser.setString(1, "%" + anyKey + "%");
        userBrowser.setString(2, "%" + anyKey + "%");
        userBrowser.setString(3, "%" + anyKey + "%");
        userBrowser.setString(4, "%" + anyKey + "%");
        return userBrowser.executeQuery();
    }

    private String prepareInsertStatement(User u) {
        String statement = "INSERT INTO users SET ";

        statement += "username='" + u.getUsername() + "',";
        // passwordChanged() ist wichtig ansonsten liefert getPassword nur null aus!!!!
        if(u.passwordChanged())
        {
            statement += "password='" + u.getPassword() + "',";
        }
        statement += "room='" + u.getRoom() + "',";
        statement += "surname='" + u.getSurname() + "',";
        statement += "givenname='" + u.getGivenname() + "',";
        statement += "email='" + u.getEmail() + "',";
        statement += "expiration_date='" + u.getExpirationDate() + "';";

        return statement;
    }

    private String prepareUpdateStatement(User u) {
        String statement = "UPDATE users SET ";

        /*statement += "username='" + u.username + "',";
        statement += "password='" + u.password + "',";
        statement += "room='" + u.room + "',";
        statement += "surname='" + u.surname + "',";
        statement += "givenname='" + u.givenname + "',";
        statement += "email='" + u.email + "',";
*/
        statement += "username='" + u.getUsername() + "',";
        
        // passwordChanged() ist wichtig ansonsten liefert getPassword nur null aus!!!!
        if(u.passwordChanged())
        {
            statement += "password='" + u.getPassword() + "',";
        }
        statement += "room='" + u.getRoom() + "',";
        statement += "surname='" + u.getSurname() + "',";
        statement += "givenname='" + u.getGivenname() + "',";
        statement += "email='" + u.getEmail() + "',";
        
        statement += "expiration_date="
                + (u.getExpirationDate().isEmpty()
                        ? "NULL"
                        : "'" + u.getExpirationDate() + "'");

        statement += " WHERE id=" + u.getUser_id() + ";";

        return statement;
    }

    private String prepareDeleteStatement(User user) {
        /*
         * now, we absolutely NEED TO ASSURE, that the given user 
         * id is unique, so as not to losemore data than intended.
         * since `users`.`id` is PK/UNIQ/AI, a WHERE `id` = '%id'
         * should suffice.
         */

        return "DELETE FROM `users` WHERE `id` = '" + user.getUser_id() + "'";
    }

    private String prepareTransactionStatement(Transaction t) {
        String statement = "INSERT INTO `transactions` ";
        statement += "(`operator`, `user`, `username`, `amount_paid`, `comment`) VALUES ";
        statement += "(CURRENT_USER(), '" + t.getAccount().getUser_id() + "', '"
                + t.getAccount().getUsername() + "', '" + t.getAmountPaid() + "', '"
                + t.getDescription() + "');";
        return statement;
    }

    private String prepareLookupTransactionStatement(User user) {
        // returns this lookup statement string on userid
        return "SELECT * FROM `transactions` WHERE user=" +
                user.getUser_id() + " ORDER BY `timestamp` DESC;";
    }

}
