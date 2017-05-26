/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Random;
import util.DatosGrafica;
import util.Functions;
import util.Nodo;
import util.TipoCruce;
import util.TipoFuncion;
import util.TipoInicializar;
import util.TipoMutacion;
import util.TipoOperacion;
import util.TipoSeleccion;

/**
 *
 * @author josemanuel
 */
public class AlgoritmoGenetico {

    public static double ELITE = 0.05;
    public static int NGRUPOS = 3;
    
    private int tamPoblacion;
    private int iteraciones;
    private double probCruces;
    private double probMutacion;
    private double precision;
    private Seleccion seleccion;
    private Problema problema;
    private Cromosoma[] pob;
    private int nvars;
    private boolean elitismo;
    private int tamElite;
    private boolean inv;
    private int maxDepth;
    private boolean ifs;
    private TipoInicializar ini;
    private ArrayList<ArrayList<Boolean>> casos;
    Factoria f;

    public AlgoritmoGenetico(TipoFuncion funcion, int tampob, int iteraciones,
            double probCruces, double probMutacion, int maxDepth,
            TipoSeleccion tSeleccion, int nvars, boolean elitismo, TipoCruce c, TipoMutacion m, boolean inv
            ,boolean ifs) {
        this.ifs = ifs;
        casos = new ArrayList<>();
        Functions.generadorCasos(casos, (int) (nvars + pow(2,nvars)));
        tamPoblacion = tampob;
        this.iteraciones = iteraciones;
        this.probCruces = probCruces / 100;
        this.probMutacion = probMutacion / 100;
        this.elitismo = elitismo;
        f = new Factoria(funcion, tSeleccion);
        seleccion = f.factoriaSeleccion();
        tamElite = calcularTamElite();
        this.inv = inv;
        problema = new Problema(casos,c, m,(int) (nvars + pow(2,nvars)),nvars);
        this.nvars = nvars;
        this.maxDepth = maxDepth;
        ini = TipoInicializar.CRECIENTE;
    }

    /**
     * funcion que calcula el tamanio que debe tener la elite teniendo en cuenta
     * el porcentaje de la poblacion que forma parte de la elite
     *
     * @return
     */
    private int calcularTamElite() {
        double aux = tamPoblacion * ELITE;
        int ret = (int) aux;
        if (ret <= 0 || ret > tamPoblacion) {
            elitismo = false;
        }
        return ret;
    }

    public String[] generarTerminales(){
        String[] ret = new String[this.nvars+(int)pow(2,nvars)];
        for(int i = 0; i < nvars;i++){
            ret[i] = "a"+i;
        }
        for(int i = nvars;i<this.nvars+(int)pow(2,nvars);i++){
            ret[i] = "d"+(i-nvars);
        }
        return ret;
    }
    
    /**
     * algoritmo genetico
     *
     * @param semilla
     * @return
     */
    public DatosGrafica ejecuta(int semilla) {
        String aux[] = generarTerminales();
        ArrayList<Double> best = new ArrayList<>();
        ArrayList<Double> bestPob = new ArrayList<>();
        ArrayList<Double> media = new ArrayList<>();
        Cromosoma mejorPob;
        pobInicial(semilla);
        if (this.elitismo) {
            problema.iniElite(pob, tamElite);
            problema.elitismo(pob, tamElite);
        }
        mejorPob = problema.evaluacion(pob,nvars);
        bestPob.add(mejorPob.getAptitud());
        best.add(problema.getBest().getAptitud());
        media.add(problema.media(tamPoblacion));
        for (int i = 1; i < iteraciones; i++) {
            pob = seleccion.selecciona(pob,nvars,casos);
            /*for(Cromosoma c: pob)
                System.out.println(c.getArbol().toString(aux));
            System.out.println("--------------");*/
            problema.reproduccion(pob, probCruces);
            problema.mutacion(pob, probMutacion);
            problema.bloating(pob, 2,this.ini,this.maxDepth,this.ifs,this.nvars);
            if (this.elitismo) {
                problema.elitismo(pob, tamElite);
            }
            mejorPob = problema.evaluacion(pob,nvars);
            bestPob.add(mejorPob.getAptitud());
            best.add(problema.getBest().getAptitud());
            media.add(problema.media(tamPoblacion));
           /* for(Cromosoma c: pob){
                System.out.println(c.getArbol().toString(aux));
            }
            System.out.println("--------------");*/
        }
        System.out.println(problema.getBest().toString());
        return new DatosGrafica(best, iteraciones, bestPob, media, problema.getBest(),aux);
    }

    /**
     * funcion que genera la población inicial
     *
     * @param semilla
     */
    private void pobInicial(int semilla) {
        Random r = new Random(semilla);
        pob = new Cromosoma[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) {
            pob[i] = new Cromosoma(this.problema.inicializa(ini,this.maxDepth,ifs),casos,nvars);
        }
    }

    
    
    public int factorial(int numero) {
        if (numero == 0) {
            return 1;
        } else {
            return numero * factorial(numero - 1);
        }
    }
}
