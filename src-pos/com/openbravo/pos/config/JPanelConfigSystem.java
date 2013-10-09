//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2013 uniCenta
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

package com.openbravo.pos.config;

import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.util.DirectoryEvent;
import java.awt.Component;
import javax.swing.JOptionPane;


public class JPanelConfigSystem extends javax.swing.JPanel implements PanelConfig {
    
    private DirtyManager dirty = new DirtyManager();
    
    /** Creates new form JPanelConfigDatabase */
    public JPanelConfigSystem() {
        
        initComponents();
                
        
        jtxtStartupLogo.getDocument().addDocumentListener(dirty);
        jbtnLogoName.addActionListener(new DirectoryEvent(jtxtStartupLogo));
        jTextAutoLogoffTime.getDocument().addDocumentListener(dirty);
        jtxtStartupText.getDocument().addDocumentListener(dirty);
        jbtnLogoText.addActionListener(new DirectoryEvent(jtxtStartupText));
        jMarineOpt.addActionListener(dirty);
        jchkHideInfo.addActionListener(dirty);
        jchkTextOverlay.addActionListener(dirty);
        jchkAutoLogoff.addActionListener(dirty);
        jchkAutoLogoffToTables.addActionListener(dirty);
        jchkShowCustomerDetails.addActionListener(dirty);
        jchkShowWaiterDetails.addActionListener(dirty);
        jCustomerColour.addActionListener(dirty);
        jWaiterColour.addActionListener(dirty);
        jTableNameColour.addActionListener(dirty);
        jTaxIncluded.addActionListener(dirty);
        jCheckPrice00.addActionListener(dirty);          
        jMoveAMountBoxToTop.addActionListener(dirty);
        jCloseCashbtn.addActionListener(dirty); 

    }
    
    
    @Override
    public boolean hasChanged() {
        return dirty.isDirty();
    }
    
    @Override
    public Component getConfigComponent() {
        return this;
    }
   
    @Override
    public void loadProperties(AppConfig config) {

        
        jtxtStartupLogo.setText(config.getProperty("start.logo"));
        jtxtStartupText.setText(config.getProperty("start.text"));  
//lets test for our settings       
        String timerCheck =(config.getProperty("till.autotimer"));
        if (timerCheck == null){
            config.setProperty("till.autotimer","100");
        }                
        jTextAutoLogoffTime.setText(config.getProperty("till.autotimer").toString());

        jMarineOpt.setSelected(Boolean.valueOf(config.getProperty("till.marineoption")).booleanValue()); 
        jchkShowCustomerDetails.setSelected(Boolean.valueOf(config.getProperty("table.showcustomerdetails")).booleanValue());
        jchkShowWaiterDetails.setSelected(Boolean.valueOf(config.getProperty("table.showwaiterdetails")).booleanValue());
        jchkHideInfo.setSelected(Boolean.valueOf(config.getProperty("till.hideinfo")).booleanValue());        
        jchkTextOverlay.setSelected(Boolean.valueOf(config.getProperty("payments.textoverlay")).booleanValue());        
        jchkAutoLogoff.setSelected(Boolean.valueOf(config.getProperty("till.autoLogoff")).booleanValue());    
        jchkAutoLogoffToTables.setSelected(Boolean.valueOf(config.getProperty("till.autoLogoffrestaurant")).booleanValue());           
        jTaxIncluded.setSelected(Boolean.valueOf(config.getProperty("till.taxincluded")).booleanValue());
        jCheckPrice00.setSelected(Boolean.valueOf(config.getProperty("till.pricewith00")).booleanValue());        
        jMoveAMountBoxToTop.setSelected(Boolean.valueOf(config.getProperty("till.amountattop")).booleanValue());  
        jCloseCashbtn.setSelected(Boolean.valueOf(config.getProperty("screen.600800")).booleanValue());
        
        
        if (config.getProperty("table.customercolour")==null){
            jCustomerColour.setSelectedItem("blue");
        }else{
            jCustomerColour.setSelectedItem(config.getProperty("table.customercolour"));
        }
        if (config.getProperty("table.waitercolour")==null){
            jWaiterColour.setSelectedItem("red");
        }else{
            jWaiterColour.setSelectedItem(config.getProperty("table.waitercolour"));
        }
        if (config.getProperty("table.tablecolour")==null){                
            jTableNameColour.setSelectedItem("black");      
        }else{
            jTableNameColour.setSelectedItem((config.getProperty("table.tablecolour")));  
        }
    
        
        if (jchkAutoLogoff.isSelected()){
                jchkAutoLogoffToTables.setVisible(true);
                jLabelInactiveTime.setVisible(true);
                jLabelTimedMessage.setVisible(true);
                jTextAutoLogoffTime.setVisible(true);
        }else{    
                jchkAutoLogoffToTables.setVisible(false);
                jLabelInactiveTime.setVisible(false);
                jLabelTimedMessage.setVisible(false);
                jTextAutoLogoffTime.setVisible(false);
        }
        
        
        dirty.setDirty(false);
    }
   
