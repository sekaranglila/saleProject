<%@page import="java.util.Base64"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="org.json.simple.parser.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="net.java.dev.jaxb.array.StringArray"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : yourProduct.jsp
    Created on : Nov 11, 2016, 2:12:49 AM
    Author     : Kris
--%>

<%@page import="org.json.simple.JSONObject"%>
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
        <title>Sale Project | Your Product</title>
    </head>
    <body>
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
                    <th class = "padth navth"><a href="yourProduct.jsp" class = "white">Your Product</a></th>
                    <th class = "padth"><a href="addProduct.jsp" class = "black">Add Product</a></th>
                    <th class = "padth"><a href="sales.jsp" class = "black">Sales</a></th>
                    <th class = "padth"><a href="purchaseHistory.jsp" class = "black">Purchases</a></th>
                </tr>
            </table>
            <br>
            <h2>What are you going to sell today?</h2>
            <hr>
            <br><br>
            <%
            try {
                webservice.ProductWebService_Service service = new webservice.ProductWebService_Service();
                webservice.ProductWebService port = service.getProductWebServicePort();
                 // TODO initialize WS operation arguments here
                int uid = userid;
                
                // TODO process result here
                java.util.List<net.java.dev.jaxb.array.StringArray> result = port.getAllProductByUserID(uid);
                for (StringArray i : result) {
                    String target = i.getItem().get(3);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date results =  df.parse(target);
                    DateFormat df1 = new SimpleDateFormat("EEEEEEE, dd MMMMMMMMM yyyy " );
                    DateFormat df2 = new SimpleDateFormat("HH:mm");
                    out.println(df1.format(results).toString() + "<br> at " + df2.format(results) + "<br>");
                    out.println("<hr>");      
                    String img = "data:image/jpeg;base64," + i.getItem().get(5);
            %>
                    <div class = "image">
                        <img height = "100" width="100" src="<%=img%>" >
                    </div>
                    <div class = "desc">
                        <p><%=i.getItem().get(0)%><br><%=i.getItem().get(1)%><br><%=i.getItem().get(2)%></p>
                    </div>
                    <div class ="lipurcount">
                        <%=port.getLikesCount(Integer.parseInt(i.getItem().get(4)))%> likes <br> <%=port.getPurchasesCount(Integer.parseInt(i.getItem().get(4)))%> purchases
                        <button class= "buttonstyle yel"  onclick="post(<%="'editProduct.jsp'"%>, {id_product: '<%=i.getItem().get(4)%>'});" >EDIT</button>
                        <button class="buttonstyle re" onclick="deleteconf({id_product: '<%=i.getItem().get(4)%>',username: '<%= identityConnect.getUser(userid).get("uname")%>'});" >DELETE</button>
                    </div>
                    <br><br>
            <%
                }
            } catch (Exception ex) {
                // TODO handle custom exceptions here
            }
            
            %>
        </div>
</html>
