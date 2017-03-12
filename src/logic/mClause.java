/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class mClause {
    private ArrayList<Integer> T;
    private ArrayList<Integer> F;

    public mClause() {
        T = new ArrayList<>();
        F = new ArrayList<>();
    }
    
    public void addT(int x) {
        T.add(x);
    }
    
    public void addF(int x) {
        F.add(x);
    }
    
    public int getT(int index) {
        return T.get(index);
    }
    
    public int getF(int index) {
        return F.get(index);
    }
    
    public int sizeT() {
        return T.size();
    }
    
    public int sizeF() {
        return F.size();
    }
    
}
