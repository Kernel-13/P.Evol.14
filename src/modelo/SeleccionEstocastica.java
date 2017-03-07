/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;
/**
 *
 * @author Jose Manuel Perez , Ederson Funes
 */
public class SeleccionEstocastica extends Seleccion {
    public SeleccionEstocastica(){}
    
    @Override
    Cromosoma[] selecciona(Cromosoma[] pob, double[] puntAc) {
       int j;
       boolean salir;
       Cromosoma[] ret = new Cromosoma[pob.length];
       for(int i=0; i < pob.length;i++){
           double valorAle = 1/pob.length;
           j = 0;
           salir = false;
           while(!salir && j < puntAc.length){
               if(puntAc[j] > valorAle){
                   salir = true;
               }
               valorAle += valorAle;
               j++;
           }
           ret[i] = pob[j];
       }
       return ret;
    }
}
