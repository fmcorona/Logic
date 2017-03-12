/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author fmcorona
 */
public class Logic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        //truthTable table = new truthTable("a b > a c = &"); // (a > b) & (a = c) en notación postfija (Polaca Inversa)
        //truthTable table = new truthTable("a - b - & a c - & | a - c & |"); // (-a & -b) | (a & -c) | (-a & c)
        //truthTable table = new truthTable("a b - c | &"); // a & (-b | c)
        truthTable table = new truthTable("a b | c d & |"); // a | b | (c & d)
        
        table.print();
        table.GACC();
        table.CACC();
        table.RACC();
    }
    
}
