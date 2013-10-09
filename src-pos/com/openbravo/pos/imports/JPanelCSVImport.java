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
//    CSV Import Panel added by JDL - February 2013
//    Additonal library required - javacsv


package com.openbravo.pos.imports;

import com.csvreader.CsvReader;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

      

public class JPanelCSVImport extends JPanel implements JPanelView {    
    
    private ArrayList<String> Headers = new ArrayList<>();
    private Session s;
    private Connection con;  
    private ResultSet rs;
    private Statement stmt;
    private String ID;
    private String SQL;
    private PreparedStatement pstmt;
    private String csvFileName;
    private String dTax;
    private Double dTaxRate;
    private Double dOriginalRate;
    private String dCategory;
    private CsvReader products;
    private DatabaseMetaData md;
    private int pre302;
    private double oldSell=0;
    private double oldBuy=0;
    private int currentRecord;
    private int rowCount=0;


public JPanelCSVImport (AppView oApp) {
        this(oApp.getProperties());        
}
    @SuppressWarnings("empty-statement")  
    public JPanelCSVImport (AppProperties props) {  

        initComponents();       

        try{
            s=AppViewConnection.createSession(props);
            con=s.getConnection();           
        }
        catch (BasicException | SQLException e){;
        }
        
       
      
      DocumentListener documentListener;
        documentListener = new DocumentListener() {
@Override
public void changedUpdate(DocumentEvent documentEvent) {
jHeaderRead.setEnabled(true);  
}
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
if (!"".equals(jFileName.getText().trim())){
jHeaderRead.setEnabled(true);
}}
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
if (jFileName.getText().trim().equals("")){  
     jHeaderRead.setEnabled(false); 
}}
};
        jFileName.getDocument().addDocumentListener(documentListener);  
        
        }
    
private void GetheadersFromFile(String CSVFileName) throws IOException {
        File f = new File(CSVFileName);
            if (f.exists()){
                	products = new CsvReader(CSVFileName);
                        products.setDelimiter(((String)jComboSeparator.getSelectedItem()).charAt(0));  
                        products.readHeaders();
// We need a minimum of 5 columns to map all required fields                            
                        if (products.getHeaderCount()< 5) {
                            JOptionPane.showMessageDialog(null,
                                    "Insufficient headers found in file", 
                                    "Invalid Header Count.",
                                    JOptionPane.WARNING_MESSAGE);
                            products.close();
                            return;
                        }   
                        rowCount=0;
                        int i = 0;
                        Headers.clear();
                        Headers.add("");                                     
                         jComboName.addItem("");
                         jComboReference.addItem("");
                         jComboBarcode.addItem("");
                         jComboBuy.addItem("");
                         jComboSell.addItem("");
                        while(i < products.getHeaderCount()){                          
                          jComboName.addItem(products.getHeader(i));
                          jComboReference.addItem(products.getHeader(i));
                          jComboBarcode.addItem(products.getHeader(i));
                          jComboBuy.addItem(products.getHeader(i));
                          jComboSell.addItem(products.getHeader(i));
                          Headers.add(products.getHeader(i));
                            ++i;
                        }
                        jHeaderRead.setEnabled(false);
                        jImport.setEnabled(false);
// Enable all the combo boxes & Check boxex
                        jComboReference.setEnabled(true);
                        jComboName.setEnabled(true);
                        jComboBarcode.setEnabled(true);
                        jComboBuy.setEnabled(true);
                        jComboSell.setEnabled(true);
                        jCheckInCatalogue.setEnabled(true);      
                        jCheckSellIncTax.setEnabled(true);
                        
                        while (products.readRecord())
                        {
                            ++rowCount;
                        }
                                                
                        jTextRecords.setText(Long.toString(rowCount));
// close to file we will open again when required                        
                        products.close();                         

    } else {
               JOptionPane.showMessageDialog(null,"Unable to locate "
                       + CSVFileName, 
                       "File not found",
                       JOptionPane.WARNING_MESSAGE);
            }    
}
 
