<%-- 
    Document   : register.jsp
    Created on : Nov 8, 2016, 4:55:09 PM
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
        <title>Sale Project | Register</title>
        <script>
            deleteToken();
        </script>
    </head>
    <body>
        <div class = "center">
            <h1 class = "title1">Sale<span class="title2">Project</span></h1>
        </div>
        <div class = "container">
            <h2 class = "hline">Please Register</h2>
            <%
                if (request.getSession().getAttribute("status") == "userExist") {
            %>
                    <%="<div id = \"errorExist\">"
                    + "Username or E-mail has been taken !"
                    + "</div>"%>
            <%
                    request.getSession().invalidate();
                }
            %>
            <div id = "errorreg"></div>
            <script>
                requestPermission();
            </script>
            <form name = "registration" action="register" method="POST" onsubmit="return validateRegister()">
                Full Name
                <input type="text" name="fullname" value="" class ="textbox" /><br><br>
                Username
                <input type="text" name="username" value="" class ="textbox" /><br><br>
                Email
                <input type="text" name="useremail" value="" class ="textbox" /><br><br>
                Password
                <input type="password" name="password" value="" class ="textbox" /><br><br>
                Confirm Password
                <input type="password" name="cpassword" value="" class ="textbox" /><br><br>
                Full Address
                <textarea name="useraddress" rows="2" class = "textbox"></textarea><br><br>
                Postal Code
                <input type="text" name="postcode" value="" class ="textbox" onkeypress="return validateNumber(event)" /><br><br>
                Phone Number
                <input type="text" name="phonenum" value="" class ="textbox" onkeypress="return validateNumber(event)" /><br><br>
                City
                <input type="text" name="city" value="" class ="textbox" /><br><br>
                <input type="hidden" name="fbtoken" />
                <input type="submit" value="REGISTER" class ="button right" />
            </form>
            <br><br><br>
            Already registered ? Login <a class = "linkColor" href="login.jsp">here</a>
        </div>
    </body>
</html>
