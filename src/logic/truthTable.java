/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 *
 * @author fmcorona
 */
public class truthTable {
    private final String P;
    private final mClause[] Px;
    private final ArrayList<String> clauses;
    private final int row;
    private final int col;
    private final String[][] table;
    
    public truthTable(String P) {
        this.P = P;
        clauses = new ArrayList<>();
        
        obtainClauses();
        
        Px = new mClause[clauses.size()];
        
        for(int i = 0; i < clauses.size(); i++) {
            Px[i] = new mClause();
        }
        
        row = (int) pow(2, clauses.size());
        col = 2*clauses.size() + 1;
        
        table = new String[row][col];  
                
        build();
    } //End TruthTable
    
    private void build() {
        init();
        fillColumn(clauses.size(), P);
        
        for(int i = 0; i < clauses.size(); i++) { 
            majorClause(clauses.get(i));
        }
    } //End build
    
    private void init() {
        boolean put_true = true;
        int amoutTrue = row/2,  amoutFalse = row/2;
        
        for(int j = 0; j < clauses.size(); j++) {
            for(int i = 0; i < row; i++) {
                if(put_true) {
                    table[i][j] = "T";
                    amoutTrue--;
                } else {
                    table[i][j] = "F";
                    amoutFalse--;
                    //amoutTrue++; //Se puede recuperar el valor de amoutTrue aquí, en lugar de en el "if"
                }
                
                if(amoutTrue == 0) {
                    put_true = false;
                    amoutTrue = amoutFalse; //Para no perder el numero de Ts y Fs que se tienen que escribir
                }
                
                if(amoutFalse == 0) {
                    put_true = true;
                    amoutFalse = amoutTrue;
                }
            }
            amoutTrue /=2;
            amoutFalse /=2;
        }
    } //End init
    
    private void obtainClauses() {
        clauses.addAll(Arrays.asList(P.split(" ")));
        Collections.sort(clauses);
        
        for(int i = 0; i < clauses.size(); i++){            
            if(!Character.isLetter(clauses.get(i).charAt(0))) {
                clauses.remove(i);
                i--; //Se recorrer el índice, porque se eliminó un elemento de clauses
            }
            else if(i > 0 && clauses.get(i).equals(clauses.get(i - 1))) {
                clauses.remove(i);
                i--; //Se recorrer el índice, porque se eliminó un elemento de clauses
            }
        }
    } //End obtainClauses
    
    private void majorClause(String a) {
        String Pa, Pa_false = P, Pa_true = P;
        
        Pa_false = Pa_false.replace(a.charAt(0), 'F');
        Pa_true = Pa_true.replace(a.charAt(0), 'T');
        
        Pa = Pa_true + ' ' + Pa_false + ' ' + '^';
        
        fillColumn(clauses.size() + clauses.indexOf(a) + 1, Pa);        
    } //End majorClause
    
    private void fillColumn(int c, String p) {        
        Stack<String> StackE = new Stack<> ();
        Stack<String> StackP = new Stack<> ();
        String expr;
        
        for(int k = 0; k < row; k++) {
            expr = p;
            
            for(int i = 0; i < clauses.size(); i++) { //Asignar valores, de la tabla, a las clausulas
                expr = expr.replace(clauses.get(i).charAt(0), table[k][i].charAt(0));
            }

            String[] post = expr.split(" ");

            for(int i = post.length - 1; i >= 0; i--) {
                StackE.push(post[i]);
            }
            
            String operators = "-&|^>=";

            while (!StackE.isEmpty()) {
                if (operators.contains("" + StackE.peek())) {
                    if(StackE.peek().equals("-")) { // Si hay una negación, se evalúa
                        if(StackP.pop().equals("T")) {
                            StackP.push("F");
                        } else {
                            StackP.push("T");
                        }
                        StackE.pop();
                    } else { 
                        StackP.push(evaluate(StackE.pop(), StackP.pop(), StackP.pop()) + "");
                    }
                } else {
                    StackP.push(StackE.pop());
                }
            }
            
            table[k][c] = StackP.pop();
            
            //Si c es una columna de algun Px, obtenemos la posición donde Px = T
            //y los separamos por los valores, T o F, de x
            //c - clauses.size() - 1 es el número de columna de la clausula x
            if(c > clauses.size() && table[k][c].equals("T")) {
                if(table[k][c - clauses.size() - 1].equals("T")) {
                    Px[c - clauses.size() - 1].addT(k + 1);
                } else {
                    Px[c - clauses.size() - 1].addF(k + 1);
                }
            }
        }
    } //End fillColumn
    
