/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pevpr1;

import controlador.Controlador;
import java.util.ArrayList;
import modelo.AlgoritmoGenetico;
import modelo.Cromosoma;
import modelo.CromosomaF1;
import modelo.Problema;
import modelo.ProblemaF1;
import util.DatosGrafica;
import util.Functions;
import util.TipoFuncion;
import util.TipoSeleccion;
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
        /*TipoFuncion funcion = TipoFuncion.F1;
        int tampob = 10, iteraciones = 10;
        double probCruces = 0.7, probMutacion = 0.1, precision = 0.01;
        TipoSeleccion tSeleccion = TipoSeleccion.RULETA;
        AlgoritmoGenetico algo = new AlgoritmoGenetico(funcion, tampob, iteraciones,
                probCruces, probMutacion,precision,tSeleccion);
        DatosGrafica g = algo.ejecuta();
        System.out.println(g.toString());
        */
       Controlador c = new Controlador();
        Interfaz i = new Interfaz(c);
        i.setVisible(true);
       System.err.println(Functions.long_cromosoma(-250, 250, 0.001));
    }
    
}
