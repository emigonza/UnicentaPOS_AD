/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales.restaurant;

import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;




/**
 *
 * @author JDL
 */


public class RestaurantDBUtils {
    private Session s;
    private Connection con;  
    private Statement stmt;
    private PreparedStatement pstmt;
    private String SQL;
    private ResultSet rs;
    private AppView m_App;
    protected DataLogicSystem dlSystem;
    
    
public RestaurantDBUtils(AppView oApp) {
        m_App=oApp;
        
                              
//get database connection details        
       try{
            s=m_App.getSession();
            con=s.getConnection();                      
        }
        catch (Exception e){System.out.print("No session or connection");
        }
        

    }

public void moveCustomer(String newTable, String ticketID){
      String oldTable=getTableDetails(ticketID); 
        if (countTicketIdInTable(ticketID)>1){
            setCustomerNameInTable(getCustomerNameInTable(oldTable),newTable);
            setWaiterNameInTable(getWaiterNameInTable(oldTable),newTable);  
            setTicketIdInTable(ticketID,newTable);
// remove the data for the old table 
            oldTable = getTableMovedName(ticketID);  
                if ((oldTable != null) && (oldTable != newTable)){
                    clearCustomerNameInTable(oldTable);
                    clearWaiterNameInTable(oldTable); 
                    clearTicketIdInTable(oldTable);
                    clearTableMovedFlag(oldTable);
                }
                else{
                    oldTable = getTableMovedName(ticketID);
                    clearTableMovedFlag(oldTable);                    
                }      
        }
  }


public void setCustomerNameInTable(String custName, String tableName){
        try{
            SQL = "UPDATE PLACES SET CUSTOMER=? WHERE NAME=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,custName); 
            pstmt.setString(2,tableName);    
            pstmt.executeUpdate();
        }catch(Exception e){
            }
    }
 
public void setCustomerNameInTableById(String custName, String tableID){
        try{
            SQL = "UPDATE PLACES SET CUSTOMER=? WHERE ID=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,custName); 
            pstmt.setString(2,tableID);    
            pstmt.executeUpdate();
        }catch(Exception e){
            }
    }

public void setCustomerNameInTableByTicketId(String custName, String ticketID){
        try{
            SQL = "UPDATE PLACES SET CUSTOMER=? WHERE TICKETID=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,custName); 
            pstmt.setString(2,ticketID);    
            pstmt.executeUpdate();
        }catch(Exception e){
            }
}


