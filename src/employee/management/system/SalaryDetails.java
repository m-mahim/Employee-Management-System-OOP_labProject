package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SalaryDetails extends JFrame {

    String empId;
    EmployeePanel parentPanel;
    JTable table;

    SalaryDetails(String empId, EmployeePanel parentPanel) {
        this.empId = empId;
        this.parentPanel = parentPanel;

        setTitle("Salary Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Salary Details", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(new Color(0, 51, 102));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(heading, BorderLayout.NORTH);

        // Table setup
        table = new JTable();
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        // Back Button
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(130, 45));
        backBtn.addActionListener(e -> {
            dispose();
            parentPanel.setVisible(true);
        });
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backBtn.setBackground(Color.RED);
            }

            public void mouseExited(MouseEvent e) {
                backBtn.setBackground(Color.RED);
            }
        });

        // Print Button
        JButton printBtn = new JButton("Print Payslip");
        printBtn.setFont(new Font("Arial", Font.BOLD, 18));
        printBtn.setBackground(new Color(34, 139, 34));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printBtn.setPreferredSize(new Dimension(200, 45));
        printBtn.addActionListener(e -> {
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        });

        buttonPanel.add(backBtn);
        buttonPanel.add(printBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load Salary Details
        loadSalary();

        setVisible(true);
    }

    private void loadSalary() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT salary FROM employee WHERE empId = '" + empId + "'");
            if (rs.next()) {
                String rawSalary = rs.getString("salary").replaceAll("[^\\d.]", ""); // Remove symbols/text
                double monthlySalary = Double.parseDouble(rawSalary);

                double annualSalary = monthlySalary * 12;
                double tax = annualSalary * 0.05;
                double monthlyAfterTax = (annualSalary - tax) / 12;

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Description");
                model.addColumn("Amount ");

                model.addRow(new Object[]{"Monthly Salary", monthlySalary});
                model.addRow(new Object[]{"Annual Package", annualSalary});
                model.addRow(new Object[]{"5% Annual Tax", tax});
                model.addRow(new Object[]{"Monthly After Tax", String.format("%.2f", monthlyAfterTax)});

                table.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "Salary not found for Employee ID: " + empId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary: " + e.getMessage());
        }
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    public static void main(String[] args) {
        new SalaryDetails("E001", null); // For testing only
    }
}
