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
@WebServlet(urlPatterns = {"/getUserServlet"})
public class getUserServlet extends HttpServlet {

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
        JSONObject js = new JSONObject();
        try {
            response.setContentType("text/html;charset=UTF-8");
            int uid = Integer.parseInt(request.getParameter("uid"));
            Connection con = DBConnection.connectDB();
            String selector;
            String querySQL = "SELECT COUNT(*) AS userCount,userName,userEmail,userFullName,userFullAddress,userPostCode,userPhoneNum,userCity FROM userCredentials WHERE userID = ?";
            PreparedStatement SQLStatement =con.prepareStatement(querySQL);
            SQLStatement.setInt(1, uid);
            ResultSet SQLResult = SQLStatement.executeQuery();
            SQLResult.next();
            if (SQLResult.getInt("userCount") == 1) { 
                js.put("uid",uid);
                js.put("uname",SQLResult.getString("userName"));
                js.put("uemail",SQLResult.getString("userEmail"));
                js.put("ufullname",SQLResult.getString("userFullName"));
                js.put("ufulladdress",SQLResult.getString("userFullAddress"));
                js.put("upostcode",SQLResult.getString("userPostCode"));
                js.put("uphonenum",SQLResult.getString("userPhoneNum"));
                js.put("ucity",SQLResult.getString("userCity"));
            }
        } catch (SQLException ex) {
            js.put("status",ex.getMessage());
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