private void ImportCsvFile(String CSVFileName) throws IOException {

                        int newRecords=0;
                        int invalidRecords=0;
                        int priceUpdates=0;
                        int missingData=0;
                        int noChanges=0;
                        Double productBuyPrice;
                        Double productSellPrice;
                        int badPrice=0;
           
// lets start to process the file          
// get the default category & tax
       
try{
        SQL = "SELECT id "
                + "from categories "
                + "where name ="
                + "'" + ((String)jComboCategory.getSelectedItem()) + "'";     
        
        rs = stmt.executeQuery(SQL);
        while (rs.next()){
            dCategory = (rs.getString("id"));           
        }
        
        SQL = "SELECT * "
                + "from taxcategories "
                + "where name ="
                + "'" + ((String)jComboTax.getSelectedItem()) + "'";     
        
        rs = stmt.executeQuery(SQL);
        while (rs.next()){
            dTax = (rs.getString("id"));              
        } 
        SQL = "SELECT * "
                + "from taxes "
                + "where category ="
                + "'" + dTax +"'";    
        
        rs = stmt.executeQuery(SQL);
        while (rs.next()){
            dTaxRate = (rs.getDouble("rate"));                
        } 
        
        
} catch (Exception e){
    
}
                       
          File f = new File(CSVFileName);
            if (f.exists()){
// Count rows in csv file 
                        products = new CsvReader(CSVFileName);
                        products.setDelimiter(((String)jComboSeparator.getSelectedItem()).charAt(0)); 
                        products.readHeaders();                                                

// reset the csv file ready to 
                    //    products = new CsvReader(CSVFileName);
                    //    products.setDelimiter(((String)jComboSeparator.getSelectedItem()).charAt(0)); 
                    //    products.readHeaders();

                        currentRecord=0;
                        while (products.readRecord())
			{
                                String productReference = products.get((String)jComboReference.getSelectedItem());
				String productName = products.get((String)jComboName.getSelectedItem());
                                String productBarcode = products.get((String)jComboBarcode.getSelectedItem());
                                String BuyPrice = products.get((String)jComboBuy.getSelectedItem());
                                String SellPrice = products.get((String)jComboSell.getSelectedItem());
                                currentRecord++;       
                                
// Check if we have values in all the above
                        if ("".equals(productReference) 
                                | "".equals(productName) 
                                | "".equals(productBarcode) 
                                |  "".equals(BuyPrice) 
                                | "".equals(SellPrice)
                                | (!validateNumber(BuyPrice)) 
                                | (!validateNumber(SellPrice)) )
                        {    
                         
                          if (validateNumber(BuyPrice)){
                              productBuyPrice = Double.parseDouble(BuyPrice);
                          }else{
                              productBuyPrice = null;       
                          }
                          if (validateNumber(SellPrice)){
                              productSellPrice = Double.parseDouble(SellPrice);
                          }else{
                              productSellPrice = null;    
                          }
                          
                          if ((validateNumber(BuyPrice)) | (validateNumber(SellPrice))){ 
                              badPrice++;
                          }else{
                              missingData++;
                          }
                          
                          CSVData(currentRecord,productReference, productBarcode, productName,productBuyPrice ,productSellPrice , "Missing data or Invalid number", null,null);
                           
                        }else{  
// Add a new record into the database                            
                                productBuyPrice = Double.parseDouble(BuyPrice);
                                productSellPrice = getSellPrice(SellPrice);
                                String recordType;
                                recordType=getRecordType(productReference, productName, productBarcode);
                                switch (recordType){
                                    case "new":
                                        addRecord(productReference, productName, productBarcode, productBuyPrice, productSellPrice , dCategory, dTax);
                                        newRecords++;
                                        CSVData(currentRecord,productReference, productBarcode, productName,productBuyPrice ,Double.parseDouble(SellPrice) , "New product", null,null);
                                        break;
                                    case "update":
                                        if (!"".equals(updateRecord(ID,productBuyPrice,Double.parseDouble(SellPrice)))){
                                        priceUpdates++;
                                        CSVData(currentRecord,productReference, productBarcode, productName,productBuyPrice ,productSellPrice*(1+dOriginalRate) , "Updated Price Details", oldBuy,oldSell*(1+dOriginalRate));
                                        }else{
                                        noChanges++;
                                        }
                                        break;
                                    case "":
                                        break;
                                    default:    
                                        invalidRecords++;
                                        CSVData(currentRecord,productReference, productBarcode, productName,productBuyPrice ,productSellPrice , recordType, null,null);
                                        break;
                                }                                                             
                            }
                        }                        			
            }else{
               JOptionPane.showMessageDialog(null,"Unable to locate " +  CSVFileName,"File not found",JOptionPane.WARNING_MESSAGE);
            }         
// update the record fields on the form
            jTextNew.setText(Integer.toString(newRecords));
            jTextUpdate.setText(Integer.toString(priceUpdates));
            jTextInvalid.setText(Integer.toString(invalidRecords));
            jTextMissing.setText(Integer.toString(missingData));
            jTextNoChange.setText(Integer.toString(noChanges));
            jTextBadPrice.setText(Integer.toString(badPrice));
 }

private Boolean validateNumber(String testString){
    try{
            Double res = Double.parseDouble(testString);
            return(true);
        }catch (NumberFormatException e){                             
            return(false);
        } 
}

