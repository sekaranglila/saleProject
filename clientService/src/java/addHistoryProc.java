/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import webservice.ProductWebService_Service;

/**
 *
 * @author SASHINOVITASARI
 */
@WebServlet(urlPatterns = {"/addHistoryProc"})
public class addHistoryProc extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addHistoryProc</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addHistoryProc at " + request.getContextPath() + "</h1>");
            String idp = (String) request.getSession().getAttribute("idp");
            String price = (String) request.getSession().getAttribute("price");
            String pn = (String) request.getSession().getAttribute("pName");
            String qty = request.getParameter("Qtty");
            String rcvn = request.getParameter("fullname");
            String rcvaddr = request.getParameter("addr");       
            String rcvpo = request.getParameter("post");        
            String rcvph = request.getParameter("pnum");    
            String crec = request.getParameter("digit12");
            String crev = request.getParameter("digit3");
            String idu = request.getSession().getAttribute("uid").toString();
            //S"1";//GANTI
            
            String addr="",city="";
            int len = rcvaddr.length(), i=0;
            
            if (rcvaddr.contains(".")){
                while( rcvaddr.charAt(i)!='.'&& i!=len){
                    addr+= Character.toString(rcvaddr.charAt(i));
                    i++;
                }
                while(rcvaddr.charAt(i)==' '|| rcvaddr.charAt(i)=='.'&& i!=len)i++;

                while(i!=len){
                    city+= Character.toString(rcvaddr.charAt(i));
                    i++;
                }
            }else addr=rcvaddr;
            
           /* out.println("IDP = "+idp);
            out.println("Product Name ="+pn);
            out.println("price ="+price);
            out.println("QTTY ="+qty);
            out.println("full name ="+rcvn);
            out.println("addr = "+addr+"||"+city);
            out.println("rcvpo ="+rcvpo);
            out.println("rcvph ="+rcvph);
            out.println("crec ="+crec);
            out.println("crev ="+crev);
            */
            out.println(addHistory(idp,qty,idu,rcvn, addr, city, rcvpo, rcvph,crec,crev));
            response.sendRedirect("purchaseHistory.jsp");

            out.println("</body>");
            out.println("</html>");
        }
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

    private String addHistory(java.lang.String idp, java.lang.String qty, java.lang.String uBuy, java.lang.String rcvn, java.lang.String rcvadd, java.lang.String rcvc, java.lang.String rcvpo, java.lang.String rcvph, java.lang.String crecard, java.lang.String creval) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        webservice.ProductWebService port = service.getProductWebServicePort();
        return port.addHistory(idp, qty, uBuy, rcvn, rcvadd, rcvc, rcvpo, rcvph, crecard, creval);
    }

}

