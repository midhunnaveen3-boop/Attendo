package com.attendance.dao;

import com.attendance.DatabaseConnection;
import com.attendance.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    public static void addTeacher(Teacher t) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO teachers(teacher_id,name,subject) VALUES(?,?,?)");
        ps.setString(1, t.getTeacherId());
        ps.setString(2, t.getName());
        ps.setString(3, t.getSubject());
        ps.executeUpdate();
        ps.close();
    }

    public static List<Teacher> listTeachers() throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT teacher_id,name,subject FROM teachers ORDER BY name");
        List<Teacher> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Teacher(rs.getString(1), rs.getString(2), rs.getString(3)));
        }
        rs.close();
        s.close();
        return list;
    }

    public static void deleteTeacher(String teacherId) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM teachers WHERE teacher_id = ?");
        ps.setString(1, teacherId);
        ps.executeUpdate();
        ps.close();
    }
}
