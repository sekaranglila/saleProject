/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Kris
 */
@WebServlet(urlPatterns = {"/loginServlet"})
public class loginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String un = request.getParameter("username");
        String up = request.getParameter("userpass");
        Connection con = DBConnection.connectDB();
        
        String querySQL = "SELECT COUNT(*) AS userCount, userID FROM userCredentials WHERE (userEmail = ? OR userName = ?) AND userPassword = MD5(?)";
        PreparedStatement SQLStatement;
        JSONObject js = new JSONObject();
        try {
            SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setString(1, un);
            SQLStatement.setString(2, un);
            SQLStatement.setString(3, up);
            try (ResultSet SQLResult = SQLStatement.executeQuery()) {
                SQLResult.next();
                if (SQLResult.getInt("userCount") == 1) {
                    int uid = SQLResult.getInt("userID");
                    querySQL = "DELETE FROM userTokens WHERE userID = ?";
                    
                    SQLStatement = con.prepareStatement(querySQL);
                    SQLStatement.setInt(1,uid);
                    SQLStatement.execute();
                    String token = UUID.randomUUID().toString();
                    String ipAddress = request.getParameter("ip");
                    String usAg = request.getParameter("ua");
                    token = token + "#" + ipAddress + "#" + usAg;
                    Calendar date = Calendar.getInstance();
                    long t = date.getTimeInMillis();
                    Date expirationDate = new Date(t + ( 20*(1000)));
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    querySQL = "INSERT INTO userTokens (userID,expireDate,sessionToken,tokenFB) VALUE (?,?,?,?)";
                    
                    SQLStatement = con.prepareStatement(querySQL);
                    SQLStatement.setInt(1, uid);
                    SQLStatement.setString(2, df.format(expirationDate));
                    SQLStatement.setString(3, token);
                    SQLStatement.setString(4,request.getParameter("fbtoken"));
                    SQLStatement.execute();
                    js.put("token",token);
                    js.put("uid",uid);
                }
                else {
                    js.put("token",null);
                    js.put("uid",null);
                }
                SQLStatement.close();
            }
        } catch (SQLException ex) {
            js.put("token",null);
            js.put("uid",null);
            js.put("error",ex);
            System.out.println(ex.getMessage());
        }
        response.getWriter().println(js.toJSONString());
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public boolean isEmail(String testString) {
        return false;
    }
}
