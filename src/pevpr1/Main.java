/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pevpr1;

import controlador.Controlador;
import util.Functions;
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
        
       Controlador c = new Controlador();
        Interfaz i = new Interfaz(c);
        i.setVisible(true);
       System.err.println(Functions.long_cromosoma(-250, 250, 0.001));
       
    }
    
}
