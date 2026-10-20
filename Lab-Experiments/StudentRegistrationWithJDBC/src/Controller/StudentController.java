package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import DAO.StudentDAO;
import Models.Student;
import View.StudentListView;
import View.StudentRegistrationView;

public class StudentController implements ActionListener {

    private StudentRegistrationView view;
    private StudentDAO dao;

    public StudentController(StudentRegistrationView view, StudentDAO dao) {

        this.view = view;
        this.dao = dao;

        view.submit.addActionListener(this);
        view.reset.addActionListener(this);
        view.viewStudents.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == view.submit) {

            registerStudent();

        } 
        else if (e.getSource() == view.reset) {

            resetForm();

        } 
        else if (e.getSource() == view.viewStudents) {

            ArrayList<Student> students =
                    dao.getAllStudents();

            new StudentListView(students);
        }
    }


    private void registerStudent() {

        // Name validation
        if (view.txtName.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    view,
                    "Please Enter Student Name"
            );

            return;
        }


        // Roll number validation
        if (view.txtRoll.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    view,
                    "Please Enter Roll Number"
            );

            return;
        }


        // Branch validation
        if (view.txtBranch.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    view,
                    "Please Enter Branch"
            );

            return;
        }


        // Gender validation
        if (!view.male.isSelected() &&
            !view.female.isSelected()) {

            JOptionPane.showMessageDialog(
                    view,
                    "Please Select Gender"
            );

            return;
        }


        // Terms validation
        if (!view.terms.isSelected()) {

            JOptionPane.showMessageDialog(
                    view,
                    "Please Accept Terms & Conditions"
            );

            return;
        }


        // Convert Roll Number
        int rollNo;

        try {

            rollNo = Integer.parseInt(
                    view.txtRoll.getText().trim()
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    view,
                    "Roll Number must be a number"
            );

            return;
        }


        // Get Gender
        String gender;

        if (view.male.isSelected()) {

            gender = "Male";

        } else {

            gender = "Female";
        }


        // Create Student object
        Student student = new Student(
                rollNo,
                view.txtName.getText().trim(),
                gender,
                view.txtBranch.getText().trim()
        );


        // Insert into database
        boolean success = dao.insertStudent(student);


        if (success) {

            JOptionPane.showMessageDialog(
                    view,
                    "Registration Successful!"
            );

            resetForm();

        } else {

            JOptionPane.showMessageDialog(
                    view,
                    "Registration Failed!"
            );
        }
    }


    private void resetForm() {

        view.txtName.setText("");
        view.txtRoll.setText("");
        view.txtBranch.setText("");

        view.genderGroup.clearSelection();

        view.terms.setSelected(false);
    }
}