private String getRecordType (String pReference, String pName , String pBarcode){
// check the status of new record
    try{
        SQL="Select ID "
                + "FROM PRODUCTS "
                + "WHERE REFERENCE=?"
                + " AND CODE=? AND NAME=?";
        
        pstmt = con.prepareStatement(SQL);    
        pstmt.setString(1,pReference);      //  Reference String
        pstmt.setString(2,pBarcode);        //  Barcode String
        pstmt.setString(3,pName);           //  Name String
        rs = pstmt.executeQuery();

        if (rs.next()){
            ID =rs.getString("ID");
            return("update");
        }

        SQL="Select ID "
                + "FROM PRODUCTS "
                + "WHERE REFERENCE=? "
                + "OR CODE=? OR NAME=?";
        
        pstmt = con.prepareStatement(SQL);    
        pstmt.setString(1,pReference);      //  Reference String
        pstmt.setString(2,pBarcode);        //  Barcode String
        pstmt.setString(3,pName);           //  Name String
        rs = pstmt.executeQuery();

       if (!rs.next()){          
            return("new");
        }
    
    
// lets check which line exists.
        SQL="Select ID "
                + "FROM PRODUCTS "
                + "WHERE REFERENCE=? AND CODE=?";
        
        pstmt = con.prepareStatement(SQL);    
        pstmt.setString(1,pReference);      //  Reference String
        pstmt.setString(2,pBarcode);        //  Barcode String
        rs = pstmt.executeQuery();

        if (rs.next()){          
            return("Possible Description or Name error.");
        }
    
        SQL="Select ID "
                + "FROM PRODUCTS "
                + "WHERE REFERENCE=? AND NAME=?";
        
        pstmt = con.prepareStatement(SQL);    
        pstmt.setString(1,pReference);      //  Reference String
        pstmt.setString(2,pName);        //  Barcode String
        rs = pstmt.executeQuery();

        if (rs.next()){          
            return("Possible Barcode error.");
        }
        
        SQL="Select ID "
                + "FROM PRODUCTS "
                + "WHERE CODE=? AND NAME=?";
        
        pstmt = con.prepareStatement(SQL);    
        pstmt.setString(1,pBarcode);      //  Reference String
        pstmt.setString(2,pName);        //  Barcode String
        rs = pstmt.executeQuery();

        if (rs.next()){          
            return("Possible Reference error.");
        }
        
        SQL="Select ID "
                + "FROM PRODUCTS "
                + "WHERE REFERENCE=?";
        
        pstmt = con.prepareStatement(SQL);    
        pstmt.setString(1,pReference);      //  Reference String
        rs = pstmt.executeQuery();

        if (rs.next()){          
            return("Duplicate Reference found.");
        }
        
        SQL="Select ID "
                + "FROM PRODUCTS "
                + "WHERE CODE=?";
        
        pstmt = con.prepareStatement(SQL);    
        pstmt.setString(1,pBarcode);      //  Reference String
        rs = pstmt.executeQuery();

        if (rs.next()){          
            return("Duplicate Barcode found");
        }
        
        SQL="Select ID "
                + "FROM PRODUCTS "
                + "WHERE NAME=?";
        
        pstmt = con.prepareStatement(SQL);    
        pstmt.setString(1,pName);      //  Reference String
        rs = pstmt.executeQuery();

        if (rs.next()){          
            return("Duplicate Description found");
        }       
       return("");
    }catch (Exception e){
        return("");
}
}   
  
private Double getSellPrice(String pSellPrice){
  // Check if the selling price icludes taxes 
    if (jCheckSellIncTax.isSelected()){
        return ((Double.parseDouble(pSellPrice))/(1 + dTaxRate));
    }else{
        return(Double.parseDouble(pSellPrice));        
    }   
  }
  
private String updateRecord(String pID, Double pBuy, Double pSell) {

    
// always update record with the tax set for it.    
    try{
      SQL="SELECT TAXES.RATE FROM TAXES, PRODUCTS WHERE PRODUCTS.ID ='" + pID +"' AND TAXES.ID=PRODUCTS.TAXCAT";  
        rs = stmt.executeQuery(SQL);
        while (rs.next()){
            dOriginalRate = (rs.getDouble("rate"));                
        } 
        
       // if (jCheckSellIncTax.isSelected()){
            pSell=pSell/(1 + dOriginalRate);
       //  }
        
        SQL="SELECT * FROM PRODUCTS WHERE ID='" + pID + "'";
        rs = stmt.executeQuery(SQL);
        while (rs.next()){
            oldSell = rs.getDouble("Pricesell");
            oldBuy = rs.getDouble("pricebuy");
        }
        
        

// Now we can update the record    
    SQL = "UPDATE PRODUCTS "
            + "SET PRICESELL=?, "
            + "PRICEBUY=? "
            + "WHERE ID=?";
    
            pstmt=con.prepareStatement(SQL);
            pstmt.setDouble(1,pSell); 
            pstmt.setDouble(2,pBuy);    
            pstmt.setString(3, ID);
            pstmt.executeUpdate();
        }catch(Exception e){
            }
    
        if ((oldSell != pSell) & (oldBuy !=pBuy)){
                return("Buy and Sell prices changed");
        }else{
            if (oldSell != pSell){
                return("Selling price changed");
            }else{
               if (oldBuy != pBuy){ 
                return("Buy price changed");
            }else {
               return("");
               }
            }            
            }
}

