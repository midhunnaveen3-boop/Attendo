package com.attendance.ui;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.model.AttendanceRecord;
import com.attendance.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TeacherDashboard extends JFrame {
    private String teacherId;
    private JTable table;
    private List<Student> students;

    public TeacherDashboard(String teacherId) {
        this.teacherId = teacherId;
        setTitle("Teacher Dashboard - " + teacherId);
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel();
        top.add(new JLabel("Class (filter):"));
        JTextField classFilter = new JTextField(10);
        top.add(classFilter);
        JButton load = new JButton("Load Students");
        load.addActionListener(ev -> loadStudents(classFilter.getText().trim()));
        top.add(load);

        // Date field so teacher can select date to mark attendance
        top.add(new JLabel("Date (yyyy-MM-dd):"));
        JTextField dateField = new JTextField(10);
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        top.add(dateField);

        top.add(new JLabel("Subject:"));
        JTextField subjectField = new JTextField(10);
        top.add(subjectField);

        JButton mark = new JButton("Mark Attendance");
        mark.addActionListener(ev -> onMarkAttendanceWithFields(dateField.getText().trim(), subjectField.getText().trim()));
        top.add(mark);

        JButton logout = new JButton("Logout");
        logout.addActionListener(ev -> { dispose(); new LoginFrame().setVisible(true); });
        top.add(logout);

        add(top, BorderLayout.NORTH);

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh Percentages");
        refresh.addActionListener(ev -> showPercentages());
        add(refresh, BorderLayout.SOUTH);
    }

    private void loadStudents(String classFilter) {
        try {
            students = StudentDAO.listStudents();
            java.util.List<Student> filtered = new java.util.ArrayList<>();
            for (Student s : students) if (classFilter.isEmpty() || s.getClassName().equalsIgnoreCase(classFilter)) filtered.add(s);
            String[] cols = {"StudentId","Name","Class","Roll","Present"};
            Object[][] data = new Object[filtered.size()][5];
            for (int i=0;i<filtered.size();i++) {
                Student s = filtered.get(i);
                data[i][0] = s.getStudentId();
                data[i][1] = s.getName();
                data[i][2] = s.getClassName();
                data[i][3] = s.getRollNo();
                data[i][4] = Boolean.TRUE; // default present
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data,cols) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 4) return Boolean.class;
                    return super.getColumnClass(columnIndex);
                }
            });
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void onMarkAttendance(ActionEvent ev) {
        // kept for compatibility but not used
        onMarkAttendanceWithFields(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "");
    }

    private void onMarkAttendanceWithFields(String date, String subject) {
        try {
            if (date == null || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a date in yyyy-MM-dd format.");
                return;
            }
            if (subject == null || subject.isEmpty()) {
                subject = JOptionPane.showInputDialog(this, "Subject for this attendance:");
                if (subject == null) return;
            }
            for (int i=0;i<table.getRowCount();i++) {
                String sid = (String) table.getValueAt(i,0);
                Boolean present = (Boolean) table.getValueAt(i,4);
                AttendanceRecord r = new AttendanceRecord(sid, date, present ? "present" : "absent", subject);
                AttendanceDAO.markAttendance(r);
            }
            JOptionPane.showMessageDialog(this, "Attendance saved for " + date);
        } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void showPercentages() {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<table.getRowCount();i++) {
                String sid = (String) table.getValueAt(i,0);
                int total = AttendanceDAO.countTotalClasses(sid);
                int pres = AttendanceDAO.countPresent(sid);
                double perc = total==0?0:(pres*100.0/total);
                sb.append(String.format("%s: %.2f%%\n", sid, perc));
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Percentages", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
