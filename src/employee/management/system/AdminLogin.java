package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminLogin extends JFrame implements ActionListener {
    
    private JTextField tfusername;
    private JPasswordField tfpassword;
    private JButton login, back;
    
    AdminLogin() {
        setTitle("Admin Login - Employee Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        // Background Color
        getContentPane().setBackground(new Color(240, 248, 255));

        // Title Label
        JLabel title = new JLabel("Admin Login");
        title.setFont(new Font("Serif", Font.BOLD, 40));
        title.setBounds(500, 50, 400, 50);
        add(title);
        
        // Username Label & Field
        JLabel lblusername = new JLabel("Username:");
        lblusername.setFont(new Font("Tahoma", Font.PLAIN, 22));
        lblusername.setBounds(480, 150, 150, 30);
        add(lblusername);
        
        tfusername = new JTextField();
        tfusername.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tfusername.setBounds(650, 150, 250, 35);
        add(tfusername);
        
        // Password Label & Field
        JLabel lblpassword = new JLabel("Password:");
        lblpassword.setFont(new Font("Tahoma", Font.PLAIN, 22));
        lblpassword.setBounds(480, 220, 150, 30);
        add(lblpassword);
        
        tfpassword = new JPasswordField();
        tfpassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tfpassword.setBounds(650, 220, 250, 35);
        add(tfpassword);
        
        // Login Button
        login = new JButton("LOGIN");
        login.setFont(new Font("Tahoma", Font.BOLD, 20));
        login.setBounds(650, 300, 120, 40);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.addActionListener(this);
        add(login);
        
        // Back Button
        back = new JButton("BACK");
        back.setFont(new Font("Tahoma", Font.BOLD, 20));
        back.setBounds(800, 300, 120, 40);
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(this);
        add(back);
        

        

        // KeyListener for Enter Key Login
        tfpassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginAction();
                }
            }
        });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            loginAction();
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Splash();
        }
    }

    private void loginAction() {
        try {
            String username = tfusername.getText();
            String password = new String(tfpassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both username and password");
                return;
            }

            Conn c = new Conn();
            String query = "SELECT * FROM login WHERE username = ? AND password = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                setVisible(false);
                SwingUtilities.invokeLater(() -> new Home());
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AdminLogin();
    }
}
