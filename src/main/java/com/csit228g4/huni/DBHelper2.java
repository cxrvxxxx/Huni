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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brent
 */
public class DBHelper2 {
    private final String url, username, password;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    
    public DBHelper2() {
        // Connect to remote MySQL server
        url = "jdbc:mysql://localhost:3306/dbHuni";
        username = "root";
        password = "";
    }
    
    public DBHelper2(String url, String username, String password) {
        // Connect to custom MySQL server
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public void connectdb() {
        if (conn != null) return;
        
        // Create a MySQL connection
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disconnect() {
        if (conn == null) return;
        
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conn = null;
    }
    
    public int insertSongRecord(String title, String author) {
        // Adds a new song to the database
        try {
            // Check if the song already exists
            String sql = "SELECT * FROM tblSong WHERE title='" + title + "' AND author='" + author + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Exit if a song already exists
            if (rs.next()) return -1;
            
            // Add song to the database
            sql = "INSERT INTO tblSong (source, title, author, url, thumbnail) VALUES ('test', '" + title + "', '" + author + "', 'test', 'test')";
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    public Song displaySongRecord(int songId) {
        // Fetch a song from the database by id
        try {
            String sql = "SELECT * FROM tblSong WHERE id=" + songId;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Create and return a song instance if a song is found
            if (rs.next())
                return new Song(rs.getInt("id"), rs.getString("source"), rs.getString("title"), rs.getString("author"), rs.getString("url"), rs.getString("thumbnail"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return null;
    }
    
    public Song displaySongRecord(String title) {
        // Fetch a song from the database by title
        try {
            String sql = "SELECT * FROM tblSong WHERE title='" + title + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Create and return a song instance if a song is found
            if (rs.next())
                return new Song(rs.getInt("id"), rs.getString("source"), rs.getString("title"), rs.getString("author"), rs.getString("url"), rs.getString("thumbnail"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return null;
    }
    
    public Song displaySongRecord(String title, String author) {
        // Fetch a song from the database by title and author
        try {
            String sql = "SELECT * FROM tblSong WHERE title='" + title + "' AND author='" + author + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Create and return a song instance if a song is found
            if (rs.next())
                return new Song(rs.getInt("id"), rs.getString("source"), rs.getString("title"), rs.getString("author"), rs.getString("url"), rs.getString("thumbnail"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return null;
    }
    
    public ArrayList<Song> displayAllRecords() {
        // Fetch all songs from the database
        ArrayList<Song> songs = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblSong";
            stmt = conn.createStatement();
            rs =  stmt.executeQuery(sql);
           
            // Create a song instance and add to the list
            while (rs.next())
                songs.add(new Song(rs.getInt("id"), rs.getString("source"), rs.getString("title"), rs.getString("author"), rs.getString("url"), rs.getString("thumbnail")));

        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
            
        return songs;
    }
    
    public int updateSongRecord(Song s) {
        if (displaySongRecord(s.getId()) == null) return -1;
        
        try {
            String sql = "UPDATE tblSong SET title='" + s.getTitle() + "', author='" + s.getAuthor() + "' WHERE id=" + s.getId();
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    public int deleteSongRecord(Song s) {
        if (displaySongRecord(s.getId()) == null) return -1;
        
        try {
            String sql = "DELETE FROM tblSong WHERE id=" + s.getId();
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return 0;
    } 
}