    private String evaluate(String op, String b, String a) { // a op b
        int val_a = 0, val_b = 0;

        if(a.equals("T")) {
            val_a = 1;
        } else if(a.equals("F")) {
            val_a = 0;
        }

        if(b.equals("T")) {
            val_b = 1;
        } else if(b.equals("F")) {
            val_b = 0;
        }

        if(op.equals("&") && val_a + val_b == 2) return "T"; //And
        if(op.equals("|") && val_a + val_b >= 1) return "T"; //Or
        if(op.equals("^") && val_a != val_b) return "T"; //Exclusive Or
        if(op.equals(">")) { //Implication
            if(val_a == 1 && val_b == 0)
                return "F";

            return "T";
        }
        if(op.equals("=") && val_a == val_b) return "T"; //Equivalence

        return "F";
    } //End evaluate
    
    public void GACC() {
        System.out.println("The result for GACC is:"
                + "\nMajor Clause\tSet of possible tests");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("     " + clauses.get(i) + "\t\t");
            
            for(int j = 0; j < Px[i].sizeT(); j++) {
                for(int k = 0; k < Px[i].sizeF(); k++) {
                    System.out.print("(" + Px[i].getT(j) + ","+ Px[i].getF(k) + ") ");
                }
            }
            
            System.out.println();
        }
    }
    
    public void CACC() {
        System.out.println("The result for CACC is:"
                + "\nMajor Clause\tSet of possible tests");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("     " + clauses.get(i) + "\t\t");
            
            for(int j = 0; j < Px[i].sizeT(); j++) {
                for(int k = 0; k < Px[i].sizeF(); k++) {
                    //Es Px[i].getT(j) - 1 porque cuando se hace Px[index].addT(row) row representa
                    //el número de renglón más 1. Análogamente para valueOfPredicate(Px[i].getF(k) - 1)
                    if(!valueOfPredicate(Px[i].getT(j) - 1).equals(valueOfPredicate(Px[i].getF(k) - 1))) {
                        System.out.print("(" + Px[i].getT(j) + ","+ Px[i].getF(k) + ") ");
                    }
                }
            }
            
            System.out.println();
        }
    }
    
    public void RACC() {
        System.out.println("The result for RACC is:"
                + "\nMajor Clause\tSet of possible tests");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("     " + clauses.get(i) + "\t\t");
            
            for(int j = 0; j < Px[i].sizeT(); j++) {
                for(int k = 0; k < Px[i].sizeF(); k++) {
                    if(!valueOfPredicate(Px[i].getT(j) - 1).equals(valueOfPredicate(Px[i].getF(k) - 1))) {
                        if(compareMinorClauses(i, Px[i].getT(j) - 1, Px[i].getF(k) - 1)) {
                            System.out.print("(" + Px[i].getT(j) + ","+ Px[i].getF(k) + ") ");
                        }
                    }
                }
            }
            
            System.out.println();
        }        
    }
    
    private String valueOfPredicate(int r) {
        return table[r][clauses.size()];
    }
    
    private boolean compareMinorClauses(int col_mclause, int r1, int r2) {
        for(int i = 0; i < clauses.size(); i++) {
            if(i != col_mclause && !table[r1][i].equals(table[r2][i]))
                return false;
        }
        
        return true;
    }
    
    public void print() {        
        System.out.println("Truth table of\t " + P + ":");
        System.out.print("row\t");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print(clauses.get(i) + "\t");
        }
        
        System.out.print("P\t");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("P" + clauses.get(i) + "\t");
        }
        
        System.out.println();
        
        for(int i = 0; i < row; i++) {
            System.out.print(" " + (i + 1) + "\t");
            
            for(int j = 0; j < 2*clauses.size() + 1; j++) {
                System.out.print(table[i][j] + "\t");
            }
            System.out.println();
        }
    } //End print
    
}
