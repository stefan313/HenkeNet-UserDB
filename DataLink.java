/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nick
 */

import java.util.ArrayList;

public interface DataLink {    
    public boolean connect();
    public void disconnect();
    
    public int insert(User user);
    public int insert(User user, String comment);
    public int insert(User user, String comment, int amountReceivedInCents);
    
    public int update(User user);
    public int update(User user, String comment);
    public int update(User user, String comment, int amountReceivedInCents);
    
    public boolean delete(User user);
    public boolean delete(User user, String comment);
    public boolean delete(User user, String comment, int amountReceivedInCents);
    
    public boolean commitTransaction(Transaction transaction);
    
    public User getUser(String username);
    public User getUser(int uid);
    
    public ArrayList<User> lookupUser(String anyKey);
    public ArrayList<Transaction> lookupTransactions(User user);
}
