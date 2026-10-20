package StudentRegistration;
//Simple Student registration form using Java swing
import javax.swing.*;
import java.awt.event.*;

public class StudentRegistration extends JFrame implements ActionListener {
JLabel name, roll, branch, gender;
JTextField txtname, txtroll, txtbranch;
JRadioButton  male, female;
JCheckBox terms;
JButton submit , reset;
ButtonGroup g;

	StudentRegistration(){
		setSize(500, 500);
		setTitle( "Student Registration Form (GIT)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		name = new JLabel( "Name ");
		name.setBounds(30, 30, 100, 20);
		add(name);

		txtname= new JTextField();
		txtname.setBounds(120, 30, 100, 20);
		add(txtname);

		roll = new JLabel( "Roll No. ");
		roll.setBounds(30, 60, 100, 20);
		add(roll);

		txtroll = new JTextField();
		txtroll.setBounds(120, 60, 100, 20);
		add(txtroll);

		branch = new JLabel( "Branch ");
		branch.setBounds(30, 90, 100, 20);
		add(branch);

		txtbranch = new JTextField();
		txtbranch.setBounds(120, 90, 100, 20);
		add(txtbranch);

		gender = new JLabel( "Gender ");
		gender.setBounds(30, 120, 100, 20);
		add(gender);

		male = new JRadioButton( "Male");
		male.setBounds(120, 120, 100, 20);
		add(male);

		female = new JRadioButton( "Female");
		female.setBounds(120, 140, 100, 20);
		add(female);

		g = new ButtonGroup();
		g.add(male);
		g.add(female);

		terms = new JCheckBox( "I accept terms & Conditions ");
		terms.setBounds(30, 170, 250, 20);
		add(terms);

		submit = new JButton("Submit");
		submit.setBounds(60, 210, 100, 20);
		submit.addActionListener(this);
		add(submit);

		reset= new JButton("reset");
		reset.setBounds(170, 210, 100, 20);
		reset.addActionListener(this);
		add(reset);

		setVisible(true);
	}


	@Override
public void actionPerformed(ActionEvent e) {

    if(e.getSource()==submit){
		//validation
        if(txtname.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter Name");
            return;
        }

        if(txtroll.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter Roll Number");
            return;
        }

        if(txtbranch.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter Branch");
            return;
        }

        if(!male.isSelected() && !female.isSelected()){
            JOptionPane.showMessageDialog(this,"Select Gender");
            return;
        }

        if(!terms.isSelected()){
            JOptionPane.showMessageDialog(this,"Accept Terms");
            return;
        }

        JOptionPane.showMessageDialog(this,"Registration Successful");
		//after submit reset the form
		txtname.setText("");
        txtroll.setText("");
        txtbranch.setText("");
        g.clearSelection();
        terms.setSelected(false);
    }

    else if(e.getSource()==reset){

        txtname.setText("");
        txtroll.setText("");
        txtbranch.setText("");

        g.clearSelection();

        terms.setSelected(false);

    }

}


public static void main( String[] args ){
	   new StudentRegistration();

}

}