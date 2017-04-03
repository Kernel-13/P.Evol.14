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
	
	/**
	 * calculo de la funcion 2 de la practica
	 * @param x1
	 * @param x2
	 * @return
	 */
	public static double f2(double x1,double x2){
		double ret,seno1,seno2;
		seno1 = Math.sin(Math.sqrt((Math.abs(x2+x1/2+47))));
		seno2 = Math.sin(Math.sqrt((Math.abs(x1-(x2+47)))));
		ret = ((0-(x2+47))*seno1) - (x1*seno2);
		return ret;
	}
	
	public static double f3(double x,double y){
		// 21.5 + x*sen(4*pi*x) + y*sen(20*pi*y)
		double ret,seno1,seno2;
		seno1 = Math.sin(4*Math.PI*x);
		seno2 = Math.sin(20*Math.PI*y);
		ret = 21.5 + x*seno1 + y*seno2;
		return ret;
	}
	
	public static double f4(ArrayList<Double> x, int n){
		// - SUM[i to n] { sen(Xi) * (sen(((i+1)*Xi^2)/pi)))^20 }
		double sum = 0;
		for(int i = 1; i <= n; i++){
			double frac,seno1,seno2;
			seno1 = Math.sin(x.get(i-1));
			frac = ((i+1)*Math.pow(x.get(i-1), 2))/Math.PI;
			seno2 = Math.pow(Math.sin(frac),20);
			sum += seno1*seno2;
		}
		return (0-sum);
	}
	
	public static double f5(double x1, double x2){
		// - SUM[i to n] { sen(Xi) * (sen(((i+1)*Xi^2)/pi)))^20 }
		double sum1, sum2;
		sum1 = f5_aux(x1);
		sum2 = f5_aux(x2);
		return sum1*sum2;
	}
	
	private static double f5_aux(double x){
		double sum = 0;
		for(int i = 1; i <= 5; i++){
			double cos;
			cos = Math.cos((1+i)*x + i);
			sum += cos*i;
		}
		return sum;
	}
	
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
