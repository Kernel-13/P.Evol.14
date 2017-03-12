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
public class ProblemaFuncion1 extends Problema
{
    private double best;
    
    public ProblemaFuncion1(){
        best = 0;
    }
    
    @Override
    public ArrayList<Double> evaluacion(Cromosoma[] pob) {
        double maxApt = 0;  // Se debe calcular fuera, y entrar como parametro de la funcion
        ArrayList<Double> aptitudes = new ArrayList<>();
        double sum = 0;
        for(int i = 0; i < pob.length; i++){
            double apt = aptitudReal(pob[i], maxApt);
            aptitudes.add(apt);
            sum += apt;
            if(apt > best){
                best = apt;
            }
        }
        return aptitudes;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        ArrayList<Boolean> parent1 = ((CromosomaF1)new1).getGenes(); // = pob[pos1].getGenes();
        ArrayList<Boolean> parent2 = ((CromosomaF1)new2).getGenes();  // = pob[pos2].getGenes();
        ArrayList<Boolean> son1 = new ArrayList<>(); 
        ArrayList<Boolean> son2 = new ArrayList<>();
        Random r = new Random();
        int lcrom = 17;        // int lcrom = pob[0].getGenes().length;
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
        
        new1 = new CromosomaF1(son1);
        new2 = new CromosomaF1(son2);
    }

    @Override
    public void mutacion(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();
        CromosomaF1[] pobAux = ((CromosomaF1[])pob);
        CromosomaF1 individuo;
        for (int j=0; j < pobAux.length;j++) {
            individuo = pobAux[j];
            boolean cambio = false;
            ArrayList<Boolean> original = individuo.getGenes();
            ArrayList<Boolean> mutado = new ArrayList<Boolean>();
           
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
                CromosomaF1 nuevo = new CromosomaF1(mutado);
                nuevo = individuo;
                pob[j] = nuevo;
            }
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public double getBest(){
        return best;
    }
    
}
