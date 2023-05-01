/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.csit228g4.huni;

/**
 *
 * @author Brent
 */
public class Huni {
    public static void main(String[] args) {
        // Create db connection
        Session.dbh.connectdb();
        
        // Init main frame instance
        HomeFrame frame = new HomeFrame();
        
        // Make frame visible
        frame.setVisible(true);
    }
}
