/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csit228g4.huni;

/**
 *
 * @author Brent
 */
public class Song {
    private int id;
    private String source, title, author, url, thumbnail;
    
    public Song() {
        id = -1;
    }
    
    public Song(int id, String source, String title, String author, String url, String thumbnail) {
        this.id = id;
        this.source = source;
        this.title = title;
        this.author = author;
        this.url = url;
        this.thumbnail = thumbnail;
    }
    
    public int getId() { return id; }
    public String getSource() { return source; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getUrl() { return url; }
    public String getThumbnail() { return thumbnail; }
}
