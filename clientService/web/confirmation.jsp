<%-- 
    Document   : confirmation
    Created on : Nov 11, 2016, 6:01:50 PM
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
        <title>Sale Project | Confirmation</title>
    </head>
    <body>
        
        
            <%-- start web service invocation --%>
    <%
        java.util.List<java.lang.String> result=null;
        try {
            webservice.ProductWebService_Service service = new webservice.ProductWebService_Service();
            webservice.ProductWebService port = service.getProductWebServicePort();
             // TODO initialize WS operation arguments here
            java.lang.String userID = request.getSession().getAttribute("uid").toString();
            java.lang.String idp = request.getParameter("buyButton");

            
            // TODO process result here
            result = port.purchaseRetrieve(userID, idp);
            session.setAttribute("idp", idp);   
            session.setAttribute("pName", result.get(0));   
            session.setAttribute("price", result.get(1));
            
        } catch (Exception ex) {
             response.sendRedirect("catalogPage.jsp");
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
            <h2>Please confirm your purchase</h2>
            <hr>
            <br><br>
            <form action="addHistoryProc" method="post">
                <table style="font-size:15px">
                <tr>
                    <td>Product</td>
                    <td>:  <span name="pName"><% out.print( result.get(0));%></span></td>
                </tr>
       		<tr>
                    <td>Price</td>
                    <td>: IDR <span id="p" name="price"><% out.print( result.get(1));%></span></td>
		</tr>
		<tr>
                    <td>Quantity</td>
                    <td>: <input style="width:15%; padding:5px;" type="text" name="Qtty" id="qty" class="in" value="1" onkeyup="findTotal()" /> pcs</td>
                </tr>
		<tr>
                    <td>Total Price</td>
                    <td><p>: IDR <span id="dis"><% out.print( result.get(1));%></span></p></td>
                    <script>
                        function findTotal(){
                            var x = document.getElementById("qty").value;
                            var y = <% out.print( result.get(1));%>;
                            var ttl= x*y;
                            var res = ttl.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
                            document.getElementById("dis").innerHTML = res;
			}
                    </script>
		</tr>
		<tr>
                    <td>Delivery to</td>
                    <td>:</td>
		<tr>
        </table>
	<br>
	<div>
            Cosignee
            <br>
            <input type = "text" name = "fullname" class = "in inpPurch" value="<% out.print(result.get(2));%>">
	</div>
	<br>
	<div>
            Full Address
            <br>
            <input type = "text" name = "addr" class = "in inpPurch" value="<% out.print(result.get(3));%>">
	</div>
	<br>
	<div>
            Postal Code
            <br>
            <input type = "text" name = "post" class = "in inpPurch" value="<% out.print(result.get(4));%>">
	</div>
	<br>
	<div>
            Phone Number
            <br>
            <input type = "text" name = "pnum" class = "in inpPurch" value="<% out.print(result.get(5));%>">
	</div>
	<br>
	<div>
            12 Digits Credit Card Number
            <br>
            <input type = "text" name = "digit12" class = "in inpPurch">
        </div>
	<br>
	<div>
            3 Digits Card Verification Value
            <br>
            <input type = "text" name = "digit3" class = "in inpPurch">
	</div>
	<br>
	<br>
	<br>
	<table style="width:100%; height:40px">
            <tr>
                <td style="width:70%" align="right" >
                <button type = "submit" class="purchButton2" name= "CONFIRM">CONFIRM </button></td>
    </form>
            <form action="catalogPage.jsp" method="POST">
                <td align="right" style="width:25%;padding-right:0px"><button type = "submit" class="purchButton" name= "CANCEL">CANCEL</button></td>
            </form>
            </tr>
	</table>
	<br><br>
        </div>
    </body>
</html>
