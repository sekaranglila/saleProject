/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
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
@WebServlet(urlPatterns = {"/tokenServletBonus"})
public class tokenServletBonus extends HttpServlet {
    
        Connection con = DBConnection.connectDB();
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
        String token = request.getParameter("token");
        int uid = Integer.parseInt(request.getParameter("uid"));
        String[] tokenize = token.split("#");
        String querySQL = "SELECT COUNT(*) AS userCount,expireDate,sessionToken FROM userTokens WHERE sessionToken LIKE ? AND userID = ?";
        PreparedStatement SQLStatement;
        JSONObject js = new JSONObject();
        js.put("token",token);
        js.put("uid",uid);
        try {
            SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setString(1, '%'+tokenize[0]+'%');
            SQLStatement.setInt(2, uid);
            try (ResultSet SQLResult = SQLStatement.executeQuery()) {
                SQLResult.next();
                System.out.println("COUNT" +SQLResult.getInt("userCount"));
                if (SQLResult.getInt("userCount") == 1) {   
                    String []tok = SQLResult.getString("sessionToken").split("#");
                    if (tok[1].equals(tokenize[1])) {
                        if (tok[2].equals(tokenize[2])) {
                            Date now = new Date();
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date expireDate;
                            try {
                                expireDate = format.parse(SQLResult.getString("expireDate"));
                                if (now.before(expireDate)){
                                    js.put("status","validToken");
                                    token = UUID.randomUUID().toString();
                                    token = token + "#" + request.getParameter("ipua") ;
                                    Calendar date = Calendar.getInstance();
                                    long t = date.getTimeInMillis();
                                    java.sql.Date expirationDate = new java.sql.Date(t + (20*(1000)));
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    querySQL = "UPDATE userTokens SET expireDate = ?, sessionToken = ? WHERE userID = ?";
                                    SQLStatement = con.prepareStatement(querySQL);
                                    SQLStatement.setInt(3, uid);
                                    SQLStatement.setString(1, df.format(expirationDate));
                                    SQLStatement.setString(2, token);
                                    SQLStatement.execute();
                                    js.put("token",token);
                                }
                                else {
                                    js.put("status","invalidToken");
                                    js.put("detail","expireToken");
                                    querySQL = "DELETE FROM userTokens WHERE sessionToken LIKE ? AND userID = ?";
                                    SQLStatement = con.prepareStatement(querySQL);
                                    SQLStatement.setString(1,'%'+tokenize[0]+'%');
                                    SQLStatement.setInt(2,uid);
                                    SQLStatement.execute();

                                }
                            } catch (ParseException ex) {
                                js.put("status","parseError");
                            }
                        }
                        else {
                            js.put("status","invalidToken");
                            js.put("detail","userAgentNotEquals");
                            querySQL = "DELETE FROM userTokens WHERE sessionToken LIKE %?% AND userID = ?";
                            SQLStatement = con.prepareStatement(querySQL);
                            SQLStatement.setString(1,'%'+tokenize[0]+'%');
                            SQLStatement.setInt(2,uid);
                            SQLStatement.execute();
                        }
                    }
                    else {
                        js.put("status","invalidToken");
                        js.put("detail","ipNotEquals");
                        querySQL = "DELETE FROM userTokens WHERE sessionToken LIKE %?% AND userID = ?";
                        SQLStatement = con.prepareStatement(querySQL);
                        SQLStatement.setString(1,'%'+tokenize[0]+'%');
                        SQLStatement.setInt(2,uid);
                        SQLStatement.execute();
                    }
                }
                else {
                    js.put("status","invalidToken");
                    js.put("detail","tokenNotFound");
                }
                SQLStatement.close();
            }
        } catch (SQLException ex) {
            js.put("status",ex.getMessage());
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

}
