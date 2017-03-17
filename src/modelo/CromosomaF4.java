/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;

/**
 *
 * @author Ederson
 */
public class CromosomaF4 extends Cromosoma {

    private ArrayList<Boolean> genes;

    private ArrayList<Double> fenotipo;
    private int numVar;
    public static double max = Math.PI;
    public static double min = 0;
    private int tam;

    public CromosomaF4() {
        genes = new ArrayList<Boolean>();
    }

    public CromosomaF4(double precision) {
        tam = this.longCromosoma(precision);
        genes = new ArrayList<Boolean>();
    }

    public CromosomaF4(double precision, int n) {
        numVar = n;
        tam = this.longCromosoma(precision);
        genes = new ArrayList<Boolean>();
    }

    public CromosomaF4(ArrayList<Boolean> genes, int n) {
        this.numVar = n;
        this.genes = genes;
        this.tam = genes.size();
        fenotipo(); // Necesitamos calcularlo cada vez que cambiemos los genes
        aptitud = Functions.f4(fenotipo, numVar);
    }

    public ArrayList<Boolean> getGenes() {
        return this.genes;
    }

    public void inicializa(Random r) {
        for (int i = 0; i < tam; i++) {
            genes.add(r.nextBoolean());
        }
        fenotipo();
        aptitud = Functions.f4(fenotipo, numVar);
    }

    @Override
    public void fenotipo() {
        fenotipo = new ArrayList<Double>();
        for (int i = 1; i <= numVar; i++) {
            double aux = Functions.fenotipoBin(max, min, this.bin2dec((tam / numVar) * (i - 1), (tam / numVar) * i), tam / numVar);
            fenotipo.add(aux);
        }
    }

    @Override
    public double getAptitud() {
        return aptitud;
    }

    @Override
    public double getPuntuacion() {
        return puntuacion;
    }

    @Override
    public void setPuntuacion(double suma) {
        this.puntuacion = aptitudReal / suma;
    }

    private int bin2dec(int y, int x) {
        int ret = 0;
        int j = 0;
        for (int i = y; i < x; i++) {
            if (genes.get(i)) {
                ret += Math.pow(2, j);
            }
            j++;
        }
        return ret;
    }

    @Override
    public String toString() {
        return "ok";
        //return genes.toString() + " " + this.fenotipox1 + " " + this.fenotipox2 + " " + this.aptitud;
    }

    @Override
    protected int getTamanio() {
        return tam;
    }

    @Override
    protected double getPuntAcomulada() {
        return puntAcomulada;
    }

    @Override
    protected void setPuntAcomulada(double puntuacion) {
        puntAcomulada = puntuacion;
    }

    @Override
    protected void setAptitudDesplazada(double aptR) {
        this.aptitudReal = aptR;
    }

    @Override
    protected double getAptitudDesplazada() {
        return this.aptitudReal;
    }

    @Override
    public int longCromosoma(double p) {
        return Functions.long_cromosoma(CromosomaF4.min, CromosomaF4.max, p) * numVar;
    }

    @Override
    protected Cromosoma copy() {
        CromosomaF4 copia = new CromosomaF4();
        copia.aptitud = this.aptitud;
        copia.aptitudReal = this.aptitudReal;
        copia.fenotipo = this.fenotipo;
        copia.genes = this.genes;
        copia.puntAcomulada = this.puntAcomulada;
        copia.puntuacion = this.puntuacion;
        copia.tam = this.tam;
        return copia;
    }
}
