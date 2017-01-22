package connectFb;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kris
 */
public class connectFb {
    private static JSONObject processData;
    private static byte[] data;
    
    public static void setData (String token, String message, String userSend) {
        /**Create new JSON message**/
        JSONObject jsonMessage = new JSONObject();
        JSONObject jsonBody = new JSONObject();

        jsonMessage.put("message", message);
        jsonMessage.put("senderName", userSend);
        jsonBody.put("to", token);
        jsonBody.put("data", jsonMessage);
        
        data = jsonBody.toJSONString().getBytes();
    }
    
    public static JSONObject sendData() throws ParseException, UnsupportedEncodingException {
        String urlName = "https://fcm.googleapis.com/fcm/send";
        URL url;
        HttpURLConnection connection = null;  
 
        try {
            //Create connection
            url = new URL(urlName);
            connection = (HttpURLConnection)url.openConnection();
            //add request header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "key=AAAA45DoXN0:APA91bH2iTTu42SiFDbjfk-PKN5_KYraSCocMmt2fIDI7Gz9iYwVtXchrGNen8kLrYhB-P4bim47XbJpmc6PBYPVdJjngcl7aZ_YCQuR11uq10CiCkeJ3jsyxkxVbgavzZe3UmPw1gZv4F7I4qllwqt4XjFb_6ttFw");
            connection.setUseCaches(true);
            connection.setAllowUserInteraction(true);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            //Send request body
            try (OutputStream out = connection.getOutputStream()){
                out.write(data);
                out.close();
            }
            
            //Start Connection
            connection.connect();
            
            //Recieve Response
            StringBuilder sb = new StringBuilder();
            BufferedReader rd = new BufferedReader (new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = rd.readLine()) != null) {
                sb.append(line);
            }
            connection.disconnect();
            processData = (JSONObject) new JSONParser().parse(sb.toString());
            return processData;
        }
        catch (IOException e) {
            return null;
        } 
    }
}
