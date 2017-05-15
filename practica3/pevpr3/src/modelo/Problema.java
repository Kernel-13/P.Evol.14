/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;
import util.Nodo;
import util.TipoCruce;
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
    protected Cromosoma[] elite;

    public Problema(TipoCruce c,TipoMutacion m,int nTerminales) {
        sumaPob = 0;
        best = null;
        this.numTerminales = nTerminales;
        this.cruce = c;
        this.mut = m;
    }
   
    /**
     * calcula el fitness desplazado, las puntuaciones y las puntuaciones
     * acomuladas
     *
     * @param pob
     * @return
     */
    public Cromosoma evaluacion(Cromosoma[] pob) {
        Cromosoma bestPobActual = pob[0];
        double maxApt = 0, puntAcomulada = 0;  // Se debe calcular fuera, y entrar como parametro de la funcion
        double sum = 0;
        double sumDefault = 0;
        maxApt = pob[0].getAptitud();
        for (int j = 1; j < pob.length; j++) {
            if (pob[j].getAptitud() > maxApt) {
                maxApt = pob[j].getAptitud();
            }
        }
        for (int i = 0; i < pob.length; i++) {
            pob[i].setAptitudDesplazada(aptitudDesplazada(pob[i], maxApt));
            sumDefault += pob[i].getAptitud();
            sum += pob[i].getAptitudDesplazada();
            if (pob[i].getAptitudDesplazada() > bestPobActual.getAptitudDesplazada()) {
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
            best = bestPobActual.copy();
        }
        if (bestPobActual.getAptitud() < best.getAptitud()) {
            best = bestPobActual.copy();
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return bestPobActual;
    }	// Debe devolver la mejor aptitud, y una array de las aptitudes

    /**
     * calcula el valor del fitness desplazado
     *
     * @param individuo
     * @param maxApt
     * @return
     */
    public double aptitudDesplazada(Cromosoma individuo, double maxApt) {
        return maxApt - individuo.getAptitud();
    }

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
    protected void cruce(Cromosoma new1, Cromosoma new2) {
        ArrayList<Integer> traza1 = new ArrayList<>();
        ArrayList<Integer> traza2 = new ArrayList<>();
        Nodo aux1;
        Nodo aux2;
        Random r = new Random();
        boolean escogido = r.nextBoolean();
        boolean escogido2 = r.nextBoolean();
        //cojo un terminal o funcion de alguno de los dos
        if(escogido)
            aux1 = new1.getArbol().terminalRandom(r, traza1).copy();
        else
            aux1 = new1.getArbol().funcionRandom(r, traza1).copy();
        
        if(escogido2)
            aux2 = new2.getArbol().terminalRandom(r, traza2).copy();
        else 
            aux2 = new2.getArbol().funcionRandom(r, traza2).copy();
        
        new1.getArbol().setNodo(aux2, traza1);
        new2.getArbol().setNodo(aux1, traza2);        
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
        for(Cromosoma c: pob){
            if(r.nextDouble() < probMutacion)
                switch(mut){
                    case FUNC:
                        mutaFuncion(r,c);
                        break;
                    case TER:
                        mutaTerminal(r,c);
                        break;
                    case PERM:
                        mutaPermutacion(r,c);
                        break;
                    default:
                        mutaFuncion(r,c);
                        break;
                }
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
    
    private void mutaPermutacion(Random r, Cromosoma x){
        ArrayList<Integer> traza1 = new ArrayList<>();
        ArrayList<Integer> traza2 = new ArrayList<>();
        Nodo aux1;
        Nodo aux2;
        boolean escogido = r.nextBoolean();
        boolean escogido2 = r.nextBoolean();
        //cojo un terminal o funcion de alguno de los dos
        if(escogido)
            aux1 = x.getArbol().terminalRandom(r, traza1).copy();
        else
            aux1 = x.getArbol().funcionRandom(r, traza1).copy();
        
        if(escogido2)
            aux2 = x.getArbol().terminalRandom(r, traza2).copy();
        else 
            aux2 = x.getArbol().funcionRandom(r, traza2).copy();
        
        x.getArbol().setNodo(aux1, traza2);
        x.getArbol().setNodo(aux2, traza1);
    }
    
    private void mutaTerminal(Random r,Cromosoma x){
        ArrayList<Integer> traza = new ArrayList<>();
        x.getArbol().terminalRandom(r, traza).setTerminal(r.nextInt(numTerminales));
    }
    
   
    private void mutaFuncion(Random r,Cromosoma x){
        ArrayList<Integer> traza = new ArrayList<>();
        Nodo aux = x.getArbol().funcionRandom(r, traza);
        if(null != aux.getFuncion())switch (aux.getFuncion()) {
            case OR:
                aux.setFuncion(TipoOperacion.AND);
                break;
            case AND:
                aux.setFuncion(TipoOperacion.OR);
                break;
            case NOT:
                Nodo copia = aux.getIzq(); 
                aux = copia;
                break;
            default:
                break;
        }
        x.getArbol().setNodo(aux, traza);
    }
    
    private boolean esValido(ArrayList<Integer> crom) {
        boolean exit = true;
        for (int i = 0; i < crom.size() && exit; i++) {
            if (!crom.contains(i)) {
                exit = false;
            }
        }
        return exit;
    }

    /**
     * devuelve el mejor de la poblacion
     *
     * @return
     */
    public Cromosoma getBest() {
        return best.copy();
    }

    /**
     * llama a la funcion elitismo maximizacion o minimizacion segun el problema
     *
     * @param pob
     * @param elite
     */
    public void elitismo(Cromosoma[] pob, int elite) {
        elitismoMinimizacion(pob, elite);
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
            elite[i] = pob[i].copy();
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
                pob[i] = elite[j].copy();
            }
            j++;
        }
        posElites = minimosPoblacion(pob, numElite);
        j = 0;
        for (int i : posElites) {
            if (elite[j].getAptitud() > pob[i].getAptitud()) {
                elite[j] = pob[i].copy();
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
                pob[i] = elite[j].copy();
            }
            j++;
        }
        posElites = maximosPoblacion(pob, numElite);
        j = 0;
        for (int i : posElites) {
            if (elite[j].getAptitud() < pob[i].getAptitud()) {
                elite[j] = pob[i].copy();
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
    
    private boolean contiene(int[] x,int elem){
        for(int y : x)
            if(y == elem)
                return true;
        return false;
    }
    
    private int duplicado(int[] array){
        int ret = -1;
        for(int i = 0; i < array.length;i++)
            for(int j = 0; j < array.length;j++)
                if(array[j] == array[i] && (i != j) && array[j]!=-1)
                    ret = j;
        return ret;
    }

}
