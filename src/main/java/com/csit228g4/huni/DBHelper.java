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
    
    public DBHelper() {
        url = "jdbc:mysql://170.187.197.155:3306/dbHuni";
        username = "huni";
        password = "Huni_2023";
    }
    
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
    
    private String getCurrentDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
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
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, getCurrentDate());
                     
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    public User login(String username, String password) {
        try {
            String sql = "SELECT * FROM tblUser WHERE username=? AND password=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            
            if (!rs.next()) return new User();
            
            return new User(rs.getInt("id"), rs.getString("username"), getCurrentDate());
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new User();
    }
    
    public int createPlaylist(String name) {
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE name=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            
            if (rs.next()) return -1;
            
            sql = "INSERT INTO tblPlaylist (name, createdBy, createdOn) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, String.valueOf(Session.activeUser.getId()));
            stmt.setString(3, getCurrentDate());
            return stmt.executeUpdate();           
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return 0;
    }
    
    public ResultSet getAllPlaylists(int userId) {
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(userId));
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);  
        }
        return rs;
    }
    
    public int deletePlaylist(String name) {
        try {
            String sql = "DELETE FROM tblPlaylist WHERE name=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