private void CSVData(Integer currentRow, String pReference, String pBarcode, 
        String pName , Double pBuy, Double pSell, String pCSVError, 
        Double pPrevBuy, Double pPrevSell ){
    ID = UUID.randomUUID().toString();
    
    SQL="INSERT INTO CSVIMPORT (ID, "
            + "ROWNUMBER, "
            + "CSVERROR, "
            + "REFERENCE, "
            + "CODE, "
            + "NAME, "
            + "PRICEBUY, "
            + "PRICESELL, "
            + "PREVIOUSBUY, "
            + "PREVIOUSSELL) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
     
    try{           
        pstmt = con.prepareStatement(SQL);
        pstmt.setString(1,ID);       //  Category String
        pstmt.setString(2,Integer.toString(currentRow));       
        pstmt.setString(3,pCSVError);       //  Category String
        pstmt.setString(4,pReference);      //  Reference String
        if ("".equals(pReference)){
            pstmt.setString(4,null);
         }  
        pstmt.setString(5,pBarcode);        //  Barcode String
        if ("".equals(pBarcode)){
            pstmt.setString(5,null);
         }
        pstmt.setString(6,pName);           //  Name String
        if ("".equals(pName)){
            pstmt.setString(6,null);
         }
        
        
         if (pBuy == null){
            pstmt.setNull(7,java.sql.Types.DOUBLE);
         }else{
            pstmt.setDouble(7,pBuy);        //  Buyprice Double
         }       
         if (pSell == null){
            pstmt.setNull(8,java.sql.Types.DOUBLE);
         }else{
            pstmt.setDouble(8,pSell);        //  Buyprice Double
         }       
         if (pPrevBuy == null){
            pstmt.setNull(9,java.sql.Types.DOUBLE);
         }else{
            pstmt.setDouble(9,pPrevBuy);        //  Buyprice Double
         }        
         if (pPrevSell == null){
            pstmt.setNull(10,java.sql.Types.DOUBLE);
         }else{
            pstmt.setDouble(10,pPrevSell);        //  Buyprice Double
         }
     
        pstmt.executeUpdate();
    } catch (SQLException e){
        System.out.println(e.getMessage());
    }
    
}

private void addRecord (String pReference, String pName , String pBarcode, Double pBuy, Double pSell, String pCategory, String pTax){
        ID = UUID.randomUUID().toString();
      
// Check for earlier versions
//       switch (pre302){
/*           case 1:
              SQL="INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ATTRIBUTESET_ID, IMAGE, STOCKCOST,"
                + "STOCKVOLUME, ATTRIBUTES, ISKITCHEN, CODETYPE, PRINTKB, SENDSTATUS, ISSERVICE) VALUES" 
                + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";   
               break;
           case 2:
              SQL="INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ATTRIBUTESET_ID, IMAGE, STOCKCOST,"
                + "STOCKVOLUME, ATTRIBUTES, ISKITCHEN, CODETYPE, PRINTKB, SENDSTATUS, ISSERVICE, DISPLAY) VALUES" 
                + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";                
               break;
           case 4:
              SQL="INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ATTRIBUTESET_ID, IMAGE, STOCKCOST,"
                + "STOCKVOLUME, ATTRIBUTES, ISKITCHEN, CODETYPE, PRINTKB, SENDSTATUS, ISSERVICE, DISPLAY, ISVPRICE) VALUES" 
                + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";                 
               break;
           case 8:
*/
SQL="INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ATTRIBUTESET_ID, IMAGE, STOCKCOST,"
                + "STOCKVOLUME, ATTRIBUTES, ISKITCHEN, CODETYPE, PRINTKB, SENDSTATUS, ISSERVICE, DISPLAY, ISVPRICE, ISVERPATRIB, TEXTTIP, WARRANTY) VALUES" 
                + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";  
/*               break;
           default:
               SQL="INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ATTRIBUTESET_ID, IMAGE, STOCKCOST,"
                + "STOCKVOLUME, ATTRIBUTES, ISKITCHEN, CODETYPE, PRINTKB, SENDSTATUS) VALUES" 
                + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";   
       } 
  */      
       try{           
        pstmt = con.prepareStatement(SQL);
        pstmt.setString(1, ID);             //  ID String
        pstmt.setString(2,pReference);      //  Reference String
        pstmt.setString(3,pBarcode);        //  Barcode String
        pstmt.setString(4,pName);           //  Name String
        pstmt.setBoolean(5,false);          //  Iscom   Boolean
        pstmt.setBoolean(6,false);          //  isscale Boolean
        pstmt.setDouble(7,pBuy);            //  Buyprice Double
        pstmt.setDouble(8,pSell);           //  Sell price double
        pstmt.setString(9,pCategory);       //  Category String
        pstmt.setString(10,pTax);           //  TaxCat String
        pstmt.setString(11,null);           //  Attributeset String
        pstmt.setBytes(12,null);            //  Image Image
        pstmt.setNull(13,java.sql.Types.DOUBLE); //  Stock cost Double
        pstmt.setNull(14,java.sql.Types.DOUBLE); //  Stock volume Double
        pstmt.setBytes(15,null);           //  Attrubutes Bytes
        pstmt.setBoolean(16,false);         //  Iskitchen Boolean       
        pstmt.setString(17,null);           //  Codetype String
        pstmt.setBoolean(18,false);         //  Printkb Boolean
        pstmt.setBoolean(19,false);         //  Sendstatus Boolean      
    //    switch (pre302){
    //        case 8:
                pstmt.setBoolean(20,false);         //  Isserice Boolean
                pstmt.setString(21,"<HTML>" + pName);//  Display String
                pstmt.setBoolean(22,false);         //  isvprice Boolean
                pstmt.setBoolean(23,false);         //  isverattrib Boolean 
                pstmt.setString(24,pName);           //  set the text tip message
                pstmt.setBoolean(25,false);
      /*      case 4:
                pstmt.setBoolean(20,false);         //  Isserice Boolean
                pstmt.setString(21,"<HTML>" + pName);//  Display String
                pstmt.setBoolean(22,false);         //  isvprice Boolean                
            case 2:
                pstmt.setBoolean(20,false);         //  Isserice Boolean
                pstmt.setString(21,"<HTML>" + pName);//  Display String              
            case 1:    
                pstmt.setBoolean(20,false);         //  Isserice Boolean                                
        */
         //}
                        
// insert the record        
        pstmt.executeUpdate();
         
 // put into catalogue if required  
        if (jCheckInCatalogue.isSelected()){
        SQL="INSERT INTO products_cat (product, catorder ) VALUES(?, ?)";
        pstmt = con.prepareStatement(SQL);
        pstmt.setString(1,ID );             
        pstmt.setNull(2,java.sql.Types.INTEGER); 
        pstmt.executeUpdate();     
        }
    } catch (Exception e){
            
        }
 }   
    
    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.CSVImport");
    } 
    
    @Override
    public JComponent getComponent() {
        return this;
    }
     
    @Override
    public void activate() throws BasicException {

         try{
            stmt = (Statement) con.createStatement();           
            
// get the categories and populate our comboboxes
        SQL = "SELECT name from categories";     
        rs = stmt.executeQuery(SQL);
        while (rs.next()){
            jComboCategory.addItem(rs.getString("name"));           
        }                 
// get the taxcategories and populate our combobox
        SQL = "SELECT name from taxcategories";     
        rs = stmt.executeQuery(SQL);
        while (rs.next()){
            jComboTax.addItem(rs.getString("name"));           
        }
/* 
        md=con.getMetaData();
        rs=md.getColumns(null,null,"products","isservice");
        if (rs.next()){
            pre302=1;
        }
        rs=md.getColumns(null,null,"products","display");
         if (rs.next()){
            pre302 = 2;
        }   
        rs=md.getColumns(null,null,"products","isvprice");
         if (rs.next()){
            pre302 = 4;
        }
        rs=md.getColumns(null,null,"products","isverpatrib");
         if (rs.next()){
            pre302 = 8;
        }
*/         
            } catch (Exception e) {                
       }    

        jComboSeparator.removeAllItems();
     // Set the column delimiter
        jComboSeparator.addItem(",");   
        jComboSeparator.addItem(";"); 
        jComboSeparator.addItem("~"); 
        jComboSeparator.addItem("^");       
        


    }
    


