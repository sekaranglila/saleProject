<%-- 
    Document   : login
    Created on : Nov 7, 2016, 1:36:20 PM
    Author     : Kris
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="manifest" href="manifest.json">
        <script src="https://www.gstatic.com/firebasejs/3.6.1/firebase.js"></script>
        <style type="text/css">
            <%@include file="saleProject.css" %>
        </style>
        <script type="text/javascript">
            <%@include file="saleProject.js" %>
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sale Project | Login</title>
        <script>
            deleteToken();
        </script>
    </head>
    <body>
        <div class = "center">
            <h1 class = "title1">Sale<span class="title2">Project</span></h1>
        </div>
        <div class = "container">
            <h2 class = "hline">Please Login</h2>
            <%
                if (request.getSession().getAttribute("status") == "notAuthenticated") {
            %>
                    <%="<div id = \"errorAuth\">"
                    + "Wrong Credentials !"
                    + "</div>"%>
            <%
                    request.getSession().invalidate();
                }
                else if (request.getSession().getAttribute("status") == "endSession") {
                    if (request.getSession().getAttribute("detail") == "ipNotEquals") {
            %>
                        <%="<div id = \"errorAuth\">"
                    + "Your session is over due to invalid IP Address. Please log in again."
                    + "</div>"%>
            <%            
                    }
                    else if (request.getSession().getAttribute("detail") == "userAgentNotEquals") {
            %>
                        <%="<div id = \"errorAuth\">"
                    + "Your session is over due to invalid User Agent. Please log in again."
                    + "</div>"%>
            <%
                    }
                    else {
            %>
                        <%="<div id = \"errorAuth\">"
                    + "Your session is over. Please log in again."
                    + "</div>"%>
            <%
                    }
                    request.getSession().invalidate();
                }
            %>
            <script>
                requestPermission();
            </script>
            <div id = "error"></div>
            <form name = "login" action="login" method="POST" onsubmit="return validateLogin()">
                Email or Username
                <input type="text" name="username" value="" class ="textbox" /><br><br>
                Password
                <input type="password" name="userpass" value="" class ="textbox" /><br><br>
                <input type="hidden" name="fbToken"/>
                <input type="submit" value="LOGIN" class = "button right" />
            </form>
            <br><br><br>
            
            Don't have an account yet ? Register <a class = "linkColor" href="register.jsp">here</a>
        </div>
    </body>
</html>
