package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Notice extends JFrame {
    private String empId;
    private EmployeePanel parentPanel;
    private JTable noticeTable;
    private DefaultTableModel tableModel;

    public Notice(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Notice Board");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Heading
        JLabel heading = new JLabel("Company Notices", JLabel.CENTER);
        heading.setFont(new Font("Georgia", Font.BOLD, 30));
        heading.setForeground(new Color(44, 62, 80));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(heading, BorderLayout.NORTH);

        // Table for Notices
        String[] columns = {"Notices"};
        tableModel = new DefaultTableModel(columns, 0);
        noticeTable = new JTable(tableModel);
        noticeTable.setFont(new Font("Tahoma", Font.PLAIN, 18));
        noticeTable.setRowHeight(30);
        noticeTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(noticeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton printButton = new JButton("Print Notices");
        printButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        printButton.setBackground(new Color(52, 152, 219));
        printButton.setForeground(Color.WHITE);
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    noticeTable.print();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error printing: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        backButton.setBackground(new Color(231, 76, 60)); // Red
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        backButton.addActionListener(e -> {
            dispose();
            parentPanel.setVisible(true);
        });

        // Ensuring back button remains red on hover
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(231, 76, 60)); // Keep red
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(231, 76, 60)); // Keep red
            }
        });

        buttonPanel.add(printButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Fetch and display notices
        loadNotices();
        setVisible(true);
    }

    private void loadNotices() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT notice FROM notices ORDER BY id DESC");
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("notice")});
            }
            if (tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[]{"No notices available."});
            }
        } catch (Exception e) {
            e.printStackTrace();
            tableModel.addRow(new Object[]{"Error fetching notices."});
        }
    }
}
