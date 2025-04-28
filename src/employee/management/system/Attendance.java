package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Attendance extends JFrame implements ActionListener {
    private JButton clockIn, clockOut, back;
    private String empId;
    private EmployeePanel parentPanel;

    public Attendance(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Employee Attendance");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        getContentPane().setBackground(new Color(240, 248, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Header
        JLabel heading = new JLabel("Mark Your Attendance", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(heading, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 40, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 100, 200));

        clockIn = createButton("Clock In", new Color(46, 204, 113));
        clockOut = createButton("Clock Out", new Color(52, 152, 219));
        back = createButton("Back", Color.RED);

        buttonPanel.add(clockIn);
        buttonPanel.add(clockOut);
        buttonPanel.add(back);

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setBorder(BorderFactory.createLineBorder(baseColor.darker()));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Red button stays red even on hover
        if (text.equals("Back")) {
            button.setRolloverEnabled(false);
        } else {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(baseColor.darker());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(baseColor);
                }
            });
        }

        button.addActionListener(this);
        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == clockIn || source == clockOut) {
            String status = (source == clockIn) ? "Clocked In" : "Clocked Out";
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            try {
                Conn c = new Conn();
                String query = "INSERT INTO attendance (empId, date, status) VALUES ('" + empId + "', '" + date + "', '" + status + "')";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Attendance Marked: " + status, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error marking attendance.\nPlease contact Admin to check your attendance via Admin Panel.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }
}
