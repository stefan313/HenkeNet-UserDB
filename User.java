/**
 * class User
 *
 * User implements a single HenkeNet user account.
 *
 */

/**
 * @author nick
 *
 */
public class User {

    private String username, password, surname, givenname, room, email, expirationDate;

    private int user_id;
    public boolean isModified;
    private boolean passwordChanged = false;


    /***
     * Erstellt einene neuen Nutzer
     * @param username Required
     * @param password Nullable
     * @param room
     * @param surname
     * @param givenName
     * @param email
     * @param expirationDate 
     */
    public User(String username, String password, String room, String surname,
            String givenName, String email, String expirationDate) {
        this.username = username;
        this.password = password;
        this.surname = surname;
        this.givenname = givenName;
        this.room = room;
        this.email = email;
        this.expirationDate = expirationDate;

        isModified = true;
    }

    /***
     * Bitte nicht mehr Verwenden
     * @param userID
     * @param username
     * @param password
     * @param room
     * @param surname
     * @param givenName
     * @param email
     * @param expirationDate
     * @deprecated
     */
    @Deprecated
    public User(int userID, String username, String password, String room,
            String surname, String givenName, String email, String expirationDate) {
        // "User" instances with a given id refer to ones already existing
        // in the DB, hence the usual constructor is called and afterwards
        // isModified is set to false.

        //ARGGHH, NICK!
        // kann ich ja nich wissen, dass ich keine anderen konstruktoren
        // aufrufen kann.
        // Java ist einfach scheisse! nehmen wir C++?
        this.username = username;
        this.password = password;
        this.surname = surname;
        this.givenname = givenName;
        this.room = room;
        this.email = email;
        this.expirationDate = expirationDate;
        this.user_id = userID;
        isModified = false;
    }

    /**
     *
     * @param dataLink
     */
    public void update(DataLink dataLink) {
        dataLink.update(this);
        isModified = false;
    }

    /*
     * @param dataLink   
     */
    public void insert(DataLink dataLink) {
        dataLink.insert(this);
        isModified = false;
    }

    /**
     * Insert this user as new user into the database.
     *
     * @param dataLink The data base connection.
     * @param t Transaction for this action. Must not be null.
     */
    public void insert(DataLink dataLink, Transaction t) {
        dataLink.insert(this);
        t.commit(dataLink);
    }

    /**
     * Extend the validity of an account to the given new expiration data.
     *
     * @param dataLink The data base connection.
     * @param expirationDate The new expiration date to be set.
     * @param t Transaction for this action. Must not be null.
     */
    public void extendValidity(DataLink dataLink, String expirationDate,
            Transaction t) {
        // TODO: sanitize data (expirationDate)
        this.expirationDate = expirationDate;
        this.update(dataLink);
        t.commit(dataLink);
    }

    /**
     * Deletes a user from the Database, if uids are equal
     *
     * @param dataLink The data base connection.
     * @param t Transaction. Can be null, if none occurred. (e.g. a user just
     * moved out, ...)
     */
    public void delete(DataLink dataLink, Transaction t) {
        dataLink.delete(this);
        if (t != null) {
            t.commit(dataLink);
        }
    }

    /*
    
    //vorsicht !!! kaputt!!!!
    //Private Helper Methods
    @Deprecated
    private String prepareInsertStatement() {
        String statement = "INSERT INTO users SET ";

        statement += "username='" + username + "',";
        statement += "password='" + password + "',";
        statement += "room='" + room + "',";
        statement += "surname='" + surname + "',";
        statement += "givenname='" + givenname + "',";
        statement += "email='" + email + "',";
        statement += "expiration_date='" + expirationDate + "';";

        return statement;
    }

    @Deprecated
    private String prepareUpdateStatement() {
        String statement = "UPDATE users SET ";

        statement += "username='" + username + "',";
        statement += "password='" + password + "',";
        statement += "room='" + room + "',";
        statement += "surname='" + surname + "',";
        statement += "givenname='" + givenname + "',";
        statement += "email='" + email + "',";

        statement += "expiration_date="
                + (expirationDate == null
                        ? "NULL"
                        : "'" + expirationDate + "'");

        statement += " WHERE id=" + user_id + ";";

        return statement;
    }
*/

    /***
     * Getter for Field
     * @return 
     */
    public boolean passwordChanged() {
       return this.passwordChanged;
    }
    
    /***
     * Getter for Field
     * @return 
     */
    public String getPassword() {
        if(this.passwordChanged)
            return this.password;
        else return null;
    }
    
    /***
     * Getter for Field
     * @return 
     */
    public String getUsername() {
       return this.username;
    }
    
    /***
     * Setter for Field
     * TODO SCHMERZ (na ja egal)
     */
    public void setUser_id(int user_id) {
       this.user_id = user_id;
    }

    /***
     * Setter for Field
     * @return 
     */
    public void setSurname(String surname) {
        this.surname = surname;
        this.isModified = true;
    }

    /***
     * Setter for Field
     * @return 
     */
    public void setGivenname(String givenname) {
        this.givenname = givenname;
        this.isModified = true;
    }

    /***
     * Setter for Field
     * @return 
     */
    public void setRoom(String room) {
        this.room = room;
        this.isModified = true;
    }

    /***
     * Setter for Field
     * @return 
     */
    public void setEmail(String email) {
        this.email = email;
        this.isModified = true;
    }

    /***
     * Setter for Field
     * @return 
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        this.isModified = true;
    }
    
    /***
     * Setter for Field
     * @return 
     */
    public void setPassword(String password) {
        this.password = password;
        this.isModified = true;
        this.passwordChanged = true;
    }

    /***
     * Getter for Field
     * @return 
     */
    public String getSurname() {
        return surname;
    }

    /***
     * Getter for Field
     * @return 
     */
    public String getGivenname() {
        return givenname;
    }

    /***
     * Getter for Field
     * @return 
     */
    public String getRoom() {
        return room;
    }

    /***
     * Getter for Field
     * @return 
     */
    public String getEmail() {
        return email;
    }

    /***
     * Getter for Field
     * @return 
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /***
     * Getter for Field
     * @return 
     */
    public int getUser_id() {
        return user_id;
    }

    /***
     * Getter for Field
     * @return 
     */
    public boolean isIsModified() {
        return isModified;
    }
}
