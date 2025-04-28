package employee.management.system;

import javax.swing.*;
import net.proteanit.sql.DbUtils;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AttendanceChecker extends JFrame implements ActionListener {

    JTable table;
    JScrollPane scrollPane;
    JButton back;
    Home home;

    public AttendanceChecker(Home home) {
        this.home = home;

        setTitle("Attendance Checker");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Employee Attendance Records", JLabel.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 36));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        back = new JButton("Back");
        back.setFont(new Font("Tahoma", Font.BOLD, 18));
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.setPreferredSize(new Dimension(100, 50));
        back.addActionListener(this);
        add(back, BorderLayout.SOUTH);

        populateAttendanceData();
        setVisible(true);
    }

    private void populateAttendanceData() {
        try {
            Conn c = new Conn();
            String query = "SELECT empId AS 'Employee ID', date AS 'Date', status AS 'Status' FROM attendance ORDER BY date DESC";
            ResultSet rs = c.s.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load attendance records", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            this.dispose();
            home.setVisible(true);
        }
    }
}
