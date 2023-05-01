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
        // Connect to remote MySQL server
        url = "jdbc:mysql://170.187.197.155:3306/dbHuni";
        username = "huni";
        password = "Huni_2023";
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
    
    private String getCurrentDate() {
        // Get current date and convert to string
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }
    
    public int register(String username, String password) {
        // Add a new user to the database
        try {
            // Check if the user already exists
            String sql = "SELECT * FROM tblUser WHERE username=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            // If the user already exists
            if (rs.next()) return -1;
            
            // Add the user data to the database
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
        // Check user credentials
        try {
            // Check if user exists
            String sql = "SELECT * FROM tblUser WHERE username=? AND password=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            
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
            String sql = "SELECT * FROM tblPlaylist WHERE name=? AND createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, String.valueOf(Session.activeUser.getId()));
            rs = stmt.executeQuery();
            
            // If the playlist already exists
            if (rs.next()) return -1;
            
            // Add playlist data into the database
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
        // Fetch playlist from database using id
        try {
            String sql = "SELECT * FROM tblPlaylist WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(playlistId));
            rs = stmt.executeQuery();
            
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
            String sql = "SELECT * FROM tblPlaylist WHERE name=? AND createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, String.valueOf(Session.activeUser.getId()));
            rs = stmt.executeQuery();
            
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
            String sql = "SELECT * FROM tblPlaylist WHERE createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(Session.activeUser.getId()));
            rs = stmt.executeQuery();
            
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
            String sql = "UPDATE tblPlaylist SET name=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pl.getName());
            stmt.setString(2, String.valueOf(pl.getId()));
            res = stmt.executeUpdate();
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
            String sql = "DELETE FROM tblPlaylist WHERE name=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pl.getName());
            res = stmt.executeUpdate();
            
            // Delete all songs from the playlist
            sql = "DELETE FROM tblPlaylistSong WHERE playlistId=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(pl.getId()));
            res = stmt.executeUpdate();
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
            String sql = "SELECT * FROM tblPlaylist WHERE name=? AND createdBy=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, playlistName);
            stmt.setString(2, String.valueOf(Session.activeUser.getId()));
            rs = stmt.executeQuery();
            
            // Create a playlist instance
            if (rs.next()) 
                pl = new Playlist(rs.getInt("id"), rs.getString("name"), rs.getInt("createdBy"), rs.getString("createdOn"));
            
            // Return empty list if no playlist is found
            if (pl == null) return songs;
            
            // Find all songs that belongs to playlist
            sql = "SELECT * FROM tblPlaylistSong WHERE playlistId=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(pl.getId()));
            ResultSet plSongs =  stmt.executeQuery();
            
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
            String sql = "SELECT * FROM tblSong WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(songId));
            rs = stmt.executeQuery();
            
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
            String sql = "SELECT * FROM tblSong WHERE title=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            rs = stmt.executeQuery();
            
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
            String sql = "SELECT * FROM tblSong WHERE title=? AND author=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            rs = stmt.executeQuery();
            
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
            stmt = conn.prepareStatement(sql);
            rs =  stmt.executeQuery();
           
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
            String sql = "SELECT * FROM tblSong WHERE title=? AND author=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            rs = stmt.executeQuery();
            
            // Exit if a song already exists
            if (rs.next()) return -1;
            
            // Add song to the database
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
        // Add a song to a playlist
        try {
            // Check if the song is already in the playlist
            String sql = "SELECT * FROM tblPlaylistSong WHERE playlistId=? AND songId=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(playlistId));
            stmt.setString(2, String.valueOf(songId));
            rs = stmt.executeQuery();
            
            // Exit if song is already in the playlist
            if (rs.next()) return -1;
            
            // Add song to playlist
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
    
    public int removePlaylistSong(int playlistId, int songId) {
        // Remove song from playlist
        int res = 0;
        try {
            String sql = "DELETE FROM tblPlaylistSong WHERE playlistId=? AND songId=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(playlistId));
            stmt.setString(2, String.valueOf(songId));
            
            res = stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return res;
    }
}
