package RMI_ClientServer;
import java.rmi.Naming;

public class Client{
    public static void main(String[] args){
        try{
            Calculator obj = (Calculator) Naming.lookup("rmi://localhost/Calculator");
            System.out.println("Addition = " + obj.add(75, 5));
            System.out.println("Subtraction = " + obj.subtract(75, 5));
            System.out.println("Multiplication = " + obj.multiply(75, 5));
            System.out.println("Division = " + obj.divide(75, 5));
        } catch(Exception e){
        System.out.println(e);
        }
    }
}