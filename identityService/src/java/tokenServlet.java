/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "tokenServlet", urlPatterns = {"/tokenServlet"})
public class tokenServlet extends HttpServlet {

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
        Connection con = DBConnection.connectDB();
        String selector;
        String querySQL = "SELECT COUNT(*) AS userCount,expireDate FROM userTokens WHERE sessionToken = ? AND userID = ?";
        PreparedStatement SQLStatement;
        JSONObject js = new JSONObject();
        js.put("token",token);
        js.put("uid",uid);
        try {
            SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setString(1, token);
            SQLStatement.setInt(2, uid);
            try (ResultSet SQLResult = SQLStatement.executeQuery()) {
                SQLResult.next();
                if (SQLResult.getInt("userCount") == 1) {         
                    Date now = new Date();
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date expireDate;
                    try {
                        expireDate = format.parse(SQLResult.getString("expireDate"));
                        if (now.before(expireDate)){
                            js.put("status","validToken");
                        }
                        else {
                            js.put("status","expiredToken");
                            querySQL = "DELETE FROM userTokens WHERE sessionToken = ? AND userID = ?";
                            SQLStatement = con.prepareStatement(querySQL);
                            SQLStatement.setString(1,token);
                            SQLStatement.setInt(2,uid);
                            SQLStatement.execute();
                        }
                    } catch (ParseException ex) {
                        js.put("status","parseError");
                    }
                }
                else {
                    js.put("status","invalidToken");
                }
                SQLStatement.close();
            }
        } catch (SQLException ex) {
            js.put("status","SQLError");
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
