/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factorial;

/**
 *
 * @author teo
 */
public class Factorial {

    /**
     * @param args the command line arguments
     */
    
    public long factorial(long number){
    if(number<=1)
        return 1;
    else
        return number*factorial(number-1);
    }
    
    public void display(){
    for(int i=0;i<10;i++){
        System.out.printf("%d!=\t%d\n",i,factorial(i));
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Factorial calc=new Factorial();
        calc.display();
    }
    
}
