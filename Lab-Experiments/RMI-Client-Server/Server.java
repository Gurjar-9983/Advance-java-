package RMI_ClientServer;
import java.rmi.Naming;

public class Server {

    public static void main(String[] args){
        try{
            CalculatorImplimentation obj = new CalculatorImplimentation();
            Naming.rebind("rmi://localhost/Calulator", obj);
            System.out.println("RMI Server Started");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
}
