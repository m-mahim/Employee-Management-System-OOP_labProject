package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EmployeePanel extends JFrame implements ActionListener {
    JButton viewAndUpdateProfile, applyLeave, salaryDetails, logout, attendance, noticeBoard;
    String employeeId;

    public EmployeePanel(String employeeId) {
        this.employeeId = employeeId;
        setTitle("Employee User Panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 250, 255));

        // Heading
        JLabel heading = new JLabel("Welcome to Employee Dashboard", JLabel.CENTER);
        heading.setFont(new Font("Georgia", Font.BOLD, 38));
        heading.setForeground(new Color(44, 62, 80));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(heading, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 25, 25));
        buttonPanel.setBackground(new Color(245, 250, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));

        viewAndUpdateProfile = createStyledButton("View & Update Profile", new Color(52, 152, 219));
        applyLeave = createStyledButton("Apply for Leave", new Color(46, 204, 113));
        salaryDetails = createStyledButton("Salary Details", new Color(155, 89, 182));
        attendance = createStyledButton("Attendance", new Color(241, 196, 15));
        noticeBoard = createStyledButton("Notice Board", new Color(230, 126, 34));
        logout = createStyledButton("Logout", new Color(231, 76, 60)); // Red

        buttonPanel.add(viewAndUpdateProfile);
        buttonPanel.add(applyLeave);
        buttonPanel.add(salaryDetails);
        buttonPanel.add(attendance);
        buttonPanel.add(noticeBoard);
        buttonPanel.add(logout);

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 20));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect (preserves red color for logout)
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        button.addActionListener(this);
        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == viewAndUpdateProfile) {
            setVisible(false);
            new ViewProfile(employeeId, this);  // View & Update handled inside
        } else if (ae.getSource() == applyLeave) {
            setVisible(false);
            new ApplyLeave(employeeId, this);
        } else if (ae.getSource() == salaryDetails) {
            setVisible(false);
            new SalaryDetails(employeeId, this);
        } else if (ae.getSource() == attendance) {
            setVisible(false);
            new Attendance(employeeId, this);
        } else if (ae.getSource() == noticeBoard) {
            setVisible(false);
            new Notice(employeeId, this);
        } else if (ae.getSource() == logout) {
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            dispose();
            new EmployeeLogin();
        }
    }

    public static void main(String[] args) {
        // Use this only when launching for testing. Remove or replace with login session.
        // new EmployeePanel("EMP123");
    }
}
