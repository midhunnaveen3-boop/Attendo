package com.attendance.service;

import com.attendance.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {
    /**
     * Authenticate with userid and password. Return role (admin/teacher/student) if valid, otherwise null.
     */
    public String authenticate(String userId, String password) throws Exception {
        try (Connection c = DatabaseConnection.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT role,password FROM users WHERE user_id = ?");
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pw = rs.getString(2);
                String role = rs.getString(1);
                rs.close();
                ps.close();
                if (pw.equals(password)) return role;
            } else {
                rs.close();
                ps.close();
            }
        }
        return null;
    }
}
