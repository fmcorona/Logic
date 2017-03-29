/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author fmcorona
 */
public class Logic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String predicate = read("predicate.txt");
        
        if(!predicate.equals("")) {
            TruthTable table = new TruthTable(predicate);
        
            table.print();
            table.GACC();
            table.CACC();
            table.RACC();
            table.GICC();
            table.RICC();
        }        
    } //End main
    
    /**
     *Lee un archivo y regresa el predicado que se va a analizar.
     *En caso de que el archivo no exista imprime un mensaje de error.
     * 
     * @param name  nombre del archivo donde está el predicado
     * @return      predicado
     */
    private static String read(String name) {
        String predicate = "";

        File file = new File(name);
        
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            predicate = br.readLine();
        } catch(IOException e) {
            System.out.println("File does not exist:\n" + e);
        }
        
        return predicate;
    } //End read
    
}
