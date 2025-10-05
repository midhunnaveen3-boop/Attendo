package com.attendance.ui;

import com.attendance.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    // role chooser removed, role will be derived from DB

    public LoginFrame() {
        setTitle("Attendance System - Login");
        setSize(400,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(4,2,5,5));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(new JLabel("User ID:"));
        userField = new JTextField();
        p.add(userField);

        p.add(new JLabel("Password:"));
        passField = new JPasswordField();
        p.add(passField);

    // role selection removed; role is determined by credentials
    p.add(new JLabel("(Role is derived from your account)"));
    p.add(new JLabel(""));

        JButton login = new JButton("Login");
    login.addActionListener(this::onLogin);
        p.add(login);

        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> System.exit(0));
        p.add(exit);

        add(p);
    }

    private void onLogin(ActionEvent e) {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        try {
            UserService us = new UserService();
            String role = us.authenticate(user, pass);
            if (role == null) {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
                return;
            }
            dispose();
            if ("admin".equals(role)) new AdminDashboard().setVisible(true);
            else if ("teacher".equals(role)) new TeacherDashboard(user).setVisible(true);
            else new StudentDashboard(user).setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage());
        }
    }
}
