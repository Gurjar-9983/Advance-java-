package BasicCalculator;
import java.awt.event.*;

public class CController implements ActionListener{
    CModels cm;
    CView cv;

    public CController(CModels cm, CView cv){
        this.cm=cm;
        this.cv= cv;

        cv.addition.addActionListener(this);
        cv.subtraction.addActionListener(this);
        cv.multiplication.addActionListener(this);
        cv.division.addActionListener(this);
    
    }

    @Override
    public void actionPerformed(ActionEvent e){
        double a= Double.parseDouble(cv.txta.getText());
        double b= Double.parseDouble(cv.txtb.getText());
        double result=0;
        if(e.getSource()==cv.addition){
            result=cm.addition(a,b);
        }
        else if(e.getSource()==cv.subtraction){
            result=cm.subtraction(a,b);
        }
        else if(e.getSource()==cv.multiplication){
            result=cm.multiplication(a,b);
        }
        else if(e.getSource()==cv.division){
            result=cm.division(a,b);
        }

        cv.txtresult.setText(String.valueOf(result));
    }
}
