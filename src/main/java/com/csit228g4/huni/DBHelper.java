/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csit228g4.huni;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brent
 */
public class DBHelper {
    private final String url, username, password;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    
    public DBHelper(String url) {
        this.url = url;
        username = "root";
        password = "";
    }
    
    public DBHelper(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public void connectdb() {
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet displayAllRecords(String tableName) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public int register(String username, String password) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM tblUser WHERE username=" + username);
            
            if (rs.next()) return -1;
            
            stmt = conn.createStatement();
            
            java.util.Date date = new java.util.Date();
            String createdOn = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            
            int res = stmt.executeUpdate(
                    "INSERT INTO tblUser (username, password, createdOn)"
                  + "VALUES (" + username + "," + password + ","+ createdOn + ")"
            );
            
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    public User login(String username, String password) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT * FROM tblUser WHERE username=" + username + " " +
                    "AND password=" + password);
            
            if (!rs.next()) return new User();
            
            java.util.Date date = new java.util.Date();
            String loginDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            
            return new User(rs.getInt("id"), rs.getString("username"), loginDate);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new User();
    }
}
