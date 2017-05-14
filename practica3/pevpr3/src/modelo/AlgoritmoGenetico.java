/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.DatosGrafica;
import util.Nodo;
import util.TipoCruce;
import util.TipoFuncion;
import util.TipoMutacion;
import util.TipoOperacion;
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
    private boolean inv;
    private int maxDepth;
    private int numTerminales;
    Factoria f;

    public AlgoritmoGenetico(TipoFuncion funcion, int tampob, int iteraciones,
            double probCruces, double probMutacion, double precision,
            TipoSeleccion tSeleccion, int nvars, boolean elitismo, int[][] f2,
            int[][] d, TipoCruce c, TipoMutacion m, boolean inv) {

        tamPoblacion = tampob;
        this.iteraciones = iteraciones;
        this.probCruces = probCruces / 100;
        this.probMutacion = probMutacion / 100;
        this.precision = precision;
        this.nvars = nvars;
        this.elitismo = elitismo;
        f = new Factoria(funcion, tSeleccion);
        seleccion = f.factoriaSeleccion();
        //problema = f.factoriaProblema(nvars);
        tamElite = calcularTamElite();
        this.f2 = f2;
        this.d = d;
        this.inv = inv;
        problema = new Problema(c, m, f2, d);
        numTerminales = 6;
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
        ArrayList<Double> best = new ArrayList<>();
        ArrayList<Double> bestPob = new ArrayList<>();
        ArrayList<Double> media = new ArrayList<>();
        CromosomaAsigC mejorPob;
        pobInicial(semilla);
        if (this.elitismo) {
            problema.iniElite(pob, tamElite);
            problema.elitismo(pob, tamElite);
        }
        mejorPob = problema.evaluacion(pob);
        bestPob.add(mejorPob.getAptitud());
        best.add(problema.getBest().getAptitud());
        media.add(problema.media(tamPoblacion));
        for (int i = 1; i < iteraciones; i++) {
            pob = seleccion.selecciona(pob);
            problema.reproduccion(pob, probCruces);
            problema.mutacion(pob, probMutacion);
            if (this.elitismo) {
                problema.elitismo(pob, tamElite);
            }
            mejorPob = problema.evaluacion(pob);
            bestPob.add(mejorPob.getAptitud());
            best.add(problema.getBest().getAptitud());
            media.add(problema.media(tamPoblacion));
            //System.out.println(problema.getBest().toString());
        }
//        for(CromosomaAsigC c: pob){
//            System.out.println(c.toString());
//        }
        System.out.println(problema.getBest().toString());
        return new DatosGrafica(best, iteraciones, bestPob, media, problema.getBest());
    }

    /**
     * funcion que genera la población inicial
     *
     * @param semilla
     */
    private void pobInicial(int semilla) {
        boolean repetidos = false;
        Random r = new Random(semilla);
        pob = new CromosomaAsigC[tamPoblacion];
        if (factorial(nvars) > pob.length) {
            repetidos = true;
        }

        for (int i = 0; i < tamPoblacion; i++) {
            pob[i] = new CromosomaAsigC(nvars);
            pob[i].inicializa(r, f2, d);
            if (containsLista(pob[i], i - 1)) {
                pob[i].inicializa(r, f2, d);
            }
        }

    }

    private TipoOperacion randomFunction() {
        int pick = new Random().nextInt(TipoOperacion.values().length);
        return TipoOperacion.values()[pick];
    }

    private Nodo inicioCompleto(int depth){
        Nodo node = null;
        if(depth < this.maxDepth){
            TipoOperacion f = randomFunction();
            while(f == TipoOperacion.HOJA){
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
            node = new Nodo(f, r.nextInt(numTerminales), null, null, null);
        }
        return node;
    }
    
    private Nodo inicioCreciente(int depth) {
        Random r = new Random();
        TipoOperacion op;
        op = randomFunction();
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
                return new Nodo(op, r.nextInt(numTerminales), null, null, null);
        }
    }
    
    
    /*private Nodo inicioRampedAndHalf(boolean modo,int tamGrupo,int depth){
        if(modo && depth%tamGrupo == 0)
            modo = !modo;
        if(modo)
            return inicioCreciente(depth);
        else
            return inicioCompleto(depth);
    }*/

    private boolean containsLista(CromosomaAsigC elem, int n) {
        boolean ret = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < elem.getTamanio(); j++) {
                if (pob[i].getGenes().get(j) != elem.getGenes().get(j)) {
                    ret = false;
                }
            }
            if (ret) {
                return true;
            }
        }
        return false;
    }

    public int factorial(int numero) {
        if (numero == 0) {
            return 1;
        } else {
            return numero * factorial(numero - 1);
        }
    }
}
