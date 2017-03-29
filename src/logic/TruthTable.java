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
import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author fmcorona
 */
public class TruthTable {
    private final String p_postfix;
    private final String p_infix;
    private final TFArrays[] tf_arr;
    private final TFArrays[] Px;
    private final ArrayList<String> clauses;
    private final int row;
    private final int col;
    private final String[][] table;
    
    public TruthTable(String predicate) {
        this.p_infix = cleanString(predicate);
        this.p_postfix = infixToPostfix(predicate);
        clauses = new ArrayList<>();
        
        obtainClauses();
        
        tf_arr = new TFArrays[2*clauses.size() + 1]; // cláusulas + predicado + Pxs
        Px = new TFArrays[clauses.size()];
        
        for(int i = 0; i < clauses.size(); i++) {
            tf_arr[i] = new TFArrays();
            tf_arr[clauses.size() + i] = new TFArrays();
            Px[i] = new TFArrays();
        }
        
        tf_arr[2*clauses.size()] = new TFArrays();
        
        row = (int) pow(2, clauses.size());
        col = 2*clauses.size() + 1;
        
        table = new String[row][col];  
                
        build();
    } //End TruthTable
    
    /**
     *Construye la tabla.
     */
    private void build() {
        init();
        fillColumn(clauses.size(), p_postfix);
        
        for(int i = 0; i < clauses.size(); i++) { 
            fillColumn(clauses.size() + i + 1, majorClause(clauses.get(i)));
        }
    } //End build
    
