/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageLeaves extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton approveBtn, rejectBtn, backBtn;
    Home homeRef;

    ManageLeaves(Home homeRef) {
        this.homeRef = homeRef;

        setTitle("Manage Leave Applications");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(240, 248, 255));
        setContentPane(contentPanel);

        JLabel heading = new JLabel("Leave Requests", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 36));
        heading.setBounds(500, 30, 400, 50);
        contentPanel.add(heading);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        model.addColumn("Request ID");
        model.addColumn("Employee ID");
        model.addColumn("Leave Type");
        model.addColumn("Reason");
        model.addColumn("Leave Date");
        model.addColumn("Status");

        fetchLeaveRequests();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(200, 100, 1000, 400);
        contentPanel.add(scrollPane);

        approveBtn = new JButton("Approve");
        approveBtn.setBounds(400, 530, 150, 40);
        approveBtn.setBackground(new Color(0, 153, 76));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setFont(new Font("Arial", Font.BOLD, 16));
        approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        approveBtn.addActionListener(this);
        contentPanel.add(approveBtn);

        rejectBtn = new JButton("Reject");
        rejectBtn.setBounds(600, 530, 150, 40);
        rejectBtn.setBackground(Color.RED);
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setFont(new Font("Arial", Font.BOLD, 16));
        rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rejectBtn.addActionListener(this);
        contentPanel.add(rejectBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(800, 530, 150, 40);
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setOpaque(true);
        backBtn.addActionListener(this);
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backBtn.setBackground(Color.RED);
            }
            public void mouseExited(MouseEvent e) {
                backBtn.setBackground(Color.RED);
            }
        });
        contentPanel.add(backBtn);

        setVisible(true);
    }

    private void fetchLeaveRequests() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM leave_requests");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("empId"),
                    rs.getString("leaveType"),
                    rs.getString("reason"),
                    rs.getString("leaveDate"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLeaveStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to " + status.toLowerCase() + "!");
            return;
        }

        int requestId = (int) model.getValueAt(selectedRow, 0);

        try {
            Conn c = new Conn();
            PreparedStatement ps = c.c.prepareStatement("UPDATE leave_requests SET status = ? WHERE id = ?");
            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();

            model.setValueAt(status, selectedRow, 5);
            JOptionPane.showMessageDialog(this, "Request " + status + " successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status!");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == approveBtn) {
            updateLeaveStatus("Approved");
        } else if (ae.getSource() == rejectBtn) {
            updateLeaveStatus("Rejected");
        } else if (ae.getSource() == backBtn) {
            dispose();
            homeRef.setVisible(true);
        }
    }
}
