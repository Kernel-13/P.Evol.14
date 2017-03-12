/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pevpr1;

import controlador.Controlador;
import java.util.ArrayList;
import modelo.Cromosoma;
import modelo.CromosomaF1;
import modelo.Problema;
import modelo.ProblemaFuncion1;
import vista.Interfaz;

/**
 *
 * @author usuario_local
 */
public class Main {

    
    public static int tamP = 10;
    public static int tamC = 19;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        CromosomaF1[] pob = new CromosomaF1[tamP];
        CromosomaF1[] newPob;
        ArrayList<Double> aptitudes;
        Problema p = new ProblemaFuncion1(); 
        
        for(int i=0; i < tamP;i++){
            pob[i] = new CromosomaF1(tamC);
            pob[i].inicializa();
        }
        
        aptitudes = p.evaluacion(pob);
        
        for(int i=0; i < pob.length;i++){
            System.out.println(pob[i].getGenes().toString());
        }
        
        p.mutacion(pob, 1);
        
        System.out.println("-------------------------------");
        
        for(int i=0; i < pob.length;i++){
            System.out.println(pob[i].getGenes().toString());
        }
        /*boolean[] prueba = new boolean[4];
        for(int i = 0; i < 4;i++){
            prueba[i] = true;
        }
        Controlador c = new Controlador();
        Interfaz i = new Interfaz(c);
        i.setVisible(true);*/
    }
    
}
