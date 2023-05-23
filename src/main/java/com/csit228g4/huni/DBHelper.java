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
public class DBHelper {
    private final String url, username, password;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    
    public DBHelper() {
        // Connect to remote MySQL server
        url = "jdbc:mysql://localhost:3306/dbHuni";
        username = "root";
        password = "";
    }
    
    public DBHelper(String url, String username, String password) {
        // Connect to custom MySQL server
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public void connectdb() {
        // Create a MySQL connection
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean checkUser(String username) {
        try {
            String sql = "SELECT * FROM tblUser WHERE username='" + username + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    private String getCurrentDate() {
        // Get current date and convert to string
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }
    
    public int register(String username, String password) {
        // Add a new user to the database
        try {
            // Check if the user already exists
            if (checkUser(username))
                return -1;
            
            // Add the user data to the database
            String sql = "INSERT INTO tblUser (username, password, createdOn) VALUES ('" + username + "', '" + password + "', '" + getCurrentDate() + "')";
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    public User login(String username, String password) {
        // Check user credentials
        try {
            // Check if user exists
            String sql = "SELECT * FROM tblUser WHERE username='" + username + "' AND password='" + password + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Return a default user instance if user not in database
            if (!rs.next()) return new User();
            
            // Create and return a user instance if found
            return new User(rs.getInt("id"), rs.getString("username"), getCurrentDate());
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new User();
    }
    
    public int createPlaylist(String name) {
        // Add a new playlist to the database
        try {
            // Check if the playlist already exists
            String sql = "SELECT * FROM tblPlaylist WHERE name='" + name + "' AND createdBy=" + Session.activeUser.getId();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // If the playlist already exists
            if (rs.next()) return -1;
            
            // Add playlist data into the database
            sql = "INSERT INTO tblPlaylist (name, createdBy, createdOn) VALUES ('" + name + "', " + Session.activeUser.getId() + ", '" + getCurrentDate() + "')";
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return 0;
    }
    
    public Playlist getPlaylist(int playlistId) {
        // Fetch playlist from database using id
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE id=" + playlistId;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Create and return default instance if playlist does not exist
            if (!rs.next()) return new Playlist();
            
            // Create and return a playlist instance
            return new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);              
        }
        
        return new Playlist();
    }
    
    public Playlist getPlaylist(String name) {
        // Fetch playlist from database using name
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE name='" + name + "' AND createdBy=" + Session.activeUser.getId();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Create and return default instance if playlist does not exist
            if (!rs.next())
                return new Playlist();
            
            // Create and return a playlist instance
            return new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn"));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);              
        }
        
        return new Playlist();
    }
    
    public ArrayList<Playlist> getAllPlaylists() {
        // Fetch all playlists made by currently logged in user
        ArrayList<Playlist> playlists = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE createdBy=" + Session.activeUser.getId();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Add new playlist instance to array list
            while (rs.next())
                playlists.add(new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn")));

        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);  
        }
        return playlists;
    }
    
    public int updatePlaylist(Playlist pl) {
        // Updates a playlist in the database
        int res = 0;
        try {
            // Update playlist
            String sql = "UPDATE tblPlaylist SET name='" + pl.getName() + "' WHERE id=" + pl.getId();
            stmt = conn.createStatement();
            res = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);              
        }
        return res;
    }
    
    public int deletePlaylist(String playlistName) {
        // Deletes a playlist from the database
        int res = 0;
        try {
            // Fetch playlist from database using the name
            Playlist pl = getPlaylist(playlistName);
            
            // Exit if no playlist is found
            if (pl.getId() == -1) return -1;
            
            // Delete matching playlist from the database
            String sql = "DELETE FROM tblPlaylist WHERE name='" + playlistName + "'";
            stmt = conn.createStatement();
            res = stmt.executeUpdate(sql);
            
            // Delete all songs from the playlist
            sql = "DELETE FROM tblPlaylistSong WHERE playlistId=" + pl.getId();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public ArrayList<Song> getAllSongsFromPlaylist(String playlistName) {
        // Fetch all songs that belongs to specified playlist
        ArrayList<Song> songs = new ArrayList<>();
        Playlist pl = null;
        try {
            // Retrieve specified playlist from database
            String sql = "SELECT * FROM tblPlaylist WHERE name='" + playlistName + "' AND createdBy=" + Session.activeUser.getId();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Create a playlist instance
            if (rs.next()) 
                pl = new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn"));
            
            // Return empty list if no playlist is found
            if (pl == null) return songs;
            
            // Find all songs that belongs to playlist
            sql = "SELECT * FROM tblPlaylistSong WHERE playlistId=" + pl.getId();
            stmt = conn.createStatement();
            ResultSet plSongs =  stmt.executeQuery(sql);
            
            // Create and add song instance to array list
            while (plSongs.next())
                songs.add(getSong(plSongs.getInt("songId")));
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return songs;
    }
    
    public Song getSong(int songId) {
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
        return new Song();
    }
    
    public Song getSong(String title) {
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
        return new Song();
    }
    
    public Song getSong(String title, String author) {
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
        return new Song();
    }
    
    public ArrayList<Song> getAllSongs() {
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
    
    public int addSong(String title, String author) {
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
    
    public int addPlaylistSong(int playlistId, int songId) {
        // Add a song to a playlist
        try {
            // Check if the song is already in the playlist
            String sql = "SELECT * FROM tblPlaylistSong WHERE playlistId=" + playlistId + " AND songId=" + songId;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            // Exit if song is already in the playlist
            if (rs.next()) return -1;
            
            // Add song to playlist
            sql = "INSERT INTO tblPlaylistSong (playlistId, songId, addedOn) VALUES (" + playlistId + ", " + songId + ", '" + getCurrentDate() + "')";
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int removePlaylistSong(int playlistId, int songId) {
        // Remove song from playlist
        int res = 0;
        try {
            String sql = "DELETE FROM tblPlaylistSong WHERE playlistId=" + playlistId + " AND songId=" + songId;
            stmt = conn.createStatement();
            res = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return res;
    }
}
