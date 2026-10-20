package View;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Models.Student;

public class StudentListView extends JFrame {

    private JTable studentTable;
    private DefaultTableModel tableModel;

    public StudentListView(ArrayList<Student> students) {

        setTitle("Registered Students");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        // Table columns
        String[] columns = {
            "Roll No.",
            "Name",
            "Gender",
            "Branch"
        };


        // Create table model
        tableModel = new DefaultTableModel(columns, 0);

        studentTable = new JTable(tableModel);


        // Add student data
        for (Student student : students) {

            Object[] row = {
                student.getId(),
                student.getName(),
                student.getGender(),
                student.getBranch()
            };

            tableModel.addRow(row);
        }


        // Add table inside scroll pane
        JScrollPane scrollPane =
                new JScrollPane(studentTable);

        add(scrollPane);

        setVisible(true);
    }
}