# Attendance Management System (Java Swing + SQLite)

This is a sample Attendance Management System built with Java Swing and SQLite (JDBC).

# Attendo — Attendance Management System (Java Swing + SQLite)

Attendo is a lightweight Attendance Management System implemented in Java using Swing for the UI and SQLite (JDBC) for storage. It demonstrates a small MVC-style desktop application with separate model, DAO, service and UI layers.

This README explains how to build and run the project on Windows (PowerShell), what features are included, and recommended next steps.

Contents
- Features
- Requirements
- Quick start (PowerShell)
- Project structure
- Default credentials and data
- Development notes, security, and next steps

Features
- Multi-role support: Admin, Teacher, Student
- Admin: add/delete students and teachers, manage accounts, view simple reports
- Teacher: load class list, mark daily attendance for a selected date and subject
- Student: view personal attendance records and percentage
- Persistence: SQLite (file: `attendance.db` created in project root)
- Utilities: `ResetAdmin` tool to create/update the default admin user

Requirements
- Java JDK 8 or newer (javac + java required). Project targets Java 1.8 for compatibility.
- PowerShell (Windows) or any shell with Java available
- No system-wide Maven installation is required — the included `run.ps1` script compiles and runs using `javac` and the libraries in `lib/`.

Quick start (Windows / PowerShell)

1) Open PowerShell and change to the project folder:

```powershell
cd "C:\Users\MIDHUN\Java project\attendance-system"
```

2) Compile & run using the included runner (will download jars to `lib/` when needed):

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1
```

3) Login (default admin):

- user: `admin`
- password: `admin123`

Notes
- The runner (`run.ps1`) compiles sources with `javac` and launches the application; it also downloads `sqlite-jdbc` and `commons-csv` into `lib/` if they are missing. If `javac` is not available you must install a JDK and ensure `javac` is on PATH.
- The project includes a `ResetAdmin` tool at `com.attendance.tools.ResetAdmin` that can be used to set/reset the admin credentials from the command line if needed.

Project structure (important files)
- `pom.xml` — Maven descriptor (kept for convenience; the runner does not require Maven to be installed)
- `src/main/java/com/attendance/` — Java sources
- `src/main/java/com/attendance/ui/` — Swing UI frames (`LoginFrame`, `AdminDashboard`, `TeacherDashboard`, `StudentDashboard`)
- `src/main/java/com/attendance/dao/` — DAO classes (SQLite persistence)
- `src/main/java/com/attendance/service/` — service layer that contains business logic
- `src/main/java/com/attendance/tools/ResetAdmin.java` — CLI utility to set default admin
- `attendance.db` — SQLite DB file created at runtime in project root
- `lib/` — third-party jars (sqlite-jdbc, commons-csv)

Security & limitations
- Passwords are currently stored in plaintext in the `users` table. This is NOT suitable for production. Recommended next steps:
	- Add bcrypt hashing for passwords (use a library like BCrypt or Spring Security crypto)
	- Provide a migration tool to re-hash existing passwords
- DAOs are implemented with static methods. For a fully object-oriented design you can:
	- Introduce DAO interfaces (IUserDao, IStudentDao, etc.)
	- Implement concrete `SqlUserDao`/`SqlAttendanceDao` classes and inject them into services via a simple `AppContext`

Development & testing
- To run only specific classes or tools you can compile and run with `javac`/`java` directly — the `run.ps1` automates this.
- To reset the admin user from code (example):

```powershell
# compile the ResetAdmin tool and run it (the runner already does the compile)
powershell -NoProfile -ExecutionPolicy Bypass -File .\run.ps1 com.attendance.tools.ResetAdmin admin admin123
```

Suggested next improvements (prioritized)
1) DAO interface refactor + dependency injection (complete OO refactor)
2) Password hashing (BCrypt) + migration
3) Unit tests for DAOs and services
4) CSV/PDF export, charts (JFreeChart), and better reporting filters

License & attribution
- This project is a demonstration/learning project. If you reuse code in production, please audit and harden it.

Contact / Issues
- If anything in the runner fails on your machine (javac missing, PATH issues), copy the terminal output and I can help debug.

Enjoy — open an issue describing any feature you want next and I can implement it.
