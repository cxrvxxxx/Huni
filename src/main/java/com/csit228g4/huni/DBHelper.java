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
import java.util.ArrayList;
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
            String sql = "SELECT * FROM tblPlaylist WHERE name=? AND createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, String.valueOf(Session.activeUser.getId()));
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
    
    public Playlist getPlaylist(int playlistId) {
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(playlistId));
            rs = stmt.executeQuery();
            
            if (!rs.next())
                return new Playlist();
            
            return new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);              
        }
        
        return new Playlist();
    }
    
    public Playlist getPlaylist(String name) {
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE name=? AND createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, String.valueOf(Session.activeUser.getId()));
            rs = stmt.executeQuery();
            
            if (!rs.next())
                return new Playlist();
            
            return new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);              
        }
        
        return new Playlist();
    }
    
    public ArrayList<Playlist> getAllPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(Session.activeUser.getId()));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                playlists.add(new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);  
        }
        return playlists;
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
    
    public ArrayList<Song> getAllSongsFromPlaylist(String playlistName) {
        ArrayList<Song> songs = new ArrayList<>();
        Playlist pl = null;
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE name=? AND createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, playlistName);
            stmt.setString(2, String.valueOf(Session.activeUser.getId()));
            rs = stmt.executeQuery();
            
            if (rs.next()) 
                pl = new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn"));
            
            if (pl == null) return songs;
            
            sql = "SELECT * FROM tblPlaylistSong WHERE playlistId=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(pl.getId()));
            ResultSet plSongs =  stmt.executeQuery();
            
            while (plSongs.next()) {
                songs.add(getSong(plSongs.getInt("songId")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return songs;
    }
    
    public Song getSong(int songId) {
        try {
            String sql = "SELECT * FROM tblSong WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(songId));
            rs = stmt.executeQuery();
            
            if (rs.next())
                return new Song(rs.getInt("id"), rs.getString("source"), rs.getString("title"), rs.getString("author"), rs.getString("url"), rs.getString("thumbnail"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return new Song();
    }
    
    public Song getSong(String title) {
        try {
            String sql = "SELECT * FROM tblSong WHERE name=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            rs = stmt.executeQuery();
            
            if (rs.next())
                return new Song(rs.getInt("id"), rs.getString("source"), rs.getString("title"), rs.getString("author"), rs.getString("url"), rs.getString("thumbnail"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return new Song();
    }
    
    public Song getSong(String title, String author) {
        try {
            String sql = "SELECT * FROM tblSong WHERE title=? AND author=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            rs = stmt.executeQuery();
            
            if (rs.next())
                return new Song(rs.getInt("id"), rs.getString("source"), rs.getString("title"), rs.getString("author"), rs.getString("url"), rs.getString("thumbnail"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return new Song();
    }
    
    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblSong";
            stmt = conn.prepareStatement(sql);
            rs =  stmt.executeQuery();
           
            while (rs.next())
                songs.add(new Song(rs.getInt("id"), rs.getString("source"), rs.getString("title"), rs.getString("author"), rs.getString("url"), rs.getString("thumbnail")));

        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return songs;
    }
    
    public int addSong(String title, String author) {
        try {
            String sql = "SELECT * FROM tblSong WHERE title=? AND author=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            rs = stmt.executeQuery();
            if (rs.next()) return -1;
            
            sql = "INSERT INTO tblSong (source, title, author, url, thumbnail) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "test");
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, "test");
            stmt.setString(5, "test");

            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int addPlaylistSong(int playlistId, int songId) {
        try {
            String sql = "SELECT * FROM tblPlaylistSong WHERE playlistId=? AND songId=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(playlistId));
            stmt.setString(2, String.valueOf(songId));
            rs = stmt.executeQuery();
            
            if (rs.next()) return -1;
            
            sql = "INSERT INTO tblPlaylistSong (playlistId, songId, addedOn) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(playlistId));
            stmt.setString(2, String.valueOf(songId));
            stmt.setString(3, getCurrentDate());
            
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    

}
