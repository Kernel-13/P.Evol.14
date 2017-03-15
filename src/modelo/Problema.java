/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import util.Functions;

/**
 *
 * @author Ederson
 */
public abstract class Problema {
    
    protected Cromosoma best;
    protected double sumaPob;
    
    protected Cromosoma[] elite;
    
    public abstract Cromosoma evaluacion(Cromosoma[] pob);	// Debe devolver la mejor aptitud, y una array de las aptitudes
    
    abstract double aptitudReal(Cromosoma individuo, double maxApt);	// Debe devolver la aptitud tras aplicar desplazamiento
    // El parametro maxApt debera ser calculado con anterioridad, y quiza tenerlo como un atributo de AG
    // Quiza es mejor hacerla una funcion privada, ya que solo es utilizada por evaluacion
	
    abstract double media(int tam);
    
    public abstract Cromosoma[] reproduccion(Cromosoma[] pob, double probCruce); // Debe devolver una nueva poblacion con cromosomas 
    // cruzados. 
    // 

    abstract void cruce(Cromosoma[] pob, Cromosoma son1, Cromosoma son2); // Cruza 2 cromosomas - obtenemos 2 hijos
    // Tambien creo que deberia ser privada

    public abstract void mutacion(Cromosoma[] pob, double probMutacion);	// Recorre la pob. y cada individuo, y segun la probabilidad cambia un gen o no
    // 
    public abstract Cromosoma getBest();
    
    public abstract void elitismo(Cromosoma[] pob,int elite);
    
    public void iniElite(Cromosoma[] pob,int numElite){
        elite = new Cromosoma[numElite];
        for(int i = 0; i < numElite;i++){
            elite[i] = pob[i].copy();
        }
    }
    
    public void elitismoMinimizacion(Cromosoma[] pob,int numElite){
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = maximosElite(pob, numElite);
        int j = 0;
        for(int i :posPeores){
            if(elite[j].getAptitud() < pob[i].getAptitud()){
               pob[i] = elite[j].copy();
            }
            j++;
        }
        posElites = minimosElite(pob, numElite);
        j = 0;
        for(int i :posElites){
            if(elite[j].getAptitud() > pob[i].getAptitud()){
                elite[j] = pob[i].copy();
            }
            j++;
        }
    }
    
    public void elitismoMaximizacion(Cromosoma[] pob,int numElite){
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = minimosElite(pob, numElite);
        int j = 0;
        for(int i :posPeores){
            if(elite[j].getAptitud() > pob[i].getAptitud()){
                pob[i] = elite[j].copy();
            }
            j++;
        }
        posElites = maximosElite(pob, numElite);
        j = 0;
        for(int i :posElites){
            if(elite[j].getAptitud() < pob[i].getAptitud()){
                elite[j] = pob[i].copy();
            }
            j++;
        }
    }
    

    
    private ArrayList<Integer>  minimosElite(Cromosoma[] pob,int numElemElite){
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Double> apt = new ArrayList<>();
        
        for(int j = 0; j < numElemElite; j++){
            pos.add(j);
            apt.add(elite[j].getAptitud());
        }
        
        if(numElemElite > pob.length)
            return pos;
        
        boolean salir = false;
        for(int i = 0; i < pob.length; i++){
            for(int j = 0; j < numElemElite && !salir;j++){
                if(apt.get(j) > pob[i].getAptitud()){
                    apt.add(j, pob[i].getAptitud());
                    pos.add(j,i);
                    apt.remove(numElemElite);
                    pos.remove(numElemElite);
                    salir = true;
                }
            }
            salir = false;
        }
        return pos;
    }
    
    
    private ArrayList<Integer>  maximosElite(Cromosoma[] pob,int numElemElite){
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Double> apt = new ArrayList<>();
        
        for(int j = 0; j < numElemElite; j++){
            pos.add(j);
            apt.add(elite[j].getAptitud());
        }
        
        if(numElemElite > pob.length)
            return pos;
        boolean salir = false;
        for(int i = 0; i < pob.length; i++){
            for(int j = 0; j < numElemElite && !salir;j++){
                if(apt.get(j) < pob[i].getAptitud()){
                    apt.add(j, pob[i].getAptitud());
                    pos.add(j,i);
                    apt.remove(numElemElite);
                    pos.remove(numElemElite);
                    salir = true;
                }
            }
            salir = false;
        }
        return pos;
    }
    
}
