/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;

/**
 *
 * @author Ederson
 */
public abstract class Problema {
    
    protected Cromosoma best;
    protected double sumaPob;
    
    protected Cromosoma[] elite;
    
    /**
     * calcula el fitness desplazado, las puntuaciones 
     * y las puntuaciones acomuladas
     * @param pob
     * @return 
     */
    public Cromosoma evaluacion(Cromosoma[] pob) {
        Cromosoma bestPobActual = pob[0];
        double maxApt = 0, puntAcomulada = 0;  // Se debe calcular fuera, y entrar como parametro de la funcion
        double sum = 0;
        double sumDefault = 0;
        maxApt = pob[0].getAptitud(); 
        for(int j = 1; j < pob.length;j++){
            if(pob[j].getAptitud() > maxApt)
                maxApt = pob[j].getAptitud();
        }
        for(int i = 0; i < pob.length; i++){
            pob[i].setAptitudDesplazada(aptitudDesplazada(pob[i], maxApt));
            sumDefault += pob[i].getAptitud();
            sum += pob[i].getAptitudDesplazada();
            if(pob[i].getAptitudDesplazada() > bestPobActual.getAptitudDesplazada()){
                bestPobActual = pob[i];
            }
        }
        sumaPob = sumDefault;
        for(int k=0; k < pob.length;k++){
            pob[k].setPuntuacion(sum);
            pob[k].setPuntAcomulada(pob[k].getPuntuacion()+puntAcomulada);
            puntAcomulada += pob[k].getPuntuacion();
        }
        if(best == null){
            best = bestPobActual.copy();
        }
        if(bestPobActual.getAptitud()<best.getAptitud())
            best = bestPobActual.copy();
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return bestPobActual;
    }	// Debe devolver la mejor aptitud, y una array de las aptitudes
    
    /**
     * calcula el valor del fitness desplazado
     * @param individuo
     * @param maxApt
     * @return 
     */
    abstract double aptitudDesplazada(Cromosoma individuo, double maxApt);	// Debe devolver la aptitud tras aplicar desplazamiento
    // El parametro maxApt debera ser calculado con anterioridad, y quiza tenerlo como un atributo de AG
    // Quiza es mejor hacerla una funcion privada, ya que solo es utilizada por evaluacion
	
    /**
     * calcula la media de la poblacion
     * @param tam
     * @return 
     */
    public double media(int tam){
        return sumaPob/tam;
    }
    /**
     * devuelve una nueva poblacion con cromosomas 
     * cruzados. 
     * @param pob
     * @param probCruce
     * @return 
     */
    public abstract Cromosoma[] reproduccion(Cromosoma[] pob, double probCruce); 

