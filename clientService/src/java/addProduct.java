/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.ws.WebServiceRef;
import webservice.ProductWebService_Service;

/**
 *
 * @author Kris
 */
@WebServlet(urlPatterns = {"/addProduct"})
@MultipartConfig(maxFileSize = 16177215)
public class addProduct extends HttpServlet {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_19806/saleProjectService/productWebService.wsdl")
    private ProductWebService_Service service;

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
        String prodname;
        String prodprice;
        String proddesc;
        Part filePart;
        int userID;
        prodname = request.getParameter("prodname");
        prodprice = request.getParameter("price");
        proddesc = request.getParameter("description");
        userID = Integer.parseInt(request.getSession().getAttribute("uid").toString());
        filePart = request.getPart("image");
        InputStream inStream = filePart.getInputStream();
        byte[] getByte = new byte[(int)filePart.getSize()];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int stat;
        while ((stat = inStream.read(getByte)) != -1 ){
            output.write(getByte,0,stat);
        }
        String status = addProduct(userID,prodname,Integer.parseInt(prodprice),proddesc,output.toByteArray());
        response.sendRedirect("yourProduct.jsp");
        
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

    private String addProduct(int userID, java.lang.String prodname, int prodprice, java.lang.String proddesc, byte[] prodpic) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        webservice.ProductWebService port = service.getProductWebServicePort();
        return port.addProduct(userID, prodname, prodprice, proddesc, prodpic);
    }


  

    


    

}
