package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Database.DBConnection;
import Models.Student;

public class StudentDAO {

    // INSERT STUDENT
    public boolean insertStudent(Student student) {

        String query =
                "INSERT INTO student(id, name, gender, branch) VALUES (?, ?, ?, ?)";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getGender());
            ps.setString(4, student.getBranch());

            int rows = ps.executeUpdate();

            ps.close();
            con.close();

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }


    // GET ALL STUDENTS
    public ArrayList<Student> getAllStudents() {

        ArrayList<Student> students = new ArrayList<>();

        String query =
                "SELECT id, name, gender, branch FROM student";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");

                String name = rs.getString("name");

                String gender = rs.getString("gender");

                String branch = rs.getString("branch");

                Student student =
                        new Student(id, name, gender, branch);

                students.add(student);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return students;
    }
}