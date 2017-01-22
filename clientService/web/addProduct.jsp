<%-- 
    Document   : addProduct
    Created on : Nov 12, 2016, 4:36:12 PM
    Author     : Kris
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
                    <th class = "padth"><a href="yourProduct.jsp" class = "black">Your Product</a></th>
                    <th class = "padth navth"><a href="addProduct.jsp" class = "white">Add Product</a></th>
                    <th class = "padth"><a href="sales.jsp" class = "black">Sales</a></th>
                    <th class = "padth"><a href="purchaseHistory.jsp" class = "black">Purchases</a></th>
                </tr>
            </table>
            <br>
            <h2>What are you going to sell today?</h2>
            <hr>
            <br><br>
        <form action="addProduct" method="POST" enctype="multipart/form-data">
            Name<br>
            <input type="text" name="prodname" value="" class ="textbox"/><br><br>
            Description<br>
            <input type="text" name="description" value="" class ="textbox" /><br><br>
            Price (IDR)<br>
            <input type="text" name="price" value="" class ="textbox" /><br><br>
            Photo<br>
            <input type="file" name="image" value="" /><br><br>
            
            <input class= "button right" type="submit" value="ADD" /><br><br>
        </form>
            </div>
    </body>
</html>
