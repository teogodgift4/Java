/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fibonacci;

/**
 *
 * @author teo
 */
public class Fibonacci {

    /**
     * @param args the command line arguments
     */
    
    public long fibonacci(int number){
    if(number==0)
        return 0;
    else if(number==1)
        return 1;
    else
        return fibonacci(number-1)+fibonacci(number-2);
    }
    
    public void display(){
        for(int i=0;i<10;i++)
    System.out.printf("%dfib=%d\n",i,fibonacci(i));
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Fibonacci calc=new Fibonacci();
        calc.display();
    }
    
}
