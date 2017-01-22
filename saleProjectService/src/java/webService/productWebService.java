/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webService;

import identityConnect.identityConnect;
import databaseAdapter.DBConnection;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import javax.servlet.ServletException;
/**
 *
 * @author Kris
 */
@WebService(serviceName = "productWebService")
public class productWebService {
    Connection con = DBConnection.connectDB();
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    
    /**
     * Web service operation
     * @param uid
     * @return 
     */
    @WebMethod(operationName = "getAllProductByUserID")
    public String[][] getAllProductByUserID(@WebParam(name = "uid") int uid) {
        String querySQL = "SELECT  * FROM catalog WHERE userID = ?";
        try {
            PreparedStatement SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setInt(1, uid);
            ResultSet resultSQL = SQLStatement.executeQuery();
            resultSQL.last();
            String[][] result = new String[resultSQL.getRow()][6];
            resultSQL.beforeFirst();
            int i =0;
            while (resultSQL.next()) {
                result[i][0] = resultSQL.getString("productName");
                result[i][1] = resultSQL.getString("price");
                result[i][2] = resultSQL.getString("description");
                result[i][3] = resultSQL.getString("addDate");
                result[i][4] = resultSQL.getString("id_product");
                byte[] pic = resultSQL.getBytes("pict");
                result[i][5] = Base64.getEncoder().encodeToString(pic);
                i++;
            }
            con.close();
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getLikesCount")
    public int getLikesCount(@WebParam(name = "product_id") int product_id) {
        //TODO write your implementation code here:
        String querySQL = "SELECT  * FROM product_like WHERE id_product = ?";
        try {
            PreparedStatement SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setInt(1, product_id);
            ResultSet resultSQL = SQLStatement.executeQuery();
            resultSQL.last();
            
            con.close();
            return resultSQL.getRow();
        } catch (SQLException ex) {
            return 0;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getPurchasesCount")
    public int getPurchasesCount(@WebParam(name = "product_id") int product_id) {
        String querySQL = "SELECT  * FROM purchase_history WHERE idproduct = ?";
        try {
            PreparedStatement SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setInt(1, product_id);
            ResultSet resultSQL = SQLStatement.executeQuery();
            resultSQL.last();
            con.close();
            return resultSQL.getRow();
        } catch (SQLException ex) {
            return 0;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteProduct")
    public void deleteProduct(@WebParam(name = "idproduct") int idproduct) {
        String querySQL = "DELETE FROM catalog WHERE id_product = ?";
        try {
            PreparedStatement SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setInt(1, idproduct);
            SQLStatement.execute();
            querySQL = "DELETE FROM product_like WHERE id_product = ?";
            SQLStatement.setInt(1, idproduct);
            SQLStatement.execute();
            
            con.close();
        } catch (SQLException ex) {
        }
    }
/**
     * Web service operation
     * @param uid
     * @return 
     */
    @WebMethod(operationName = "getSalesProductByUserID")
    public String[][] getSalesProductByUserID(@WebParam(name = "uid") int uid) {
        String query= "SELECT DATE_FORMAT(buyTime, '%W, %e %M %Y') as date, DATE_FORMAT(buyTime, 'at %H:%i') as waktu, pict, price, productName, userID_Buy, qty, rcv_name, rcv_addr, rcv_city, rcv_phone, rcv_postal FROM purchase_history WHERE userID_sell="+uid+" ORDER BY buyTime DESC";
      
     
        try{
            PreparedStatement smt= null;
            try {
                smt = con.prepareStatement(query);
            } catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ResultSet res = smt.executeQuery();
            res.last();
            int size = res.getRow();
            res.beforeFirst();
            String result[][] = new String[13][size];
                      
            int i=0;
            while(res.next()){
                result[0][i] = res.getString("date");
                result[1][i] = res.getString("waktu");
                
                byte[] pic = res.getBytes("pict");
                result[2][i] = Base64.getEncoder().encodeToString(pic);
                
                //result[2][i] = res.getBlob("pict")+"";
                result[3][i] = res.getInt("price")+"";
                result[4][i] = res.getString("productName");
                
                JSONObject j = identityConnect.getUser(res.getInt("userID_Buy")); 
                
                result[5][i] = j.get("uname").toString();
                result[6][i] = res.getInt("qty")+"";
                result[7][i] = res.getString("rcv_name");
                result[8][i] = res.getString("rcv_addr");
                result[9][i] = res.getString("rcv_city");
                result[10][i] = res.getString("rcv_phone");
                result[11][i] = res.getString("rcv_postal");
                result[12][i] = (res.getInt("qty") * res.getInt("price")) +"";
                i++;
            }
            
            con.close();
            return result;
        } catch (SQLException ex) {
            return null;
        }      
    }

    /**
     * Web service operation
     * @param userID
     * @param prodname
     * @param prodprice
     * @param proddesc
     * @param prodpic
     */
    @WebMethod(operationName = "addProduct")
    public String addProduct(@WebParam(name = "userID") int userID, @WebParam(name = "prodname") String prodname, @WebParam(name = "prodprice") int prodprice, @WebParam(name = "proddesc") String proddesc, @WebParam(name = "prodpic") byte[] prodpic) {
        try {
            Blob blob = new javax.sql.rowset.serial.SerialBlob(prodpic);
            String querySQL = "INSERT INTO catalog(userID, productName, price, description, addDate, pict) VALUES ('"+ userID +"','"+ prodname +"','"+ prodprice +"', '"+ proddesc +"', NOW(), ?)";
            PreparedStatement smt= null;
            smt = con.prepareStatement(querySQL);
            smt.setBlob(1, blob);
            int res = smt.executeUpdate();
            con.close();
            return "berhasil ji";
        } catch (SQLException ex) {
            return ex.getMessage();
        }
    }

    /**
    * Web service operation
    * @param prodID
    * @param prodname
    * @param prodprice
    * @param proddesc
    * @return 
    */
    @WebMethod(operationName = "editProduct")
    public String editProduct(@WebParam(name = "prodID") int prodID, @WebParam(name = "prodname") String prodname, @WebParam(name = "prodprice") int prodprice, @WebParam(name = "proddesc") String proddesc) {
        String querySQL = "UPDATE catalog SET productName='"+ prodname +"', price='"+ prodprice +"', description='"+ proddesc +"' WHERE id_product='"+ prodID +"'";
        try{
            PreparedStatement smt= null;
            smt = con.prepareStatement(querySQL);
            
            con.close();
            return Integer.toString(smt.executeUpdate());
            
        } catch (SQLException ex) {
            return (ex.getMessage());
        }
    }
    
    @WebMethod(operationName = "productRetrieve")
    public java.lang.String[] productRetrieve (@WebParam(name = "id_prod") int id_prod){
        String [] res = new String[3];
        String querySQL = "SELECT productName, price, description FROM catalog WHERE id_product='"+ id_prod +"'";
        PreparedStatement smt = null;
        try{
            smt = con.prepareStatement(querySQL);
            ResultSet rest = smt.executeQuery();
            while(rest.next()){
                res[0]= rest.getString("productName");
                res[1] = rest.getInt("price")+"";
                res[2] = rest.getString("description");
            }
            
            con.close();
        }catch (SQLException ex) {
            Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
     @WebMethod(operationName = "catalogRetrieve")
    public java.lang.String[][] catalogRetrieve (@WebParam(name = "userID") String userID, @WebParam(name = "keyword") String keyword, @WebParam(name = "category") String category )  throws ServletException, IOException  {
        
        String query="";
                
        if ("product".equals(category) && !"none".equals(keyword)) 
            query = "SELECT DATE_FORMAT(addDate, 'added this on %W, %d %M %Y, at %H:%i') as date, pict, id_product, price, productName, userID, description FROM catalog where productName LIKE '%"+keyword+"%' ORDER BY addDate DESC";
        else if ("store".equals(category) && !"none".equals(keyword)){ 
            JSONObject j = identityConnect.getUserByUserName(keyword);
            if (j!=null){
                String keyname = j.get("uid").toString();
                if (keyname!=null)
                query = "SELECT DATE_FORMAT(addDate, 'added this on %W, %d %M %Y, at %H:%i') as date, pict, id_product, price, productName, userID, description FROM catalog where userID ="+keyname+" ORDER BY addDate DESC";
            }
            else {
                return null;
            }
        }else 
            query = "SELECT DATE_FORMAT(addDate, 'added this on %W, %d %M %Y, at %H:%i') as date, pict, id_product, price, productName, userID, description FROM catalog ORDER BY addDate DESC";

        
        try{
            PreparedStatement smt= null;
            try {
                smt = con.prepareStatement(query);
            } catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSet res = smt.executeQuery();
            res.last();
            int size = res.getRow();
            res.beforeFirst();
            String result[][] = new String[11][size];
                      
            int i=0;
            while(res.next()){
                result[0][i] = res.getString("date");
                byte[] pic = res.getBytes("pict"); result[1][i] = Base64.getEncoder().encodeToString(pic);//res.getBlob("pict")+"";//new String(pc.getBytes(1l, (int) pc.length()));
                result[2][i] = res.getInt("id_product")+"";
                result[3][i] = res.getInt("price")+"";
                result[4][i] = res.getString("productName");
                result[5][i] = res.getInt("userID")+"";
                result[6][i] = res.getString("description");
                result[7][i] = sumLike(result[2][i]);
                result[8][i] = sumBuy(result[2][i]);
                result[9][i] = isLike(userID, result[2][i]);
                
                JSONObject j = identityConnect.getUser(res.getInt("userID"));
                result[10][i] = j.get("uname").toString();
                i++;
            }
            
            con.close();
            return result;
        } catch (SQLException ex) {
            return null;
        }       
    }
    
    @WebMethod(operationName = "purchaseRetrieve")
    public java.lang.String[] purchaseRetrieve (@WebParam(name = "userID") String userID, @WebParam(name = "idp") String idp){
        String [] res = new String[6];
        String qInsert = "Select productName, price from catalog where id_product="+idp;
        try{
            PreparedStatement smt = null;
            try{
                smt = con.prepareStatement(qInsert);
            }catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSet rest = smt.executeQuery();

            while(rest.next()){
                res[0]= rest.getString("productName");
                res[1] = rest.getInt("price")+"";
            }
            JSONObject i = identityConnect.getUser(1);
            JSONObject j = identityConnect.getUserByUserName(i.get("uname").toString());
            res[2] =  j.get("ufullname").toString();//(identityConnect.getUser(1).toJSONString()==null)+"";
            res[3] = j.get("ufulladdress").toString() + "."+j.get("ucity").toString();
            res[4] = j.get("upostcode").toString();
            res[5] = j.get("uphonenum").toString();
            
            con.close();
            return res;
        
        }catch (SQLException ex){
            res[0]="-";
            res[1]=ex.getMessage();
            return res;
        }//res = id.get("userID")+"";
     
    }
    
    @WebMethod(operationName = "addHistory")
    public java.lang.String addHistory(@WebParam(name = "idp") String idp,
                            @WebParam(name = "qty") String qty,
                            @WebParam(name = "uBuy") String uBuy,
                            @WebParam(name = "rcvn") String rcvn,
                            @WebParam(name = "rcvadd") String rcvadd,
                            @WebParam(name = "rcvc") String rcvc,
                            @WebParam(name = "rcvpo") String rcvpo,
                            @WebParam(name = "rcvph") String rcvph,
                            @WebParam(name = "crecard") String crecard,
                            @WebParam(name = "creval") String creval){
        String qInfo =  "SELECT productName, price, pict, userID from catalog where id_product="+idp;
        ResultSet resInfo, resInsert;
        try{
            PreparedStatement smt= null;
            try {
                smt = con.prepareStatement(qInfo);
            } catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            resInfo = smt.executeQuery();
            resInfo.next();

            Connection conIns = DBConnection.connectDB();
            PreparedStatement sIns = null;
            String qInsert = "INSERT INTO purchase_history (id_product, productName, price, qty, buyTime, pict, userID_sell, userID_buy, rcv_name, rcv_addr, rcv_city, rcv_postal, rcv_phone, credit_card, credit_value) VALUES ("+idp+",'"+resInfo.getString("productName")+"',"+ resInfo.getInt("price") +","+qty+",NOW(), ? ,"+ resInfo.getInt("userID")+","+uBuy+", '"+rcvn+"', '"+rcvadd+"', '"+rcvc+"', '"+rcvpo+"', '"+rcvph+"','"+crecard+"','"+creval+"')";   
            try{
                
                try{
                    
                    sIns = conIns.prepareStatement(qInsert);
                    Blob b = resInfo.getBlob("pict");
                    sIns.setBlob(1,b);
                    
                }catch (SQLException ex) {
                    Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
                }
                int rest = sIns.executeUpdate();
                
            con.close();
                return ("SUCC");
            }catch (SQLException ex){
                return ex.getMessage();
            }            
        } catch (SQLException ex) {
            return ex.getMessage();
        }
        
        
    }
    
    @WebMethod(operationName = "showHistory")
    public java.lang.String[][] showHistory(@WebParam(name = "idu") String idu){
        
        String query= "SELECT DATE_FORMAT(buyTime, '%W, %e %M %Y') as date, DATE_FORMAT(buyTime, 'at %H:%i') as waktu, pict, price, productName, userID_sell, qty, rcv_name, rcv_addr, rcv_city, rcv_phone, rcv_postal FROM purchase_history WHERE userID_buy="+idu+" ORDER BY buyTime DESC";
      
     
        try{
            PreparedStatement smt= null;
            try {
                smt = con.prepareStatement(query);
            } catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ResultSet res = smt.executeQuery();
            res.last();
            int size = res.getRow();
            res.beforeFirst();
            String result[][] = new String[13][size];
                      
            int i=0;
            while(res.next()){
                result[0][i] = res.getString("date");
                result[1][i] = res.getString("waktu");
                
                byte[] pic = res.getBytes("pict");
                result[2][i] = Base64.getEncoder().encodeToString(pic);
                
                //result[2][i] = res.getBlob("pict")+"";
                result[3][i] = res.getInt("price")+"";
                result[4][i] = res.getString("productName");
                
                JSONObject j = identityConnect.getUser(res.getInt("userID_sell")); 
                
                result[5][i] = j.get("uname").toString();
                result[6][i] = res.getInt("qty")+"";
                result[7][i] = res.getString("rcv_name");
                result[8][i] = res.getString("rcv_addr");
                result[9][i] = res.getString("rcv_city");
                result[10][i] = res.getString("rcv_phone");
                result[11][i] = res.getString("rcv_postal");
                result[12][i] = (res.getInt("qty") * res.getInt("price")) +"";
                i++;
            }
            
            con.close();
            return result;
        } catch (SQLException ex) {
            return null;
        }      
    }
    
    @WebMethod(operationName = "likeButton")
    public java.lang.String likeButton(@WebParam(name = "idu") String idu, @WebParam(name = "idp") String idp){
        
        String qInsert = "INSERT INTO product_like (id_product, userID) values ("+idp+", "+idu+")";
        try{
            PreparedStatement sIns = null;
            try{
                sIns = con.prepareStatement(qInsert);
            }catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            int rest = sIns.executeUpdate();
            
            con.close();
            return sumLike(idp);    
        
        }catch (SQLException ex){
            return idp;
        }    
    }
    
    @WebMethod(operationName = "unlikeButton")
    public java.lang.String unlikeButton(@WebParam(name = "idu") String idu, @WebParam(name = "idp") String idp){
        
        String qInsert = "delete from product_like where id_product="+idp+" AND userID="+idu;
        try{
            PreparedStatement sIns = null;
            try{
                sIns = con.prepareStatement(qInsert);
            }catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            int rest = sIns.executeUpdate();
            
            con.close();
            return sumLike(idp);    
        
        }catch (SQLException ex){
            return idp;
        }    
    }
    
    
    public String sumLike(String idp){
        String qL = "SELECT COUNT(*) as likes from product_like where id_product="+idp;
        try{
            PreparedStatement sL=null;
            try{
                sL = con.prepareStatement(qL); 
            } catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSet rL = sL.executeQuery();
            rL.next();
            
            con.close();
            return rL.getInt("likes")+"";
        } catch (SQLException ex) {
            return "0";
        }
    }
    
    public String sumBuy(String idp){
        String qL = "SELECT COUNT(*) as buy from purchase_history where id_product="+idp;
        try{
            PreparedStatement sL=null;
            try{
                sL = con.prepareStatement(qL); 
            } catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSet rL = sL.executeQuery();
            rL.next();
            
            con.close();
            return rL.getInt("buy")+"";
        } catch (SQLException ex) {
            return "0";
        }
    }
    
    public String isLike(String userID, String productID){
        String qL = "SELECT COUNT(*) as likes from product_like where id_product="+productID+" AND userID="+userID;
        try{
            PreparedStatement sL=null;
            try{
                sL = con.prepareStatement(qL); 
            } catch (SQLException ex) {
                Logger.getLogger(productWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSet rL = sL.executeQuery();
            rL.next();
            
            con.close();
            return rL.getInt("likes")+"";
        } catch (SQLException ex) {
            return "0";
        }
    }
}
