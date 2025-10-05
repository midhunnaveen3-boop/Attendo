package com.attendance.model;

public class AttendanceRecord {
    private int id;
    private String studentId;
    private String date; // ISO yyyy-MM-dd
    private String status; // present/absent
    private String subject;

    public AttendanceRecord(int id, String studentId, String date, String status, String subject) {
        this.id = id;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
        this.subject = subject;
    }

    public AttendanceRecord(String studentId, String date, String status, String subject) {
        this(-1, studentId, date, status, subject);
    }

    public int getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getSubject() { return subject; }
}
