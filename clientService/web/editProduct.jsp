<%-- 
    Document   : editProduct
    Created on : Nov 12, 2016, 16:53:50 PM
    Author     : Sekar Anglila / 13514069
--%>

<%@page import="identityConnect.identityConnect"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <style type="text/css">
            <%@include file="saleProject.css" %>
        </style>
        <script type="text/javascript">
            <%@include file="saleProject.js" %>
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sale Project | Edit Product</title>
    </head>
    <body>
        
            <%-- start web service invocation --%>
    <%
        java.util.List<java.lang.String> result=null;
        int userID=0,id_prod=0;
        try {
            webservice.ProductWebService_Service service = new webservice.ProductWebService_Service();
            webservice.ProductWebService port = service.getProductWebServicePort();
             // TODO initialize WS operation arguments here
            userID = Integer.parseInt(request.getSession().getAttribute("uid").toString());//request.getSession().getAttribute("userID")+"";
            id_prod = Integer.parseInt(request.getParameter("id_product"));
            
            // TODO process result here
            result = port.productRetrieve(id_prod);
            session.setAttribute("id_prod", id_prod);   
            session.setAttribute("prodname", result.get(0));   
            session.setAttribute("prodprice", result.get(1));
            session.setAttribute("proddesc", result.get(2));
            
        } catch (Exception ex) {
             response.sendRedirect("yourProducts.jsp");
            // TODO handle custom exceptions here
        }
    %>
        <div class = "center">
            <h1 class = "title1">Sale<span class="title2">Project</span></h1>
        </div>
        <div class = "container">
            <div class = "right">
                Hi,
                <%
                    int userid = Integer.parseInt(request.getSession().getAttribute("uid").toString());
                    out.println(identityConnect.getUser(userid).get("uname"));
                %>
                <form action="logout" method="POST">
                    <input type="submit" value="logout" name ="logout" class = "logout"/>
                </form>
            </div>  
            <br><br><br>

            <table border="1" class="nav-menu">
                <tr>
                    <th class = "padth "><a href="catalogPage.jsp" class = "black">Catalog</a></th>
                    <th class = "padth"><a href="yourProduct.jsp" class = "black">Your Product</a></th>
                    <th class = "padth"><a href="addProduct.jsp" class = "black">Add Product</a></th>
                    <th class = "padth"><a href="sales.jsp" class = "black">Sales</a></th>
                    <th class = "padth"><a href="purchaseHistory.jsp" class = "black">Purchases</a></th>
                </tr>
            </table>
            <br>
            <h2>Edit Product</h2>
            <hr>
            <br><br>
            <form  id="editproduct" action="editProduct" method="post">
                <div>    
                    Name<br>
                    <input class="textbox" type="text" name="prodname" value="<% out.print(result.get(0));%>">
                </div> <br><br>
                <div>
                    Description<br>
                    <textarea class="textbox" form ="editproduct" name="proddesc" value=""><% out.print(result.get(2));%></textarea>
                </div><br><br>
                <div>
                    Price (IDR)<br>
                    <input class="textbox" type="text" name="prodprice" value="<% out.print(result.get(1));%>">
                </div> <br><br>
                <div>
                    Photo<br>
                    <button type="submit" class="editPhoto" disabled>Choose File</button> 
                    <% out.print(id_prod +"-"+ userID + ".jpg");%>
                </div> <br><br>
                <table style="width:100%; height:40px">
                    <tr>
                        <td style="width:70%" align="right" >
                            <button type = "submit" class="purchButton2" name= "CONFIRM">CONFIRM</button>
                        </td>
            </form>
                        <form action="yourProduct.jsp" method="POST">
                            <td align="right" style="width:25%;padding-right:0px">
                                <button type = "submit" class="purchButton" name= "CANCEL">CANCEL</button>
                            </td>
                        </form>
                    </tr>
                </table><br><br>
        </div>
    </body>
</html>