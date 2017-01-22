/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identityConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import java.net.URL;
import java.net.URLEncoder;
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
public class identityConnect {
    private final static String IDENTITYURL = "http://localhost:10471/identityService";
    public static JSONObject processRequest(String servlet, byte[] query) {
        JSONObject response = null;
        if (servlet != null && !servlet.startsWith("/"))
                servlet = "/" + servlet;
        try {
            // Establish HTTP connection with Identity Service
            String urlString = IDENTITYURL + servlet;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            //Create the form content
            try (OutputStream out = conn.getOutputStream()) {
                out.write(query);
                out.close();
            }
            conn.connect();
            StringBuilder sb;
            try ( // Buffer the result into a string
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()))) {
                sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
            }
            conn.disconnect();
            response = (JSONObject) new JSONParser().parse(sb.toString());
                      
        } catch (IOException | ParseException e) {
            
        } 
        return response;
    }
    public static JSONObject validateLogin(String username, String password, String fbToken,String ua,String ip) {
        JSONObject response = null;
        try {
            String utf8 = java.nio.charset.StandardCharsets.UTF_8.name();
            String query = String.format("username=%s&userpass=%s&fbtoken=%s&ua=%s&ip=%s",
                    URLEncoder.encode(username, utf8),
                    URLEncoder.encode(password, utf8),
                    URLEncoder.encode(fbToken, utf8),
                    URLEncoder.encode(ua, utf8),
                    URLEncoder.encode(ip, utf8));
            response = processRequest("/loginServlet", query.getBytes());
        } catch (UnsupportedEncodingException e) {
        }
        return response;
    }
    
    public static JSONObject validateToken(String token, int id, String ipua) {
        JSONObject response = null;
        try {
            String utf8 = java.nio.charset.StandardCharsets.UTF_8.name();
            String query = String.format("token=%s&uid=%d&ipua=%s",
                    URLEncoder.encode(token, utf8),
                    Integer.parseInt(URLEncoder.encode(Integer.toString(id), utf8)),
                    URLEncoder.encode(ipua, utf8));
            response = processRequest("/tokenServletBonus", query.getBytes()); 
            
        } catch (UnsupportedEncodingException e) {
        }
        return response;
    }
    
    public static JSONObject getUser(int id) {
        JSONObject response = null;
        try {
            String utf8 = java.nio.charset.StandardCharsets.UTF_8.name();
            String query = String.format("uid=%d",
                    Integer.parseInt(URLEncoder.encode(Integer.toString(id), utf8)));
            response = processRequest("/getUserServlet", query.getBytes()); 
            
        } catch (UnsupportedEncodingException e) {
        }
        return response;
    }
    
    
    public static JSONObject logoutUser(int id) {
        JSONObject response = new JSONObject();
        try {
            String utf8 = java.nio.charset.StandardCharsets.UTF_8.name();
            String query = String.format("uid=%d",
                    Integer.parseInt(URLEncoder.encode(Integer.toString(id), utf8)));
            response = processRequest("/logoutServlet", query.getBytes()); 
            
        } catch (UnsupportedEncodingException e) {
        }
        return response;
    }
    
    public static JSONObject registerUser(String fn,String un, String up, String ue, String ua, String pc,
            String pn, String uc, String fbt, String uag, String ip) {
        String utf8 = java.nio.charset.StandardCharsets.UTF_8.name();
        JSONObject js = new JSONObject();
        try {
            String query = String.format("fn=%s&un=%s&up=%s&ue=%s&ua=%s&pc=%s&pn=%s&uc=%s",
            URLEncoder.encode(fn, utf8),URLEncoder.encode(un, utf8),URLEncoder.encode(up, utf8),
            URLEncoder.encode(ue, utf8),URLEncoder.encode(ua, utf8),URLEncoder.encode(pc, utf8),
            URLEncoder.encode(pn, utf8),URLEncoder.encode(uc, utf8));
            JSONObject midRes = processRequest("/registerServlet", query.getBytes());
            if ("successRegistered".equals(midRes.get("status").toString())) {
                js = validateLogin(un,up,fbt,uag,ip);
            }
        }
        catch (UnsupportedEncodingException e){
            js.put("status",e.getMessage());
        }
        return js;
    }
    
    
}
