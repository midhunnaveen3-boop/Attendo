package com.attendance.model;

public class Student {
    private String studentId;
    private String name;
    private String className;
    private int rollNo;

    public Student(String studentId, String name, String className, int rollNo) {
        this.studentId = studentId;
        this.name = name;
        this.className = className;
        this.rollNo = rollNo;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getClassName() { return className; }
    public int getRollNo() { return rollNo; }
}