public void resetFields() {
// Clear the form
        jComboReference.removeAllItems();
        jComboReference.setEnabled(false);
        jComboName.removeAllItems(); 
        jComboName.setEnabled(false);
        jComboBarcode.removeAllItems(); 
        jComboBarcode.setEnabled(false);
        jComboBuy.removeAllItems(); 
        jComboBuy.setEnabled(false);
        jComboSell.removeAllItems(); 
        jComboSell.setEnabled(false);
        jImport.setEnabled(false);
        jHeaderRead.setEnabled(false);
        jCheckInCatalogue.setSelected(false);
        jCheckInCatalogue.setEnabled(false);
        jCheckSellIncTax.setSelected(false);
        jCheckSellIncTax.setEnabled(false);
        jFileName.setText(null);
        csvFileName = "";
        jTextNew.setText("");
        jTextUpdate.setText("");
        jTextInvalid.setText("");
        jTextMissing.setText("");
        jTextNoChange.setText("");
        jTextRecords.setText("");
        jTextBadPrice.setText("");
        Headers.clear();  
   }

public void checkFieldMapping(){
    if (jComboReference.getSelectedItem() != "" & jComboName.getSelectedItem() != "" & jComboBarcode.getSelectedItem() != "" & 
                                jComboBuy.getSelectedItem() != "" & jComboSell.getSelectedItem() != "") {
        jImport.setEnabled(true);
    }else{
        jImport.setEnabled(false);
    }
}

    @Override
    public boolean deactivate() {
        resetFields();
        jComboCategory.removeAllItems();
        jComboTax.removeAllItems();
/*        
        try{
            pstmt.close();
            rs.close();
            stmt.close();
            con.close();
        }catch (Exception e){}
   */     
        return (true);
    }    
  
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooserPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jFileName = new javax.swing.JTextField();
        jbtnDbDriverLib = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jComboReference = new javax.swing.JComboBox();
        jComboBarcode = new javax.swing.JComboBox();
        jComboName = new javax.swing.JComboBox();
        jComboBuy = new javax.swing.JComboBox();
        jComboSell = new javax.swing.JComboBox();
        jComboCategory = new javax.swing.JComboBox();
        jComboTax = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCheckInCatalogue = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jCheckSellIncTax = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboSeparator = new javax.swing.JComboBox();
        jHeaderRead = new javax.swing.JButton();
        jImport = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextUpdates = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextRecords = new javax.swing.JTextField();
        jTextNew = new javax.swing.JTextField();
        jTextInvalid = new javax.swing.JTextField();
        jTextUpdate = new javax.swing.JTextField();
        jTextMissing = new javax.swing.JTextField();
        jTextBadPrice = new javax.swing.JTextField();
        jTextNoChange = new javax.swing.JTextField();

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(630, 430));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N
        jLabel1.setText(bundle.getString("label.csvfile")); // NOI18N

        jFileName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jFileName.setPreferredSize(new java.awt.Dimension(80, 25));
        jFileName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileNameActionPerformed(evt);
            }
        });

        jbtnDbDriverLib.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileopen.png"))); // NOI18N
        jbtnDbDriverLib.setMaximumSize(new java.awt.Dimension(64, 32));
        jbtnDbDriverLib.setMinimumSize(new java.awt.Dimension(64, 32));
        jbtnDbDriverLib.setPreferredSize(new java.awt.Dimension(64, 32));
        jbtnDbDriverLib.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDbDriverLibActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFileChooserPanelLayout = new javax.swing.GroupLayout(jFileChooserPanel);
        jFileChooserPanel.setLayout(jFileChooserPanelLayout);
        jFileChooserPanelLayout.setHorizontalGroup(
            jFileChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFileChooserPanelLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtnDbDriverLib, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );
        jFileChooserPanelLayout.setVerticalGroup(
            jFileChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFileChooserPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jFileChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jFileChooserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnDbDriverLib, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jComboReference.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboReference.setEnabled(false);
        jComboReference.setMinimumSize(new java.awt.Dimension(32, 25));
        jComboReference.setOpaque(false);
        jComboReference.setPreferredSize(new java.awt.Dimension(32, 25));
        jComboReference.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboReferenceItemStateChanged(evt);
            }
        });
        jComboReference.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboReferenceFocusGained(evt);
            }
        });

        jComboBarcode.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBarcode.setEnabled(false);
        jComboBarcode.setMinimumSize(new java.awt.Dimension(32, 25));
        jComboBarcode.setPreferredSize(new java.awt.Dimension(32, 25));
        jComboBarcode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBarcodeItemStateChanged(evt);
            }
        });
        jComboBarcode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBarcodeFocusGained(evt);
            }
        });

        jComboName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboName.setEnabled(false);
        jComboName.setMinimumSize(new java.awt.Dimension(32, 25));
        jComboName.setPreferredSize(new java.awt.Dimension(32, 25));
        jComboName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboNameItemStateChanged(evt);
            }
        });
        jComboName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboNameFocusGained(evt);
            }
        });

        jComboBuy.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBuy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        jComboBuy.setEnabled(false);
        jComboBuy.setMinimumSize(new java.awt.Dimension(32, 25));
        jComboBuy.setPreferredSize(new java.awt.Dimension(32, 25));
        jComboBuy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBuyItemStateChanged(evt);
            }
        });
        jComboBuy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBuyFocusGained(evt);
            }
        });

        jComboSell.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboSell.setEnabled(false);
        jComboSell.setMinimumSize(new java.awt.Dimension(32, 25));
        jComboSell.setPreferredSize(new java.awt.Dimension(32, 25));
        jComboSell.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboSellItemStateChanged(evt);
            }
        });
        jComboSell.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboSellFocusGained(evt);
            }
        });

        jComboCategory.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboCategory.setMinimumSize(new java.awt.Dimension(32, 25));
        jComboCategory.setPreferredSize(new java.awt.Dimension(32, 25));
        jComboCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboCategoryActionPerformed(evt);
            }
        });

        jComboTax.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboTax.setPreferredSize(new java.awt.Dimension(32, 25));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(bundle.getString("label.prodref")); // NOI18N

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText(bundle.getString("label.prodbarcode")); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText(bundle.getString("label.prodname")); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText(bundle.getString("label.prodpricebuy")); // NOI18N

        jLabel11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel11.setText(bundle.getString("label.prodpricesell")); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText(bundle.getString("label.prodcategory")); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText(bundle.getString("label.prodtaxcode")); // NOI18N

        jCheckInCatalogue.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText(bundle.getString("label.prodincatalog")); // NOI18N

        jCheckSellIncTax.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText(bundle.getString("label.csvsellingintax")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboReference, 0, 272, Short.MAX_VALUE)
                    .addComponent(jComboBarcode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBuy, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboSell, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboTax, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckInCatalogue, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckSellIncTax)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jComboReference, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBuy, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboSell, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboTax, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckInCatalogue)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckSellIncTax))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel17.setText("Import Version V1.3");

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setText(bundle.getString("label.csvdelimit")); // NOI18N

        jComboSeparator.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jComboSeparator.setPreferredSize(new java.awt.Dimension(29, 25));

        jHeaderRead.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jHeaderRead.setText(bundle.getString("label.csvread")); // NOI18N
        jHeaderRead.setEnabled(false);
        jHeaderRead.setPreferredSize(new java.awt.Dimension(65, 23));
        jHeaderRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHeaderReadActionPerformed(evt);
            }
        });

        jImport.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jImport.setText(bundle.getString("label.csvimpostbtn")); // NOI18N
        jImport.setEnabled(false);
        jImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jImportActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), bundle.getString("title.CSVImport"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel9.setText(bundle.getString("label.csvrecordsfound")); // NOI18N

        jLabel14.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel14.setText(bundle.getString("label.csvnewproducts")); // NOI18N
        jLabel14.setMaximumSize(new java.awt.Dimension(77, 14));
        jLabel14.setMinimumSize(new java.awt.Dimension(77, 14));
        jLabel14.setPreferredSize(new java.awt.Dimension(77, 14));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel16.setText(bundle.getString("label.cvsinvalid")); // NOI18N

        jTextUpdates.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jTextUpdates.setText(bundle.getString("label.csvpriceupdated")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel2.setText(bundle.getString("label.csvmissing")); // NOI18N

        jLabel15.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel15.setText(bundle.getString("label.csvbad")); // NOI18N

        jLabel13.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel13.setText(bundle.getString("label.cvsnotchanged")); // NOI18N

        jTextRecords.setBackground(new java.awt.Color(224, 223, 227));
        jTextRecords.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jTextRecords.setForeground(new java.awt.Color(102, 102, 102));
        jTextRecords.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextRecords.setBorder(null);
        jTextRecords.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextRecords.setEnabled(false);

        jTextNew.setBackground(new java.awt.Color(224, 223, 227));
        jTextNew.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jTextNew.setForeground(new java.awt.Color(102, 102, 102));
        jTextNew.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextNew.setBorder(null);
        jTextNew.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextNew.setEnabled(false);

        jTextInvalid.setBackground(new java.awt.Color(224, 223, 227));
        jTextInvalid.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jTextInvalid.setForeground(new java.awt.Color(102, 102, 102));
        jTextInvalid.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextInvalid.setBorder(null);
        jTextInvalid.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextInvalid.setEnabled(false);

        jTextUpdate.setBackground(new java.awt.Color(224, 223, 227));
        jTextUpdate.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jTextUpdate.setForeground(new java.awt.Color(102, 102, 102));
        jTextUpdate.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextUpdate.setBorder(null);
        jTextUpdate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextUpdate.setEnabled(false);

        jTextMissing.setBackground(new java.awt.Color(224, 223, 227));
        jTextMissing.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jTextMissing.setForeground(new java.awt.Color(102, 102, 102));
        jTextMissing.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextMissing.setBorder(null);
        jTextMissing.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextMissing.setEnabled(false);

        jTextBadPrice.setBackground(new java.awt.Color(224, 223, 227));
        jTextBadPrice.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jTextBadPrice.setForeground(new java.awt.Color(102, 102, 102));
        jTextBadPrice.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextBadPrice.setBorder(null);
        jTextBadPrice.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextBadPrice.setEnabled(false);

        jTextNoChange.setBackground(new java.awt.Color(224, 223, 227));
        jTextNoChange.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jTextNoChange.setForeground(new java.awt.Color(102, 102, 102));
        jTextNoChange.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextNoChange.setBorder(null);
        jTextNoChange.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextNoChange.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextUpdates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextNew, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextInvalid, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextMissing, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextBadPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextNoChange, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextNew, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextInvalid, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextUpdates, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextMissing, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextBadPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextNoChange, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(386, 386, 386)
                                .addComponent(jComboSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jFileChooserPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jHeaderRead, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jImport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFileChooserPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jImport, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jHeaderRead, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jHeaderReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHeaderReadActionPerformed
       try {            
                GetheadersFromFile(jFileName.getText());
            } catch (IOException ex) {
                Logger.getLogger(JPanelCSVImport.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_jHeaderReadActionPerformed

    private void jImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jImportActionPerformed
// prevent any more key presses
        jImport.setEnabled(false);   
        
        try {            
                ImportCsvFile(jFileName.getText());
            } catch (IOException ex) {
                Logger.getLogger(JPanelCSVImport.class.getName()).log(Level.SEVERE, null, ex);
            }
       
    }//GEN-LAST:event_jImportActionPerformed

    private void jFileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileNameActionPerformed
            jImport.setEnabled(false);
            jHeaderRead.setEnabled(true);
    }//GEN-LAST:event_jFileNameActionPerformed

    private void jbtnDbDriverLibActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDbDriverLibActionPerformed
        resetFields();
        JFileChooser chooser = new JFileChooser("C:\\");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv files", "csv");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);      
        File csvFile = chooser.getSelectedFile();
// check if a file was selected        
        if (csvFile == null){
            return;
        }
        String csv = csvFile.getName();
        if (!(csv.trim().equals(""))) {
            csvFileName = csvFile.getAbsolutePath();
            jFileName.setText(csvFileName);
           }        
    }//GEN-LAST:event_jbtnDbDriverLibActionPerformed

    private void jComboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboCategoryActionPerformed

    private void jComboSellFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboSellFocusGained
        jComboSell.removeAllItems();
        int i =1;
        jComboSell.addItem("");
        while(i < Headers.size()){
            if ((Headers.get(i) != jComboReference.getSelectedItem()) & (Headers.get(i) != jComboName.getSelectedItem()) & (Headers.get(i) != jComboBuy.getSelectedItem()) & (Headers.get(i) != jComboBarcode.getSelectedItem())) {
                jComboSell.addItem(Headers.get(i));
            }
            ++i;
        }
    }//GEN-LAST:event_jComboSellFocusGained

    private void jComboSellItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboSellItemStateChanged
        checkFieldMapping();
    }//GEN-LAST:event_jComboSellItemStateChanged

    private void jComboBuyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBuyFocusGained
        jComboBuy.removeAllItems();
        int i =1;
        jComboBuy.addItem("");
        while(i < Headers.size()){
            if ((Headers.get(i) != jComboReference.getSelectedItem()) & (Headers.get(i) != jComboName.getSelectedItem()) & (Headers.get(i) != jComboBarcode.getSelectedItem()) & (Headers.get(i) != jComboSell.getSelectedItem())) {
                jComboBuy.addItem(Headers.get(i));
            }
            ++i;
        }
    }//GEN-LAST:event_jComboBuyFocusGained

    private void jComboBuyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBuyItemStateChanged
        checkFieldMapping();
    }//GEN-LAST:event_jComboBuyItemStateChanged

    private void jComboNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboNameFocusGained
        jComboName.removeAllItems();
        int i =1;
        jComboName.addItem("");
        while(i < Headers.size()){
            if ((Headers.get(i) != jComboReference.getSelectedItem()) & (Headers.get(i) != jComboBarcode.getSelectedItem()) & (Headers.get(i) != jComboBuy.getSelectedItem()) & (Headers.get(i) != jComboSell.getSelectedItem())) {
                jComboName.addItem(Headers.get(i));
            }
            ++i;
        }
    }//GEN-LAST:event_jComboNameFocusGained

    private void jComboNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboNameItemStateChanged
        checkFieldMapping();
    }//GEN-LAST:event_jComboNameItemStateChanged

    private void jComboBarcodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBarcodeFocusGained
        jComboBarcode.removeAllItems();
        int i =1;
        jComboBarcode.addItem("");
        while(i < Headers.size()){
            if ((Headers.get(i) != jComboReference.getSelectedItem()) & (Headers.get(i) != jComboName.getSelectedItem()) & (Headers.get(i) != jComboBuy.getSelectedItem()) & (Headers.get(i) != jComboSell.getSelectedItem())) {
                jComboBarcode.addItem(Headers.get(i));
            }
            ++i;
        }
    }//GEN-LAST:event_jComboBarcodeFocusGained

    private void jComboBarcodeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBarcodeItemStateChanged
        checkFieldMapping();
    }//GEN-LAST:event_jComboBarcodeItemStateChanged

    private void jComboReferenceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboReferenceFocusGained
        jComboReference.removeAllItems();
        int i =1;
        jComboReference.addItem("");
        while(i < Headers.size()){
            if ((Headers.get(i) != jComboBarcode.getSelectedItem()) & (Headers.get(i) != jComboName.getSelectedItem()) & (Headers.get(i) != jComboBuy.getSelectedItem()) & (Headers.get(i) != jComboSell.getSelectedItem())) {
                jComboReference.addItem(Headers.get(i));
            }
            ++i;
        }
    }//GEN-LAST:event_jComboReferenceFocusGained

    private void jComboReferenceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboReferenceItemStateChanged
        checkFieldMapping();
    }//GEN-LAST:event_jComboReferenceItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckInCatalogue;
    private javax.swing.JCheckBox jCheckSellIncTax;
    private javax.swing.JComboBox jComboBarcode;
    private javax.swing.JComboBox jComboBuy;
    private javax.swing.JComboBox jComboCategory;
    private javax.swing.JComboBox jComboName;
    private javax.swing.JComboBox jComboReference;
    private javax.swing.JComboBox jComboSell;
    private javax.swing.JComboBox jComboSeparator;
    private javax.swing.JComboBox jComboTax;
    private javax.swing.JPanel jFileChooserPanel;
    private javax.swing.JTextField jFileName;
    private javax.swing.JButton jHeaderRead;
    private javax.swing.JButton jImport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextBadPrice;
    private javax.swing.JTextField jTextInvalid;
    private javax.swing.JTextField jTextMissing;
    private javax.swing.JTextField jTextNew;
    private javax.swing.JTextField jTextNoChange;
    private javax.swing.JTextField jTextRecords;
    private javax.swing.JTextField jTextUpdate;
    private javax.swing.JLabel jTextUpdates;
    private javax.swing.JButton jbtnDbDriverLib;
    // End of variables declaration//GEN-END:variables

    

}
