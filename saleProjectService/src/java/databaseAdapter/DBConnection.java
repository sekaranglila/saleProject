package databaseAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kris
 */
public class DBConnection {
    public static Connection connectDB() {
        String JDBCDriver = "com.mysql.jdbc.Driver";
        String DBUser = "root";
        String DBPass = "";
        String JDBCUrl = "jdbc:mysql://localhost:3306/marketplace";
        try {
            Class.forName(JDBCDriver); //REGISTER JDBC DRIVER
            return DriverManager.getConnection(JDBCUrl, DBUser, DBPass);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
