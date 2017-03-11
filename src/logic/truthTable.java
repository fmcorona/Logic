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
 * @author Miguel Corona
 */
public class truthTable {
    private final String P;
    private final ArrayList<String> clauses;
    private final int row;
    private final int col;
    private final String[][] table;
    
    public truthTable(String P) {
        this.P = P;
        clauses = new ArrayList<>();
        
        obtainClauses();
        
        row = (int) pow(2,clauses.size());
        col = clauses.size() + 1;
        
        table = new String[row][col];  
        
        init();        
        build();
    }
    
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
    }
    
    public void printClauses() {        
        System.out.print("Clauses: ");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print(clauses.get(i) + " ");
        }
        
        System.out.println("");
    }
    
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
    }// End init
    
    public void build() {
        Stack<String> StackE = new Stack<> ();
        Stack<String> StackP = new Stack<> ();
        String expr;
        
        for(int k = 0; k < row; k++) {
            expr = P;
            
            for(int i = 0; i < clauses.size(); i++) { //Asignar valores, de la tabla, a las clausulas
                expr = expr.replace(clauses.get(i).charAt(0), table[k][i].charAt(0));
            }

            String[] post = expr.split(" ");

            for(int i = post.length - 1; i >= 0; i--) {
                StackE.push(post[i]);
            }

            //Evaluación
            String operators = "-&|^>=";

            while (!StackE.isEmpty()) {
                if (operators.contains("" + StackE.peek())) {
                    if(StackE.peek().equals("-")) { // Si hay una negación, se evalua
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
            
            table[k][clauses.size()] = StackP.pop();
        }
    } //End build
    
    public void printTable() {        
        System.out.println("Truth table: " + P);
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print(clauses.get(i) + "\t");
        }
        
        System.out.println("P");
        
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < clauses.size() + 1; j++) {
                System.out.print(table[i][j] + "\t");
            }
            System.out.println();
        }
    }
    
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
    }
    
}
