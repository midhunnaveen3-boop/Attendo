package com.attendance;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:attendance.db";
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL);
        }
        return conn;
    }

    public static void initDatabase() throws SQLException {
        Connection c = getConnection();
        Statement s = c.createStatement();

        // users table
        s.execute("CREATE TABLE IF NOT EXISTS users (user_id TEXT PRIMARY KEY, password TEXT NOT NULL, role TEXT NOT NULL)");

        // students table
        s.execute("CREATE TABLE IF NOT EXISTS students (student_id TEXT PRIMARY KEY, name TEXT, class TEXT, roll_no INTEGER)");

        // teachers table
        s.execute("CREATE TABLE IF NOT EXISTS teachers (teacher_id TEXT PRIMARY KEY, name TEXT, subject TEXT)");

        // attendance table
        s.execute("CREATE TABLE IF NOT EXISTS attendance (id INTEGER PRIMARY KEY AUTOINCREMENT, student_id TEXT, date TEXT, status TEXT, subject TEXT, FOREIGN KEY(student_id) REFERENCES students(student_id))");

        // create default admin if not exists
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM users WHERE user_id = ?");
        ps.setString(1, "admin");
        ResultSet rs = ps.executeQuery();
        if (rs.next() && rs.getInt(1) == 0) {
            PreparedStatement ins = c.prepareStatement("INSERT INTO users(user_id,password,role) VALUES(?,?,?)");
            ins.setString(1, "admin");
            ins.setString(2, "admin"); // default password; advise changing
            ins.setString(3, "admin");
            ins.executeUpdate();
            ins.close();
        }

        rs.close();
        ps.close();
        s.close();
    }
}
