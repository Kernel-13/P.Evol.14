/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Ederson
 */
public abstract class Problema {
     
    abstract ArrayList<Double> evaluacion(Cromosoma[] pob, double best);	// Debe devolver la mejor aptitud, y una array de las aptitudes
	
    abstract double aptitudReal(Cromosoma individuo, double maxApt);	// Debe devolver la aptitud tras aplicar desplazamiento
    // El parametro maxApt debera ser calculado con anterioridad, y quiza tenerlo como un atributo de AG
    // Quiza es mejor hacerla una funcion privada, ya que solo es utilizada por evaluacion
	
    abstract Cromosoma[] reproduccion(Cromosoma[] pob, double probCruce); // Debe devolver una nueva poblacion con cromosomas 
    // cruzados. 
    // 

    abstract void cruce(Cromosoma[] pob, int pos1, int pos2, Cromosoma son1, Cromosoma son2); // Cruza 2 cromosomas - obtenemos 2 hijos
    // Tambien creo que deberia ser privada

    abstract void mutacion(Cromosoma[] pob, double probMutacion);	// Recorre la pob. y cada individuo, y segun la probabilidad cambia un gen o no
    // 
}
