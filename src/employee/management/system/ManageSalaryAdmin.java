package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageSalaryAdmin extends JFrame {

    private JTextField empIdField, salaryField;
    private JTable salaryTable;

    public ManageSalaryAdmin(JFrame parent) {
        setTitle("Manage Salary - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Heading
        JLabel heading = new JLabel("Manage Salary", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heading.setForeground(new Color(0, 51, 102));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(heading, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        JLabel empIdLabel = new JLabel("Employee ID:");
        empIdLabel.setFont(new Font("Arial", Font.BOLD, 18));
        empIdField = new JTextField(15);
        empIdField.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel salaryLabel = new JLabel("Salary :");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        salaryField = new JTextField(15);
        salaryField.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton fetchBtn = new JButton("Fetch");
        styleButton(fetchBtn, new Color(0, 102, 204));
        fetchBtn.addActionListener(e -> fetchSalary());

        JButton updateBtn = new JButton("Update Salary");
        styleButton(updateBtn, new Color(34, 139, 34));
        updateBtn.addActionListener(e -> updateSalary());

        // Layout form
        gbc.gridx = 0; gbc.gridy = 0; centerPanel.add(empIdLabel, gbc);
        gbc.gridx = 1; centerPanel.add(empIdField, gbc);
        gbc.gridx = 2; centerPanel.add(fetchBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1; centerPanel.add(salaryLabel, gbc);
        gbc.gridx = 1; centerPanel.add(salaryField, gbc);
        gbc.gridx = 2; centerPanel.add(updateBtn, gbc);

        add(centerPanel, BorderLayout.NORTH);

        // Table
        salaryTable = new JTable();
        styleTable(salaryTable);
        JScrollPane tablePane = new JScrollPane(salaryTable);
        tablePane.getViewport().setBackground(Color.WHITE);
        add(tablePane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 248, 255));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(130, 45));
        backBtn.addActionListener(e -> {
            dispose();
            parent.setVisible(true);
        });
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backBtn.setBackground(new Color(200, 0, 0));
            }

            public void mouseExited(MouseEvent e) {
                backBtn.setBackground(Color.RED);
            }
        });

        JButton printBtn = new JButton("Print");
        styleButton(printBtn, new Color(0, 102, 204));
        printBtn.addActionListener(e -> {
            try {
                salaryTable.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage());
            }
        });

        bottomPanel.add(backBtn);
        bottomPanel.add(printBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void fetchSalary() {
        String empId = empIdField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Employee ID.");
            return;
        }

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT salary FROM employee WHERE empId = '" + empId + "'");
            if (rs.next()) {
                String salary = rs.getString("salary").replaceAll("[^\\d.]", "");
                salaryField.setText(salary);

                double monthly = Double.parseDouble(salary);
                double annual = monthly * 12;
                double tax = annual * 0.05;
                double afterTax = (annual - tax) / 12;

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Description");
                model.addColumn("Amount ");
                model.addRow(new Object[]{"Monthly Salary", monthly});
                model.addRow(new Object[]{"Annual Package", annual});
                model.addRow(new Object[]{"5% Annual Tax", tax});
                model.addRow(new Object[]{"Monthly After Tax", String.format("%.2f", afterTax)});

                salaryTable.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "No record found for Employee ID: " + empId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching salary: " + e.getMessage());
        }
    }

    private void updateSalary() {
        String empId = empIdField.getText().trim();
        String salary = salaryField.getText().trim();
        if (empId.isEmpty() || salary.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.");
            return;
        }

        try {
            double salaryNum = Double.parseDouble(salary);
            Conn c = new Conn();
            int result = c.s.executeUpdate("UPDATE employee SET salary = '" + salaryNum + "' WHERE empId = '" + empId + "'");
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Salary updated successfully!");
                fetchSalary(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric salary.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
        }
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 45));
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
        new ManageSalaryAdmin(null);
    }
}