    @Override
    public void saveProperties(AppConfig config) {
        
        config.setProperty("start.logo", jtxtStartupLogo.getText());
        config.setProperty("start.text", jtxtStartupText.getText());
        config.setProperty("till.autotimer",jTextAutoLogoffTime.getText());
        config.setProperty("till.marineoption", Boolean.toString(jMarineOpt.isSelected()));
        config.setProperty("table.showcustomerdetails", Boolean.toString(jchkShowCustomerDetails.isSelected()));
        config.setProperty("table.showwaiterdetails", Boolean.toString(jchkShowWaiterDetails.isSelected()));        
        config.setProperty("till.hideinfo", Boolean.toString(jchkHideInfo.isSelected()));        
        config.setProperty("payments.textoverlay", Boolean.toString(jchkTextOverlay.isSelected()));         
        config.setProperty("till.autoLogoff", Boolean.toString(jchkAutoLogoff.isSelected()));                 
        config.setProperty("till.autoLogoffrestaurant", Boolean.toString(jchkAutoLogoffToTables.isSelected()));                        
        config.setProperty("table.customercolour",jCustomerColour.getSelectedItem().toString());
        config.setProperty("table.waitercolour",jWaiterColour.getSelectedItem().toString());
        config.setProperty("table.tablecolour",jTableNameColour.getSelectedItem().toString());         
        config.setProperty("till.taxincluded",Boolean.toString(jTaxIncluded.isSelected()));                     
        config.setProperty("till.pricewith00",Boolean.toString(jCheckPrice00.isSelected()));                         
        config.setProperty("till.amountattop",Boolean.toString(jMoveAMountBoxToTop.isSelected()));         
        config.setProperty("screen.600800",Boolean.toString(jCloseCashbtn.isSelected())); 
        
         
        dirty.setDirty(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jtxtStartupLogo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jtxtStartupText = new javax.swing.JTextField();
        jbtnLogoName = new javax.swing.JButton();
        jbtnLogoText = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jchkAutoLogoff = new javax.swing.JCheckBox();
        jchkAutoLogoffToTables = new javax.swing.JCheckBox();
        jTextAutoLogoffTime = new javax.swing.JTextField();
        jLabelInactiveTime = new javax.swing.JLabel();
        jLabelTimedMessage = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jchkShowCustomerDetails = new javax.swing.JCheckBox();
        jchkShowWaiterDetails = new javax.swing.JCheckBox();
        jCustomerColour = new javax.swing.JComboBox();
        jLabelTableNameTextColour = new javax.swing.JLabel();
        jLabelCustomerTextColour = new javax.swing.JLabel();
        jLabelServerTextColour = new javax.swing.JLabel();
        jWaiterColour = new javax.swing.JComboBox();
        jTableNameColour = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jchkTextOverlay = new javax.swing.JCheckBox();
        jMarineOpt = new javax.swing.JCheckBox();
        jchkHideInfo = new javax.swing.JCheckBox();
        jTaxIncluded = new javax.swing.JCheckBox();
        jCheckPrice00 = new javax.swing.JCheckBox();
        jMoveAMountBoxToTop = new javax.swing.JCheckBox();
        jCloseCashbtn = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(680, 190));
        setLayout(null);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("label.startuppanel"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        jPanel1.setLayout(null);

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setText(bundle.getString("label.startuplogo")); // NOI18N
        jLabel18.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel18.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel18.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel1.add(jLabel18);
        jLabel18.setBounds(20, 20, 80, 25);

        jtxtStartupLogo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtxtStartupLogo.setMaximumSize(new java.awt.Dimension(0, 25));
        jtxtStartupLogo.setMinimumSize(new java.awt.Dimension(0, 0));
        jtxtStartupLogo.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel1.add(jtxtStartupLogo);
        jtxtStartupLogo.setBounds(120, 20, 370, 25);

        jLabel19.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel19.setText(AppLocal.getIntString("label.startuptext")); // NOI18N
        jLabel19.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel19.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel19.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel1.add(jLabel19);
        jLabel19.setBounds(20, 50, 70, 25);

        jtxtStartupText.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtxtStartupText.setMaximumSize(new java.awt.Dimension(0, 25));
        jtxtStartupText.setMinimumSize(new java.awt.Dimension(0, 0));
        jtxtStartupText.setPreferredSize(new java.awt.Dimension(0, 25));
        jtxtStartupText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtStartupTextActionPerformed(evt);
            }
        });
        jtxtStartupText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTetxtStartupTextFocusGained(evt);
            }
        });
        jPanel1.add(jtxtStartupText);
        jtxtStartupText.setBounds(120, 50, 370, 25);

        jbtnLogoName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileopen.png"))); // NOI18N
        jbtnLogoName.setMaximumSize(new java.awt.Dimension(64, 32));
        jbtnLogoName.setMinimumSize(new java.awt.Dimension(64, 32));
        jbtnLogoName.setPreferredSize(new java.awt.Dimension(64, 32));
        jPanel1.add(jbtnLogoName);
        jbtnLogoName.setBounds(500, 10, 64, 32);

        jbtnLogoText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileopen.png"))); // NOI18N
        jbtnLogoText.setMaximumSize(new java.awt.Dimension(64, 32));
        jbtnLogoText.setMinimumSize(new java.awt.Dimension(64, 32));
        jbtnLogoText.setPreferredSize(new java.awt.Dimension(64, 32));
        jPanel1.add(jbtnLogoText);
        jbtnLogoText.setBounds(500, 45, 64, 32);

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 0, 153));
        jButton1.setText("X");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(75, 50, 40, 23);

        add(jPanel1);
        jPanel1.setBounds(10, 10, 600, 90);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("label.autologoffpanel"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        jPanel2.setLayout(null);

        jchkAutoLogoff.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jchkAutoLogoff.setText(bundle.getString("label.autologonoff")); // NOI18N
        jchkAutoLogoff.setMaximumSize(new java.awt.Dimension(0, 25));
        jchkAutoLogoff.setMinimumSize(new java.awt.Dimension(0, 0));
        jchkAutoLogoff.setPreferredSize(new java.awt.Dimension(0, 25));
        jchkAutoLogoff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkAutoLogoffActionPerformed(evt);
            }
        });
        jPanel2.add(jchkAutoLogoff);
        jchkAutoLogoff.setBounds(10, 20, 190, 25);

        jchkAutoLogoffToTables.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jchkAutoLogoffToTables.setText(bundle.getString("label.autoloffrestaurant")); // NOI18N
        jchkAutoLogoffToTables.setMaximumSize(new java.awt.Dimension(0, 25));
        jchkAutoLogoffToTables.setMinimumSize(new java.awt.Dimension(0, 0));
        jchkAutoLogoffToTables.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel2.add(jchkAutoLogoffToTables);
        jchkAutoLogoffToTables.setBounds(200, 20, 260, 25);

        jTextAutoLogoffTime.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextAutoLogoffTime.setText("0");
        jTextAutoLogoffTime.setMaximumSize(new java.awt.Dimension(0, 25));
        jTextAutoLogoffTime.setMinimumSize(new java.awt.Dimension(0, 0));
        jTextAutoLogoffTime.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel2.add(jTextAutoLogoffTime);
        jTextAutoLogoffTime.setBounds(200, 50, 50, 25);

        jLabelInactiveTime.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabelInactiveTime.setText(bundle.getString("label.autolofftime")); // NOI18N
        jLabelInactiveTime.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabelInactiveTime.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabelInactiveTime.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel2.add(jLabelInactiveTime);
        jLabelInactiveTime.setBounds(30, 50, 170, 25);

        jLabelTimedMessage.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabelTimedMessage.setText(bundle.getString("label.autologoffzero")); // NOI18N
        jLabelTimedMessage.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabelTimedMessage.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabelTimedMessage.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel2.add(jLabelTimedMessage);
        jLabelTimedMessage.setBounds(260, 50, 190, 25);

        add(jPanel2);
        jPanel2.setBounds(10, 110, 600, 90);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("label.tabledisplayoptions"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        jPanel3.setLayout(null);

        jchkShowCustomerDetails.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jchkShowCustomerDetails.setText(bundle.getString("label.tableshowcustomerdetails")); // NOI18N
        jchkShowCustomerDetails.setMaximumSize(new java.awt.Dimension(0, 25));
        jchkShowCustomerDetails.setMinimumSize(new java.awt.Dimension(0, 0));
        jchkShowCustomerDetails.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel3.add(jchkShowCustomerDetails);
        jchkShowCustomerDetails.setBounds(10, 20, 220, 25);

        jchkShowWaiterDetails.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jchkShowWaiterDetails.setText(bundle.getString("label.tableshowwaiterdetails")); // NOI18N
        jchkShowWaiterDetails.setMaximumSize(new java.awt.Dimension(0, 25));
        jchkShowWaiterDetails.setMinimumSize(new java.awt.Dimension(0, 0));
        jchkShowWaiterDetails.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel3.add(jchkShowWaiterDetails);
        jchkShowWaiterDetails.setBounds(10, 60, 220, 23);

        jCustomerColour.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jCustomerColour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "black", "blue", "grey", "green", "orange", "red", "white", "yellow" }));
        jCustomerColour.setMaximumSize(new java.awt.Dimension(0, 25));
        jCustomerColour.setMinimumSize(new java.awt.Dimension(0, 0));
        jCustomerColour.setPreferredSize(new java.awt.Dimension(0, 25));
        jCustomerColour.setSelectedItem("blue");
        jCustomerColour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCustomerColourActionPerformed(evt);
            }
        });
        jPanel3.add(jCustomerColour);
        jCustomerColour.setBounds(380, 20, 200, 30);

        jLabelTableNameTextColour.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabelTableNameTextColour.setText(bundle.getString("label.textclourtablename")); // NOI18N
        jLabelTableNameTextColour.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabelTableNameTextColour.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabelTableNameTextColour.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel3.add(jLabelTableNameTextColour);
        jLabelTableNameTextColour.setBounds(240, 100, 130, 30);

        jLabelCustomerTextColour.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabelCustomerTextColour.setText(bundle.getString("label.textcolourcustomer")); // NOI18N
        jLabelCustomerTextColour.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabelCustomerTextColour.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabelCustomerTextColour.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel3.add(jLabelCustomerTextColour);
        jLabelCustomerTextColour.setBounds(240, 20, 130, 25);

        jLabelServerTextColour.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabelServerTextColour.setText(bundle.getString("label.textcolourwaiter")); // NOI18N
        jLabelServerTextColour.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabelServerTextColour.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabelServerTextColour.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel3.add(jLabelServerTextColour);
        jLabelServerTextColour.setBounds(240, 60, 130, 25);

        jWaiterColour.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jWaiterColour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "black", "blue", "grey", "green", "orange", "red", "white", "yellow" }));
        jWaiterColour.setMaximumSize(new java.awt.Dimension(0, 25));
        jWaiterColour.setMinimumSize(new java.awt.Dimension(0, 0));
        jWaiterColour.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel3.add(jWaiterColour);
        jWaiterColour.setBounds(380, 60, 200, 30);

        jTableNameColour.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTableNameColour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "black", "blue", "grey", "green", "orange", "red", "white", "yellow" }));
        jTableNameColour.setMaximumSize(new java.awt.Dimension(0, 25));
        jTableNameColour.setMinimumSize(new java.awt.Dimension(0, 0));
        jTableNameColour.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel3.add(jTableNameColour);
        jTableNameColour.setBounds(380, 100, 200, 30);

        add(jPanel3);
        jPanel3.setBounds(10, 210, 600, 140);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("label.general"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        jPanel4.setLayout(null);

        jchkTextOverlay.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jchkTextOverlay.setText(bundle.getString("label.currencybutton")); // NOI18N
        jchkTextOverlay.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jchkTextOverlay.setMaximumSize(new java.awt.Dimension(0, 25));
        jchkTextOverlay.setMinimumSize(new java.awt.Dimension(0, 0));
        jchkTextOverlay.setPreferredSize(new java.awt.Dimension(0, 30));
        jPanel4.add(jchkTextOverlay);
        jchkTextOverlay.setBounds(10, 80, 260, 25);

        jMarineOpt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jMarineOpt.setText(bundle.getString("label.marine")); // NOI18N
        jMarineOpt.setMaximumSize(new java.awt.Dimension(0, 25));
        jMarineOpt.setMinimumSize(new java.awt.Dimension(0, 0));
        jMarineOpt.setPreferredSize(new java.awt.Dimension(0, 30));
        jPanel4.add(jMarineOpt);
        jMarineOpt.setBounds(10, 20, 230, 25);

        jchkHideInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jchkHideInfo.setText(bundle.getString("label.Infopanel")); // NOI18N
        jchkHideInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jchkHideInfo.setMaximumSize(new java.awt.Dimension(0, 25));
        jchkHideInfo.setMinimumSize(new java.awt.Dimension(0, 0));
        jchkHideInfo.setPreferredSize(new java.awt.Dimension(0, 30));
        jPanel4.add(jchkHideInfo);
        jchkHideInfo.setBounds(10, 50, 230, 25);

        jTaxIncluded.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTaxIncluded.setText(bundle.getString("label.taxincluded")); // NOI18N
        jTaxIncluded.setMaximumSize(new java.awt.Dimension(0, 25));
        jTaxIncluded.setMinimumSize(new java.awt.Dimension(0, 0));
        jTaxIncluded.setPreferredSize(new java.awt.Dimension(0, 30));
        jPanel4.add(jTaxIncluded);
        jTaxIncluded.setBounds(350, 20, 240, 25);

        jCheckPrice00.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jCheckPrice00.setText(bundle.getString("label.pricewith00")); // NOI18N
        jCheckPrice00.setToolTipText("");
        jCheckPrice00.setMaximumSize(new java.awt.Dimension(0, 25));
        jCheckPrice00.setMinimumSize(new java.awt.Dimension(0, 0));
        jCheckPrice00.setPreferredSize(new java.awt.Dimension(0, 30));
        jCheckPrice00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckPrice00ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckPrice00);
        jCheckPrice00.setBounds(350, 50, 240, 25);

        jMoveAMountBoxToTop.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jMoveAMountBoxToTop.setText(bundle.getString("label.inputamount")); // NOI18N
        jMoveAMountBoxToTop.setMaximumSize(new java.awt.Dimension(0, 25));
        jMoveAMountBoxToTop.setMinimumSize(new java.awt.Dimension(0, 0));
        jMoveAMountBoxToTop.setPreferredSize(new java.awt.Dimension(0, 30));
        jPanel4.add(jMoveAMountBoxToTop);
        jMoveAMountBoxToTop.setBounds(350, 80, 240, 25);

        jCloseCashbtn.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jCloseCashbtn.setText(bundle.getString("message.systemclosecash")); // NOI18N
        jCloseCashbtn.setMaximumSize(new java.awt.Dimension(0, 25));
        jCloseCashbtn.setMinimumSize(new java.awt.Dimension(0, 0));
        jCloseCashbtn.setPreferredSize(new java.awt.Dimension(0, 30));
        jPanel4.add(jCloseCashbtn);
        jCloseCashbtn.setBounds(10, 110, 250, 25);

        add(jPanel4);
        jPanel4.setBounds(10, 360, 600, 140);
    }// </editor-fold>//GEN-END:initComponents

    private void jchkAutoLogoffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkAutoLogoffActionPerformed
        if (jchkAutoLogoff.isSelected()){
                jchkAutoLogoffToTables.setVisible(true);
                jLabelInactiveTime.setVisible(true);
                jLabelTimedMessage.setVisible(true);
                jTextAutoLogoffTime.setVisible(true);
        }else{    
                jchkAutoLogoffToTables.setVisible(false);
                jLabelInactiveTime.setVisible(false);
                jLabelTimedMessage.setVisible(false);
                jTextAutoLogoffTime.setVisible(false);
        }
    }//GEN-LAST:event_jchkAutoLogoffActionPerformed

    private void jtxtStartupTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtStartupTextActionPerformed

    }//GEN-LAST:event_jtxtStartupTextActionPerformed

    private void jCheckPrice00ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckPrice00ActionPerformed

    }//GEN-LAST:event_jCheckPrice00ActionPerformed

    private void jTetxtStartupTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTetxtStartupTextFocusGained
