/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Valderlei
 */
public class Positions {
    
    public static double getX(int number){
        
        if(number > 9){
            return -40.0;
        }
        
        if(number > 6){
            return -25.0;
        }
        
        return -10.0;
    }
    
    public static double getY(int number){
        
        switch(number){
            case 11:
                return -30.0;
            case 10:
                return -10.0;
            case 9:
                return 10.0;
            case 8:
                return 30.0;
            case 7:
                return -20.0;
            case 6:
                return 0.0;
            case 5:
                return 20.0;
            case 4:
                return -15.0;
            case 3:
                return 0.0;
            case 2:
                return 15.0;
            default:
                return 0.0;
        }
    }
    
    
}
