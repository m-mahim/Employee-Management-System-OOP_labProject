package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener {

    JButton view, add, update, remove, logout, attendance, leaves, salary, notice;

    public Home() {
        setTitle("Admin Panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); // Alice Blue background

        JLabel heading = new JLabel("Admin Panel", JLabel.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 40));
        heading.setForeground(new Color(0, 51, 102));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(heading, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        // Buttons
        add = createStyledButton("Add Employee", new Color(46, 204, 113)); // Green
        view = createStyledButton("View Employees", new Color(52, 152, 219)); // Blue
        update = createStyledButton("Update Employee", new Color(230, 126, 34)); // Orange
        remove = createStyledButton("Remove Employee", new Color(231, 76, 60)); // Red
        attendance = createStyledButton("Check Attendance", new Color(155, 89, 182)); // Purple
        leaves = createStyledButton("Manage Leaves", new Color(241, 196, 15)); // Yellow
        salary = createStyledButton("Manage Salaries", new Color(26, 188, 156)); // Teal
        notice = createStyledButton("Notice Board", new Color(41, 128, 185)); // Dark Blue
        logout = createRedLogoutButton("Logout"); // Styled red button with hover

        // Add to panel
        buttonPanel.add(add);
        buttonPanel.add(view);
        buttonPanel.add(update);
        buttonPanel.add(remove);
        buttonPanel.add(attendance);
        buttonPanel.add(leaves);
        buttonPanel.add(salary);
        buttonPanel.add(notice);
        buttonPanel.add(logout);

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 20));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        button.addActionListener(this);
        return button;
    }

    private JButton createRedLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 20));
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(200, 0, 0));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.RED);
            }
        });

        button.addActionListener(this);
        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source == add) {
            setVisible(false);
            new AddEmployee();
        } else if (source == view) {
            setVisible(false);
            new ViewEmployee();
        } else if (source == update) {
            setVisible(false);
            new ViewEmployee(); // For update too
        } else if (source == remove) {
            setVisible(false);
            new RemoveEmployee();
        } else if (source == attendance) {
            setVisible(false);
            new AttendanceChecker(this);
        } else if (source == leaves) {
            setVisible(false);
            new ManageLeaves(this);
        } else if (source == salary) {
            setVisible(false);
            new ManageSalaryAdmin(this); // Final integration
        } else if (source == notice) {
            setVisible(false);
            new AdminNoticeBoard();
        } else if (source == logout) {
            setVisible(false);
            new AdminLogin();
        }
    }

    public static void main(String[] args) {
        new Home();
    }
}
