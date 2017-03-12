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
    @Override
    ArrayList<Double> evaluacion(Cromosoma[] pob, double best) {
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
    double aptitudReal(Cromosoma individuo, double maxApt) {
        return maxApt - individuo.getAptitud();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    Cromosoma[] reproduccion(Cromosoma[] pob, double probCruce) {
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
        
         for (int i = 0; i < pob.length; i++){
             CromosomaF1 new1 = new CromosomaF1();
             CromosomaF1 new2 = new CromosomaF1();
             cruce(pob, elegidos.get(i), elegidos.get(i+1), new1, new2);
            i++;
        }
        
        /*
         Debemos sustituir los padres con los hijos
         Creo que seria ideal cambiar el array de Cromosomas por un ArraList de Cromosomas
         --> pob.set(elegidos.get(i), new1);
         --> pob.set(elegidos.get(i+1), new2);
         */
         
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void cruce(Cromosoma[] pob, int pos1, int pos2, Cromosoma new1, Cromosoma new2) {  
        ArrayList<Boolean> parent1 = new ArrayList<>(); // = pob[pos1].getGenes();
        ArrayList<Boolean> parent2 = new ArrayList<>(); // = pob[pos2].getGenes();
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void mutacion(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();
        for (Cromosoma individuo : pob) {
            boolean cambio = false;
            // ArrayList<Boolean> original = individuo.getGenes();
            //ArrayList<Boolean> mutado = new ArrayList<Boolean>();
            /*
            original = individuo.genes
            for (int i = 0; i < original.length; i++){
                if ((r.nextDouble()+1) < probMutacion){
                    mutado.add(!original.get(i));
                    cambio = true;
                } else {
                    mutado.add(original.get(i));
                }
            }
            
            if(cambio){
                Cromosoma nuevo = new Cromosoma(mutado);
                individuo = nuevo;
            }
            
            */
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
