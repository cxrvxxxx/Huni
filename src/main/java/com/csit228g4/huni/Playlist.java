/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csit228g4.huni;

/**
 *
 * @author Brent
 */
public class Playlist {
    private int id, createdBy;
    private String name, createdOn;
    
    public Playlist() {
        id = -1;
    }
    
    public Playlist(int id, String name, int createdBy, String createdOn) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCreatedBy() { return createdBy; }
    public String getCreatedOn() { return createdOn; }
}
