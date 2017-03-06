/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

/**
 *
 * @author josemanuel
 */
public class Controlador {
    private int funcion;
    private int poblacion;
    private int iteraciones;
    private int porcCruces;
    private int porcMutacion;
    private double precision;
    private TipoSeleccion seleccion;
    
    public Controlador(){
        funcion = 1;
        poblacion = 100;
        iteraciones = 200;
        porcCruces = 20;
        porcMutacion = 10;
        precision = 0.001;
        seleccion = TipoSeleccion.RULETA;
    }
    
    public void cambiarFuncion (int f){
        funcion = f;
    }
    
    public void cambiarPoblacion(String pob){
        int p = 0;
        try{
            p = Integer.parseInt(pob);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(pob);
        if(p > 0)
            poblacion = p;
    }
    
    public void cambiarIteraciones(String it){
        int i = 0;
        try{
            i = Integer.parseInt(it);
        }catch(Exception e){
            e.printStackTrace();
        }
        iteraciones = i;
    }
    
    public void cambiarPorcCruces(String probc){
        int pc = 0;
        try{
            pc = Integer.parseInt(probc);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(pc >= 0 && pc <= 100)
           porcCruces = pc;
    }
    
    public void cambiarPorcMutacion(String probm){
        int pm = 0;
        try{
            pm = Integer.parseInt(probm);
        }catch(Exception e){
            e.printStackTrace();
        } 
        if(pm >= 0 && pm <= 100)
            porcMutacion = pm;
    }
    
    public void cambiarPrecision(String pre){
        int p = 0;
        try{
            p = Integer.parseInt(pre);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(p > 0 && p < 1)
            precision = p;
    }
    
    public void cambiarSeleccion(int s){
        switch(s){
            case 0:
                seleccion = TipoSeleccion.RULETA;
                break;
            case 1:
                seleccion = TipoSeleccion.TORNEO;
                break;
            case 2:
                seleccion = TipoSeleccion.ESTOCASTICO;
                break;
        }
    }
    
    
    
}
