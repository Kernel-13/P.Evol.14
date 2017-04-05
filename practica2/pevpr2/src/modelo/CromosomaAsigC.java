/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import util.Functions;

/**
 *
 * @author lolita
 */
public class CromosomaAsigC extends Cromosoma {
    private ArrayList<Integer> genes;
    
    public CromosomaAsigC(int tam){
        genes = new ArrayList<>();
        this.tam = tam;
    }
    
    public CromosomaAsigC(ArrayList<Integer> x){
        genes = x;
        this.tam = x.size();
    }
    
    
    public void inicializa(Random r,int[][] f,int[][] d) {
        while(genes.size()<tam){
            Integer rand = r.nextInt(tam);
            if(!genes.contains(rand)){
                genes.add(rand);
            }
        }
        aptitud = Functions.valorAsignacion(Functions.toArrayInt(genes.toArray()), f, d, tam);
    }

    @Override
    public void inicializa(Random r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Object[] toArray() {
        return genes.toArray();
    }

    protected void setGenes(Object[] array,int f[][],int d[][]) {
        genes = new ArrayList<>();
        for(int i = 0;i<array.length;i++){
            genes.add((Integer)array[i]);
        }
        aptitud = Functions.valorAsignacion(Functions.toArrayInt(genes.toArray()), f, d, tam);
    }

    @Override
    protected Cromosoma copy() {
        CromosomaAsigC ret = new CromosomaAsigC(tam);
        ret.aptitud = this.aptitud;
        ret.aptitudReal = this.aptitudReal;
        ret.genes = this.genes;
        ret.puntAcomulada = this.puntAcomulada;
        ret.puntuacion = this.puntuacion;
        return ret;
    }
    
    protected CromosomaAsigC copy2() {
        CromosomaAsigC ret = new CromosomaAsigC(tam);
        ret.aptitud = this.aptitud;
        ret.aptitudReal = this.aptitudReal;
        ret.genes = this.genes;
        ret.puntAcomulada = this.puntAcomulada;
        ret.puntuacion = this.puntuacion;
        return ret;
    }

    @Override
    public int longCromosoma(double p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void setGenes(int[] array) {
        genes = new ArrayList<>();
        for(int i = 0; i < array.length;i++)
            genes.add(array[i]);
    }
    protected ArrayList<Integer> getGenes(){
        return genes;
    }

    @Override
    protected void setGenes(Object[] array) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
