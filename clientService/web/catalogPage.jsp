<%-- 
    Document   : catalogPage
    Created on : Nov 11, 2016, 3:12:41 PM
    Author     : SASHINOVITASARI
--%>
<%@page import="identityConnect.identityConnect"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.sql.Blob"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ListIterator"%>
<%@page import="net.java.dev.jaxb.array.StringArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="manifest" href="manifest.json">
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <script src="https://www.gstatic.com/firebasejs/3.6.1/firebase.js"></script>
        <style type="text/css">
            <%@include file="saleProject.css" %>
        </style>
        <script data-require="jquery@*" data-semver="2.1.1" src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.9/angular.min.js"></script>
        <script type="text/javascript">
            <%@include file="saleProject.js" %>
            requestPermission();
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sale Project | Catalog</title>
    </head>
    <body  ng-app="Sojharo" ng-controller="MyController" >
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
                    <th class = "padth navth"><a href="catalogPage.jsp" class = "white">Catalog</a></th>
                    <th class = "padth"><a href="yourProduct.jsp" class = "black">Your Product</a></th>
                    <th class = "padth"><a href="addProduct.jsp" class = "black">Add Product</a></th>
                    <th class = "padth"><a href="sales.jsp" class = "black">Sales</a></th>
                    <th class = "padth"><a href="purchaseHistory.jsp" class = "black">Purchases</a></th>
                </tr>
            </table>
            <h3 class="title3 font2">What are you going to buy today?</h3>
        <form action="catalogPage.jsp" method="post" class = "user">
            <table style="width:100%">
        <td style="width:90%">
                    <input type="text" id="search" name="keyword" class="searchBar" placeholder="Search catalog..."/>
        </td>
        <td>
                    <button value ="search" type="submit" class="searchButton" name="searchButtom">GO</button>
        </td>
            </table>
            <br>
            <span class="errMess" id="errMess"></span>
            <table class="font">
                <td style="width:30%" valign="top">by</td>
        <td style="vertical-align :top">
                        <input type="radio" name="category" value="product"> product<br>
                        <input type="radio" name="category" value="store"> store <br>
                    
        </td>
            </table>
    </form>
        <br>
        <%-- start web service invocation --%><hr/>
        <%
            int size = 0;
            java.util.List<net.java.dev.jaxb.array.StringArray> result=null;
        try{
            webservice.ProductWebService_Service service = new webservice.ProductWebService_Service();
            webservice.ProductWebService port = service.getProductWebServicePort();
             // TODO initialize WS operation arguments her
            String keyword = request.getParameter("keyword")+"";
            String category = request.getParameter("category")+"";
            String userID = request.getSession().getAttribute("uid").toString();//"1"; //GANTI
            if ("null".equals(keyword)||"".equals(keyword)) keyword="none";
            if ("null".equals(category)||"".equals(category)) category="none";
            result = port.catalogRetrieve(userID,keyword, category);
            if (result!=null) size =  result.get(0).getItem().size();
            
        } catch (Exception ex) {
            
        }
        for (int i=0; i<size;i++){ %>
        <div class = "user font">
            <% String ids = result.get(5).getItem().get(i); 
                if ("1".equals(identityConnect.getOnlineStatus(Integer.parseInt(ids)).get("user_on").toString())) {
                    if (!identityConnect.getUser(userid).get("uname").equals(identityConnect.getUser(Integer.parseInt(ids)).get("uname").toString())) {
                    %><i class="material-icons bij" style="color: green;font-size: 12px;">fiber_manual_record</i>
                        <b class="bij" ng-click="setDes('<%= result.get(10).getItem().get(i) %>')"><% out.println(result.get(10).getItem().get(i));%></b>
                    <%
                    }else {
                        %>
                        <i class="material-icons" style="color: green;font-size: 12px;">fiber_manual_record</i>
                    <b><% out.println(result.get(10).getItem().get(i));%></b>
                    <%
                    }
                }
                else {
                    %><i class="material-icons" style="color: red;font-size: 12px;">fiber_manual_record</i>
                    <b><% out.println(result.get(10).getItem().get(i));%></b>
                    <%
                }
            %>
            <br>
            <div style="margin-bottom:10px" >
                <% out.println(result.get(0).getItem().get(i));%>
            </div>
            <hr style="margin-left: 5px">
            <table class="tableShow">
                <td style="width:25% ">
                    <% 
                        String img = "data:image.jpeg;base64,"+result.get(1).getItem().get(i);
                    %>    
                    <image height="100" width="100" src="<%=img%>">
        </td>
        <td style="width:49%" valign="top">
                    <div style="font-size: 18px"><b><% out.print(result.get(4).getItem().get(i));%></b><br>
                        IDR <% out.print(result.get(3).getItem().get(i));%> 
                    </div>
                        <% out.print(result.get(6).getItem().get(i));%>
        </td>
        <td valign="top">
        <br>
                <div style="margin-left:5px">
                    <span id='lid'> <% out.print(result.get(7).getItem().get(i)); %> </span> likes<br>
                    <% out.print(result.get(8).getItem().get(i)); %> buys
        </div>
        <br>
        <div>
        <br><br>                                
    
        <form action="likeOpr" method="post" class = "user">
                    <% if ("1".equals(result.get(9).getItem().get(i))){%>
                        <button type="submit" class="likeButton" name="likeb" value="<% out.print(result.get(2).getItem().get(i)+",unlike"); %>"><b>LIKED<b></button>
                    <% }else {%>
                        <button type="submit" class="unlikeButton" name="likeb" value="<% out.print(result.get(2).getItem().get(i)+",like"); %>"><b>LIKE<b></button>
                    <% }%>
        </form>
        <form action="confirmation.jsp" method="post" class = "user">
                    <button type="submit" class="buyButton" name="buyButton" value="<% out.print(result.get(2).getItem().get(i)); %>"><b>BUY<b></button>
        </form>
        </div>
        </td>
    </table>
    <br>
    <hr style="margin-left: 5px">
    <br><br>
    </div>
        <%}%> 
        </div>
        <%if (size==0 || result==null){%>
        <br><br>
            <div class="center2 font2">None</div>
        <%}%>
  <!--   
        <div ng-show="showchat" class="chat">
            <div class="chat-title"> 
              <i class="material-icons" style="font-size: 12px; color: green;">fiber_manual_record</i>
              {{name}}
              <i class="material-icons" style="float: right; color: red" ng-click="showchat">cancel</i>
            </div>
            <div class="chat-area" ng-repeat="message in messages track by $index">
              <div ng-if="message.sender == <%//identityConnect.getUser(userid).get("uname")%>">
                <div class="chat-bubble-self" >
                  <div class="messageTextInMessage">{{message.sender}} : {{message.message}}</div>
                </div> <br> 
              </div>
              <div ng-if="message.sender != <%//identityConnect.getUser(userid).get("uname")%>">
                <div class="chat-bubble-self" >
                  <div class="messageTextInMessage">{{message.sender}} : {{message.message}}</div>
                </div> <br> 
              </div>
            </div>
            <div class="field">
              <div class="chatControls">
                <form ng-submit="sendIM()">
                  <input type="text" class="chat-field" ng-model="im.message" placeholder="Tulis Pesan">
                  {{im.sender="<%//identityConnect.getUser(userid).get("uname")%>";""}}
                  <input type="submit" id="submit" value="Kirim" class="chat-submit">
                </form>
              </div>
            </div>
        </div>
-->
        <div>
      <div ng-show= "showchat" class="chatBox">
          
          <div class="chat-title">
          <i class="material-icons" style="font-size: 12px;color: green">fiber_manual_record</i>
          {{chatTitle}}
          <i ng-click="close()" class="material-icons" style="font-size: 13px;color: red;float:right">cancel</i>
          </div>
        <div id="chatBox" schroll-bottom="messages">
          <div ng-cloak ng-repeat="message in messages track by $index">
                <div ng-if ="message.sender == '<%=identityConnect.getUser(userid).get("uname")%>'" class="chat-bubble-self">
                    {{message.message}}
                </div> <br>
                <div ng-if ="message.sender != '<%=identityConnect.getUser(userid).get("uname")%>'" class="chat-bubble-other">
                   {{message.message}}
                </div>
                <br><br>
          </div>
        </div>
          <br>
        <div class="chatControls">
          <form ng-submit="sendIM()">
            <input type="text" ng-model="im.message" placeholder="Send a message" class="chatTextField" />
            {{im.sender="<%=identityConnect.getUser(userid).get("uname")%>";""}}
          </form>
      </div>
      </div>
    </div>
    </body>
</html>
