package com.attendance.ui;

import com.attendance.dao.AttendanceDAO;
import com.attendance.model.AttendanceRecord;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentDashboard extends JFrame {
    private String studentId;

    public StudentDashboard(String studentId) {
        this.studentId = studentId;
        setTitle("Student Dashboard - " + studentId);
        setSize(700,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel();
        JButton load = new JButton("Load My Attendance");
        load.addActionListener(ev -> loadAttendance());
        top.add(load);
        JButton perc = new JButton("Show Percentage");
        perc.addActionListener(ev -> showPercentage());
        top.add(perc);
        JButton logout = new JButton("Logout");
        logout.addActionListener(ev -> { dispose(); new com.attendance.ui.LoginFrame().setVisible(true); });
        top.add(logout);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(new JTable()), BorderLayout.CENTER);
    }

    private void loadAttendance() {
        try {
            com.attendance.service.AttendanceService svc = new com.attendance.service.AttendanceService();
            List<AttendanceRecord> list = svc.getAttendanceForStudent(studentId);
            String[] cols = {"Date","Status","Subject"};
            Object[][] data = new Object[list.size()][3];
            for (int i=0;i<list.size();i++) {
                AttendanceRecord r = list.get(i);
                data[i][0] = r.getDate();
                data[i][1] = r.getStatus();
                data[i][2] = r.getSubject();
            }
            JTable table = new JTable(data, cols);
            getContentPane().remove(1);
            add(new JScrollPane(table), BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void showPercentage() {
        try {
            com.attendance.service.AttendanceService svc = new com.attendance.service.AttendanceService();
            int total = svc.countTotal(studentId);
            int pres = svc.countPresent(studentId);
            double perc = total==0?0:(pres*100.0/total);
            JOptionPane.showMessageDialog(this, String.format("Attendance: %d/%d (%.2f%%)", pres, total, perc));
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
