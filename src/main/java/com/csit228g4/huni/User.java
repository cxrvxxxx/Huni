/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csit228g4.huni;

/**
 *
 * @author Brent
 */
public class User {
   private int id;
   private String username;
   private String loginDate;
   private boolean isLoggedIn = false;
   
   public User() {}
   
   public User(int id, String username, String loginDate) {
       this.id = id;
       this.username = username;
       this.loginDate = loginDate;
       isLoggedIn = true;
   }
   
   public int getId() { return id; }
   
   public String getUsername() { return username; }
   
   public String getLoginDate() { return loginDate; }

   public boolean getLoginStatus() { return isLoggedIn; }
}
