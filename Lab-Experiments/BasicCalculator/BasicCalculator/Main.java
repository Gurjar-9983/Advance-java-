package BasicCalculator;
import java.io.*;

public class Main {

   public static void main(String[] args) {

    CView cv= new CView();
    CModels cm = new CModels();

    new CController(cm, cv);
   }
}
