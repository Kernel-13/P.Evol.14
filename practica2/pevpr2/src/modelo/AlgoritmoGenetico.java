/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.DatosGrafica;
import util.TipoCruce;
import util.TipoFuncion;
import util.TipoMutacion;
import util.TipoSeleccion;

/**
 *
 * @author josemanuel
 */
public class AlgoritmoGenetico {
    public static double ELITE = 0.05;
    
    private int tamPoblacion;
    private int iteraciones;
    private double probCruces;
    private double probMutacion;
    private double precision;
    private Seleccion seleccion;
    private Problema problema;
    private CromosomaAsigC[] pob;
    private int nvars;
    private boolean elitismo;
    private int tamElite;
    private int[][] f2;
    private int[][] d;
    
    Factoria f;
    
    public AlgoritmoGenetico(TipoFuncion funcion, int tampob, int iteraciones,
            double probCruces, double probMutacion, double precision,
            TipoSeleccion tSeleccion,int nvars,boolean elitismo,int[][]f2,int[][]d,TipoCruce c,TipoMutacion m){
        
        tamPoblacion = tampob;
        this.iteraciones = iteraciones;
        this.probCruces = probCruces/100;
        this.probMutacion = probMutacion/100;
        this.precision = precision;
        this.nvars = nvars;
        this.elitismo = elitismo;
        f = new Factoria(funcion,tSeleccion);
        seleccion = f.factoriaSeleccion();
        //problema = f.factoriaProblema(nvars);
        tamElite = calcularTamElite();
        this.f2 = f2;
        this.d = d;
        problema = new Problema(c,m,f2,d);
    }
    
    /**
     * funcion que calcula el tamanio que debe tener la elite
     * teniendo en cuenta el porcentaje de la poblacion 
     * que forma parte de la elite
     * @return 
     */
    private int calcularTamElite(){
        double aux = tamPoblacion*ELITE;
        int ret = (int)aux;
        if(ret <= 0 || ret > tamPoblacion){
            elitismo = false;
        }
        return ret;
    }
    
    /**
     * algoritmo genetico
     * @param semilla
     * @return 
     */
    public DatosGrafica ejecuta(int semilla){
        ArrayList<Double> best = new ArrayList<>();
        ArrayList<Double> bestPob = new ArrayList<>();
        ArrayList<Double> media = new ArrayList<>();
        CromosomaAsigC mejorPob;
        pobInicial(semilla);
        if(this.elitismo){
            problema.iniElite(pob, tamElite);
            problema.elitismo(pob, tamElite);
        }
        mejorPob = problema.evaluacion(pob);
        bestPob.add(mejorPob.getAptitud());
        best.add(problema.getBest().getAptitud());
        media.add(problema.media(tamPoblacion));
        for(int i=1; i < iteraciones;i++){
            pob = seleccion.selecciona(pob);
            problema.reproduccion(pob, probCruces);
            problema.mutacion(pob, probMutacion);
            if(this.elitismo)
                problema.elitismo(pob, tamElite);
            mejorPob = problema.evaluacion(pob);
            bestPob.add(mejorPob.getAptitud());
            best.add(problema.getBest().getAptitud());
            media.add(problema.media(tamPoblacion));
            System.out.println(problema.getBest().toString());
        }
        for(CromosomaAsigC c: pob){
            System.out.println(c.toString());
        }
        System.out.println(problema.getBest().toString());
        return new DatosGrafica(best,iteraciones,bestPob,media);
    }
    
    /**
     * funcion que genera la población inicial
     * @param semilla 
     */
    private void pobInicial(int semilla){
        Random r = new Random(semilla);
        pob = new CromosomaAsigC[tamPoblacion];
        for(int i = 0; i < tamPoblacion;i++){
            //pob[i] = f.factoriaCromosoma(precision,nvars); find
            pob[i] = new CromosomaAsigC(nvars);
            pob[i].inicializa(r,f2,d);
        }
    }
}
