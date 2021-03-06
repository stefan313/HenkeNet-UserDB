
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tobias
 */
public class EditCreateForm extends javax.swing.JFrame {

    MainControl control;
    Integer userID = null;
    
    private final static Logger LOG = Logger.getLogger("*");
    
    /**
     * Create a new Edit/Create form. 
     * @param control The Control object.
     * @param showing The user currently showing. When creating a new account
     * this param must be set to null.
     */
    public EditCreateForm(MainControl control, User showing) {
        this.control = control;
        initComponents();
        if(showing !=null){
            String room = showing.getRoom();
            String[] parts = room.split("-");
            if(parts.length>0){
                textFieldRoomNumber1.setText(parts[0]);
            }
            if(parts.length>1){
                textFieldRoomNumber2.setText(parts[1]);
            }
            if(parts.length>2){
                textFieldRoomNumber3.setText(parts[2]);
            }
            if(parts.length>3){
                textFieldRoomNumber4.setText(parts[3]);
            }
            passwordField.setText("");
            passwordFieldCheck.setText("");
            textFieldNachname.setText(showing.getSurname());
            textFieldVorname.setText(showing.getGivenname());
            textFieldUsername.setText(showing.getUsername());
            textFieldEMail.setText(showing.getEmail());
            txtExpDate.setText(showing.getExpirationDate());
            jLabelUserID.setText("" + showing.getUser_id());
            userID = showing.getUser_id();
        } else {
            txtExpDate.setText(control.getThisExpDate());
        }
    }
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        textFieldRoomNumber1 = new javax.swing.JTextField();
        textFieldRoomNumber2 = new javax.swing.JTextField();
        textFieldRoomNumber3 = new javax.swing.JTextField();
        textFieldRoomNumber4 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        textFieldVorname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        textFieldNachname = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        textFieldEMail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        textFieldUsername = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        jLabel9 = new javax.swing.JLabel();
        passwordFieldCheck = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        buttonCommit = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtStatusBar = new javax.swing.JLabel();
        txtExpDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabelUserID = new javax.swing.JLabel();

        jLabel13.setText("Room:");
        jLabel13.setToolTipText("");

        setTitle("Create / Edit account data");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel11.setText("Room:");
        jLabel11.setToolTipText("");

        jLabel4.setText("Given name:");
        jLabel4.setToolTipText("");

        jLabel5.setText("Surname:");

        jLabel6.setText("E-Mail:");

        jLabel8.setText("Username:");

        jLabel10.setText("Password:");

        jLabel9.setText("retype Passw.:");

