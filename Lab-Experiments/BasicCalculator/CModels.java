package BasicCalculator;

public class CModels {
    public double addition(double a, double b){
        return a+b;
    }

    public double subtraction(double a, double b){
        return a-b;
    }

    public double multiplication(double a, double b){
        return a*b;
    }

    public double division(double a, double b){
        if(b==0){
            return 0;
        }
        return a/b;
    }
}
