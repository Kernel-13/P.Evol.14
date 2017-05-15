/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pevpr2;

import controlador.Controlador;
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

        //prueba mutacion
        Cromosoma[] pob = new Cromosoma[5];
        String tabla[] = {"a1","a2","d0","d1","d2","d3"};
        Problema p = new Problema(TipoCruce.CICLOS,TipoMutacion.TER, 5);
        for(int i = 0; i < 5;i++){
            pob[i] = new Cromosoma(inicioRampedAndHalf(false,5,0));
            System.out.println(pob[i].getArbol().toString(tabla));
        }
        p.mutacion(pob,1);
        for(Cromosoma x: pob){
            System.out.println(x.getArbol().toString(tabla));
        }
        
        //ESTE ES EL CODIGO QUE TIENE QUE QUEDAR CUANDO ACABEMOS DE PROBAR TODO
       /*Controlador c = new Controlador();
        Interfaz i = new Interfaz(c);
        i.setVisible(true);*/

       //System.err.println(Functions.long_cromosoma(-250, 250, 0.001));
       
    }
    
    
        private static Nodo inicioRampedAndHalf(boolean modo,int tamGrupo,int depth){
        Nodo node = null;
        boolean nuevoModo = modo;
        if(depth%tamGrupo == 0)
            nuevoModo = !modo;
        if(depth < 5 && nuevoModo){
            TipoOperacion f = randomFunction();
            while(f == TipoOperacion.HOJA){
                f = randomFunction();
            }
            switch(f) {
                case IF:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1));
                    break;
                case AND:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), null);
                    break;
                case OR:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), null);
                    break;
                case NOT:
                    node = new Nodo(f,0,inicioRampedAndHalf(nuevoModo,tamGrupo,depth + 1), null, null);
                    break;
                default:
                    break;
            }
            // pilla funcion aleatoria
        } else {
            Random r = new Random();
            TipoOperacion f = TipoOperacion.HOJA;
            node = new Nodo(f, r.nextInt(6), null, null, null);
        }
        return node;
    }
    
        private static Nodo inicioCompleto(int depth){
        Nodo node = null;
        if(depth < 6){
            TipoOperacion f = randomFunction();
            while(f == TipoOperacion.HOJA){
                f = randomFunction();
            }
            switch(f) {
                case IF:
                    node = new Nodo(f,0,inicioCompleto(depth + 1), inicioCompleto(depth + 1), inicioCompleto(depth + 1));
                    break;
                case AND:
                    node = new Nodo(f,0,inicioCompleto(depth + 1), inicioCompleto(depth + 1), null);
                    break;
                case OR:
                    node = new Nodo(f,0,inicioCompleto(depth + 1), inicioCompleto(depth + 1), null);
                    break;
                case NOT:
                    node = new Nodo(f,0,inicioCompleto(depth + 1), null, null);
                    break;
                default:
                    break;
            }
            // pilla funcion aleatoria
        } else {
            Random r = new Random();
            TipoOperacion f = TipoOperacion.HOJA;
            node = new Nodo(f, r.nextInt(6), null, null, null);
        }
        return node;
    }
        
    private static TipoOperacion randomFunction() {
        int pick = new Random().nextInt(TipoOperacion.values().length);
        return TipoOperacion.values()[pick];
    }
    
}
