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
public class Problema {

    protected int[][]f;
    protected int[][]d;
    protected CromosomaAsigC best;
    protected double sumaPob;
    protected TipoCruce cruce;
    
    protected CromosomaAsigC[] elite;
    
    
    public Problema(TipoCruce c,int[][] f,int[][] d){
        sumaPob = 0;
        best = null;
        this.f = f;
        this.d = d;
        this.cruce = c;
    }
    /**
     * calcula el fitness desplazado, las puntuaciones 
     * y las puntuaciones acomuladas
     * @param pob
     * @return 
     */
    public CromosomaAsigC evaluacion(CromosomaAsigC[] pob) {
        CromosomaAsigC bestPobActual = pob[0];
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
            best = bestPobActual.copy2();
        }
        if(bestPobActual.getAptitud()<best.getAptitud())
            best = bestPobActual.copy2();
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return bestPobActual;
    }	// Debe devolver la mejor aptitud, y una array de las aptitudes
    
    /**
     * calcula el valor del fitness desplazado
     * @param individuo
     * @param maxApt
     * @return 
     */
    public double aptitudDesplazada(Cromosoma individuo, double maxApt) {
        return maxApt - individuo.getAptitud();
    }

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
    public CromosomaAsigC[] reproduccion(CromosomaAsigC[] pob, double probCruce) {
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
         cruce(pob[elegidos.get(i)], pob[elegidos.get(i+1)]);
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
    protected void cruce(CromosomaAsigC new1, CromosomaAsigC new2) {  
        switch(cruce){
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
    private void cruceDeUnPunto(CromosomaAsigC new1, CromosomaAsigC new2) {  
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
        new1.setGenes(son1,f,d);
        new2.setGenes(son2,f,d);
    }

    /**
     * Cruza 2 cromosomas - obtenemos 2 hijos
     * El cruce aplicado en este caso es por ruleta
     * @param pob
     * @param son1
     * @param son2 
     */
    private void cruceCiclos(CromosomaAsigC new1, CromosomaAsigC new2) {  
        int[] parent1 = Functions.toArrayInt(new1.toArray()); // = pob[pos1].getGenes();
        int[] parent2 = Functions.toArrayInt(new2.toArray());   // = pob[pos2].getGenes();
        int[] son1 = new int[new1.getTamanio()]; 
        int[] son2 = new int[new2.getTamanio()];
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
    
    protected void crucePMX(CromosomaAsigC new1, CromosomaAsigC new2) {  
        int [] parent1 = Functions.toArrayInt(new1.toArray()); 
        int [] parent2 = Functions.toArrayInt(new2.toArray()); 
        ArrayList<Integer> segmento1 = new ArrayList<>();
        ArrayList<Integer> segmento2 = new ArrayList<>();
        int[] son1 = new int[new1.getTamanio()]; 
        int[] son2 = new int[new2.getTamanio()];
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

        protected void cruceOX(CromosomaAsigC new1, CromosomaAsigC new2) {  
        int[] parent1 = Functions.toArrayInt(new1.toArray()); // = pob[pos1].getGenes();
        int[] parent2 = Functions.toArrayInt(new2.toArray());   // = pob[pos2].getGenes();    
        ArrayList<Integer> segmento1 = new ArrayList<>();
        ArrayList<Integer> segmento2 = new ArrayList<>();
        int[] son1 = new int[new1.getTamanio()]; 
        int[] son2 = new int[new2.getTamanio()];
        Random r = new Random();
        int puntoUno = r.nextInt(new1.getTamanio()-1)+1;
        int puntoDos = r.nextInt(new1.getTamanio()-1)+1; 

        ArrayList<Integer> alreadyIn1 = new ArrayList<>();
        ArrayList<Integer> alreadyIn2 = new ArrayList<>();
        
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
                pos1.remove(pos1.indexOf(i));
                pos2.remove(pos2.indexOf(i));
                alreadyIn1.add(parent2[i]);
                alreadyIn2.add(parent1[i]);
            }


        // Dejamos los numeros originales que no generan conflicto (Primer Hijo)
            int ii = puntoDos, jj = puntoDos;
            while(!pos1.isEmpty()){
                if(ii == parent1.length){
                    ii = 0;
                }
                if(jj == parent1.length){
                    jj = 0;
                }

            if(pos1.contains(ii)){              // Si la posicion ii esta disponible
                int val = parent1[jj];
                if(alreadyIn1.contains(val)){
                    jj++;
                } else {
                    son1[ii] = val;
                    pos1.remove(pos1.indexOf(ii));
                    alreadyIn1.add(val);
                    ii++;
                }
            } else {
                ii++;
            }            
        }
        
        // Dejamos los numeros originales que no generan conflicto (Segundo Hijo)
        ii = puntoDos;
        jj = puntoDos;
        while(!pos2.isEmpty()){
            if(ii == parent2.length){
                ii = 0;
            }
            if(jj == parent2.length){
                jj = 0;
            }
            
            if(pos2.contains(ii)){              // Si la posicion ii esta disponible
                int val = parent2[jj];
                if(alreadyIn2.contains(val)){
                    jj++;
                } else {
                    son2[ii] = val;
                    pos2.remove(pos2.indexOf(ii));
                    alreadyIn2.add(val);
                    ii++;
                }
            } else {
                ii++;
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
    public void mutacion(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();
        CromosomaAsigC[] pobAux = new CromosomaAsigC[pob.length];
        for(int i = 0; i < pob.length;i++){
            pobAux[i] = (CromosomaAsigC)pob[i];
        }
        CromosomaAsigC individuo;
        for (int j=0; j < pobAux.length;j++) {
            individuo = pobAux[j];
            boolean cambio = false;
            ArrayList<Integer> original = individuo.getGenes();
            ArrayList<Integer> mutado = new ArrayList<>();

            original = individuo.getGenes();
            for (int i = 0; i < original.size(); i++){
                if (r.nextDouble() < probMutacion){
                    mutado.add(original.get(i));  
                    cambio = true;
                } else {
                    mutado.add(original.get(i));
                }
            }
            
            if(cambio){
                CromosomaAsigC nuevo = new CromosomaAsigC(mutado);
                pob[j] = nuevo;
            }
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * devuelve el mejor de la poblacion
     * @return 
     */
    public CromosomaAsigC getBest(){
        return best.copy2();
    }
    
    /**
     * llama a la funcion elitismo maximizacion o minimizacion segun
     * el problema
     * @param pob
     * @param elite 
     */
    public void elitismo(CromosomaAsigC[] pob,int elite){
        elitismoMinimizacion(pob,elite);
    }
    
    /**
     * inicializa el valor de la elite
     * @param pob
     * @param numElite 
     */
    public void iniElite(CromosomaAsigC[] pob,int numElite){
        elite = new CromosomaAsigC[numElite];
        for(int i = 0; i < numElite;i++){
            elite[i] = pob[i].copy2();
        }
    }
    
    /**
     * actualiza la elite en problemas de minimizacion
     * @param pob
     * @param numElite 
     */
    public void elitismoMinimizacion(CromosomaAsigC[] pob,int numElite){
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = maximosPoblacion(pob, numElite);
        int j = 0;
        for(int i :posPeores){
            if(elite[j].getAptitud() < pob[i].getAptitud()){
               pob[i] = elite[j].copy2();
           }
           j++;
       }
       posElites = minimosPoblacion(pob, numElite);
       j = 0;
       for(int i :posElites){
        if(elite[j].getAptitud() > pob[i].getAptitud()){
            elite[j] = pob[i].copy2();
        }
        j++;
    }
}

    /**
     * actualiza la elite en problemas de maximizacion
     * @param pob
     * @param numElite 
     */
    public void elitismoMaximizacion(CromosomaAsigC[] pob,int numElite){
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = minimosPoblacion(pob, numElite);
        int j = 0;
        for(int i :posPeores){
            if(elite[j].getAptitud() > pob[i].getAptitud()){
                pob[i] = elite[j].copy2();
            }
            j++;
        }
        posElites = maximosPoblacion(pob, numElite);
        j = 0;
        for(int i :posElites){
            if(elite[j].getAptitud() < pob[i].getAptitud()){
                elite[j] = pob[i].copy2();
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
    private ArrayList<Integer>  minimosPoblacion(CromosomaAsigC[] pob,int numElemElite){
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
    private ArrayList<Integer>  maximosPoblacion(CromosomaAsigC[] pob,int numElemElite){
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