        jLabel7.setText("Create / Edit account data:");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        buttonCommit.setText("Submit");
        buttonCommit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCommitActionPerformed(evt);
            }
        });

        jLabel12.setText("Exp date.:");

        txtStatusBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtStatusBar.setName("txtStatusBar"); // NOI18N

        txtExpDate.setEditable(false);
        txtExpDate.setBackground(new java.awt.Color(200, 200, 200));
        txtExpDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExpDateActionPerformed(evt);
            }
        });

        jLabel1.setText("-");

        jLabel2.setText("-");

        jLabel3.setText("-");

        jLabel14.setText("ID:");
        jLabel14.setToolTipText("");

        jLabelUserID.setText("     ");
        jLabelUserID.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonCommit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textFieldRoomNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldRoomNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldRoomNumber3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldRoomNumber4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelUserID, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(textFieldVorname, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldNachname, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldUsername, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldEMail, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordFieldCheck)
                            .addComponent(txtExpDate, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGap(50, 50, 50))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(textFieldRoomNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldRoomNumber3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldRoomNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldRoomNumber4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel14)
                    .addComponent(jLabelUserID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldVorname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldNachname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldEMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordFieldCheck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(txtExpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(buttonCommit))
                .addGap(18, 18, 18)
                .addComponent(txtStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.setVisible(false);
        control.enableMain();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void buttonCommitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCommitActionPerformed
        User u = fetchData();
        if(u !=null){
            this.setVisible(false);
            if(userID != null){
                //TODO!!! dirty!!! da uid ein schlüsselkandidat ist, sollte es eigentlich nicht abänderbar sein
                u.setUser_id(userID);
                control.initUpdate(u);
            }else{
                control.initCreate(u);
            }
        }
    }//GEN-LAST:event_buttonCommitActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void txtExpDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExpDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtExpDateActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setVisible(false);
        control.enableMain();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton buttonCommit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelUserID;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JPasswordField passwordFieldCheck;
    private javax.swing.JTextField textFieldEMail;
    private javax.swing.JTextField textFieldNachname;
    private javax.swing.JTextField textFieldRoomNumber1;
    private javax.swing.JTextField textFieldRoomNumber2;
    private javax.swing.JTextField textFieldRoomNumber3;
    private javax.swing.JTextField textFieldRoomNumber4;
    private javax.swing.JTextField textFieldUsername;
    private javax.swing.JTextField textFieldVorname;
    private javax.swing.JTextField txtExpDate;
    private javax.swing.JLabel txtStatusBar;
    // End of variables declaration//GEN-END:variables

    /**
     * meh!!!
     * hier wird aus einem alten user ein neuer generiert! fehleranfällig! --> password wird extra gesetzt!
     * @return einen neuen User mit den aktuellen Daten des alten
     */
    private User fetchData(){
        if (this.getTextFieldRoomNumber1().getText().length() != 2
                    || this.getTextFieldRoomNumber2().getText().length() != 2
                    || this.getTextFieldRoomNumber3().getText().length() != 2
                    || this.getTextFieldRoomNumber4().getText().length() != 1) {
                LOG.log(Level.WARNING, "[FAIL] Review room number.");
                return null;
            }

            String vorname = getTextFieldVorname().getText(),
                    nachname = getTextFieldNachname().getText(),
                    email = getTextFieldEMail().getText(),
                    roomnumber = getTextFieldRoomNumber1().getText() + "-"
                    + getTextFieldRoomNumber2().getText() + "-"
                    + getTextFieldRoomNumber3().getText() + "-"
                    + getTextFieldRoomNumber4().getText(),
                    username = getTextFieldUsername().getText(),
                    password = String.valueOf(getPasswordField().getPassword()),
                    expyDate = getTextFieldExpDate().getText();
            //Daten (passwort) checken
            if (!password.equals(String.valueOf(getPasswordFieldCheck().getPassword()))) {
                LOG.log(Level.WARNING, "[FAIL] Passwords do not match.");
                return null;
            }
            //username auf vorhanden sein checken ( gäbe sonst eh mysql error )
            if (username.equalsIgnoreCase("")) {
                LOG.log(Level.WARNING, "[FAIL] username is empty.");
                //failer!
                return null;
            }
            User ret = new User(username, /*password, */ roomnumber, nachname, vorname, email, expyDate);
            if (password.equalsIgnoreCase("")
                    || username.equalsIgnoreCase("")) {
                LOG.log(Level.INFO, "[INFO] Password is empty.");
                //man muss ja nicht immer das pw ziehen und gleich wieder zurück schreiben wie den ganzen anderen shit --> unnötige buganfällige IO-Vorgänge
                //return null;
                //falls pw nicht leer ist neues passwort in die datenbank feuern,... hätte man eleganter modellieren sollen
            } else ret.setPassword(password);
            
            /* if (expyDate.equalsIgnoreCase("")){
             *    LOG.log(Level.WARNING, "[FAIL] Expiration date is empty.");
             *    return null;
             *}
             * // we may want to set an empty expiration date.
             */
            
            
            return ret;
    }
    
    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JPasswordField getPasswordFieldCheck() {
        return passwordFieldCheck;
    }

    public JTextField getTextFieldEMail() {
        return textFieldEMail;
    }

    public JTextField getTextFieldRoomNumber1() {
        return textFieldRoomNumber1;
    }

    public JTextField getTextFieldRoomNumber2() {
        return textFieldRoomNumber2;
    }

    public JTextField getTextFieldRoomNumber3() {
        return textFieldRoomNumber3;
    }

    public JTextField getTextFieldRoomNumber4() {
        return textFieldRoomNumber4;
    }

    public JTextField getTextFieldUsername() {
        return textFieldUsername;
    }

    public JTextField getTextFieldVorname() {
        return textFieldVorname;
    }

    public JTextField getTextFieldNachname() {
        return textFieldNachname;
    }

    public JLabel getTxtStatusBar() {
        return txtStatusBar;
    }

    public JTextField getTextFieldExpDate() {
        return txtExpDate;
    }
    

}
