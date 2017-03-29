/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;

/**
 *
 * @author fmcorona
 */
public class TFArrays {
    private final ArrayList<Integer> T;
    private final ArrayList<Integer> F;

    public TFArrays() {
        T = new ArrayList<>();
        F = new ArrayList<>();
    }
    
    /**
     *Añade index a T.
     * 
     * @param index     índice que se va a añadir a T
     */
    public void addInT(int index) {
        T.add(index);
    }
    
    /**
     *Añade index a F.
     * 
     * @param index     índice que se va a añadir a T
     */
    public void addInF(int index) {
        F.add(index);
    }
    
    /**
     *Regresa el índice almacenado, en T, en la posición i.
     * 
     * @param i     posición del índice
     * @return      índice en la posición i
     */
    public int getIndexOfTIn(int i) {
        return T.get(i);
    }
    
    /**
     *Regresa el índice  almacenado, en F, en la posición i.
     * @param i     posición del índice
     * @return      índice en la posición i
     */
    public int getIndexOfFIn(int i) {
        return F.get(i);
    }

    /**
     *Regresa el ArrayList T.
     * 
     * @return      ArrayList T
     */
    public ArrayList<Integer> getT() {
        return T;
    }

    /**
     *Regresa el ArrayList F.
     * 
     * @return      ArrayList F
     */
    public ArrayList<Integer> getF() {
        return F;
    }
    
    /**
     *Regresa el tamaño de T.
     * 
     * @return      tamaño de T
     */
    public int sizeT() {
        return T.size();
    }
    
    /**
     *Regresa el tamaño de F.
     * 
     * @return      tamaño de F
     */
    public int sizeF() {
        return F.size();
    }
    
}
