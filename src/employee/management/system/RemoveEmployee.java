package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RemoveEmployee extends JFrame implements ActionListener {

    Choice cEmpId;
    JLabel lblname, lblphone, lblemail;
    JButton delete, back;

    RemoveEmployee() {
        setTitle("Remove Employee");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Remove Employee");
        title.setFont(new Font("Tahoma", Font.BOLD, 26));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel labelId = new JLabel("Select Employee ID:");
        labelId.setFont(new Font("Tahoma", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(labelId, gbc);

        cEmpId = new Choice();
        cEmpId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1;
        formPanel.add(cEmpId, gbc);

        // Load Employee IDs
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT empId FROM employee");
            while (rs.next()) {
                cEmpId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Labels
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        lblname = new JLabel();
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblname, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        lblphone = new JLabel();
        lblphone.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblphone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        lblemail = new JLabel();
        lblemail.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblemail, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));

        delete = createStyledButton("Delete");
        back = createStyledButton("Back");

        buttonPanel.add(delete);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(back);

        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Event Listeners
        loadEmployeeInfo(cEmpId.getSelectedItem());
        cEmpId.addItemListener(e -> loadEmployeeInfo(cEmpId.getSelectedItem()));

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 35));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0, 76, 153));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0, 102, 204));
            }
        });

        btn.addActionListener(this);
        return btn;
    }

    private void loadEmployeeInfo(String empId) {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                lblname.setText(rs.getString("name"));
                lblphone.setText(rs.getString("phone"));
                lblemail.setText(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == delete) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Conn c = new Conn();
                    String query = "DELETE FROM employee WHERE empId = '" + cEmpId.getSelectedItem() + "'";
                    c.s.executeUpdate(query);
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                    setVisible(false);
                    new Home();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new RemoveEmployee();
    }
}
