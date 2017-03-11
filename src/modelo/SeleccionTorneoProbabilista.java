/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Random;

/**
 *
 * @author josemanuel
 */
public class SeleccionTorneoProbabilista extends Seleccion {
    public static int TAMALEATORIOS = 2;
    public static double P = 0.5;
    
    @Override
    Cromosoma[] selecciona(Cromosoma[] pob, double[] puntAcomulada) {
       Cromosoma[] ret = new Cromosoma[pob.length];
       Random r = new Random();
       int[] aleatorios = new int[TAMALEATORIOS];
       int escogido;
       for(int i=0; i < pob.length;i++){
            for(int j=0; j < TAMALEATORIOS;j++)
                aleatorios[j] = (int)(r.nextDouble()*pob.length);
            escogido = mejorPeor(puntAcomulada,aleatorios,r);
            ret[i] = pob[escogido];
       }
       return ret;
    }
    
    
    private int mejorPeor(double[] punt , int[] ale , Random r){
        int mejor = ale[0], peor = ale[0];
        double mejorPuntuacion=punt[ale[0]], peorPuntuacion=punt[ale[0]]; 
        for(int i=1; i < TAMALEATORIOS;i++){
            if(punt[ale[i]] > mejorPuntuacion){
                mejorPuntuacion = punt[ale[i]];
                mejor = ale[i];
            }
            if(punt[ale[i]] < peorPuntuacion){
                peorPuntuacion = punt[ale[i]];
                peor = ale[i];
            }
        }
        if(r.nextDouble() < 0.5){
            return mejor;
        }else{
            return peor;
        }
    }
}
