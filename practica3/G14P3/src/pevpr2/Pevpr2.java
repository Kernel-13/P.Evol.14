/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pevpr2;

import controlador.Controlador;
import java.util.ArrayList;
import java.util.Random;
import modelo.Cromosoma;
import modelo.Problema;
import util.Nodo;
import util.TipoCruce;
import util.TipoMutacion;
import util.TipoOperacion;
import vista.Interfaz;

/**
 *
 * @author lolita
 */
public class Pevpr2 {

    public static int tamP = 10;
    public static int tamC = 19;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //ESTE ES EL CODIGO QUE TIENE QUE QUEDAR CUANDO ACABEMOS DE PROBAR TODO
        Controlador c = new Controlador();
        Interfaz i = new Interfaz(c);
        i.setVisible(true);
    }


}