    /**
     * Cruza 2 cromosomas - obtenemos 2 hijos
     * Los cromosomas padre se pasan por referencia
     * y se convierten en los hijos posteriormente.
     * @param pob
     * @param son1
     * @param son2 
     */
    protected void cruce(Cromosoma new1, Cromosoma new2) {  
        Object[] parent1 = new1.toArray(); // = pob[pos1].getGenes();
        Object[] parent2 = new2.toArray();  // = pob[pos2].getGenes();
        Object[] son1 = new Object[new1.getTamanio()]; 
        Object[] son2 = new Object[new2.getTamanio()];
        Random r = new Random();
        int corte = r.nextInt(new1.getTamanio()-1)+1;
        
        for (int i = 0; i < new1.getTamanio(); i++){
            if(i > corte){
                son1[i] = parent2[2];
                son2[i] = parent1[1];
            } else {
                son1[i] = parent1[2];
                son2[i] = parent2[1];
            }
        }
        new1.setGenes(son1);
        new2.setGenes(son2);
    }
    
    
    /**
     * Cruza 2 cromosomas - obtenemos 2 hijos
     * El cruce aplicado en este caso es por ruleta
     * @param pob
     * @param son1
     * @param son2 
     */
    protected void cruceRuleta(Cromosoma new1, Cromosoma new2) {  
        Object[] parent1 = new1.toArray(); // = pob[pos1].getGenes();
        Object[] parent2 = new2.toArray();  // = pob[pos2].getGenes();
        Object[] son1 = new Object[new1.getTamanio()]; 
        Object[] son2 = new Object[new2.getTamanio()];
        Random r = new Random();
        int corte = r.nextInt(new1.getTamanio()-1)+1;
        
        for (int i = 0; i < new1.getTamanio(); i++){
            if(i > corte){
                son1[i] = parent2[2];
                son2[i] = parent1[1];
            } else {
                son1[i] = parent1[2];
                son2[i] = parent2[1];
            }
        }
        new1.setGenes(son1);
        new2.setGenes(son2);
    }

/**
     * Cruza 2 cromosomas - obtenemos 2 hijos
     * El cruce aplicado en este caso es por ruleta
     * @param pob
     * @param son1
     * @param son2 
     */
    protected void cruceCiclos(Cromosoma new1, Cromosoma new2) {  
        Object[] parent1 = new1.toArray(); // = pob[pos1].getGenes();
        Object[] parent2 = new2.toArray();  // = pob[pos2].getGenes();
        Object[] son1 = new Object[new1.getTamanio()]; 
        Object[] son2 = new Object[new2.getTamanio()];
        Random r = new Random();
        int corte = r.nextInt(new1.getTamanio()-1)+1;
        
        for (int i = 0; i < new1.getTamanio(); i++){
            if(i > corte){
                son1[i] = parent2[2];
                son2[i] = parent1[1];
            } else {
                son1[i] = parent1[2];
                son2[i] = parent2[1];
            }
        }
        new1.setGenes(son1);
        new2.setGenes(son2);
    }


    
    /**
     * Recorre la pob. y cada individuo, 
     * y segun la probabilidad cambia un gen o no
     * @param pob
     * @param probMutacion 
     */
    public abstract void mutacion(Cromosoma[] pob, double probMutacion);
    
    /**
     * devuelve el mejor de la poblacion
     * @return 
     */
    public Cromosoma getBest(){
        return best.copy();
    }
    
    /**
     * llama a la funcion elitismo maximizacion o minimizacion segun
     * el problema
     * @param pob
     * @param elite 
     */
    public abstract void elitismo(Cromosoma[] pob,int elite);
    
    /**
     * inicializa el valor de la elite
     * @param pob
     * @param numElite 
     */
    public void iniElite(Cromosoma[] pob,int numElite){
        elite = new Cromosoma[numElite];
        for(int i = 0; i < numElite;i++){
            elite[i] = pob[i].copy();
        }
    }
    
    /**
     * actualiza la elite en problemas de minimizacion
     * @param pob
     * @param numElite 
     */
    public void elitismoMinimizacion(Cromosoma[] pob,int numElite){
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = maximosPoblacion(pob, numElite);
        int j = 0;
        for(int i :posPeores){
            if(elite[j].getAptitud() < pob[i].getAptitud()){
               pob[i] = elite[j].copy();
            }
            j++;
        }
        posElites = minimosPoblacion(pob, numElite);
        j = 0;
        for(int i :posElites){
            if(elite[j].getAptitud() > pob[i].getAptitud()){
                elite[j] = pob[i].copy();
            }
            j++;
        }
    }
    
    
    /**
     * actualiza la elite en problemas de maximizacion
     * @param pob
     * @param numElite 
     */
    public void elitismoMaximizacion(Cromosoma[] pob,int numElite){
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = minimosPoblacion(pob, numElite);
        int j = 0;
        for(int i :posPeores){
            if(elite[j].getAptitud() > pob[i].getAptitud()){
                pob[i] = elite[j].copy();
            }
            j++;
        }
        posElites = maximosPoblacion(pob, numElite);
        j = 0;
        for(int i :posElites){
            if(elite[j].getAptitud() < pob[i].getAptitud()){
                elite[j] = pob[i].copy();
            }
            j++;
        }
    }
    

    /**
     * devuelve un array con las posiciones de los minimos de
     * la poblacion
     * @param pob
     * @param numElemElite
     * @return 
     */
    private ArrayList<Integer>  minimosPoblacion(Cromosoma[] pob,int numElemElite){
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
    
    
    
    /**
     * devuelve un array con las posiciones de los maximos de
     * la poblacion
     * @param pob
     * @param numElemElite
     * @return 
     */
    private ArrayList<Integer>  maximosPoblacion(Cromosoma[] pob,int numElemElite){
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
