/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnEmployee {
    
    Connection c;
    Statement s;

public ConnEmployee(){
    
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        c= DriverManager.getConnection("jdbc:mysql:///employeemanagementsystem","root","allah");
        s=c.createStatement();
        
    }catch(Exception e){
        e.printStackTrace();
    }
    
}
}
