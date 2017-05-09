/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.AlgoritmoGenetico;
import util.DatosGrafica;
import util.TipoCruce;
import util.TipoSeleccion;
import util.TipoFuncion;
import util.TipoMutacion;

/**
 *
 * @author josemanuel
 */
public class Controlador {
    private TipoFuncion funcion;
    private TipoCruce cruce;
    private TipoMutacion mutacion;
    private int poblacion;
    private int iteraciones;
    private int probCruces;
    private int probMutacion;
    private double precision;
    private TipoSeleccion seleccion;
    private int semilla;
    private int nvars;
    private boolean elitismo;
    private int f[][];
    private int d[][];
    private boolean inv;
    
    public Controlador(){
        funcion = TipoFuncion.F1;
        poblacion = 10;
        iteraciones = 200;
        probCruces = 20;
        probMutacion = 10;
        precision = 0.001;
        semilla = 2;
        seleccion = TipoSeleccion.RULETA;
        cruce = TipoCruce.OX;
        mutacion = TipoMutacion.INS;
        nvars = 4;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente a la funcion
     * @param f 
     */
    public void cambiarFuncion (int f){
        switch(f){
            case 0:
                funcion = TipoFuncion.F1;
                break;
            case 1:
                funcion = TipoFuncion.F2;
                break;
            case 2:
                funcion = TipoFuncion.F3;
                break;
            case 3:
                funcion = TipoFuncion.F4;
                break;
            case 4:
                funcion = TipoFuncion.F5;
                break;
            default:
                funcion = TipoFuncion.F3;
        }
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente al tamaño de población
     * @param pob 
     */
    public String cambiarPoblacion(String pob){
        String error = "";
        int p = 0;
        try{
            p = Integer.parseInt(pob);
            if(p < 1){
                error += "poblacion";
            }
        }catch(Exception e){
            error += "poblacion";
            return error;
        }
        //System.out.println(pob);
        if(p > 0)
            poblacion = p;
        return error;
    }
    
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente al numero de iteraciones
     * @param it 
     */
    public String cambiarIteraciones(String it){
        String error = "";
        int i = 0;
        try{
            i = Integer.parseInt(it);
            if(i < 1){
                error += "iteraciones";
            }
        }catch(Exception e){
            error += "iteraciones";
            return error;
        }
        if(i > 0)
            iteraciones = i;
        return error;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente a la probabilidad de cruce
     * @param probc 
     */
    public String cambiarPorcCruces(String probc){
        String error = "";
        int pc = 0;
        try{
            pc = Integer.parseInt(probc);
        }catch(Exception e){
            error += "cruce";
            return error;
        }
        if(pc >= 0 && pc <= 100)
           probCruces = pc;
        else{
            error+="cruce";
        }
        return error;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente a la probabilidad de mutacion
     * @param probm 
     */
    public String cambiarPorcMutacion(String probm){
        String error = "";
        int pm = 0;
        try{
            pm = Integer.parseInt(probm);
        }catch(Exception e){
            error += "mutacion";
            return error;
        } 
        if(pm >= 0 && pm <= 100)
            probMutacion = pm;
        else{
            error += "mutacion";
        }
        return error;
    }
    
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente a la precision
     * @param pre 
     */
    public String cambiarPrecision(String pre){
        double p = 0;
        try{
            p = Double.parseDouble(pre);
        }catch(Exception e){
            return "precision";
        }
        if(p > 0 && p < 1)
            precision = p;
        else{
            return "precision";
        }
        return "";
    }
    
    /**
     * Cambia el tipo de mutacion. 
     * @param m 
     */
    public void cambiarMutacion(int m){
        // Inserción, Intercambio, Inversión, Heurística
        switch(m){
            case 0:
                mutacion = TipoMutacion.INS;
                break;
            case 1:
                mutacion = TipoMutacion.INT;
                break;
            case 2:
                mutacion = TipoMutacion.INV;
                break;
            case 3:
                mutacion = TipoMutacion.HEU;
                break;
            case 4:
                mutacion = TipoMutacion.PROPIO;
                break;    
            default:
                mutacion = TipoMutacion.INS;
        }
    }
    
    /**
     * Cambia el tipio de cruce.
     * @param c 
     */
    public void cambiarCruce(int c){
        switch(c){
            case 0:
                cruce = TipoCruce.OX;
                break;
            case 1:
                cruce = TipoCruce.PMX;
                break;
            case 2:
                cruce = TipoCruce.ERX;
                break;
            case 3:
                cruce = TipoCruce.CODORD;
                break;
            case 4:
                cruce = TipoCruce.CICLOS;
                break;
            case 5:
                cruce = TipoCruce.VAROX1;
                break;
            case 6:
                cruce = TipoCruce.VAROX2;
                break;
            case 7:
                cruce = TipoCruce.PROPIO;
                break;
            default:
                cruce = TipoCruce.OX;
        }
    }
    /**
     * (funcion que se llama desde la vista)
     * cambia el parametro correspondiente al tipo de seleccion
     * @param s 
     */
    public void cambiarSeleccion(int s){
        switch(s){
            case 0:
                seleccion = TipoSeleccion.RULETA;
                break;
            case 1:
                seleccion = TipoSeleccion.TORNEODET;
                break;
            case 2:
                seleccion = TipoSeleccion.ESTOCASTICO;
                break;
            case 3:
                seleccion = TipoSeleccion.TORNEOPROB;
                break;
            default:
                seleccion = TipoSeleccion.RULETA;
        }
    }
    
    /**
     * cambia la semilla que se usa para generar la poblacion inicial
     * @param nueva 
     */
    public String cambiarSemilla(String nueva){
        int newsemilla = 2;
        try{
            newsemilla = Integer.parseInt(nueva);
        }catch(Exception e){
            return "semilla";
        } 
        semilla = newsemilla;
        return "";
    }
    
    /**
     * cambia el numero de variables de la funcion 4
     * @param nuevo 
     */
    public String cambiarNvars(String nuevo){
        int newNvars = 4;
        try{
            newNvars = Integer.parseInt(nuevo);
        }catch(Exception e){
            return "vars f4";
        } 
        if(newNvars > 0)
            nvars = newNvars;
        else{
            return "vars f4";
        }
        return "";
    }
    
    /**
     * actica/desactiva el elitismo
     * @param el 
     */
    public void cambiarElitismo(boolean el){
        elitismo = el;
    }
    
    public void cambiarInversion(boolean selected) {
        inv = selected;
    }
    
    /**
     * ejecuta el algoritmo genetico y devuelve los datos para generar 
     * la grafica
     * @return 
     */
    public DatosGrafica ejecuta(String archivo){
        //aqui hay que leer archivo e inicializar f y d
        nvars = leerArchivo(archivo);
        AlgoritmoGenetico algo = new AlgoritmoGenetico(funcion, poblacion, iteraciones,
                probCruces, probMutacion,precision,seleccion,nvars,elitismo,f,d,cruce,mutacion,inv);
        return algo.ejecuta(semilla);
    }
    
    /**
     * lee un archivo de datos con el siguiente formato
     * el 5 es el valor de n , la primera tabla es f y la segunda es d 
5

0 5 2 4 1
5 0 3 0 2
2 3 0 0 0
4 0 0 0 5
1 2 0 5 0

0 1 1 2 3
1 0 2 1 2
1 2 0 1 2
2 1 1 0 1
3 2 2 1 0

     * @param archivo
     * @param f
     * @param d
     * @return 
     */
    private int leerArchivo(String archivo){
        int n = 0;
        String cadena;
        try {
            FileReader file = new FileReader(archivo);
            BufferedReader b = new BufferedReader(file);
            cadena = b.readLine();
            n = Integer.parseInt(cadena);
            f = new int[n][n];
            d = new int[n][n];
            b.readLine();
            procesaMatriz(b,f);
            b.readLine();
            procesaMatriz(b,d);
            b.close();
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n; 
    }
    
    private void procesaMatriz(BufferedReader b,int[][]matriz) throws IOException{
        String cadena;
        for(int i = 0; i < matriz.length;i++){
            cadena = b.readLine();
            int k = 0;
            int j = 0;
            while(j < matriz.length){
                String subcadena = "";
                String c = cadena.charAt(k)+"";
                while (!c.equals(" ")){
                    subcadena += c;
                    k++;
                    if(k < cadena.length())
                        c = cadena.charAt(k)+"";
                    else
                        c = " ";
                }
                matriz[i][j] = Integer.parseInt(subcadena);
                j++;
                k++;
            }
        }
    }


    
}