  public String getCustomerNameInTable(String tableName){
       try{
            SQL = "SELECT CUSTOMER FROM PLACES WHERE NAME='"+ tableName + "'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            String customer =rs.getString("CUSTOMER");
            return(customer);
        }    
        }catch(Exception e){
            }   
       return "";
  }

    public String getCustomerNameInTableById(String tableId){
       try{
            SQL = "SELECT CUSTOMER FROM PLACES WHERE ID='"+ tableId + "'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            String customer =rs.getString("CUSTOMER");
            return(customer);
        }    
        }catch(Exception e){
            }   
       return "";
  }
  
    
    
 public void clearCustomerNameInTable(String tableName){
        try{
            SQL = "UPDATE PLACES SET CUSTOMER=null WHERE NAME=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,tableName);     
            pstmt.executeUpdate();
        }catch(Exception e){
            }
    }

  public void clearCustomerNameInTableById(String tableID){
        try{
            SQL = "UPDATE PLACES SET CUSTOMER=null WHERE ID=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,tableID);     
            pstmt.executeUpdate();
        }catch(Exception e){
            }
    }
 
  public void setWaiterNameInTable(String waiterName, String tableName){
        try{
            SQL = "UPDATE PLACES SET WAITER=? WHERE NAME=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,waiterName); 
            pstmt.setString(2,tableName);    
            pstmt.executeUpdate();
        }catch(Exception e){
            }
    }
  
    public void setWaiterNameInTableById(String waiterName, String tableID){
        try{
            SQL = "UPDATE PLACES SET WAITER=? WHERE ID=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,waiterName); 
            pstmt.setString(2,tableID);    
            pstmt.executeUpdate();
        }catch(Exception e){
            }
    }
  
    
 public String getWaiterNameInTable(String tableName){
       try{
            SQL = "SELECT WAITER FROM PLACES WHERE NAME='"+ tableName + "'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            String waiter =rs.getString("WAITER");
            return(waiter);
        }    
        }catch(Exception e){
            }   
       return "";
  }  
  
  public String getWaiterNameInTableById(String tableID){
       try{
            SQL = "SELECT WAITER FROM PLACES WHERE ID='"+ tableID + "'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            String waiter =rs.getString("WAITER");
            return(waiter);
        }    
        }catch(Exception e){
            }   
       return "";
  } 
 
  
 public void clearWaiterNameInTable(String tableName){
        try{
            SQL = "UPDATE PLACES SET WAITER=null WHERE NAME=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,tableName);     
            pstmt.executeUpdate();
        }catch(Exception e){
            }
       }
    

  public void clearWaiterNameInTableById(String tableID){
        try{
            SQL = "UPDATE PLACES SET WAITER=null WHERE ID=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,tableID);     
            pstmt.executeUpdate();
        }catch(Exception e){
            }
       }
    
 
    public String getTicketIdInTable(String ID){
       try{
            SQL = "SELECT TICKETID FROM PLACES WHERE ID='"+ ID + "'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            String customer =rs.getString("TICKETID");
            return(customer);
        }    
        }catch(Exception e){
            }   
       return "";
  }  

      public void setTicketIdInTable(String TicketID, String tableName){
        try{
            SQL = "UPDATE PLACES SET TICKETID=? WHERE NAME=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,TicketID); 
            pstmt.setString(2,tableName);    
            pstmt.executeUpdate();
        }catch(Exception e){
            }
    }  
      
   public void clearTicketIdInTable(String tableName){
        try{
            SQL = "UPDATE PLACES SET TICKETID=null WHERE NAME=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,tableName);     
            pstmt.executeUpdate();
        }catch(Exception e){
            }
       }
   
   
   
  public void clearTicketIdInTableById(String tableID){
        try{
            SQL = "UPDATE PLACES SET TICKETID=null WHERE ID=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,tableID);     
            pstmt.executeUpdate();
        }catch(Exception e){
            }
       }
 public Integer countTicketIdInTable(String ticketID){
      try{
            SQL = "SELECT COUNT(*) AS RECORDCOUNT FROM PLACES WHERE TICKETID='"+ ticketID + "'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            Integer count =rs.getInt("RECORDCOUNT");
            return(count);
        }    
        }catch(Exception e){
            }
     
     return 0;
 }
      
      
   public String getTableDetails (String ticketID){
       try{
            SQL = "SELECT NAME FROM PLACES WHERE TICKETID='"+ ticketID + "'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            String name =rs.getString("NAME");
            return(name);
        }    
        }catch(Exception e){
            }
       return "";
   }
  
 public void setTableMovedFlag (String tableID){
        try{
            SQL = "UPDATE PLACES SET TABLEMOVED='true' WHERE ID=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,tableID);     
            pstmt.executeUpdate();
        }catch(Exception e){
            }
       }   
  
 public String getTableMovedName (String ticketID){
       try{
            SQL = "SELECT NAME FROM PLACES WHERE TICKETID='"+ ticketID + "' AND TABLEMOVED ='true'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            String name =rs.getString("NAME");
            return(name);
        }    
        }catch(Exception e){
            }
       return null;
       }  

  public Boolean getTableMovedFlag (String ticketID){
       try{
            SQL = "SELECT TABLEMOVED FROM PLACES WHERE TICKETID='"+ ticketID + "'";   
            stmt = (Statement) con.createStatement();  
            rs = stmt.executeQuery(SQL);
      if (rs.next()){
            return(rs.getBoolean("TABLEMOVED"));
        }    
        }catch(Exception e){
            }
       return (false);
       }  
 
 public void clearTableMovedFlag (String tableID){
        try{
            SQL = "UPDATE PLACES SET TABLEMOVED='false' WHERE NAME=?";
            pstmt=con.prepareStatement(SQL);
            pstmt.setString(1,tableID);     
            pstmt.executeUpdate();
        }catch(Exception e){
            }
       }  
 
}

