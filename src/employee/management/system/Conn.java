package employee.management.system;
import java.sql.*;

public class Conn {
    public Connection c;
    public Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///employeemanagementsystem", "root", "allah");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
