/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author Miguel Corona
 */
public class Logic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        //truthTable table = new truthTable("a b > a c = &"); // (a > b) & (a = c) en notación postfija (Polaca Inversa)
        truthTable table = new truthTable("a b - & - -"); // --(a & -b) en notación postfija (Polaca Inversa)
        //truthTable table = new truthTable("a b ^"); // a ^ b (disyunción eclusiva)
        table.printClauses();
        table.printTable();
    }
    
}
