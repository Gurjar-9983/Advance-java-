
package RMI_ClientServer;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class CalculatorImplimentation extends UnicastRemoteObject implements Calculator {
    CalculatorImplimentation() throws RemoteException{
        super();
    }

    public int add(int a ,int b){
        return a+b;
    }

    public int subtract(int a, int b){
        return a-b;
    }

    public int multiply(int a, int b){
        return a*b;
    }

    public int divide(int a, int b){
        if(b!=0){
            return a/b;
        }
        else{
            System.out.println("denominator cannot be 0");
            return 0;
        }
    }
    
}
