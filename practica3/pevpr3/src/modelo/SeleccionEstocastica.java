/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Jose Manuel Perez , Ederson Funes
 */
public class SeleccionEstocastica extends Seleccion {
    public SeleccionEstocastica(){}
    
    @Override
    Cromosoma[] selecciona(Cromosoma[] pob) {
       int j;
       boolean salir;
       Cromosoma[] ret = new Cromosoma[pob.length];
       for(int i=0; i < pob.length;i++){
           double valorAle = 1/pob.length;
           j = 0;
           salir = false;
           while(!salir && j < pob.length){
               if(pob[j].getPuntAcomulada() > valorAle){
                   salir = true;
               }
               valorAle += valorAle;
               j++;
           }
           if(j < pob.length)
               ret[i] = pob[j];
           else
               ret[i] = pob[pob.length-1];
       }
       return ret;
    }
}
