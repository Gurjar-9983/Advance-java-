
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistration extends JFrame implements ActionListener {

    JLabel name, roll, branch, gender;
    JTextField txtname, txtroll, txtbranch;
    JRadioButton male, female;
    JCheckBox terms;
    JButton submit, reset;

    ButtonGroup g;

    StudentRegistration() {

        setSize(500, 500);
        setTitle("Student Registration Form (GIT)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Name
        name = new JLabel("Name");
        name.setBounds(30, 30, 100, 20);
        add(name);

        txtname = new JTextField();
        txtname.setBounds(120, 30, 150, 20);
        add(txtname);

        // Roll Number
        roll = new JLabel("Roll No.");
        roll.setBounds(30, 60, 100, 20);
        add(roll);

        txtroll = new JTextField();
        txtroll.setBounds(120, 60, 150, 20);
        add(txtroll);

        // Branch
        branch = new JLabel("Branch");
        branch.setBounds(30, 90, 100, 20);
        add(branch);

        txtbranch = new JTextField();
        txtbranch.setBounds(120, 90, 150, 20);
        add(txtbranch);

        // Gender
        gender = new JLabel("Gender");
        gender.setBounds(30, 120, 100, 20);
        add(gender);

        male = new JRadioButton("Male");
        male.setBounds(120, 120, 100, 20);
        add(male);

        female = new JRadioButton("Female");
        female.setBounds(120, 140, 100, 20);
        add(female);

        // Button Group
        g = new ButtonGroup();
        g.add(male);
        g.add(female);

        // Terms
        terms = new JCheckBox("I accept Terms & Conditions");
        terms.setBounds(30, 170, 250, 20);
        add(terms);

        // Submit
        submit = new JButton("Submit");
        submit.setBounds(60, 210, 100, 25);
        submit.addActionListener(this);
        add(submit);

        // Reset
        reset = new JButton("Reset");
        reset.setBounds(180, 210, 100, 25);
        reset.addActionListener(this);
        add(reset);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Submit Button
        if (e.getSource() == submit) {

            // Name validation
            String studentName = txtname.getText().trim();

            if (studentName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Name");
                return;
            }

            // Roll Number validation
            int rollNo;

            try {
                rollNo = Integer.parseInt(txtroll.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Roll Number must be a valid number"
                );
                return;
            }

            // Branch validation
            String studentBranch = txtbranch.getText().trim();

            if (studentBranch.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Branch");
                return;
            }

            // Gender validation
            if (!male.isSelected() && !female.isSelected()) {
                JOptionPane.showMessageDialog(this, "Select Gender");
                return;
            }

            String studentGender =
                    male.isSelected() ? "Male" : "Female";

            // Terms validation
            if (!terms.isSelected()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Accept Terms & Conditions"
                );
                return;
            }

            // Database configuration from environment variables
            String url = System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/collegeJava"
            );

            String username = System.getenv().getOrDefault(
                    "DB_USER",
                    "root"
            );

            String password = System.getenv("DB_PASSWORD");

            if (password == null || password.isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database password is not configured.\n"
                                + "Please set the DB_PASSWORD environment variable."
                );
                return;
            }

            String query =
                    "INSERT INTO student(id, name, gender, branch) "
                    + "VALUES (?, ?, ?, ?)";

            // Database operation
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                try (
                        Connection con = DriverManager.getConnection(
                                url,
                                username,
                                password
                        );
                        PreparedStatement ps = con.prepareStatement(query)
                ) {
                    ps.setInt(1, rollNo);
                    ps.setString(2, studentName);
                    ps.setString(3, studentGender);
                    ps.setString(4, studentBranch);

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Registration Successful!"
                        );

                        resetForm();
                    }
                }

            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "MySQL JDBC Driver not found!"
                );

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database Error: " + ex.getMessage()
                );
                ex.printStackTrace();
            }
        }

        // Reset Button
        else if (e.getSource() == reset) {
            resetForm();
        }
    }

    private void resetForm() {
        txtname.setText("");
        txtroll.setText("");
        txtbranch.setText("");
        g.clearSelection();
        terms.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistration::new);
    }
}