    /**
     *Inicializa los valores, de verdad, de las cláusulas.  
     */
    private void init() {
        boolean put_true = true;
        int amoutTrue = row/2,  amoutFalse = row/2;
        
        for(int j = 0; j < clauses.size(); j++) {
            for(int i = 0; i < row; i++) {
                if(put_true) {
                    table[i][j] = "T";
                    tf_arr[j].addInT(i + 1); //Llenar T para la cluásula en la posición j
                    amoutTrue--;
                } else {
                    table[i][j] = "F";
                    tf_arr[j].addInF(i + 1); //Llenar F para la cláusula en la posición j
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
    
    /**
     *Regresa el predicado en notación postfija.
     * 
     * @param expr  predicado en notación infija
     * @return      predicado en notación postfija
     */
    private String infixToPostfix(String expr) {
        Stack<String> InStack = new Stack <>();
        Stack<String> OpStack = new Stack <>();
        Stack<String> OutStack = new Stack <>();
        String[] arrayInfix;        
        
        expr = cleanString(expr);        
        arrayInfix = expr.split(" ");
        
        for (int i = arrayInfix.length - 1; i >= 0; i--) {
            InStack.push(arrayInfix[i]);
        }
        
        while (!InStack.isEmpty()) {
            switch (hierarchy(InStack.peek())){
                case 1: //"("
                    OpStack.push(InStack.pop());
                    break;
                    
                case 3: case 4: case 5: //"-", "&", "|", "^", ">", "="
                    while(hierarchy(OpStack.peek()) >= hierarchy(InStack.peek())) {
                        OutStack.push(OpStack.pop());
                    }
                    OpStack.push(InStack.pop());
                    break; 
                
                case 2: //")"
                    while(!OpStack.peek().equals("(")) {
                        OutStack.push(OpStack.pop());
                    }
                    OpStack.pop();
                    InStack.pop();
                    break;
                    
                default:
                    OutStack.push(InStack.pop()); 
            } 
        }
        
        return OutStack.toString().replaceAll("[\\]\\[,]", "");
    } //End infixToPostfix
    
    /**
     *Regresa el predicado con el formato adecuado para transformarlo de
     *notación infija a notación postfija.
     * 
     * @param s     predicado en notación infija
     * @return      predicado sin más de un espacio entre cada símbolo
     */
    private String cleanString(String s) {
        String symbols = "-&|^>=()";
        String str = "";
        
        s = s.replaceAll(" ", "");
        s = "(" + s + ")";
        
        //Espacios entre conectivos lógicos
        for (int i = 0; i < s.length(); i++) {
            if (symbols.contains("" + s.charAt(i))) {
                str += " " + s.charAt(i) + " ";
            }else str += s.charAt(i);
        }
        return str.replaceAll("\\s+", " ").trim();
    } //End cleanString
    
    /**
     *Regresa la precedencia, jerarquía, de un operador lógico.
     * 
     * @param op    operador lógica
     * @return      valor jerárquico del operador o 0 en caso de no ser un
     *              operador válido
     */
    private int hierarchy(String op) {    
        if (op.equals("-")) return 5;
        if (op.equals(">") || op.equals("=")) return 4;
        if (op.equals("&") || op.equals("|") || op.equals("^")) return 3;
        if (op.equals(")")) return 2;
        if (op.equals("(")) return 1;

        return 0;
    } //End hierarchy
    
    /**
     *Obtiene las cláusulas del predicado.
     */
    private void obtainClauses() {
        clauses.addAll(Arrays.asList(p_postfix.split(" ")));
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
    
     /**
      *Regresa Pa, valor exacto bajo el cual la cláusula "a" determina el valor 
      *del predicado.
      * 
      * @param a    cláusula
      * return      Pa en notación postfija
      */
    private String majorClause(String a) {
        String Pa_false = p_postfix, Pa_true = p_postfix;
        
        Pa_false = Pa_false.replace(a.charAt(0), 'F');
        Pa_true = Pa_true.replace(a.charAt(0), 'T');
        
        return Pa_true + ' ' + Pa_false + ' ' + '^';
    } //End majorClause
    
    /**
     *Construye los valores de la tabla, en la columna c, para el predicado p.
     * 
     * @param c     columna de la tabla que se va a construir
     * @param p     predicado cuyos valores se van a calcular
     */
    private void fillColumn(int c, String p) {        
        Stack<String> StackE = new Stack<> ();
        Stack<String> StackP = new Stack<> ();
        String expr;
        
        for(int k = 0; k < row; k++) {
            expr = p;
            
            for(int i = 0; i < clauses.size(); i++) { //Asignar valores, de la tabla, a las cláusulas
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
            //y los separamos por los valores, T o F, de la cláusula x
            //c - clauses.size() - 1 es el número de columna de la cláusula x
            if(c > clauses.size() && table[k][c].equals("T")) {
                if(table[k][c - clauses.size() - 1].equals("T")) {
                    Px[c - clauses.size() - 1].addInT(k + 1);
                } else {
                    Px[c - clauses.size() - 1].addInF(k + 1);
                }
                tf_arr[c].addInT(k + 1); //Obtenemos la posición donde Px es true
            } else if(c > clauses.size() && table[k][c].equals("F")) {
                tf_arr[c].addInF(k + 1); //Obtenemos la posición donde Px es false
            }
            
            //Si c es la columna del predicado, P, obtenemos la posición donde
            //P = T y P = F.
            if(c == clauses.size()) {
                if(table[k][c].equals("T")) {
                    tf_arr[c].addInT(k + 1);
                } else {
                    tf_arr[c].addInF(k + 1);
                }
            }
        }
    } //End fillColumn
    
    /**
     *Regresa el valor obtenido de evaluar (a op b).
     * 
     * @param op    operador
     * @param b     operando derecho
     * @param a     operando izquierdo
     * @return      valor de a op b
     */
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
        if(op.equals("^") && val_a != val_b) return "T";     //Exclusive Or
        if(op.equals(">")) {                                 //Implication
            if(val_a == 1 && val_b == 0)
                return "F";

            return "T";
        }
        if(op.equals("=") && val_a == val_b) return "T";     //Equivalence

        return "F";
    } //End evaluate
    
    /**
     *Imprime los valores del criterio General Active Clause Coverage (GACC)
     */
    public void GACC() {
        System.out.println("\nThe result for GACC is:\nMajor Clause\tSet of possible tests");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("     " + clauses.get(i) + "\t\t");
            
            for(int j = 0; j < Px[i].sizeT(); j++) {
                for(int k = 0; k < Px[i].sizeF(); k++) {
                    System.out.print("(" + Px[i].getIndexOfTIn(j) + ","+ Px[i].getIndexOfFIn(k) + ") ");
                }
            }
            
            System.out.println();
        }
    } //End GACC
    
    /**
     *Imprime los valores del criterio Correlated Active Clause Coverage (CACC)
     */
    public void CACC() {
        System.out.println("\nThe result for CACC is:\nMajor Clause\tSet of possible tests");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("     " + clauses.get(i) + "\t\t");
            
            for(int j = 0; j < Px[i].sizeT(); j++) {
                for(int k = 0; k < Px[i].sizeF(); k++) {
                    //Es Px[i].getT(j) - 1 porque cuando se hace Px[index].addT(row) row representa
                    //el número de renglón más 1. Análogamente para valueOfPredicate(Px[i].getF(k) - 1)
                    if(!valueOfPredicate(Px[i].getIndexOfTIn(j) - 1).equals(valueOfPredicate(Px[i].getIndexOfFIn(k) - 1))) {
                        System.out.print("(" + Px[i].getIndexOfTIn(j) + ","+ Px[i].getIndexOfFIn(k) + ") ");
                    }
                }
            }
            
            System.out.println();
        }
    } //End CACC
    
    /**
     *Imprime los valores del criterio Restricted Active Clause Coverage (RACC)
     */
    public void RACC() {
        System.out.println("\nThe result for RACC is:\nMajor Clause\tSet of possible tests");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("     " + clauses.get(i) + "\t\t");
            
            for(int j = 0; j < Px[i].sizeT(); j++) {
                for(int k = 0; k < Px[i].sizeF(); k++) {
                    if(!valueOfPredicate(Px[i].getIndexOfTIn(j) - 1).equals(valueOfPredicate(Px[i].getIndexOfFIn(k) - 1))) {
                        if(compareMinorClauses(i, Px[i].getIndexOfTIn(j) - 1, Px[i].getIndexOfFIn(k) - 1)) {
                            System.out.print("(" + Px[i].getIndexOfTIn(j) + ","+ Px[i].getIndexOfFIn(k) + ") ");
                        }
                    }
                }
            }
            
            System.out.println();
        }        
    } //End RACC
    
    /**
     *Imprime los valores del criterio General Inactive Clause Coverage (GICC)
     */
    public void GICC() {
        ArrayList<Integer> aux_true, aux_false, a_true, a_false, b_true, b_false;
        
        System.out.println("\nThe result for GICC is:\nMajor Clause\tSet of possible tests");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("     " + clauses.get(i) + "\t\t");
            
            aux_true = intersection(tf_arr[clauses.size()].getT(), tf_arr[clauses.size() + i + 1].getF());
            aux_false = intersection(tf_arr[clauses.size()].getF(), tf_arr[clauses.size() + i + 1].getF());
            
            a_true = intersection(aux_true, tf_arr[i].getT());
            a_false = intersection(aux_true, tf_arr[i].getF());
            
            b_true = intersection(aux_false, tf_arr[i].getT());
            b_false = intersection(aux_false, tf_arr[i].getF());
            
            System.out.print("P = T: ");
            printValuesOfGICC(a_true, a_false);
            
            System.out.print("\n\t\tP = F: ");
            printValuesOfGICC(b_true, b_false);
            
            System.out.println();
        }
    } //End GICC
    
    /**
     *Imprime los valores del criterio Restricted Inactive Clause Coverage (RICC)
     */
    public void RICC() {
        ArrayList<Integer> aux_true, aux_false, a_true, a_false, b_true, b_false;
        
        System.out.println("\nThe result for RICC is:\nMajor Clause\tSet of possible tests");
        
        for(int i = 0; i < clauses.size(); i++) {
            System.out.print("     " + clauses.get(i) + "\t\t");
            
            aux_true = intersection(tf_arr[clauses.size()].getT(), tf_arr[clauses.size() + i + 1].getF());
            aux_false = intersection(tf_arr[clauses.size()].getF(), tf_arr[clauses.size() + i + 1].getF());
            
            a_true = intersection(aux_true, tf_arr[i].getT());
            a_false = intersection(aux_true, tf_arr[i].getF());
            
            b_true = intersection(aux_false, tf_arr[i].getT());
            b_false = intersection(aux_false, tf_arr[i].getF());
            
            System.out.print("P = T: ");
            printValuesOfRICC(i, a_true, a_false);
            
            System.out.print("\n\t\tP = F: ");
            printValuesOfRICC(i, b_true, b_false);
            
            System.out.println();
        }
    } //End RICC    
    
    /**
     *Imprime los pares (m,n), o un indica que no hay pares, para el criterio GICC.
     * 
     * @param a_true        ArrayList que contiene los posibles valores de m para los pares (m,n), para
     *                      los cuales el valor del predicado "a" es verdadero
     * @param a_false       ArrayList que contiene los posibles valores de n para los pares (m,n), para
     *                      los cuales el valor del predicado "a" es falso
     */
    private void printValuesOfGICC(ArrayList<Integer> a_true, ArrayList<Integer> a_false) {
        if(a_true.isEmpty() || a_false.isEmpty()) {
                System.out.print("No feasible pairs");
            } else {
                Iterator<Integer> iteratorA_true = a_true.iterator(), iteratorA_false;

                while(iteratorA_true.hasNext()) {
                    Integer elementA_true = iteratorA_true.next();
                    
                    iteratorA_false = a_false.iterator();
                    
                    while(iteratorA_false.hasNext()) {
                        Integer elementA_false = iteratorA_false.next();
                        
                        System.out.print("(" + elementA_true + "," + elementA_false + ") ");
                    }
                }
            }
    }
    
    /**
     *Imprime los pares (m,n), o un indica que no hay pares, para el criterio RICC.
     * 
     * @param col_clause    número de columna, de la tabla, en la cual se encuentra la cláusula
     *                      para la cual se van a imprimir los pares (m,n). Se utiliza como
     *                      referencia, para comparar los valores de las otras cláusulas
     * @param a_true        ArrayList que contiene los posibles valores de m para los pares (m,n), para
     *                      los cuales el valor del predicado "a" es verdadero
     * @param a_false       ArrayList que contiene los posibles valores de n para los pares (m,n), para
     *                      los cuales el valor del predicado "a" es falso
     */
    private void printValuesOfRICC(int col_clause, ArrayList<Integer> a_true, ArrayList<Integer> a_false) {
        if(a_true.isEmpty() || a_false.isEmpty()) {
                System.out.print("No feasible pairs");
            } else {
                Iterator<Integer> iteratorA_true = a_true.iterator(), iteratorA_false;

                while(iteratorA_true.hasNext()) {
                    Integer elementA_true = iteratorA_true.next();
                    
                    iteratorA_false = a_false.iterator();
                    
                    while(iteratorA_false.hasNext()) {
                        Integer elementA_false = iteratorA_false.next();
                        
                        if(compareMinorClauses(col_clause, elementA_true - 1, elementA_false - 1)) {
                            System.out.print("(" + elementA_true + "," + elementA_false + ") ");
                        }
                    }
                }
            }
    }
    
    /**
     *Regresa la intersección de A y B.
     *
     * @param A     ArrayList de Integers
     * @param B     ArrayList de Integers
     * @return      ArrayList cuyos elementos pertenecen a la intersección de A y B
     */
    private ArrayList<Integer> intersection(ArrayList<Integer> A, ArrayList<Integer> B) {
        ArrayList<Integer> A_cap_B = new ArrayList<>();
        
        A_cap_B.addAll(A);
        A_cap_B.retainAll(B);
        
        return A_cap_B;
    } //End intersection
    
    /**
     *Regresa el valor, de verdad, del predicado en el renglón r.
     * 
     * @param r     renglón
     * @return      valor de verdad del predicado
     */
    private String valueOfPredicate(int r) {
        return table[r][clauses.size()];
    } //End valueOfPredicate
    
    /**
     *Regresa el valor booleano, obtenido, de comparar los valores de verdad de
     *las cláusulas menores que están en los renglones r1 y r2.
     * 
     * @param col_mclause   columna en la cual se encuentra la cláusula mayor
     * @param r1            renglón 1
     * @param r2            renglón 2
     * @return              true si las cláusulas menores en los renglones r1
     *                      y r2 tienen los mismos valores de verdad
     */
    private boolean compareMinorClauses(int col_mclause, int r1, int r2) {
        for(int i = 0; i < clauses.size(); i++) {
            if(i != col_mclause && !table[r1][i].equals(table[r2][i]))
                return false;
        }
        
        return true;
    } //End compareMinorClauses
    
    /**
     *Imprime la tabla de verdad.
     */
    public void print() {
        System.out.println("Truth table of " + p_infix + ":");
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
