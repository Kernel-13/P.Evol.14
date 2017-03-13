/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;

/**
 *
 * @author Jose Manuel Perez Zamorano
 *         Ederson Funes Castillo
 */
public class DatosGrafica {
    public ArrayList<Double> mejoresDeTodas;
    public ArrayList<Double> mejoresIteracion;
    public ArrayList<Double> mediaPorIteracion;
    public DatosGrafica(ArrayList<Double> mejoresTodo
            ,ArrayList<Double> mejoresIt, ArrayList<Double> mediaPobIt){
        mejoresDeTodas = mejoresTodo;
        mejoresIteracion = mejoresIt;
        mediaPorIteracion = mediaPobIt; 
    }
    
    public String toString(){
        return this.mejoresDeTodas.toString()+" "+this.mejoresIteracion.toString()
                +" "+this.mediaPorIteracion.toString();
    }
    
    
}
