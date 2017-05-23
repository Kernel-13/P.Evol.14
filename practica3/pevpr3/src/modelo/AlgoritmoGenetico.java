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
    private ArrayList<ArrayList<Boolean>> casos;
    Factoria f;

    public AlgoritmoGenetico(TipoFuncion funcion, int tampob, int iteraciones,
            double probCruces, double probMutacion, double precision,
            TipoSeleccion tSeleccion, int nvars, boolean elitismo, TipoCruce c, TipoMutacion m, boolean inv
            ,int maxProf,boolean ifs) {
        this.ifs = ifs;
        casos = new ArrayList<>();
        Functions.generadorCasos(casos, (int) (nvars + pow(2,nvars)));
        tamPoblacion = tampob;
        this.iteraciones = iteraciones;
        this.probCruces = probCruces / 100;
        this.probMutacion = probMutacion / 100;
        this.precision = precision;
        this.elitismo = elitismo;
        f = new Factoria(funcion, tSeleccion);
        seleccion = f.factoriaSeleccion();
        tamElite = calcularTamElite();
        this.inv = inv;
        problema = new Problema(casos,c, m,(int) (nvars + pow(2,nvars)),nvars);
        this.nvars = nvars;
        maxDepth = maxProf;
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

    /**
     * algoritmo genetico
     *
     * @param semilla
     * @return
     */
    public DatosGrafica ejecuta(int semilla) {
        String aux[] = {"a0","a1","do","d1","d2","d3"};
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
            for(Cromosoma c: pob){
                System.out.println(c.getArbol().toString(aux));
            }
            System.out.println("--------------");
            pob = seleccion.selecciona(pob);
            problema.reproduccion(pob, probCruces);
            problema.mutacion(pob, probMutacion);
            if (this.elitismo) {
                problema.elitismo(pob, tamElite);
            }
            mejorPob = problema.evaluacion(pob,nvars);
            bestPob.add(mejorPob.getAptitud());
            best.add(problema.getBest().getAptitud());
            media.add(problema.media(tamPoblacion));
            for(Cromosoma c: pob){
                System.out.println(c.getArbol().toString(aux));
            }
            System.gc();
        }
        System.out.println(problema.getBest().toString());
        return new DatosGrafica(best, iteraciones, bestPob, media, problema.getBest());
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
            pob[i] = new Cromosoma(inicializa(TipoInicializar.CRECIENTE),casos,nvars);
        }

    }

    private TipoOperacion randomFunction() {
        int pick = new Random().nextInt(TipoOperacion.values().length);
        return TipoOperacion.values()[pick];
    }

    private Nodo inicializa(TipoInicializar ini){
        switch(ini){
            case COMPLETO:
                return inicioCompleto(0);
            case RANDH:
                return inicioRampedAndHalf(false,NGRUPOS,0);
            case CRECIENTE:
                return inicioCreciente(0);
            default:
                return inicioCompleto(0);
        }
    }
    
    private Nodo inicioCompleto(int depth){
        Nodo node = null;
        if(depth < this.maxDepth){
            TipoOperacion f = randomFunction();
            while(f == TipoOperacion.HOJA || (f==TipoOperacion.IF && !ifs)){
                f = randomFunction();
            }
            switch(f) {
                case IF:
                    node = new Nodo(f,0,inicioCompleto(depth + 1), inicioCompleto(depth + 1), inicioCompleto(depth + 1));
                    break;
                case AND:
                    node = new Nodo(f,0,inicioCompleto(depth + 1), inicioCompleto(depth + 1), null);
                    break;
                case OR:
                    node = new Nodo(f,0,inicioCompleto(depth + 1), inicioCompleto(depth + 1), null);
                    break;
                case NOT:
                    node = new Nodo(f,0,inicioCompleto(depth + 1), null, null);
                    break;
                default:
                    break;
            }
            // pilla funcion aleatoria
        } else {
            Random r = new Random();
            TipoOperacion f = TipoOperacion.HOJA;
            node = new Nodo(f, r.nextInt((int) (nvars + pow(2,nvars))), null, null, null);
        }
        return node;
    }
    
    private Nodo inicioCreciente(int depth) {
        Random r = new Random();
        TipoOperacion op;
        op = randomFunction();
        while((op==TipoOperacion.IF && !ifs)){
            op = randomFunction();
        }
        if(depth >= this.maxDepth){
            op = TipoOperacion.HOJA;
        }
        switch (op) {
            case AND:
                return new Nodo(op, 0, inicioCreciente(depth + 1), inicioCreciente(depth + 1), null);
            case OR:
                return new Nodo(op, 0, inicioCreciente(depth + 1), inicioCreciente(depth + 1), null);
            case NOT:
                return new Nodo(op, 0, inicioCreciente(depth + 1), null, null);
            case IF:
                return new Nodo(op, 0, inicioCreciente(depth + 1), inicioCreciente(depth + 1), inicioCreciente(depth + 1));
            default:
                return new Nodo(op, r.nextInt((int) (nvars + pow(2,nvars))), null, null, null);
        }
    }
    
    private Nodo inicioRampedAndHalf(boolean modo,int tamGrupo,int depth){
        Nodo node = null;
        boolean nuevoModo = modo;
        if(depth%tamGrupo == 0)
            nuevoModo = !modo;
        if(depth < this.maxDepth && nuevoModo){
            TipoOperacion f = randomFunction();
            while(f == TipoOperacion.HOJA || (f==TipoOperacion.IF && !ifs)){
                f = randomFunction();
            }
            switch(f) {
                case IF:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1));
                    break;
                case AND:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), null);
                    break;
                case OR:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), null);
                    break;
                case NOT:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), null, null);
                    break;
                default:
                    break;
            }
            // pilla funcion aleatoria
        } else {
            Random r = new Random();
            TipoOperacion f = TipoOperacion.HOJA;
            node = new Nodo(f, r.nextInt((int) (nvars + pow(2,nvars))), null, null, null);
        }
        return node;
    }
    
    public int factorial(int numero) {
        if (numero == 0) {
            return 1;
        } else {
            return numero * factorial(numero - 1);
        }
    }
}
