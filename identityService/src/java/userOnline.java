//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
/**
 *
 * @author Sekar Anglila / 13514069
 */
@WebServlet(urlPatterns = {"/userOnline"})
public class userOnline extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
        Connection con = DBConnection.connectDB();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Kamus Lokal
        int id = Integer.parseInt(request.getParameter("id"));
        JSONObject json = new JSONObject();
        JSONArray user_on = new JSONArray();
        JSONObject js;
        response.setContentType("text/html;charset=UTF-8");
        
        //Algoritma
        try {
            String querySQL = "SELECT DISTINCT COUNT(*) as onx FROM userTokens WHERE userId = ?";
            PreparedStatement SQLStatement = con.prepareStatement(querySQL);
            SQLStatement.setInt(1, id);
            ResultSet SQLResult = SQLStatement.executeQuery();
            SQLResult.next();
            if (SQLResult.getInt("onx") > 0) {
                json.put("user_on","1");
            }
            else {
                json.put("user_on","0");
            }
        } catch (SQLException ex) {
            json.put("status",ex.getMessage());
        }
        response.setContentType("application/json");
        response.getWriter().println(json.toJSONString());
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
        doPost(request, response);
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