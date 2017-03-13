/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import util.Functions;

/**
 *
 * @author Ederson
 */
public abstract class Problema {
     
    public abstract Cromosoma evaluacion(Cromosoma[] pob);	// Debe devolver la mejor aptitud, y una array de las aptitudes
	
    public abstract void init(int tamCromosoma);
    
    abstract double aptitudReal(Cromosoma individuo, double maxApt);	// Debe devolver la aptitud tras aplicar desplazamiento
    // El parametro maxApt debera ser calculado con anterioridad, y quiza tenerlo como un atributo de AG
    // Quiza es mejor hacerla una funcion privada, ya que solo es utilizada por evaluacion
	
    abstract double media(int tam);
    
    public abstract Cromosoma[] reproduccion(Cromosoma[] pob, double probCruce); // Debe devolver una nueva poblacion con cromosomas 
    // cruzados. 
    // 

    abstract void cruce(Cromosoma[] pob, Cromosoma son1, Cromosoma son2); // Cruza 2 cromosomas - obtenemos 2 hijos
    // Tambien creo que deberia ser privada

    public abstract void mutacion(Cromosoma[] pob, double probMutacion);	// Recorre la pob. y cada individuo, y segun la probabilidad cambia un gen o no
    // 
    public abstract Cromosoma getBest();
    
    public abstract int longCromosoma(double p);
    
    
}
