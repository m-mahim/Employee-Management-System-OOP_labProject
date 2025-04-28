package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Splash extends JFrame implements ActionListener {

    JButton btnAdmin, btnEmployee, btnExit;

    Splash() {
        // Set up the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true); // Optional: for a cleaner fullscreen UI
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(null);

        // Title Heading
        JLabel heading = new JLabel("EMPLOYEE MANAGEMENT SYSTEM", JLabel.CENTER);
        heading.setBounds(150, 50, 1000, 70);
        heading.setFont(new Font("Serif", Font.BOLD, 48));
        heading.setForeground(new Color(25, 25, 112));
        add(heading);

        // Button Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 20, 20));
        panel.setBounds(250, 350, 800, 200);
        panel.setBackground(new Color(240, 248, 255));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 50, 20));
        row1.setBackground(new Color(240, 248, 255));

        btnAdmin = createButton("Admin", new Color(60, 179, 113));
        btnEmployee = createButton("Employee", new Color(30, 144, 255));

        row1.add(btnAdmin);
        row1.add(btnEmployee);

        JPanel row2 = new JPanel();
        row2.setBackground(new Color(240, 248, 255));
        btnExit = createButton("Exit", new Color(220, 20, 60));
        row2.add(btnExit);

        panel.add(row1);
        panel.add(row2);

        add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        if (ae.getSource() == btnAdmin) {
            new AdminLogin().setVisible(true);
        } else if (ae.getSource() == btnEmployee) {
            new EmployeeLogin().setVisible(true);
        } else if (ae.getSource() == btnExit) {
            System.exit(0);
        }
    }

    // Function to create an interactive button
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 24));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 80));

        button.addActionListener(this);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Splash());
    }
}
