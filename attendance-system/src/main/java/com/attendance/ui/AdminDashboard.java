package com.attendance.ui;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.dao.TeacherDAO;
import com.attendance.dao.UserDAO;
import com.attendance.model.Student;
import com.attendance.model.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboard extends JFrame {
    private JTable studentTable;
    private JTable teacherTable;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Students", studentsPanel());
        tabs.addTab("Teachers", teachersPanel());
        tabs.addTab("Reports", reportsPanel());

    add(tabs);
    // add logout button at bottom
    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton logout = new JButton("Logout");
    logout.addActionListener(ev -> { dispose(); new LoginFrame().setVisible(true); });
    bottom.add(logout);
    add(bottom, BorderLayout.SOUTH);
    }

    private JPanel studentsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JButton add = new JButton("Add Student");
        add.addActionListener(this::onAddStudent);
        top.add(add);
        JButton del = new JButton("Delete Selected");
        del.addActionListener(this::onDeleteStudent);
        top.add(del);
        p.add(top, BorderLayout.NORTH);

        studentTable = new JTable();
        refreshStudents();
        p.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        return p;
    }

    private void onAddStudent(ActionEvent e) {
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField cls = new JTextField();
        JTextField roll = new JTextField();
        JTextField userpw = new JTextField();

        Object[] fields = {"Student ID:", id, "Name:", name, "Class:", cls, "Roll No:", roll, "Initial password:", userpw};
        int res = JOptionPane.showConfirmDialog(this, fields, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            Student s = new Student(id.getText().trim(), name.getText().trim(), cls.getText().trim(), Integer.parseInt(roll.getText().trim()));
            StudentDAO.addStudent(s);
            UserDAO.createUser(s.getStudentId(), userpw.getText().trim(), "student");
            refreshStudents();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage());
        }
    }

    private void onDeleteStudent(ActionEvent e) {
        int r = studentTable.getSelectedRow();
        if (r < 0) return;
        String studentId = (String) studentTable.getValueAt(r,0);
        try {
            StudentDAO.deleteStudent(studentId);
            UserDAO.deleteUser(studentId);
            refreshStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting: " + ex.getMessage());
        }
    }

    private void refreshStudents() {
        try {
            List<Student> list = StudentDAO.listStudents();
            String[] cols = {"ID","Name","Class","RollNo"};
            Object[][] data = new Object[list.size()][4];
            for (int i=0;i<list.size();i++) {
                Student s = list.get(i);
                data[i][0] = s.getStudentId();
                data[i][1] = s.getName();
                data[i][2] = s.getClassName();
                data[i][3] = s.getRollNo();
            }
            studentTable.setModel(new javax.swing.table.DefaultTableModel(data,cols));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private JPanel teachersPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JButton add = new JButton("Add Teacher");
        add.addActionListener(this::onAddTeacher);
        top.add(add);
        JButton del = new JButton("Delete Selected");
        del.addActionListener(this::onDeleteTeacher);
        top.add(del);
        p.add(top, BorderLayout.NORTH);
        teacherTable = new JTable();
        refreshTeachers();
        p.add(new JScrollPane(teacherTable), BorderLayout.CENTER);
        return p;
    }

    private void refreshTeachers() {
        try {
            java.util.List<Teacher> list = TeacherDAO.listTeachers();
            String[] cols = {"ID","Name","Subject"};
            Object[][] data = new Object[list.size()][3];
            for (int i=0;i<list.size();i++) {
                Teacher s = list.get(i);
                data[i][0] = s.getTeacherId();
                data[i][1] = s.getName();
                data[i][2] = s.getSubject();
            }
            teacherTable.setModel(new javax.swing.table.DefaultTableModel(data,cols));
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void onAddTeacher(ActionEvent e) {
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField subject = new JTextField();
        JTextField userpw = new JTextField();

        Object[] fields = {"Teacher ID:", id, "Name:", name, "Subject:", subject, "Initial password:", userpw};
        int res = JOptionPane.showConfirmDialog(this, fields, "Add Teacher", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            Teacher t = new Teacher(id.getText().trim(), name.getText().trim(), subject.getText().trim());
            TeacherDAO.addTeacher(t);
            UserDAO.createUser(t.getTeacherId(), userpw.getText().trim(), "teacher");
            JOptionPane.showMessageDialog(this, "Teacher added");
            // refresh teachers tab by recreating tabs (simple)
            this.dispose();
            new AdminDashboard().setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding teacher: " + ex.getMessage());
        }
    }

    private void onDeleteTeacher(ActionEvent e) {
        int r = -1;
        if (teacherTable != null) r = teacherTable.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Please select a teacher to delete.");
            return;
        }
        String teacherId = (String) teacherTable.getValueAt(r, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete teacher " + teacherId + "? This will also remove the user's login.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            TeacherDAO.deleteTeacher(teacherId);
            // also remove user credentials
            com.attendance.dao.UserDAO.deleteUser(teacherId);
            refreshTeachers();
            JOptionPane.showMessageDialog(this, "Teacher deleted: " + teacherId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting teacher: " + ex.getMessage());
        }
    }

    private JPanel reportsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        JButton refresh = new JButton("Refresh Summary");
        refresh.addActionListener(ev -> {
            try {
                java.util.List<Student> students = StudentDAO.listStudents();
                StringBuilder sb = new StringBuilder();
                for (Student s : students) {
                    int total = AttendanceDAO.countTotalClasses(s.getStudentId());
                    int pres = AttendanceDAO.countPresent(s.getStudentId());
                    double perc = total == 0 ? 0 : (pres * 100.0 / total);
                    sb.append(String.format("%s - %s: %d/%d (%.2f%%)\n", s.getStudentId(), s.getName(), pres, total, perc));
                }
                area.setText(sb.toString());
            } catch (SQLException ex) { ex.printStackTrace(); }
        });
        p.add(refresh, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }
}
