/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* global firebase */
var config = {
    apiKey: "AIzaSyBGbxYGACXsQsEMy71RC5Gy1EtpywL4mGE",
    authDomain: "saleproject-iti.firebaseapp.com",
    databaseURL: "https://saleproject-iti.firebaseio.com",
    storageBucket: "saleproject-iti.appspot.com",
    messagingSenderId: "977388723421"
  };
  firebase.initializeApp(config);
  const messaging = firebase.messaging();
  
function requestPermission() {
    messaging.requestPermission()
    .then(function() {
        console.log('Have Permission');
        return messaging.getToken();
    })
    .then(function(token) {
        console.log(token);
        if (document.forms['login'] !=  null) {document.forms['login']['fbToken'].value = token;}
        if (document.forms['registration'] != null) {document.forms['registration']['fbtoken'].value = token;}
    })
    .catch(function(err) {
        console.log(err);
    });
}  
function registerFb(email,password) {
    firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
        console.log(email);
        console.log(password);
        console.log(error);
      });
} 
function loginFb(email,password) {
    firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
        console.log(email);
        console.log(password);
        console.log(error);
      });
}
function logout() {
    firebase.auth().signOut().then(function() {
  // Sign-out successful.
    }, function(error) {
      // An error happened.
    });
}
function deleteToken() {
    // Delete Instance ID token.
    // [START delete_token]
    messaging.getToken()
    .then(function(currentToken) {
        if (currentToken != null) {
      messaging.deleteToken(currentToken)
      .then(function() {
        console.log('Token deleted.');
        //setTokenSentToServer(false);
        // [START_EXCLUDE]
        // Once token is deleted update UI.
        //resetUI();
        // [END_EXCLUDE]
      })
      .catch(function(err) {
        console.log('Unable to delete token. ', err);
      });
      // [END delete_token]
        }})
    .catch(function(err) {
      console.log('Error retrieving Instance ID token. ', err);
    });
 }
  
  
