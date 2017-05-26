/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author lolita
 */
public enum TipoOperacion {
    AND,OR,NOT,IF,HOJA;
    
    public static int length = 4;
    
    public String toString(){
        switch(this){
            case AND:
                return "AND";
            case OR:
                return "OR";
            case NOT:
                return "NOT";
            case IF:
                return "IF";
            default:
                return "HOJA";
        }
    }
}
