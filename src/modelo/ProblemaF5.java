/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Ederson
 */
public class ProblemaF5 extends Problema{
    
    public ProblemaF5(){
        sumaPob = 0;
        best = null;
    }
    
    public double media(int tam){
        return sumaPob/tam;
    }
    
    @Override
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
            pob[i].setAptitudDesplazada(aptitudReal(pob[i], maxApt)); 
            sum += pob[i].getAptitudReal();
            sumDefault += pob[i].getAptitud();
            if(pob[i].getAptitudReal() > bestPobActual.getAptitudReal()){
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
    }

    @Override
    public double aptitudReal(Cromosoma individuo, double maxApt) {
        return maxApt - individuo.getAptitud();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cromosoma[] reproduccion(Cromosoma[] pob, double probCruce) {
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
             cruce(pob, pob[elegidos.get(i)], pob[elegidos.get(i+1)]);
        }
        
        /*
         Debemos sustituir los padres con los hijos
         Creo que seria ideal cambiar el array de Cromosomas por un ArraList de Cromosomas
         --> pob.set(elegidos.get(i), new1);
         --> pob.set(elegidos.get(i+1), new2);
         */
        return pob;
    }

    @Override
    void cruce(Cromosoma[] pob, Cromosoma new1, Cromosoma new2) {  
        ArrayList<Boolean> parent1 = ((CromosomaF5)new1).getGenes(); // = pob[pos1].getGenes();
        ArrayList<Boolean> parent2 = ((CromosomaF5)new2).getGenes();  // = pob[pos2].getGenes();
        ArrayList<Boolean> son1 = new ArrayList<>(); 
        ArrayList<Boolean> son2 = new ArrayList<>();
        Random r = new Random();
        int lcrom = ((CromosomaF5)pob[0]).getGenes().size();
        int corte = r.nextInt(lcrom-1)+1;
        
        for (int i = 0; i < lcrom; i++){
            if(i > corte){
                son1.add(parent2.get(i));
                son2.add(parent1.get(i));
            } else {
                son1.add(parent1.get(i));
                son2.add(parent2.get(i));
            }
        }
        
        new1 = new CromosomaF5(son1);
        new2 = new CromosomaF5(son2);
    }

    @Override
    public void mutacion(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();
        CromosomaF5[] pobAux = new CromosomaF5[pob.length];
        for(int i = 0; i < pob.length;i++){
            pobAux[i] = (CromosomaF5)pob[i];
        }
        CromosomaF5 individuo;
        for (int j=0; j < pobAux.length;j++) {
            individuo = pobAux[j];
            boolean cambio = false;
            ArrayList<Boolean> original = individuo.getGenes();
            ArrayList<Boolean> mutado = new ArrayList<>();
           
            original = individuo.getGenes();
            for (int i = 0; i < original.size(); i++){
                if (r.nextDouble() < probMutacion){
                    mutado.add(!original.get(i));  
                    cambio = true;
                } else {
                    mutado.add(original.get(i));
                }
            }
            
            if(cambio){
                CromosomaF5 nuevo = new CromosomaF5(mutado);
                pob[j] = nuevo;
            }
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public Cromosoma getBest() {
        return best.copy();
    }

    @Override
    public void elitismo(Cromosoma[] pob,int numElite) {
        super.elitismoMinimizacion(pob, numElite);
    }

}