function post(path, params, method) {
    method = method || "post"; // Set method to post by default if not specified.
    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);
    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);
            form.appendChild(hiddenField);
         }
    }
    document.body.appendChild(form);
    form.submit();
}
function deleteconf(params) {
    if (confirm("Delete Product?")){
        if (confirm("Are you sure?")){
            if (confirm("Are you really really sure?")){
                if (confirm("Don't blame me ya if you lost your product?")){
                     post('deleteProduct',params);
                }
            }
        }
    }
}
function validateLogin() {
    document.getElementById("error").innerHTML="";
    if (document.getElementById("errorAuth"))
    {
        document.getElementById("errorAuth").innerHTML="";
        document.getElementById("errorAuth").style.display="none";
    }
    var x = document.forms["login"]["username"].value;
    var i =0;
    if (x === null || x === "") {
        document.getElementById("error").innerHTML += "Username must not be empty</br>";     
        i=i+1;
    }
    var y = document.forms["login"]["userpass"].value;
    if (y === null || y === "") {
        document.getElementById("error").innerHTML += "Password must not be empty</br>";       
        i= i+1;
    }
    if (i === 0) {
        //loginFb(x,y);
        return true;
    }
    else {
        document.getElementById("error").setAttribute("style", "border: 2px solid #d0021b; padding: 2%; background : #fa8072;\n\
    color : #d0021b;margin-bottom : 3%;");
        return false;
    }
}
function validateEmail(email) {
  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
}
function validateNumber(event) {
    var key = window.event ? event.keyCode : event.which;
if (event.keyCode == 8 || event.keyCode == 46
 || event.keyCode == 37 || event.keyCode == 39 || event.keyCode == 9) {
    return true;
}
else if ( key < 48 || key > 57 ) {
    return false;
}
else return true;
};
function validateAddress(addr) {
    var re = /\w+(\.(.)+)+$/;
    return re.test(addr);
}
function validateRegister() {
    document.getElementById("errorreg").innerHTML="";
    if (document.getElementById("errorExist"))
    {
        document.getElementById("errorExist").innerHTML="";
        document.getElementById("errorExist").style.display="none";
    }
    var a = document.forms["registration"]["fullname"].value;
    var b = document.forms["registration"]["username"].value;
    var c = document.forms["registration"]["useremail"].value;
    var d = document.forms["registration"]["password"].value;
    var e = document.forms["registration"]["cpassword"].value;
    var f = document.forms["registration"]["useraddress"].value;
    var i =0;
    if (a === null || a === "") {
        document.getElementById("errorreg").innerHTML = "Fullname must not be empty</br>";        
        i=i+1;
    }
    if (b === null || b === "") {
        document.getElementById("errorreg").innerHTML += "Username must not be empty</br>";        
        i= i+1;
    }
    if (c === null || c === "") {
        document.getElementById("errorreg").innerHTML += "Email must not be empty</br>";        
        i= i+1;
    }
    else {
        if (validateEmail(c) === false) {
            i= i+ 1;
            document.getElementById("errorreg").innerHTML += "Please enter a valid e-mail</br>";        
        }
    }
    if (d === null || d === "") {
        document.getElementById("errorreg").innerHTML += "Password must not be empty</br>";        
        i= i+1;
    }
    if (e === null || e === "") {
        document.getElementById("errorreg").innerHTML += "Confirm password must not be empty</br>";        
        i= i+1;
    }
    if (f === null || f === "") {
        document.getElementById("errorreg").innerHTML += "Full address must not be empty</br>";        
        i= i+1;
    }
    var g = document.forms["registration"]["postcode"].value;
    if (g === null || g === "") {
        document.getElementById("errorreg").innerHTML += "Post code must not be empty</br>";        
        i= i+1;
    }
    var h = document.forms["registration"]["phonenum"].value;
    if (d !== e) {
        document.getElementById("errorreg").innerHTML += "Passwords does not match</br>";        
        i= i+1; 
    }
    if (h === null || h === "") {
        document.getElementById("errorreg").innerHTML += "Phone number must not be empty</br>";        
        i= i+1;
    }
    else {
        if (h.length < 10) {
            document.getElementById("errorreg").innerHTML += "Please enter a valid phone number</br>";        
            i= i+1;
        }
    }
    var q = document.forms["registration"]["city"].value;
    if (q === null || q === "") {
        document.getElementById("errorreg").innerHTML += "City must not be empty</br>";        
        i= i+1; 
    }
    if (i === 0) {
        //registerFb(c,d);
        //loginFb(c,d);
        return true;
    }
    else {
        document.getElementById("errorreg").setAttribute("style", "border: 2px solid #d0021b; padding: 2%; background : #fa8072;\n\
    color : #d0021b;margin-bottom : 3%;");
        return false;
    }
    
    
    
}
angular.module('Sojharo', [])
.directive('schrollBottom', function () {
  return {
    scope: {
      schrollBottom: "="
    },
    link: function (scope, element) {
      scope.$watchCollection('schrollBottom', function (newValue) {
        if (newValue)
        {
          $(element).scrollTop($(element)[0].scrollHeight);
        }
      });
    }
  }
})
.controller('MyController', function($scope,$http) {
  $scope.messages = [];
  $scope.im = {};
  $scope.dest = "=";
  $scope.showchat = false;
  $scope.sendIM = function() {
    
    $scope.messages.push($scope.im); 
   $http.get("/toChat",{ params:{ message: $scope.im.message , sender: $scope.im.sender, dest: $scope.dest } }).success( function(response) {
        console.log(response);
   });
    
     
    $scope.im = {};
   
  }
  $scope.setDes = function(x) {
      $scope.dest = x;
      $scope.chatTitle = x;
      $scope.showchat = !$scope.showchat;
  }
  
  $scope.close = function() {
      $scope.showchat = !$scope.showchat;
      $scope.messages = [];
  }
  messaging.onMessage(function(payload) {
       $scope.chatTitle = payload.data.senderName;
       $scope.showchat = true;
       $scope.im.message = payload.data.message;
       $scope.im.sender = payload.data.senderName;
       $scope.messages.push($scope.im);
       $scope.im = {};
       $scope.dest = payload.data.senderName;
       $scope.$apply();
       console.log(payload);
    });
});
messaging.onMessage(function(payload) {
       console.log(payload);
    });