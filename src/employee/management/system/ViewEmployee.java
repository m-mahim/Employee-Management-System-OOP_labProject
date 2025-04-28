package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

public class ViewEmployee extends JFrame implements ActionListener {

    JTable table;
    Choice cemployeeId;
    JButton search, print, update, back;

    ViewEmployee() {
        setTitle("View Employee");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        // Top Panel (Search bar and buttons)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        JLabel heading = new JLabel("Search by Employee ID:");
        heading.setFont(new Font("Tahoma", Font.BOLD, 18));
        heading.setForeground(new Color(0, 102, 204));
        topPanel.add(heading);

        cemployeeId = new Choice();
        cemployeeId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT empId FROM employee");
            while (rs.next()) {
                cemployeeId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        topPanel.add(cemployeeId);

        search = createButton("Search");
        print = createButton("Print");
        update = createButton("Update");
        back = createButton("Back");

        topPanel.add(search);
        topPanel.add(print);
        topPanel.add(update);
        topPanel.add(back);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel (Center)
        table = new JTable();
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Load all data initially
        loadTable();

        setVisible(true);
    }

    private void loadTable() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM employee");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 235, 255));
        table.getTableHeader().setForeground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(Color.LIGHT_GRAY);
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String selectedEmpId = cemployeeId.getSelectedItem();

        if (source == search) {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM employee WHERE empId = '" + selectedEmpId + "'");
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (source == update) {
            setVisible(false);
            new UpdateEmployee(selectedEmpId);
        } else if (source == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new ViewEmployee();
    }
}