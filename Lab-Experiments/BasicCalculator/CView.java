package BasicCalculator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFrame;

public class CView extends JFrame{
JLabel a, b, result;
JTextField txta, txtb, txtresult;
JButton addition, subtraction, multiplication, division;

CView(){

    setSize(500, 500);
    setTitle("Calculator");
    setLayout(null);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    a = new JLabel("Enter A");
    a.setBounds(30, 30, 100, 30);
    add(a);

    txta = new JTextField();
    txta.setBounds(30, 60, 100, 30);
    add(txta);

   b = new JLabel("Enter B");
b.setBounds(150, 30, 100, 30);
add(b);

    txtb = new JTextField();
    txtb.setBounds(150, 60, 100, 30);
    add(txtb);

    addition = new JButton("+");
   addition.setBounds(30, 110, 60, 30);
    add(addition);

    subtraction = new JButton("-");
   subtraction.setBounds(100, 110, 60, 30);
    add(subtraction);

    multiplication = new JButton("*");
   multiplication.setBounds(170, 110, 60, 30);
    add(multiplication);

    division = new JButton("/");
   division.setBounds(240, 110, 60, 30);
    add(division);

    result = new JLabel("Result");
    result.setBounds(60, 150, 60, 30);
    add(result);

    txtresult = new JTextField();
    txtresult.setBounds(120, 150, 100, 30);
    txtresult.setEditable(false);
    add(txtresult);
    setVisible(true);

}
    
}
