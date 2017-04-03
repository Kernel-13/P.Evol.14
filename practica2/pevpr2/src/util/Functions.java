package util;

import java.util.ArrayList;

public class Functions {
	
	/**
	 * calculo de la funcion 1 de la practica
	 * @param x
	 * @return
	 */
	public static double f1(double x){
		double raiz,ret,sen1;
		raiz = Math.sqrt(Math.abs(x)); 	
		sen1 = Math.sin(raiz);		
		ret = 0 - Math.abs(sen1*x); 
		return ret;
	}
	
        
        public static double valorAsignacion(int[] pos,double[][] f,double[][] d ,int n){
            double suma = 0;
            for(int i=0; i < n; i++)
                for(int j=0; j < n; j++)
                    suma+=f[i][j]*d[pos[i]][pos[j]];
            return suma;
        }
        
	/**
         * Calcula la longitud del cromosoma utilizando el maximo el minimo 
         * y la tolerancia o precision.
         * @param min
         * @param max
         * @param tol
         * @return 
         */
        public static int long_cromosoma(double min, double max, double tol){
            double aux = ((max - min)/tol)+1;
            double longitud = Math.log(aux)/Math.log(2);   
            return Math.round((float)longitud);
        }
        
        public static double fenotipoBin(double max, double min, double b2d, int lcrom){
                double fenotipo = min + b2d*((max-min)/(Math.pow(2,lcrom)-1));
                return fenotipo;
        }
        
}
