/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import util.DatosGrafica;
import util.TipoFuncion;
import util.TipoSeleccion;

/**
 *
 * @author josemanuel
 */
public class AlgoritmoGenetico {
    private int tamPoblacion;
    private int iteraciones;
    private double probCruces;
    private double probMutacion;
    private double precision;
    private Seleccion seleccion;
    private Problema problema;
    private Cromosoma[] pob;
    Factoria f;
    
    public AlgoritmoGenetico(TipoFuncion funcion, int tampob, int iteraciones,
            double probCruces, double probMutacion, double precision,
            TipoSeleccion tSeleccion){
        
        tamPoblacion = tampob;
        this.iteraciones = iteraciones;
        this.probCruces = probCruces/100;
        this.probMutacion = probMutacion/100;
        this.precision = precision;
        f = new Factoria(funcion,tSeleccion);
        seleccion = f.factoriaSeleccion();
        problema = f.factoriaProblema();
    }
    
    public DatosGrafica ejecuta(){
        ArrayList<Double> best = new ArrayList<>();
        ArrayList<Double> bestPob = new ArrayList<>();
        ArrayList<Double> media = new ArrayList<>();
        int tamCromosoma = problema.longCromosoma(precision);
        problema.init(tamCromosoma);
        Cromosoma mejorPob;
        pobInicial(tamCromosoma);
        mejorPob = problema.evaluacion(pob);
        for(int i=0; i < iteraciones;i++){
            pob = seleccion.selecciona(pob);
            problema.reproduccion(pob, probCruces);
            problema.mutacion(pob, probMutacion);
            mejorPob = problema.evaluacion(pob);
            bestPob.add(mejorPob.getAptitud());
            best.add(problema.getBest().getAptitud());
            media.add(problema.media(tamPoblacion));
        }
        return new DatosGrafica(best,iteraciones,bestPob,media);
    }
    
    
    
    public void pobInicial(int tamCromosoma){
        pob = new Cromosoma[tamPoblacion];
        for(int i = 0; i < tamPoblacion;i++){
            pob[i] = f.factoriaCromosoma(tamCromosoma);
            pob[i].inicializa();
        }
    }
}
