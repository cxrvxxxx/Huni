/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csit228g4.huni;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brent
 */
public class DBHelper {
    private final String url, username, password;
    private Connection conn;
    private PreparedStatement stmt;
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
            String sql = "SELECT * FROM ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tableName);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public int register(String username, String password) {
        try {
            String sql = "SELECT * FROM tblUser WHERE username=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            if (rs.next()) return -1;
            
            sql = "INSERT INTO tblUser (username, password, createdOn) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            
            java.util.Date date = new java.util.Date();
            String createdOn = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, createdOn);
                     
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
//    public User login(String username, String password) {
//        try {
//            stmt = conn.prepareStatement(sql);
//            rs = stmt.executeQuery(
//                    "SELECT * FROM tblUser WHERE username=" + username + " " +
//                    "AND password=" + password);
//            
//            if (!rs.next()) return new User();
//            
//            java.util.Date date = new java.util.Date();
//            String loginDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//            
//            return new User(rs.getInt("id"), rs.getString("username"), loginDate);
//        } catch (SQLException ex) {
//            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return new User();
//    }
}
