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
public class ProblemaF1 extends Problema
{
    
    public ProblemaF1(){
        sumaPob = 0;
        best = null;
    }
    
    
    

    @Override
    public double aptitudDesplazada(Cromosoma individuo, double maxApt) {
        return maxApt - individuo.getAptitud();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void mutacion(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();
        CromosomaF1[] pobAux = new CromosomaF1[pob.length];
        for(int i = 0; i < pob.length;i++){
            pobAux[i] = (CromosomaF1)pob[i];
        }
        CromosomaF1 individuo;
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
                CromosomaF1 nuevo = new CromosomaF1(mutado);
                pob[j] = nuevo;
            }
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
   

    @Override
    public void elitismo(Cromosoma[] pob,int numElite) {
        super.elitismoMinimizacion(pob, numElite);
    }


    
}
