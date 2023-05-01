/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csit228g4.huni;

/**
 *
 * @author Brent
 * 
 * This class stores the data for the currently logged in user
 */
public class Session {
    public static User activeUser = new User();
    public static DBHelper dbh = new DBHelper();
}
