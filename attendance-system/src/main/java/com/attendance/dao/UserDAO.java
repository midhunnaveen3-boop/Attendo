package com.attendance.dao;

import com.attendance.DatabaseConnection;

import java.sql.*;

public class UserDAO {
    public static boolean authenticate(String userId, String password, String role) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT password FROM users WHERE user_id = ? AND role = ?");
        ps.setString(1, userId);
        ps.setString(2, role);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String pw = rs.getString(1);
            rs.close();
            ps.close();
            return pw.equals(password);
        }
        rs.close();
        ps.close();
        return false;
    }

    public static void createUser(String userId, String password, String role) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO users(user_id,password,role) VALUES(?,?,?)");
        ps.setString(1, userId);
        ps.setString(2, password);
        ps.setString(3, role);
        ps.executeUpdate();
        ps.close();
    }

    public static void updatePassword(String userId, String newPassword) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("UPDATE users SET password = ? WHERE user_id = ?");
        ps.setString(1, newPassword);
        ps.setString(2, userId);
        ps.executeUpdate();
        ps.close();
    }

    public static void deleteUser(String userId) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE user_id = ?");
        ps.setString(1, userId);
        ps.executeUpdate();
        ps.close();
    }
}
