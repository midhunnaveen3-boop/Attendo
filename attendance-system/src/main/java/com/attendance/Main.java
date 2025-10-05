package com.attendance;

import com.attendance.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.initDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to initialize database: " + e.getMessage());
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
