<%-- 
    Document   : purchaseHistory
    Created on : Nov 11, 2016, 11:09:10 PM
    Author     : SASHINOVITASARI
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
        <title>Sale Project | Purchases </title>
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
                    <th class = "padth"><a href="addProduct.jsp" class = "black">Add Product</a></th>
                    <th class = "padth"><a href="sales.jsp" class = "black">Sales</a></th>
                    <th class = "padth navth"><a href="purchaseHistory.jsp" class = "white">Purchases</a></th>
                </tr>
            </table>
            <br>
            <h2>Here are your purchases</h2>
            <hr>
            <br><br>
        <%
            java.util.List<net.java.dev.jaxb.array.StringArray> result=null;
            try {
                webservice.ProductWebService_Service service = new webservice.ProductWebService_Service();
                webservice.ProductWebService port = service.getProductWebServicePort();
                java.lang.String idu =  request.getSession().getAttribute("uid").toString();//"1";//GANTI
                result = port.showHistory(idu);
            } catch (Exception ex) {
                out.println("None");
            }
            
            int size =  result.get(0).getItem().size();
            for (int i=0; i<size; i++){ %>
		<div class = "user"><b><% out.println(result.get(0).getItem().get(i));%></b><br>
                    <div style="margin-bottom:10px"><% out.println(result.get(1).getItem().get(i));%></div>
                    <hr style="margin-left: 5px">
                    <table class="tableShow">
                        <td style="width:25% ">
                            <% 
                                String img = "data:image.jpeg;base64,"+result.get(2).getItem().get(i);
                                
                            %>    
                            <image height="100" width="100" src="<%=img%>">
                        </td>
                        <td style="width:50%" valign="top">
                            <div style="font-size: 15px"><b><% out.println(result.get(4).getItem().get(i));%></b><br>
                                IDR <% out.println(result.get(12).getItem().get(i));%><br>
                                <% out.println(result.get(6).getItem().get(i));%> pcs<br>
                                @IDR <% out.println(result.get(3).getItem().get(i));%><br><br>
                            </div>
                                brought from <b><% out.println(result.get(5).getItem().get(i));%></b>
                        </td>
                        <td valign="top">
                            Delivery to <b><% out.println(result.get(7).getItem().get(i));%></b><br>
                            <% out.println(result.get(8).getItem().get(i));%><br>
                            <% out.println(result.get(9).getItem().get(i));%><br>
                            <% out.println(result.get(11).getItem().get(i));%><br>
                            <% out.println(result.get(10).getItem().get(i));%><br>
                        </td>
                    </table>
                    <br>
                    <hr style="margin-left: 5px">
                    <br><br>
                    <%}%>
                    <br>
		</div>
	<!--------------------------->
		<p class="clear"></p>
		</div>
    
    </body>
</html>
