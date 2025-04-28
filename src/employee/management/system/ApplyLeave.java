package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ApplyLeave extends JFrame implements ActionListener {

    Choice leaveType;
    JTextArea reasonArea;
    JTextField dateField;
    JButton apply, back;
    String empId;
    EmployeePanel parentPanel;

    ApplyLeave(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Apply for Leave");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel(null);
        contentPanel.setBackground(new Color(240, 248, 255));
        setContentPane(contentPanel);

        JLabel heading = new JLabel("Apply for Leave", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 36));
        heading.setBounds(550, 30, 400, 50);
        contentPanel.add(heading);

        JLabel leaveLabel = new JLabel("Leave Type:");
        leaveLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        leaveLabel.setBounds(400, 120, 150, 30);
        contentPanel.add(leaveLabel);

        leaveType = new Choice();
        leaveType.add("Sick Leave");
        leaveType.add("Casual Leave");
        leaveType.add("Paid Leave");
        leaveType.setBounds(600, 125, 250, 25);
        contentPanel.add(leaveType);

        JLabel dateLabel = new JLabel("Leave Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dateLabel.setBounds(400, 180, 250, 30);
        contentPanel.add(dateLabel);

        dateField = new JTextField();
        dateField.setBounds(650, 185, 200, 25);
        contentPanel.add(dateField);

        JLabel reasonLabel = new JLabel("Reason:");
        reasonLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        reasonLabel.setBounds(400, 240, 100, 30);
        contentPanel.add(reasonLabel);

        reasonArea = new JTextArea();
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setBounds(600, 245, 300, 120);
        contentPanel.add(scrollPane);

        apply = new JButton("Apply");
        apply.setBounds(550, 400, 120, 40);
        apply.setBackground(new Color(0, 153, 76));
        apply.setForeground(Color.WHITE);
        apply.setFont(new Font("Arial", Font.BOLD, 16));
        apply.setCursor(new Cursor(Cursor.HAND_CURSOR));
        apply.addActionListener(this);
        contentPanel.add(apply);

        back = new JButton("Back");
        back.setBounds(700, 400, 120, 40);
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.setFocusPainted(false);
        back.setBorderPainted(false);
        back.setOpaque(true);
        back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                back.setBackground(Color.RED);
            }

            public void mouseExited(MouseEvent e) {
                back.setBackground(Color.RED);
            }
        });
        back.addActionListener(this);
        contentPanel.add(back);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == apply) {
            String leave = leaveType.getSelectedItem();
            String reason = reasonArea.getText().trim();
            String leaveDate = dateField.getText().trim();

            if (leaveDate.isEmpty() || reason.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate date format
            if (!isValidDate(leaveDate)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid date in YYYY-MM-DD format!", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Conn c = new Conn();
                String query = "INSERT INTO leave_requests (empId, leaveType, reason, leaveDate, status) VALUES (?, ?, ?, ?, 'Pending')";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, empId);
                ps.setString(2, leave);
                ps.setString(3, reason);
                ps.setDate(4, java.sql.Date.valueOf(leaveDate)); // MySQL DATE

                int result = ps.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Leave Applied Successfully!");
                    dispose();
                    parentPanel.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Something went wrong. Try again.");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Unexpected Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } else if (ae.getSource() == back) {
            dispose();
            parentPanel.setVisible(true);
        }
    }

    private boolean isValidDate(String input) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
