package View;

import javax.swing.*;

public class StudentRegistrationView extends JFrame {

    public JLabel nameLabel, rollLabel, branchLabel, genderLabel;

    public JTextField txtName, txtRoll, txtBranch;

    public JRadioButton male, female;

    public JCheckBox terms;

    public JButton submit, reset, viewStudents;

    public ButtonGroup genderGroup;

    public StudentRegistrationView() {

        setTitle("Student Registration Form (GIT)");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Name
        nameLabel = new JLabel("Name");
        nameLabel.setBounds(30, 30, 100, 25);
        add(nameLabel);

        txtName = new JTextField();
        txtName.setBounds(150, 30, 200, 25);
        add(txtName);

        // Roll Number
        rollLabel = new JLabel("Roll No.");
        rollLabel.setBounds(30, 70, 100, 25);
        add(rollLabel);

        txtRoll = new JTextField();
        txtRoll.setBounds(150, 70, 200, 25);
        add(txtRoll);

        // Branch
        branchLabel = new JLabel("Branch");
        branchLabel.setBounds(30, 110, 100, 25);
        add(branchLabel);

        txtBranch = new JTextField();
        txtBranch.setBounds(150, 110, 200, 25);
        add(txtBranch);

        // Gender
        genderLabel = new JLabel("Gender");
        genderLabel.setBounds(30, 150, 100, 25);
        add(genderLabel);

        male = new JRadioButton("Male");
        male.setBounds(150, 150, 80, 25);
        add(male);

        female = new JRadioButton("Female");
        female.setBounds(240, 150, 100, 25);
        add(female);

        genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);

        // Terms
        terms = new JCheckBox("I accept Terms & Conditions");
        terms.setBounds(30, 190, 250, 25);
        add(terms);

        // Submit
        submit = new JButton("Submit");
        submit.setBounds(80, 240, 100, 30);
        add(submit);

        // Reset
        reset = new JButton("Reset");
        reset.setBounds(200, 240, 100, 30);
        add(reset);

        viewStudents = new JButton("View Students");
        viewStudents.setBounds(320, 240, 130, 30);
        add(viewStudents);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}