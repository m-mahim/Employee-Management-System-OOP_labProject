package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class AdminNoticeBoard extends JFrame {
    private JTextArea noticeInputArea;
    private JTable noticeTable;
    private JButton addBtn, updateBtn, deleteBtn, backBtn;
    private DefaultListModel<String> noticeListModel;
    private int selectedId = -1;

    public AdminNoticeBoard() {
        setTitle("Admin Notice Board");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Admin Notice Board", JLabel.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 32));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(heading, BorderLayout.NORTH);

        // Top Panel for TextArea and Buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        noticeInputArea = new JTextArea(5, 50);
        noticeInputArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        noticeInputArea.setBorder(BorderFactory.createTitledBorder("Write or Edit Notice"));
        JScrollPane scroll = new JScrollPane(noticeInputArea);
        topPanel.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        addBtn = styledButton("Add Notice", new Color(52, 152, 219));
        updateBtn = styledButton("Update Notice", new Color(241, 196, 15));
        deleteBtn = styledButton("Delete Notice", new Color(231, 76, 60));
        backBtn = styledButton("Back", new Color(192, 57, 43));
        forceRedBackButton(backBtn);

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.CENTER);

        // Table to show notices
        noticeTable = new JTable();
        noticeTable.setRowHeight(30);
        noticeTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        noticeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        noticeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(noticeTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("All Notices"));
        add(tableScroll, BorderLayout.SOUTH);

        // Load and show notices
        loadNotices();

        // Event Listeners
        addBtn.addActionListener(e -> addNotice());
        updateBtn.addActionListener(e -> updateNotice());
        deleteBtn.addActionListener(e -> deleteNotice());
        backBtn.addActionListener(e -> {
            setVisible(false);
            new Home(); // Go back to Home
        });

        noticeTable.getSelectionModel().addListSelectionListener(e -> {
            int row = noticeTable.getSelectedRow();
            if (row != -1) {
                selectedId = (int) noticeTable.getValueAt(row, 0);
                String selectedNotice = (String) noticeTable.getValueAt(row, 1);
                noticeInputArea.setText(selectedNotice);
            }
        });

        setVisible(true);
    }

    private JButton styledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void forceRedBackButton(JButton button) {
        button.setBackground(new Color(192, 57, 43));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(192, 57, 43)); // Stay red
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(192, 57, 43)); // Stay red
            }
        });
    }

    private void addNotice() {
        String noticeText = noticeInputArea.getText().trim();
        if (noticeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Notice cannot be empty!");
            return;
        }

        try {
            Conn c = new Conn();
            PreparedStatement ps = c.c.prepareStatement("INSERT INTO notices (notice) VALUES (?)");
            ps.setString(1, noticeText);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Notice added successfully!");
            noticeInputArea.setText("");
            loadNotices();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateNotice() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notice to update!");
            return;
        }

        String updatedText = noticeInputArea.getText().trim();
        if (updatedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Updated notice cannot be empty!");
            return;
        }

        try {
            Conn c = new Conn();
            PreparedStatement ps = c.c.prepareStatement("UPDATE notices SET notice=? WHERE id=?");
            ps.setString(1, updatedText);
            ps.setInt(2, selectedId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Notice updated successfully!");
            noticeInputArea.setText("");
            selectedId = -1;
            loadNotices();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteNotice() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notice to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this notice?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Conn c = new Conn();
                PreparedStatement ps = c.c.prepareStatement("DELETE FROM notices WHERE id=?");
                ps.setInt(1, selectedId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Notice deleted successfully!");
                noticeInputArea.setText("");
                selectedId = -1;
                loadNotices();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void loadNotices() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM notices ORDER BY id DESC");
            noticeTable.setModel(buildTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Notice");

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getInt("id"));
            row.add(rs.getString("notice"));
            data.add(row);
        }

        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        new AdminNoticeBoard();
    }
}
