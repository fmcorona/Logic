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
public class MClause {
    private final ArrayList<Integer> T;
    private final ArrayList<Integer> F;

    public MClause() {
        T = new ArrayList<>();
        F = new ArrayList<>();
    }
    
    public void addT(int i) {
        T.add(i);
    }
    
    public void addF(int i) {
        F.add(i);
    }
    
    public int getT(int i) {
        return T.get(i);
    }
    
    public int getF(int i) {
        return F.get(i);
    }
    
    public int sizeT() {
        return T.size();
    }
    
    public int sizeF() {
        return F.size();
    }
    
}
