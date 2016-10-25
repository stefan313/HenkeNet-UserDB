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
     *
     * TODO bitte wissen was dieser konstruktor macht? keine user id? (fehlen des wichtigsten primary keys
     * Erstellt einene neuen Nutzer
     * @param username Required
     * @param password Nullable
     * @param room Raumnummer (Required)
     * @param surname Nachname
     * @param givenName Vorname
     * @param email Mailaddr (nullable)
     * @param expirationDate Date for password Expiration, required
     */
    @Deprecated
    public User(String username, /*String password,*/ String room, String surname,
            String givenName, String email, String expirationDate) {
        this.username = username;
        //this.password = password;
        this.surname = surname;
        this.givenname = givenName;
        this.room = room;
        this.email = email;
        this.expirationDate = expirationDate;

        isModified = true;
    }

    /***
     * Bitte nicht mehr Verwenden
     * @param userID wichtiger primary key, nicht final, aber vermutlich broken wenn geschrieben
     * @param username unique, nicht nullable
     * @param password nullable falls nicht neu gesetzt
     * @param room schluessel kandidat, not null
     * @param surname nachname
     * @param givenName vorname
     * @param email mail, nullable
     * @param expirationDate Date for password Expiration, required
     * @deprecated
     */
    @Deprecated
    public User(int userID, String username, /*String password, */ String room,
            String surname, String givenName, String email, String expirationDate) {
        // "User" instances with a given id refer to ones already existing
        // in the DB, hence the usual constructor is called and afterwards
        // isModified is set to false.

        //ARGGHH, NICK!
        // kann ich ja nich wissen, dass ich keine anderen konstruktoren
        // aufrufen kann.
        // Java ist einfach scheisse! nehmen wir C++?
        this.username = username;
        //this.password = password;
        this.surname = surname;
        this.givenname = givenName;
        this.room = room;
        this.email = email;
        this.expirationDate = expirationDate;
        this.user_id = userID;
        isModified = false;
    }

    /**
     * eklig
     * @param dataLink soll eine extension eines Links zu einem JDBD sein, fuehrt das insert darauf mit sich selbst aus
     */
    public void update(DataLink dataLink) {
        dataLink.update(this);
        isModified = false;
    }

    /**
     * auch eklig
     * @param dataLink soll eine extension eines Links zu einem JDBD sein, fuehrt das insert darauf mit sich selbst aus
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
        // TODO: sanitize data (expirationDate) // TODO bitte nach deutsch uebsersetzen :P
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
     * @return Field value
     */
    public boolean passwordChanged() {
       return this.passwordChanged;
    }
    
    /***
     * Getter for Field
     * passwordChanged() ist wichtig ansonsten liefert getPassword nur null aus!!!!
     * @return password (if changed) else null
     */
    public String getPassword() {
        if(this.passwordChanged)
            return this.password;
        else return null;
    }
    
    /***
     * Getter for Field
     * @return Field value
     */
    public String getUsername() {
       return this.username;
    }
    
    /***
     * Setter for Field
     * TODO SCHMERZ (na ja egal) hier fuer muessen wir mal ne iss oeffnen und den code refactoren (prio low)
     */
    public void setUser_id(int user_id) {
       this.user_id = user_id;
    }

    /***
     * Setter for Field
     */
    public void setSurname(String surname) {
        this.surname = surname;
        this.isModified = true;
    }

    /***
     * Setter for Field
     */
    public void setGivenname(String givenname) {
        this.givenname = givenname;
        this.isModified = true;
    }

    /***
     * Setter for Field
     */
    public void setRoom(String room) {
        this.room = room;
        this.isModified = true;
    }

    /***
     * Setter for Field
     */
    public void setEmail(String email) {
        this.email = email;
        this.isModified = true;
    }

    /***
     * Setter for Field
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        this.isModified = true;
    }
    
    /***
     * Setter for Field
     */
    public void setPassword(String password) {
        this.password = password;
        this.isModified = true;
        this.passwordChanged = true;
    }

    /***
     * Getter for Field
     * @return Field value
     */
    public String getSurname() {
        return surname;
    }

    /***
     * Getter for Field
     * @return Field value
     */
    public String getGivenname() {
        return givenname;
    }

    /***
     * Getter for Field
     * @return Field value
     */
    public String getRoom() {
        return room;
    }

    /***
     * Getter for Field
     * @return Field value
     */
    public String getEmail() {
        return email;
    }

    /***
     * Getter for Field
     * @return Field value
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /***
     * Getter for Field
     * @return Field value
     */
    public int getUser_id() {
        return user_id;
    }

    /***
     * Getter for Field
     * @return Field value
     */
    public boolean isIsModified() {
        return isModified;
    }
}
