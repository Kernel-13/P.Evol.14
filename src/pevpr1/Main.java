/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pevpr1;

import controlador.Controlador;
import cromosomas.Cromosoma;
import vista.Interfaz;

/**
 *
 * @author usuario_local
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean[] prueba = new boolean[4];
        for(int i = 0; i < 4;i++){
            prueba[i] = true;
        }
        Controlador c = new Controlador();
        Interfaz i = new Interfaz(c);
        i.setVisible(true);
    }
    
}
