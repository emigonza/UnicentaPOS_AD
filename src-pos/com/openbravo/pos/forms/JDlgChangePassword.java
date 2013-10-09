//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2012 uniCenta
//    http://www.unicenta.net/unicentaopos
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.forms;

import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.util.Hashcypher;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JFrame;

/**
 *
 * @author adrianromero
 */
public class JDlgChangePassword extends javax.swing.JDialog {
    
    private String m_sOldPassword;
    private String m_sNewPassword;
    
    /** Creates new form ChangePassword */
    private JDlgChangePassword(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }
    /** Creates new form ChangePassword */
    private JDlgChangePassword(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }    

    private String init(String sOldPassword) {
        
        initComponents();

        getRootPane().setDefaultButton(jcmdOK);   
   
        m_sOldPassword = sOldPassword;
        m_sNewPassword = null;
        
        //show();
        setVisible(true);
        
        return m_sNewPassword;
    }
    
    
    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }    
    
    public static String showMessage(Component parent, String sOldPassword) {
         
        Window window = getWindow(parent);      
        
        JDlgChangePassword myMsg;
        if (window instanceof Frame) { 
            myMsg = new JDlgChangePassword((Frame) window, true);
        } else {
            myMsg = new JDlgChangePassword((Dialog) window, true);
        }
        return myMsg.init(sOldPassword);
    }    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jcmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtxtPasswordOld = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jtxtPasswordNew = new javax.swing.JPasswordField();
        jtxtPasswordRepeat = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("title.changepassword")); // NOI18N
        setResizable(false);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jcmdOK.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/ok.png"))); // NOI18N
        jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdOK);

        jcmdCancel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jcmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/cancel.png"))); // NOI18N
        jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText(AppLocal.getIntString("label.passwordold")); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 20, 120, 25);

        jtxtPasswordOld.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel1.add(jtxtPasswordOld);
        jtxtPasswordOld.setBounds(140, 20, 180, 25);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText(AppLocal.getIntString("label.passwordnew")); // NOI18N
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 50, 120, 25);

        jtxtPasswordNew.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel1.add(jtxtPasswordNew);
        jtxtPasswordNew.setBounds(140, 50, 180, 25);

        jtxtPasswordRepeat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel1.add(jtxtPasswordRepeat);
        jtxtPasswordRepeat.setBounds(140, 80, 180, 25);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(AppLocal.getIntString("label.passwordrepeat")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 80, 120, 25);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-416)/2, (screenSize.height-205)/2, 416, 205);
    }// </editor-fold>//GEN-END:initComponents

    private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdCancelActionPerformed

        dispose();
    }//GEN-LAST:event_jcmdCancelActionPerformed

    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdOKActionPerformed

        if (new String(jtxtPasswordNew.getPassword()).equals(new String(jtxtPasswordRepeat.getPassword()))) {
            if (Hashcypher.authenticate(new String(jtxtPasswordOld.getPassword()), m_sOldPassword)) {
                m_sNewPassword = Hashcypher.hashString(new String(jtxtPasswordNew.getPassword()));
                dispose();
            } else {
                JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.BadPassword")));
            }
        } else {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.changepassworddistinct")));
        }
    }//GEN-LAST:event_jcmdOKActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jcmdCancel;
    private javax.swing.JButton jcmdOK;
    private javax.swing.JPasswordField jtxtPasswordNew;
    private javax.swing.JPasswordField jtxtPasswordOld;
    private javax.swing.JPasswordField jtxtPasswordRepeat;
    // End of variables declaration//GEN-END:variables
    
}
