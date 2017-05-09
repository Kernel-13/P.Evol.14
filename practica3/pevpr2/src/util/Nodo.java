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
public class Nodo {
    private int terminal;
    private TipoOperacion f;
    private Nodo der;
    private Nodo izq;
    private Nodo cond;
    
    public Nodo(TipoOperacion f,int valor ,Nodo izq, Nodo der,Nodo cond){
        this.f = f;
        this.izq = izq;
        this.der = der;
        this.cond = cond;
    }
    
    public TipoOperacion funcion(){
        return this.f;
    }
    
    public boolean valor(boolean[] tabla){
        if(this.f == TipoOperacion.HOJA)
            return tabla[this.terminal];
        else{
            switch(f){
                case AND:
                    return der.valor(tabla)&&izq.valor(tabla);
                case OR:
                    return der.valor(tabla)||izq.valor(tabla);
                case NOT:
                    return !izq.valor(tabla);
                case IF:
                    return this.cond.valor(tabla) ? izq.valor(tabla) : der.valor(tabla);
                default:
                    return tabla[this.terminal];
            }
        }
    }
    
    public Nodo getDer(){
        return der;
    }
    
    public Nodo getIzq(){
        return izq;
    }
    
    public String toString(String[] tabla){
        if(this.f == TipoOperacion.HOJA)
            return tabla[this.terminal];
        else{
            switch(f){
                case HOJA:
                    return tabla[this.terminal];
                case NOT:
                    return "("+this.f.toString()+" "+this.izq.toString(tabla)+")";
                case IF:
                    return "("+this.f.toString()+" "+this.cond.toString(tabla)+" "+izq.toString(tabla)+" "+der.toString(tabla)+")";
                default:
                    return "("+this.f.toString()+" "+izq.toString(tabla)+" "+der.toString(tabla)+")";
            }
        }
    }
}
