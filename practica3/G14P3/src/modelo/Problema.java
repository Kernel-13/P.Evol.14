/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Random;
import static modelo.AlgoritmoGenetico.NGRUPOS;
import util.Nodo;
import util.TipoCruce;
import util.TipoInicializar;
import util.TipoMutacion;
import util.TipoOperacion;

/**
 *
 * @author Ederson
 */
public class Problema {

    protected int[][] f;
    protected int[][] d;
    protected Cromosoma best;
    protected double sumaPob;
    protected TipoCruce cruce;
    protected TipoMutacion mut;
    protected int numTerminales;
    protected int nvars;
    protected Cromosoma[] elite;
    private ArrayList<ArrayList<Boolean>> casos;

    public Problema(ArrayList<ArrayList<Boolean>> casos, TipoCruce c, TipoMutacion m, int nTerminales, int nvars) {
        sumaPob = 0;
        best = null;
        this.numTerminales = nTerminales;
        this.cruce = c;
        this.mut = m;
        this.casos = casos;
        this.nvars = nvars;
    }

    /**
     * calcula el fitness desplazado, las puntuaciones y las puntuaciones
     * acomuladas
     *
     * @param pob
     * @return
     */
    public Cromosoma evaluacion(Cromosoma[] pob, int nIn) {
        Cromosoma bestPobActual = pob[0];       // Cromosoma que se toma como referencia
        double maxApt = 0, puntAcomulada = 0;   // Se debe calcular fuera, y entrar como parametro de la funcion
        double sum = 0;
        double sumDefault = 0;
        maxApt = pob[0].getAptitud();
        for (int j = 1; j < pob.length; j++) {
            if (pob[j].getAptitud() > maxApt) {
                maxApt = pob[j].getAptitud();
            }
        }
        for (int i = 0; i < pob.length; i++) {
            sumDefault += pob[i].getAptitud();
            sum += pob[i].getAptitud();
            if (pob[i].getAptitud() > bestPobActual.getAptitud()) {
                bestPobActual = pob[i];
            }
        }
        sumaPob = sumDefault;
        for (int k = 0; k < pob.length; k++) {
            pob[k].setPuntuacion(sum);
            pob[k].setPuntAcomulada(pob[k].getPuntuacion() + puntAcomulada);
            puntAcomulada += pob[k].getPuntuacion();
        }
        if (best == null) {
            best = bestPobActual.copy(nvars, casos);
        }
        if (bestPobActual.getAptitud() > best.getAptitud()) {
            best = bestPobActual.copy(nvars, casos);
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return bestPobActual.copy(nIn, casos);
    }	// Debe devolver la mejor aptitud, y una array de las aptitudes

    /**
     * calcula la media de la poblacion
     *
     * @param tam
     * @return
     */
    public double media(int tam) {
        return sumaPob / tam;
    }

    /**
     * devuelve una nueva poblacion con cromosomas cruzados.
     *
     * @param pob
     * @param probCruce
     * @return
     */
    public Cromosoma[] reproduccion(Cromosoma[] pob, double probCruce) {
        Random r = new Random();
        ArrayList<Integer> elegidos = new ArrayList<>();
        for (int i = 0; i < pob.length; i++) {
            if (r.nextDouble() < probCruce) {
                elegidos.add(i);
            }
        }

        if (elegidos.size() % 2 != 0) {
            elegidos.remove(elegidos.size() - 1);
        }

        for (int i = 0; i < elegidos.size(); i += 2) {
            cruce(pob[elegidos.get(i)], pob[elegidos.get(i + 1)]);
        }
        return pob;
    }

     /**
     * Cruza 2 cromosomas - obtenemos 2 hijos Los cromosomas padre se pasan por
     * referencia y se convierten en los hijos posteriormente.
     *
     * @param new1
     * @param new2
     */
    protected void cruce(Cromosoma new1, Cromosoma new2){
        switch(this.cruce){
            case FUNCION:
                cruceFuncion(new1,new2);
                break;
            case TERMINAL:
                cruceTerminal(new1,new2);
                break;
            case NORMAL:
                cruceNormal(new1,new2);
                break;
            case PROPIO:
                cruceFuncion(new1,new2);
                cruceTerminal(new1,new2);
                break;
            default:
                cruceNormal(new1,new2);
                break;
        }
    }
    
    /**
     * Cruza dos ramas aleatorias del arbol, pueden ser tanto 
     * terminales como funciones.
     * @param new1
     * @param new2 
     */
    protected void cruceNormal(Cromosoma new1, Cromosoma new2) {
        ArrayList<Integer> traza1 = new ArrayList<>();
        ArrayList<Integer> traza2 = new ArrayList<>();
        Nodo aux1;
        Nodo aux2;
        Random r = new Random();
        boolean escogido = r.nextBoolean();
        boolean escogido2 = r.nextBoolean();
        //cojo un terminal o funcion de alguno de los dos
        if (escogido) {
            aux1 = new1.getArbol().terminalRandom(r, traza1).copy();
        } else {
            if (new1.getArbol().getFuncion() == TipoOperacion.HOJA) {
                aux1 = new1.getArbol().terminalRandom(r, traza1);
            } else {
                aux1 = new1.getArbol().funcionRandom(r, traza1);
            }
        }

        if (escogido2) {
            aux2 = new2.getArbol().terminalRandom(r, traza2);
        } else {
            if (new2.getArbol().getFuncion() == TipoOperacion.HOJA) {
                aux2 = new2.getArbol().terminalRandom(r, traza2);
            } else {
                aux2 = new2.getArbol().funcionRandom(r, traza2);
            }
        }
        if(aux2 != null && aux1 != null){
            new1.getArbol().setNodo(aux2.copy(), traza1, 0);
            new2.getArbol().setNodo(aux1.copy(), traza2, 0);
            new1.calculoAptitud(casos, nvars);
            new2.calculoAptitud(casos, nvars);
        }
    }

    /**
     * Cruza dos terminales aleatorios de cada arbol
     * 
     * @param new1
     * @param new2
     */
    protected void cruceTerminal(Cromosoma new1, Cromosoma new2) {
        ArrayList<Integer> traza1 = new ArrayList<>();
        ArrayList<Integer> traza2 = new ArrayList<>();
        Nodo aux1;
        Nodo aux2;
        Random r = new Random();
        boolean escogido = r.nextBoolean();
        boolean escogido2 = r.nextBoolean();
        //cojo un terminal o funcion de alguno de los dos

        aux1 = new1.getArbol().terminalRandom(r, traza1).copy();
        aux2 = new2.getArbol().terminalRandom(r, traza2).copy();
        int prof1 = new1.getArbol().profundidad();
        int prof2 = new2.getArbol().profundidad();

        if (prof1 != aux1.profundidad() && prof2 != aux2.profundidad()) {
            new1.getArbol().setNodo(aux2, traza1, 0);
            new2.getArbol().setNodo(aux1, traza2, 0);
            new1.calculoAptitud(casos, nvars);
            new2.calculoAptitud(casos, nvars);
        }
    }

    /**
     * Cruza dos funciones aleatorias del arbol 
     *
     * @param new1
     * @param new2
     */
    protected void cruceFuncion(Cromosoma new1, Cromosoma new2) {
        ArrayList<Integer> traza1 = new ArrayList<>();
        ArrayList<Integer> traza2 = new ArrayList<>();
        Nodo aux1;
        Nodo aux2;
        Random r = new Random();
        boolean escogido = r.nextBoolean();
        boolean escogido2 = r.nextBoolean();
        //cojo un terminal o funcion de alguno de los dos

        aux1 = new1.getArbol().funcionRandom(r, traza1);
        aux2 = new2.getArbol().funcionRandom(r, traza2);

        if (aux1 != null && aux2 != null) {
            new1.getArbol().setNodo(aux2.copy(), traza1, 0);
            new2.getArbol().setNodo(aux1.copy(), traza2, 0);
            new1.calculoAptitud(casos, nvars);
            new2.calculoAptitud(casos, nvars);
        }
    }

    /**
     * Recorre la pob. y cada individuo, y segun la probabilidad cambia un gen o
     * no
     *
     * @param pob
     * @param probMutacion
     */
    public void mutacion(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();
        for (int i = 0; i < pob.length; i++) {
            if (r.nextDouble() < probMutacion) {
                switch (mut) {
                    case FUNC:
                        mutaFuncion(r, pob[i]);
                        break;
                    case TER:
                        mutaTerminal(r, pob[i]);
                        break;
                    case PERM:
                        mutaPermutacion(r, pob[i]);
                        break;
                    default:
                        mutaFuncion(r, pob[i]);
                        break;
                }
            }
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Permuta dos ramas del arbol.
     * 
     *    x
     * /    \
     * y     z
     * 
     *    x
     * /     \
     * z      y
     * @param r
     * @param x 
     */
    private void mutaPermutacion(Random r, Cromosoma x) {
        x.getArbol().permuta(r);
        x.calculoAptitud(casos, nvars);
    }

    
    /**
     * Cambia un terminal seleccionado de manera
     * aleatoria por otro escogido de manera aleatoria
     * 
     * @param r
     * @param x 
     */
    private void mutaTerminal(Random r, Cromosoma x) {
        ArrayList<Integer> traza = new ArrayList<>();
        x.getArbol().terminalRandom(r, traza).mutaTerminal(r, nvars + (int) pow(2, nvars) - 1);
        x.calculoAptitud(casos, nvars);
    }

    
    /**
     * cambia de manera aleatoria una funcion
     * por otra funcion escogida de manera aleatoria
     * (las dos funciones tienen que tener la misma aridad)
     * @param r
     * @param x 
     */
    private void mutaFuncion(Random r, Cromosoma x) {
        if (x.getArbol().getFuncion() != TipoOperacion.HOJA && x.getArbol().hasMutableFunction()) {
            x.getArbol().mutaFuncion(r);
            x.calculoAptitud(casos, nvars);
        }
    }

    /**
     * devuelve el mejor de la poblacion
     *
     * @return
     */
    public Cromosoma getBest() {
        return best.copy(nvars, casos);
    }

    /**
     * llama a la funcion elitismo maximizacion o minimizacion segun el problema
     *
     * @param pob
     * @param elite
     */
    public void elitismo(Cromosoma[] pob, int elite) {
        elitismoMaximizacion(pob, elite);
    }

    /**
     * inicializa el valor de la elite
     *
     * @param pob
     * @param numElite
     */
    public void iniElite(Cromosoma[] pob, int numElite) {
        elite = new Cromosoma[numElite];
        for (int i = 0; i < numElite; i++) {
            elite[i] = pob[i].copy(nvars, casos);
        }
    }

    /**
     * actualiza la elite en problemas de minimizacion
     *
     * @param pob
     * @param numElite
     */
    public void elitismoMinimizacion(Cromosoma[] pob, int numElite) {
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = maximosPoblacion(pob, numElite);
        int j = 0;
        for (int i : posPeores) {
            if (elite[j].getAptitud() < pob[i].getAptitud()) {
                pob[i] = elite[j].copy(nvars, casos);
            }
            j++;
        }
        posElites = minimosPoblacion(pob, numElite);
        j = 0;
        for (int i : posElites) {
            if (elite[j].getAptitud() > pob[i].getAptitud()) {
                elite[j] = pob[i].copy(nvars, casos);
            }
            j++;
        }
    }

    /**
     * actualiza la elite en problemas de maximizacion
     *
     * @param pob
     * @param numElite
     */
    public void elitismoMaximizacion(Cromosoma[] pob, int numElite) {
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = minimosPoblacion(pob, numElite);
        int j = 0;
        for (int i : posPeores) {
            if (elite[j].getAptitud() > pob[i].getAptitud()) {
                pob[i] = elite[j].copy(nvars, casos);
            }
            j++;
        }
        posElites = maximosPoblacion(pob, numElite);
        j = 0;
        for (int i : posElites) {
            if (elite[j].getAptitud() < pob[i].getAptitud()) {
                elite[j] = pob[i].copy(nvars, casos);
            }
            j++;
        }
    }

    /**
     * devuelve un array con las posiciones de los minimos de la poblacion
     *
     * @param pob
     * @param numElemElite
     * @return
     */
    private ArrayList<Integer> minimosPoblacion(Cromosoma[] pob, int numElemElite) {
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Double> apt = new ArrayList<>();

        for (int j = 0; j < numElemElite; j++) {
            pos.add(j);
            apt.add(elite[j].getAptitud());
        }

        if (numElemElite > pob.length) {
            return pos;
        }

        boolean salir = false;
        for (int i = 0; i < pob.length; i++) {
            for (int j = 0; j < numElemElite && !salir; j++) {
                if (apt.get(j) > pob[i].getAptitud()) {
                    apt.add(j, pob[i].getAptitud());
                    pos.add(j, i);
                    apt.remove(numElemElite);
                    pos.remove(numElemElite);
                    salir = true;
                }
            }
            salir = false;
        }
        return pos;
    }

    /**
     * devuelve un array con las posiciones de los maximos de la poblacion
     *
     * @param pob
     * @param numElemElite
     * @return
     */
    private ArrayList<Integer> maximosPoblacion(Cromosoma[] pob, int numElemElite) {
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Double> apt = new ArrayList<>();

        for (int j = 0; j < numElemElite; j++) {
            pos.add(j);
            apt.add(elite[j].getAptitud());
        }

        if (numElemElite > pob.length) {
            return pos;
        }
        boolean salir = false;
        for (int i = 0; i < pob.length; i++) {
            for (int j = 0; j < numElemElite && !salir; j++) {
                if (apt.get(j) < pob[i].getAptitud()) {
                    apt.add(j, pob[i].getAptitud());
                    pos.add(j, i);
                    apt.remove(numElemElite);
                    pos.remove(numElemElite);
                    salir = true;
                }
            }
            salir = false;
        }
        return pos;
    }

    /**
     * Funcion que penalizara aquellos cromosomas cuyo arbol tenga una
     * profundidad superior a la media de profundidades de toda la poblacion,
     * asignandole un nuevo fitness muy inferior
     *
     * @param pob Es la poblacion a examinar
     * @param n
     * @param t
     * @param max
     * @param ifs
     * @param nvars
     */
    public void bloating(Cromosoma[] pob, int n,TipoInicializar t,int max, boolean ifs,int nvars) {
        double aver_long = medialongitud(pob);
        Random r = new Random();
        for (int i = 0; i < pob.length; i++) {
            double depth = pob[i].getArbol().profundidad();
            double rand = r.nextDouble();
            double div = 1/(double)n;
            if (depth > aver_long && rand < div ) {
                pob[i].arbol = inicializa(t,max,ifs);
                pob[i].calculoAptitud(casos, nvars);
               // System.out.println("********** BLOATING **********");
            }
        }
    }

    private TipoOperacion randomFunction() {
        int pick = new Random().nextInt(TipoOperacion.values().length);
        return TipoOperacion.values()[pick];
    }

    /**
     * inicializa la poblacion
     * @param ini
     * @param max
     * @param ifs
     * @return 
     */
    public Nodo inicializa(TipoInicializar ini,int max,boolean ifs){
        switch(ini){
            case COMPLETO:
                return inicioCompleto(0,max,ifs);
            case RANDH:
                return inicioRampedAndHalf(false,NGRUPOS,0,max,ifs);
            case CRECIENTE:
                return inicioCreciente(0,max,ifs);
            default:
                return inicioCompleto(0,max,ifs);
        }
    }
    
    private Nodo inicioCompleto(int depth,int max,boolean ifs){
        Nodo node = null;
        if(depth < max){
            TipoOperacion f = randomFunction();
            while(f == TipoOperacion.HOJA || (f==TipoOperacion.IF && !ifs)){
                f = randomFunction();
            }
            switch(f) {
                case IF:
                    node = new Nodo(f,0,inicioCompleto(depth + 1,max,ifs), inicioCompleto(depth + 1,max,ifs), 
                                        inicioCompleto(depth + 1,max,ifs));
                    break;
                case AND:
                    node = new Nodo(f,0,inicioCompleto(depth + 1,max,ifs), inicioCompleto(depth + 1,max,ifs), null);
                    break;
                case OR:
                    node = new Nodo(f,0,inicioCompleto(depth + 1,max,ifs), inicioCompleto(depth + 1,max,ifs), null);
                    break;
                case NOT:
                    node = new Nodo(f,0,inicioCompleto(depth + 1,max,ifs), null, null);
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
    
    private Nodo inicioCreciente(int depth,int max,boolean ifs) {
        Random r = new Random();
        TipoOperacion op;
        op = randomFunction();
        while((op==TipoOperacion.IF && !ifs) || (op==TipoOperacion.HOJA && depth<3)){
            op = randomFunction();
        }
        if(depth >= max){
            op = TipoOperacion.HOJA;
        }
        switch (op) {
            case AND:
                return new Nodo(op, 0, inicioCreciente(depth + 1,max,ifs), inicioCreciente(depth + 1,max,ifs), null);
            case OR:
                return new Nodo(op, 0, inicioCreciente(depth + 1,max,ifs), inicioCreciente(depth + 1,max,ifs), null);
            case NOT:
                return new Nodo(op, 0, inicioCreciente(depth + 1,max,ifs), null, null);
            case IF:
                return new Nodo(op, 0, inicioCreciente(depth + 1,max,ifs), inicioCreciente(depth + 1,max,ifs), 
                                       inicioCreciente(depth + 1,max,ifs));
            default:
                return new Nodo(op, r.nextInt((int) (nvars + pow(2,nvars))), null, null, null);
        }
    }
    
    private Nodo inicioRampedAndHalf(boolean modo,int tamGrupo,int depth,int max,boolean ifs){
        Nodo node = null;
        boolean nuevoModo = modo;
        if(depth%tamGrupo == 0)
            nuevoModo = !modo;
        if(depth < max && nuevoModo){
            TipoOperacion f = randomFunction();
            while(f == TipoOperacion.HOJA || (f==TipoOperacion.IF && !ifs)){
                f = randomFunction();
            }
            switch(f) {
                case IF:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1,max,ifs), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1,max,ifs),
                                        inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1,max,ifs));
                    break;
                case AND:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1,max,ifs), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1,max,ifs), null);
                    break;
                case OR:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1,max,ifs), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1,max,ifs), null);
                    break;
                case NOT:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1,max,ifs), null, null);
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
    
    /**
     * Devuelve la media de profundidad de los arboles de la poblacion
     * @param pob
     * @return 
     */
    private double medialongitud(Cromosoma[] pob) {
        double suma = 0;
        for (int i = 0; i < pob.length; i++) {
            suma += pob[i].getArbol().profundidad();
        }
        return suma / pob.length;
    }
}
