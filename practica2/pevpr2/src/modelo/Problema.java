/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;
import util.TipoCruce;

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
    public Cromosoma[] reproduccion(TipoCruce c,Cromosoma[] pob, double probCruce) {
        Random r = new Random();
	ArrayList<Integer> elegidos = new ArrayList<>(); 
        for (int i = 0; i < pob.length; i++){
            if (r.nextDouble() < probCruce){
                elegidos.add(i);
            }
        }
        
        if(elegidos.size()%2 != 0){
            elegidos.remove(elegidos.size() - 1);
        }
        
         for (int i = 0; i < elegidos.size(); i+=2){ 
             cruce(c,pob[elegidos.get(i)], pob[elegidos.get(i+1)]);
        }
        return pob;
    }

    /**
     * Cruza 2 cromosomas - obtenemos 2 hijos
     * Los cromosomas padre se pasan por referencia
     * y se convierten en los hijos posteriormente.
     * @param pob
     * @param son1
     * @param son2 
     */
    protected void cruce(TipoCruce c,Cromosoma new1, Cromosoma new2) {  
        switch(c){
            case CICLOS:
                cruceCiclos(new1,new2);
                break;
            default:
                cruceDeUnPunto(new1,new2);
        }
    }
    
    
    /**
     * Cruza 2 cromosomas - obtenemos 2 hijos
     * El cruce aplicado en este caso es por ruleta
     * @param pob
     * @param son1
     * @param son2 
     */
    private void cruceDeUnPunto(Cromosoma new1, Cromosoma new2) {  
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
    private void cruceCiclos(Cromosoma new1, Cromosoma new2) {  
        Integer[] parent1 = (Integer[])new1.toArray(); // = pob[pos1].getGenes();
        Integer[] parent2 = (Integer[])new2.toArray();  // = pob[pos2].getGenes();
        Object[] son1 = new Object[new1.getTamanio()]; 
        Object[] son2 = new Object[new2.getTamanio()];
        ArrayList<Integer> array = new ArrayList<>();
        for(int i=0; i < parent1.length; i++)
            array.add(i);
        int i = 0;
        while(i!=parent1[0]){
            i = parent2[i];
            son1[i] = parent1[i];
            son2[i] = parent2[i];
            array.remove(i);
        }
        for(Integer x:array){
            son1[x] = parent2[x];
            son2[x] = parent1[x];
        }
        new1.setGenes(son1);
        new2.setGenes(son2);
    }
    
    protected void crucePMX(Cromosoma new1, Cromosoma new2) {  
        Integer[] parent1 = (Integer[])new1.toArray();
        Integer[] parent2 = (Integer[])new2.toArray();
        ArrayList<Integer> segmento1 = new ArrayList<>();
        ArrayList<Integer> segmento2 = new ArrayList<>();
        Object[] son1 = new Object[new1.getTamanio()]; 
        Object[] son2 = new Object[new2.getTamanio()];
        Random r = new Random();
        int puntoUno = r.nextInt(new1.getTamanio()-1)+1;
        int puntoDos = r.nextInt(new1.getTamanio()-1)+1;    
        
        ArrayList<Integer> pos1 = new ArrayList<>();
        ArrayList<Integer> pos2 = new ArrayList<>();
        for (int i = 0; i < new1.getTamanio(); i++){ pos1.add(i); pos2.add(i); }
        
        // Repetimos hasta conseguir 2 puntos separados
        while(puntoUno >= puntoDos){
            puntoUno = r.nextInt(new1.getTamanio()-1)+1;
            puntoDos = r.nextInt(new1.getTamanio()-1)+1;
        }
        
        // Hacemos el intercambio de ambos segmentos
        for (int i = puntoUno; i < puntoDos; i++) {
            son1[i] = parent2[i];
            son2[i] = parent1[i];
            segmento1.add(parent2[i]);
            segmento2.add(parent1[i]);
            pos1.remove(i);
            pos2.remove(i);
        }
        
        // Dejamos los numeros originales que no generan conflicto (Derecha)
        for (int i = puntoDos; i < new1.getTamanio(); i++) {
            if(!segmento1.contains(parent1[i])){
                son1[i] = parent1[i];
                pos1.remove(i);
            }
            if(!segmento2.contains(parent2[i])){
                son2[i] = parent2[i];
                pos2.remove(i);
            }
        }
        
        // Dejamos los numeros originales que no generan conflicto (Izquierda)       
        for (int i = 0; i < puntoUno; i++) {
            if(!segmento1.contains(parent1[i])){
                son1[i] = parent1[i];
                pos1.remove(i);
            }
            if(!segmento2.contains(parent2[i])){
                son2[i] = parent2[i];
                pos2.remove(i);
            }
        }
        
        // Rellenamos los que faltan
        for (int i = 0; i < pos1.size(); i++){
            son1[pos1.get(i)] = segmento1.get(i);
        }
        for (int i = 0; i < pos2.size(); i++){
            son2[pos2.get(i)] = segmento2.get(i);
        }
        new1.setGenes(son1);
        new2.setGenes(son2);
    }
    
    protected void cruceOX(Cromosoma new1, Cromosoma new2) {  
        Integer[] parent1 = (Integer[])new1.toArray();
        Integer[] parent2 = (Integer[])new2.toArray();
        ArrayList<Integer> segmento1 = new ArrayList<>();
        ArrayList<Integer> segmento2 = new ArrayList<>();
        Object[] son1 = new Object[new1.getTamanio()]; 
        Object[] son2 = new Object[new2.getTamanio()];
        Random r = new Random();
        int puntoUno = r.nextInt(new1.getTamanio()-1)+1;
        int puntoDos = r.nextInt(new1.getTamanio()-1)+1;    
        
        ArrayList<Integer> pos1 = new ArrayList<>();
        ArrayList<Integer> pos2 = new ArrayList<>();
        for (int i = 0; i < new1.getTamanio(); i++){ pos1.add(i); pos2.add(i); }
        
        // Repetimos hasta conseguir 2 puntos separados
        while(puntoUno >= puntoDos){
            puntoUno = r.nextInt(new1.getTamanio()-1)+1;
            puntoDos = r.nextInt(new1.getTamanio()-1)+1;
        }
        
        // Hacemos el intercambio de ambos segmentos
        for (int i = puntoUno; i < puntoDos; i++) {
            son1[i] = parent2[i];
            son2[i] = parent1[i];
            segmento1.add(parent2[i]);
            segmento2.add(parent1[i]);
            pos1.remove(i);
            pos2.remove(i);
        }
        
        // Dejamos los numeros originales que no generan conflicto (Derecha)
        for (int i = puntoDos; i < new1.getTamanio(); i++) {
            if(!segmento1.contains(parent1[i])){
                son1[i] = parent1[i];
                pos1.remove(i);
            }
            if(!segmento2.contains(parent2[i])){
                son2[i] = parent2[i];
                pos2.remove(i);
            }
        }
        
        // Dejamos los numeros originales que no generan conflicto (Izquierda)       
        for (int i = 0; i < puntoUno; i++) {
            if(!segmento1.contains(parent1[i])){
                son1[i] = parent1[i];
                pos1.remove(i);
            }
            if(!segmento2.contains(parent2[i])){
                son2[i] = parent2[i];
                pos2.remove(i);
            }
        }
        
        // Rellenamos los que faltan
        for (int i = 0; i < pos1.size(); i++){
            son1[pos1.get(i)] = segmento1.get(i);
        }
        for (int i = 0; i < pos2.size(); i++){
            son2[pos2.get(i)] = segmento2.get(i);
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
