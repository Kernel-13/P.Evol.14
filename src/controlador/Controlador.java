/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import modelo.AlgoritmoGenetico;
import util.DatosGrafica;
import util.TipoSeleccion;
import util.TipoFuncion;

/**
 *
 * @author josemanuel
 */
public class Controlador {
    private TipoFuncion funcion;
    private int poblacion;
    private int iteraciones;
    private int probCruces;
    private int probMutacion;
    private double precision;
    private TipoSeleccion seleccion;
    private int semilla;
    private int nvars;
    private boolean elitismo;
    
    public Controlador(){
        funcion = TipoFuncion.F1;
        poblacion = 10;
        iteraciones = 200;
        probCruces = 20;
        probMutacion = 10;
        precision = 0.001;
        semilla = 2;
        seleccion = TipoSeleccion.RULETA;
        nvars = 4;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente a la funcion
     * @param f 
     */
    public void cambiarFuncion (int f){
        switch(f){
            case 0:
                funcion = TipoFuncion.F1;
                break;
            case 1:
                funcion = TipoFuncion.F2;
                break;
            case 2:
                funcion = TipoFuncion.F3;
                break;
            case 3:
                funcion = TipoFuncion.F4;
                break;
            case 4:
                funcion = TipoFuncion.F5;
                break;
            default:
                funcion = TipoFuncion.F3;
        }
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente al tamaño de población
     * @param pob 
     */
    public void cambiarPoblacion(String pob){
        int p = 0;
        try{
            p = Integer.parseInt(pob);
        }catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println(pob);
        if(p > 0)
            poblacion = p;
    }
    
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente al numero de iteraciones
     * @param it 
     */
    public void cambiarIteraciones(String it){
        int i = 0;
        try{
            i = Integer.parseInt(it);
        }catch(Exception e){
            e.printStackTrace();
        }
        iteraciones = i;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente a la probabilidad de cruce
     * @param probc 
     */
    public void cambiarPorcCruces(String probc){
        int pc = 0;
        try{
            pc = Integer.parseInt(probc);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(pc >= 0 && pc <= 100)
           probCruces = pc;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente a la probabilidad de mutacion
     * @param probm 
     */
    public void cambiarPorcMutacion(String probm){
        int pm = 0;
        try{
            pm = Integer.parseInt(probm);
        }catch(Exception e){
            e.printStackTrace();
        } 
        if(pm >= 0 && pm <= 100)
            probMutacion = pm;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente a la precision
     * @param pre 
     */
    public void cambiarPrecision(String pre){
        double p = 0;
        try{
            p = Double.parseDouble(pre);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(p > 0 && p < 1)
            precision = p;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente al tipo de seleccion
     * @param s 
     */
    public void cambiarSeleccion(int s){
        switch(s){
            case 0:
                seleccion = TipoSeleccion.RULETA;
                break;
            case 1:
                seleccion = TipoSeleccion.TORNEODET;
                break;
            case 2:
                seleccion = TipoSeleccion.ESTOCASTICO;
                break;
            case 3:
                seleccion = TipoSeleccion.TORNEOPROB;
                break;
            default:
                seleccion = TipoSeleccion.RULETA;
        }
    }
    
    /**
     * cambia la semilla que se usa para generar la poblacion inicial
     * @param nueva 
     */
    public void cambiarSemilla(String nueva){
        int newsemilla = 2;
        try{
            newsemilla = Integer.parseInt(nueva);
        }catch(Exception e){
            
        } 
        semilla = newsemilla;
    }
    
    /**
     * cambia el numero de variables de la funcion 4
     * @param nuevo 
     */
    public void cambiarNvars(String nuevo){
        int newNvars = 4;
        try{
            newNvars = Integer.parseInt(nuevo);
        }catch(Exception e){
            
        } 
        nvars = newNvars;
    }
    
    /**
     * actica/desactiva el elitismo
     * @param el 
     */
    public void cambiarElitismo(boolean el){
        elitismo = el;
    }
    
    /**
     * ejecuta el algoritmo genetico y devuelve los datos para generar 
     * la grafica
     * @return 
     */
    public DatosGrafica ejecuta(){
        AlgoritmoGenetico algo = new AlgoritmoGenetico(funcion, poblacion, iteraciones,
                probCruces, probMutacion,precision,seleccion,nvars,elitismo);
        return algo.ejecuta(semilla);
    }
    
    
}
