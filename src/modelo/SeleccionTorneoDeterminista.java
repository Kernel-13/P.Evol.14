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
public class SeleccionTorneoDeterminista extends Seleccion {
    public static int TAMALEATORIOS = 2;
    @Override
    Cromosoma[] selecciona(Cromosoma[] pob, double[] puntAcomulada) {
       Cromosoma[] ret = new Cromosoma[pob.length];
       Random r = new Random();
       int[] aleatorios = new int[TAMALEATORIOS];
       int escogido;
       for(int i=0; i < pob.length;i++){
            for(int j=0; j < TAMALEATORIOS;j++)
                aleatorios[j] = (int)(r.nextDouble()*pob.length);
            escogido = mejor(puntAcomulada,aleatorios);
            ret[i] = pob[escogido];
       }
       return ret;
    }
    
    
    private int mejor(double[] punt , int[] ale){
        int mejor = ale[0];
        double mejorPuntuacion=punt[ale[0]]; 
        for(int i=1; i < TAMALEATORIOS;i++){
            if(punt[ale[i]] > mejorPuntuacion){
                mejorPuntuacion = punt[ale[i]];
                mejor = ale[i];
            }
        }
        return mejor;
    }
}
