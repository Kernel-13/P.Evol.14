package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;

public class CromosomaF1 extends Cromosoma {

    private ArrayList<Boolean> genes;
    private double fenotipo;
    public static double max = 250;
    public static double min = -250;

    public CromosomaF1() {
        genes = new ArrayList<Boolean>();
    }

    public CromosomaF1(double precision) {
        tam = longCromosoma(precision);
        genes = new ArrayList<Boolean>();
    }

    public CromosomaF1(ArrayList<Boolean> genes) {
        this.genes = genes;
        this.tam = genes.size();
        fenotipo(); // Necesitamos calcularlo cada vez que cambiemos los genes
        aptitud = Functions.f1(fenotipo); // idem
    }

    public ArrayList<Boolean> getGenes() {
        return this.genes;
    }

    @Override
    public void inicializa(Random r) {
        for (int i = 0; i < tam; i++) {
            genes.add(r.nextBoolean());
        }
        fenotipo();
        aptitud = Functions.f1(fenotipo);
    }
    
    private void fenotipo() {
        fenotipo = Functions.fenotipoBin(max, min, this.bin2dec(), tam);
    }

    private int bin2dec() {
        int ret = 0;
        for (int i = 0; i < tam; i++) {
            if (genes.get(i)) {
                ret += Math.pow(2, i);
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "Fenotipo(s): " + this.fenotipo + " --- Aptitud:" + this.aptitud;
    }


    @Override
    public int longCromosoma(double p) {
        return Functions.long_cromosoma(CromosomaF1.min, CromosomaF1.max, p);
    }

    @Override
    protected Cromosoma copy() {
        CromosomaF1 copia = new CromosomaF1();
        copia.aptitud = this.aptitud;
        copia.aptitudReal = this.aptitudReal;
        copia.fenotipo = this.fenotipo;
        copia.genes = this.genes;
        copia.puntAcomulada = this.puntAcomulada;
        copia.puntuacion = this.puntuacion;
        copia.tam = this.tam;
        return copia;
    }

    @Override
    protected Object[] toArray() {
        return genes.toArray();
    }

    @Override
    protected void setGenes(Object[] array) {
        this.genes = new ArrayList<>();
        for(int i = 0; i< array.length;i++){
            this.genes.add((Boolean)array[i]);
        }
        this.fenotipo();
        aptitud = Functions.f1(fenotipo);
    }
}
