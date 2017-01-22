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
import java.sql.Statement;
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
@WebServlet(name = "registerServlet", urlPatterns = {"/registerServlet"})
public class registerServlet extends HttpServlet {

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
        JSONObject js = new JSONObject();
        Connection con = DBConnection.connectDB();
        try {
            processRequest(request, response);
            String fn = request.getParameter("fn");
            String un = request.getParameter("un");
            String ue = request.getParameter("ue");
            String ua = request.getParameter("ua");
            String up = request.getParameter("up");
            String pc = request.getParameter("pc");
            String uc = request.getParameter("uc");
            String pn = request.getParameter("pn");
            String querySQL = "SELECT COUNT(*) AS userCount FROM userCredentials WHERE userName = ? OR userEmail = ?";
            PreparedStatement SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setString(1, un);
            SQLStatement.setString(2, ue);
            ResultSet SQLResult = SQLStatement.executeQuery();
            SQLResult.next();
            if (SQLResult.getInt("userCount") == 1) {
                js.put("status","userExist");
            }
            else {
                con.setAutoCommit(false);
                querySQL = "INSERT INTO userCredentials (userName,userPassword,userEmail,userFullName,userFullAddress,userPostCode,userPhoneNum,userCity) "
                        + "VALUE (?,MD5(?),?,?,?,?,?,?)";
                SQLStatement = con.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS);
                SQLStatement.setString(1,un);
                SQLStatement.setString(2,up);
                SQLStatement.setString(3,ue);
                SQLStatement.setString(4,fn);
                SQLStatement.setString(5,ua);
                SQLStatement.setString(6,pc);
                SQLStatement.setString(7,pn);
                SQLStatement.setString(8,uc);
                SQLStatement.executeUpdate();
                SQLResult = SQLStatement.getGeneratedKeys();
                if (SQLResult.next()) {
                    js.put("status","successRegistered");
                    js.put("uid",SQLResult.getInt(1));
                }
                con.commit();
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            js.put("status",ex.getMessage());
        }
        response.getWriter().println(js.toJSONString());
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
