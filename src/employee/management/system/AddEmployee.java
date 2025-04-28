package employee.management.system;

import java.awt.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AddEmployee extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfphone, tfemail, tfaddress, tfnid;
    JDateChooser dcdob;
    JComboBox<String> cbeducation, cbdesignation;
    JLabel lblempId, lsalary;
    JButton add, back;

    AddEmployee() {
        // Fullscreen setup
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(null);

        // Title
        JLabel heading = new JLabel("Add New Employee");
        heading.setBounds(600, 30, 600, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 35));
        add(heading);

        // Name
        addLabel("Name *", 100, 150);
        tfname = addTextField(250, 150);

        // Father's Name
        addLabel("Father's Name *", 600, 150);
        tffname = addTextField(800, 150);

        // Date of Birth
        addLabel("Date of Birth *", 100, 200);
        dcdob = new JDateChooser();
        dcdob.setBounds(250, 200, 200, 30);
        add(dcdob);

        // Designation
        addLabel("Designation *", 600, 200);
        String[] designations = {"Lead Manager", "Senior Developer", "Junior Developer", "Intern"};
        cbdesignation = new JComboBox<>(designations);
        cbdesignation.setBounds(800, 200, 200, 30);
        cbdesignation.setBackground(Color.WHITE);
        add(cbdesignation);

        // Address
        addLabel("Address", 100, 250);
        tfaddress = addTextField(250, 250);

        // NID
        addLabel("NID *", 600, 250);
        tfnid = addTextField(800, 250);

        // Phone
        addLabel("Phone", 100, 300);
        tfphone = addTextField(250, 300);

        // Email
        addLabel("Email", 600, 300);
        tfemail = addTextField(800, 300);

        // Education
        addLabel("Highest Education", 100, 350);
        String[] educations = {"M.Sc", "B.Sc", "Diploma", "Non-Science"};
        cbeducation = new JComboBox<>(educations);
        cbeducation.setBounds(250, 350, 200, 30);
        cbeducation.setBackground(Color.WHITE);
        add(cbeducation);

        // Salary (auto-set)
        addLabel("Salary", 600, 350);
        lsalary = new JLabel("Auto-filled");
        lsalary.setBounds(800, 350, 200, 30);
        lsalary.setFont(new Font("serif", Font.PLAIN, 18));
        add(lsalary);

        // Employee ID
        addLabel("Employee ID", 100, 400);
        lblempId = new JLabel(generateUniqueEmpId());
        lblempId.setBounds(250, 400, 200, 30);
        lblempId.setFont(new Font("serif", Font.BOLD, 18));
        add(lblempId);

        // Buttons
        add = new JButton("Add Details");
        back = new JButton("Back");
        add.setBounds(500, 500, 180, 40);
        back.setBounds(720, 500, 180, 40);
        styleButton(add);
        styleButton(back);
        add(add);
        add(back);
        add.addActionListener(this);
        back.addActionListener(this);

        // Designation change listener to update salary
        cbdesignation.addActionListener(e -> updateSalaryBasedOnDesignation());

        setVisible(true);
    }

    private void updateSalaryBasedOnDesignation() {
        String designation = (String) cbdesignation.getSelectedItem();
        switch (designation) {
            case "Lead Manager":
                lsalary.setText("80000");
                break;
            case "Senior Developer":
                lsalary.setText("60000");
                break;
            case "Junior Developer":
                lsalary.setText("40000");
                break;
            case "Intern":
                lsalary.setText("15000");
                break;
        }
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("serif", Font.PLAIN, 20));
        add(label);
        return label;
    }

    private JTextField addTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 200, 30);
        add(tf);
        return tf;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Tahoma", Font.BOLD, 16));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private String generateUniqueEmpId() {
        int startId = 1001;
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("SELECT empId FROM employee");
            Set<Integer> existingIds = new HashSet<>();

            while (rs.next()) {
                existingIds.add(rs.getInt("empId"));
            }

            while (existingIds.contains(startId)) {
                startId++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(startId);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            String name = tfname.getText().trim();
            String fname = tffname.getText().trim();
            String dob = ((JTextField) dcdob.getDateEditor().getUiComponent()).getText().trim();
            String nid = tfnid.getText().trim();
            String phone = tfphone.getText().trim();
            String email = tfemail.getText().trim();
            String education = (String) cbeducation.getSelectedItem();
            String empId = lblempId.getText();
            String address = tfaddress.getText().trim();
            String designation = (String) cbdesignation.getSelectedItem();
            String salary = lsalary.getText();

            if (name.isEmpty() || fname.isEmpty() || dob.isEmpty() || nid.isEmpty() || designation == null || designation.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all mandatory (*) fields.");
                return;
            }

            try {
                Conn conn = new Conn();

                ResultSet rs = conn.s.executeQuery("SELECT * FROM employee WHERE nid = '" + nid + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "NID already exists. Employee already registered.");
                    return;
                }

                String query = "INSERT INTO employee (name, fname, dob, nid, phone, email, education, empId, address, designation, salary) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.c.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, fname);
                pst.setString(3, dob);
                pst.setString(4, nid);
                pst.setString(5, phone);
                pst.setString(6, email);
                pst.setString(7, education);
                pst.setString(8, empId);
                pst.setString(9, address);
                pst.setString(10, designation);
                pst.setString(11, salary);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Employee added successfully.");
                setVisible(false);
                new Home();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new AddEmployee();
    }
}
