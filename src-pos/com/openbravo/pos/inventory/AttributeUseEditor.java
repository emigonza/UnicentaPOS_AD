//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2013 uniCenta & previous Openbravo POS works
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

package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.awt.Component;
import java.util.UUID;

/**
 *
 * @author adrianromero
 */
public class AttributeUseEditor extends javax.swing.JPanel implements EditorRecord {

    private SentenceList attributesent;
    private ComboBoxValModel attributemodel;

    private Object id;
    private Object attuseid;

    private Object insertid;

    /** Creates new form AttributeSetEditor */
    public AttributeUseEditor(AppView app, DirtyManager dirty) {

        attributesent = new StaticSentence(app.getSession()
            , "SELECT ID, NAME FROM ATTRIBUTE ORDER BY NAME"
            , null
            , new SerializerRead() {@Override
 public Object readValues(DataRead dr) throws BasicException {
                return new AttributeInfo(dr.getString(1), dr.getString(2));
            }}
        );
        attributemodel = new ComboBoxValModel();

        initComponents();

        jLineno.getDocument().addDocumentListener(dirty);
        jAttribute.addActionListener(dirty);
    }

    public void setInsertId(String insertid) {

        this.insertid = insertid;
    }

    public void activate() throws BasicException {

        attributemodel = new ComboBoxValModel(attributesent.list());
        jAttribute.setModel(attributemodel);
    }

    @Override
    public void refresh() {
    }

    @Override
    public void writeValueEOF() {

        id = null;
        attuseid = null;
        attributemodel.setSelectedKey(null);
        jLineno.setText(null);

        jAttribute.setEnabled(false);
        jLineno.setEnabled(false);
    }

    @Override
    public void writeValueInsert() {

        id = UUID.randomUUID().toString();
        attuseid = insertid;
        attributemodel.setSelectedKey(null);
        jLineno.setText(null);

        jAttribute.setEnabled(true);
        jLineno.setEnabled(true);
    }

    @Override
    public void writeValueEdit(Object value) {

        Object[] obj = (Object[]) value;

        id = obj[0];
        attuseid = obj[1];
        attributemodel.setSelectedKey(obj[2]);
        jLineno.setText(Formats.INT.formatValue(obj[3]));

        jAttribute.setEnabled(true);
        jLineno.setEnabled(true);
    }

    @Override
    public void writeValueDelete(Object value) {

        Object[] obj = (Object[]) value;

        id = obj[0];
        attuseid = obj[1];
        attributemodel.setSelectedKey(obj[2]);
        jLineno.setText(Formats.INT.formatValue(obj[3]));

        jAttribute.setEnabled(false);
        jLineno.setEnabled(false);
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Object createValue() throws BasicException {
        Object[] value = new Object[5];

        value[0] = id;
        value[1] = attuseid;
        value[2] = attributemodel.getSelectedKey();
        value[3] = Formats.INT.parseValue(jLineno.getText());
        value[4] = attributemodel.getSelectedText();

        return value;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLineno = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jAttribute = new javax.swing.JComboBox();

        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(AppLocal.getIntString("label.order")); // NOI18N

        jLineno.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText(AppLocal.getIntString("label.attribute")); // NOI18N

        jAttribute.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLineno, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jAttribute, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLineno, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jAttribute, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addContainerGap(273, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jAttribute;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jLineno;
    // End of variables declaration//GEN-END:variables


}
