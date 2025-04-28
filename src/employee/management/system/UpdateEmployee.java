package employee.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateEmployee extends JFrame implements ActionListener {

    JTextField tfeducation, tffname, tfaddress, tfphone, tfnid, tfemail, tfsalary, tfdesignation;
    JLabel lblempId;
    JButton updateBtn, backBtn;
    String empId;

    UpdateEmployee(String empId) {
        this.empId = empId;

        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen

        JLabel heading = new JLabel("Update Employee Detail");
        heading.setBounds(550, 30, 500, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 32));
        heading.setForeground(new Color(30, 30, 100));
        add(heading);

        addLabel("Name", 100, 120);
        JLabel lblname = addDataLabel(250, 120);

        addLabel("Father's Name", 700, 120);
        tffname = addTextField(900, 120, "Enter father's name");

        addLabel("Date of Birth", 100, 180);
        JLabel lbldob = addDataLabel(250, 180);

        addLabel("Salary", 700, 180);
        tfsalary = addTextField(900, 180, "Enter salary");

        addLabel("Address", 100, 240);
        tfaddress = addTextField(250, 240, "Enter address");

        addLabel("Phone", 700, 240);
        tfphone = addTextField(900, 240, "Enter phone number");

        addLabel("Email", 100, 300);
        tfemail = addTextField(250, 300, "Enter email address");

        addLabel("Highest Education", 700, 300);
        tfeducation = addTextField(900, 300, "Enter highest education");

        addLabel("Designation", 100, 360);
        tfdesignation = addTextField(250, 360, "Enter designation");

        addLabel("NID Number", 700, 360);
        JLabel lblnid = addDataLabel(900, 360);

        addLabel("Employee ID", 100, 420);
        lblempId = addDataLabel(250, 420);

        // Buttons
        updateBtn = new JButton("Update Details");
        styleButton(updateBtn, 500, 600);
        updateBtn.addActionListener(this);
        add(updateBtn);

        backBtn = new JButton("Back");
        styleButton(backBtn, 750, 600);
        backBtn.addActionListener(this);
        add(backBtn);

        // Fetch data from database
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            while (rs.next()) {
                lblname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                lbldob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfsalary.setText(rs.getString("salary"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                lblnid.setText(rs.getString("aadhar")); // renamed visually to NID
                lblempId.setText(rs.getString("empId"));
                tfdesignation.setText(rs.getString("designation"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            String fname = tffname.getText();
            String salary = tfsalary.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET fname = '" + fname + "', salary = '" + salary + "', address = '" + address + "', phone = '" + phone + "', email = '" + email + "', education = '" + education + "', designation = '" + designation + "' WHERE empId = '" + empId + "'";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Details updated successfully");
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

    // Helper method to add labels
    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 180, 30);
        label.setFont(new Font("Serif", Font.PLAIN, 20));
        add(label);
    }

    // Helper method to add text fields
    private JTextField addTextField(int x, int y, String tooltip) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 200, 30);
        tf.setFont(new Font("Serif", Font.PLAIN, 18));
        tf.setToolTipText(tooltip);
        add(tf);
        return tf;
    }

    // Helper method to add data display labels
    private JLabel addDataLabel(int x, int y) {
        JLabel label = new JLabel();
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Serif", Font.PLAIN, 18));
        label.setForeground(Color.DARK_GRAY);
        add(label);
        return label;
    }

    // Helper method to style buttons
    private void styleButton(JButton btn, int x, int y) {
        btn.setBounds(x, y, 180, 40);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        new UpdateEmployee("");
    }
}