// JG 31 August 2103 GNU GPL License Warning   
        
        transferFocus();
        
        JOptionPane.showMessageDialog(jPanel1,"<html>Changing default Startup Text content may violate the <br>"
                + " Free Software Foundation's GNU General Public License GPL","GNU GPL Warning",JOptionPane.WARNING_MESSAGE); 
              

    }//GEN-LAST:event_jTetxtStartupTextFocusGained

    private void jCustomerColourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCustomerColourActionPerformed

    }//GEN-LAST:event_jCustomerColourActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            jtxtStartupText.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckPrice00;
    private javax.swing.JCheckBox jCloseCashbtn;
    private javax.swing.JComboBox jCustomerColour;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabelCustomerTextColour;
    private javax.swing.JLabel jLabelInactiveTime;
    private javax.swing.JLabel jLabelServerTextColour;
    private javax.swing.JLabel jLabelTableNameTextColour;
    private javax.swing.JLabel jLabelTimedMessage;
    private javax.swing.JCheckBox jMarineOpt;
    private javax.swing.JCheckBox jMoveAMountBoxToTop;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JComboBox jTableNameColour;
    private javax.swing.JCheckBox jTaxIncluded;
    private javax.swing.JTextField jTextAutoLogoffTime;
    private javax.swing.JComboBox jWaiterColour;
    private javax.swing.JButton jbtnLogoName;
    private javax.swing.JButton jbtnLogoText;
    private javax.swing.JCheckBox jchkAutoLogoff;
    private javax.swing.JCheckBox jchkAutoLogoffToTables;
    private javax.swing.JCheckBox jchkHideInfo;
    private javax.swing.JCheckBox jchkShowCustomerDetails;
    private javax.swing.JCheckBox jchkShowWaiterDetails;
    private javax.swing.JCheckBox jchkTextOverlay;
    private javax.swing.JTextField jtxtStartupLogo;
    private javax.swing.JTextField jtxtStartupText;
    // End of variables declaration//GEN-END:variables
    
}
