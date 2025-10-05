package com.attendance.service;

import com.attendance.dao.AttendanceDAO;
import com.attendance.model.AttendanceRecord;

import java.sql.SQLException;
import java.util.List;

public class AttendanceService {
    public void markAttendance(List<AttendanceRecord> records) throws SQLException {
        for (AttendanceRecord r : records) AttendanceDAO.markAttendance(r);
    }

    public List<AttendanceRecord> getAttendanceForStudent(String studentId) throws SQLException {
        return AttendanceDAO.getAttendanceForStudent(studentId);
    }

    public int countTotal(String studentId) throws SQLException { return AttendanceDAO.countTotalClasses(studentId); }
    public int countPresent(String studentId) throws SQLException { return AttendanceDAO.countPresent(studentId); }
}
