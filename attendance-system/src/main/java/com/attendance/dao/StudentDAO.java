package com.attendance.dao;

import com.attendance.DatabaseConnection;
import com.attendance.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public static void addStudent(Student s) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO students(student_id,name,class,roll_no) VALUES(?,?,?,?)");
        ps.setString(1, s.getStudentId());
        ps.setString(2, s.getName());
        ps.setString(3, s.getClassName());
        ps.setInt(4, s.getRollNo());
        ps.executeUpdate();
        ps.close();
    }

    public static List<Student> listStudents() throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery("SELECT student_id,name,class,roll_no FROM students ORDER BY class, roll_no");
        List<Student> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Student(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
        }
        rs.close();
        s.close();
        return list;
    }

    public static void deleteStudent(String studentId) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM students WHERE student_id = ?");
        ps.setString(1, studentId);
        ps.executeUpdate();
        ps.close();
    }
}
