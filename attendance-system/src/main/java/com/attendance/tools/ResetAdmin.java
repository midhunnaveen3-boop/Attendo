package com.attendance.tools;

import com.attendance.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResetAdmin {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java com.attendance.tools.ResetAdmin <userId> <password>");
            return;
        }
        String user = args[0];
        String pass = args[1];
        try (Connection c = DatabaseConnection.getConnection()) {
            // ensure table exists
            DatabaseConnection.initDatabase();
            PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM users WHERE user_id = ?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            boolean exists = false;
            if (rs.next()) exists = rs.getInt(1) > 0;
            rs.close();
            ps.close();
            if (exists) {
                PreparedStatement upd = c.prepareStatement("UPDATE users SET password = ?, role = ? WHERE user_id = ?");
                upd.setString(1, pass);
                upd.setString(2, "admin");
                upd.setString(3, user);
                upd.executeUpdate();
                upd.close();
                System.out.println("Updated admin user: " + user);
            } else {
                PreparedStatement ins = c.prepareStatement("INSERT INTO users(user_id,password,role) VALUES(?,?,?)");
                ins.setString(1, user);
                ins.setString(2, pass);
                ins.setString(3, "admin");
                ins.executeUpdate();
                ins.close();
                System.out.println("Created admin user: " + user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
