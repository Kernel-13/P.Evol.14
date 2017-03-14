/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import util.TipoFuncion;
import util.TipoSeleccion;

/**
 *
 * @author josemanuel
 */
public class Factoria {
    
    private TipoFuncion t;
    private TipoSeleccion s;
    
    public Factoria(TipoFuncion t2,TipoSeleccion s2){
        t = t2;
        s = s2;
    }
    
    public Seleccion factoriaSeleccion(){
        switch(s){
            case RULETA:
                return new SeleccionRuleta();
            case ESTOCASTICO:
                return new SeleccionRuleta();
            case TORNEODET:
                return new SeleccionTorneoDeterminista();
            case TORNEOPROB:
                return new SeleccionTorneoProbabilista();
            default:
                return new SeleccionRuleta();
        }
    }
    
    
    public Problema factoriaProblema(){
        switch(t){
            case F1:
                return new ProblemaF1();
            case F2:
                return new ProblemaF2();
            case F3:
                return new ProblemaF3();
            case F4:
                return new ProblemaF4();
            case F5:
                return new ProblemaF5();    
            
                
            default:
                return new ProblemaF1();
        }
    }
    
    public Cromosoma factoriaCromosoma(double l){
        switch(t){
            case F1:
                return new CromosomaF1(l);
            case F2:
                return new CromosomaF2(l);    
            case F3:
                return new CromosomaF3(l);
            case F4:
                return new CromosomaF4(l,4);
            case F5:
                return new CromosomaF5(l);
            
            default:
                return new CromosomaF1(l);
        }
    }   
}
