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
 * @author josemanuel
 */
public class SeleccionTorneoProbabilista extends Seleccion {
    public static int TAMALEATORIOS = 2;
    public static double P = 0.5;
    
    @Override
    CromosomaAsigC[] selecciona(CromosomaAsigC[] pob) {
       CromosomaAsigC[] ret = new CromosomaAsigC[pob.length];
       Random r = new Random();
       int[] aleatorios = new int[TAMALEATORIOS];
       int escogido;
       for(int i=0; i < pob.length;i++){
            for(int j=0; j < TAMALEATORIOS;j++)
                aleatorios[j] = (int)(r.nextDouble()*pob.length);
            escogido = mejorPeor(pob,aleatorios,r);
            ret[i] = pob[escogido];
       }
       return ret;
    }
    
    
    private int mejorPeor(CromosomaAsigC[] pob , int[] ale , Random r){
        int mejor = ale[0], peor = ale[0];
        double mejorPuntuacion=pob[ale[0]].getPuntuacion(), peorPuntuacion=pob[ale[0]].getPuntuacion(); 
        for(int i=1; i < TAMALEATORIOS;i++){
            if(pob[ale[i]].getPuntuacion() > mejorPuntuacion){
                mejorPuntuacion = pob[ale[i]].getPuntuacion();
                mejor = ale[i];
            }
            if(pob[ale[i]].getPuntuacion() < peorPuntuacion){
                peorPuntuacion = pob[ale[i]].getPuntuacion();
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
