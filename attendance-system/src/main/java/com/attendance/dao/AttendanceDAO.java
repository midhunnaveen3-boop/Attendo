package com.attendance.dao;

import com.attendance.DatabaseConnection;
import com.attendance.model.AttendanceRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    public static void markAttendance(AttendanceRecord r) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        // prevent duplicate for same student+date+subject
        PreparedStatement del = c.prepareStatement("DELETE FROM attendance WHERE student_id = ? AND date = ? AND subject = ?");
        del.setString(1, r.getStudentId());
        del.setString(2, r.getDate());
        del.setString(3, r.getSubject());
        del.executeUpdate();
        del.close();

        PreparedStatement ps = c.prepareStatement("INSERT INTO attendance(student_id,date,status,subject) VALUES(?,?,?,?)");
        ps.setString(1, r.getStudentId());
        ps.setString(2, r.getDate());
        ps.setString(3, r.getStatus());
        ps.setString(4, r.getSubject());
        ps.executeUpdate();
        ps.close();
    }

    public static List<AttendanceRecord> getAttendanceForStudent(String studentId) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT id,student_id,date,status,subject FROM attendance WHERE student_id = ? ORDER BY date");
        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();
        List<AttendanceRecord> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new AttendanceRecord(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        rs.close();
        ps.close();
        return list;
    }

    public static int countTotalClasses(String studentId) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM attendance WHERE student_id = ?");
        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();
        int v = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        ps.close();
        return v;
    }

    public static int countPresent(String studentId) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM attendance WHERE student_id = ? AND status = 'present'");
        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();
        int v = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        ps.close();
        return v;
    }

    public static List<AttendanceRecord> getAttendanceByDate(String date) throws SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT id,student_id,date,status,subject FROM attendance WHERE date = ? ORDER BY student_id");
        ps.setString(1, date);
        ResultSet rs = ps.executeQuery();
        List<AttendanceRecord> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new AttendanceRecord(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        rs.close();
        ps.close();
        return list;
    }
}
