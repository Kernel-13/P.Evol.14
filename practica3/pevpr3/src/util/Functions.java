package util;

import java.util.ArrayList;
import java.util.Arrays;
import modelo.Cromosoma;

public class Functions {

    /**
     * calculo de la funcion 1 de la practica
     *
     * @param x
     * @return
     */
    public static double f1(double x) {
        double raiz, ret, sen1;
        raiz = Math.sqrt(Math.abs(x));
        sen1 = Math.sin(raiz);
        ret = 0 - Math.abs(sen1 * x);
        return ret;
    }

    public static double valorAsignacion(int[] s, int[][] f, int[][] d, int n) {
        double suma = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                suma += f[i][j] * d[s[i]][s[j]];
            }
        }
        return suma;
    }

    public static int[] toArrayInt(Object[] ob) {
        int[] ret = Arrays.stream(ob).mapToInt(o -> (int) o).toArray();
        return ret;
    }

    /**
     * Calcula la longitud del cromosoma utilizando el maximo el minimo y la
     * tolerancia o precision.
     *
     * @param min
     * @param max
     * @param tol
     * @return
     */
    public static int long_cromosoma(double min, double max, double tol) {
        double aux = ((max - min) / tol) + 1;
        double longitud = Math.log(aux) / Math.log(2);
        return Math.round((float) longitud);
    }

    public static double fenotipoBin(double max, double min, double b2d, int lcrom) {
        double fenotipo = min + b2d * ((max - min) / (Math.pow(2, lcrom) - 1));
        return fenotipo;
    }

    public static double calculoAptitud(Cromosoma subject, ArrayList<ArrayList<Boolean>> casos, int nIn) {
        int apt = 0;
        for (ArrayList<Boolean> b : casos) {
            if (resultadoMult(subject, b, nIn)) {
                apt++;
            }
        }
        return apt;
    }

    // Funcion para calcular la aptitud de 1 cromosoma ()
    private static boolean resultadoMult(Cromosoma subject, ArrayList<Boolean> caso, int nIn) {
        boolean valor = subject.getArbol().valor(caso);
        int n = 0;
        for (int i = 0; i < nIn; i++) {
            n = (n << 1) | (caso.get(i) ? 1 : 0);
        }
        return valor == caso.get(nIn + n);
    }

    // Funcion que genera las combinaciones de 0's y 1's de la tabla
    public static void generadorCasos(ArrayList<ArrayList<Boolean>> casos, int n) {
        for (int i = 0; i < Math.pow(2, n); i++) {
            ArrayList<Boolean> aux = new ArrayList<Boolean>();
            String bin = Integer.toBinaryString(i);
            while (bin.length() < n) {
                bin = "0" + bin;
            }
            char[] chars = bin.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                boolean valor = chars[j] == '0' ? false : true;
                aux.add(valor);
            }
            casos.add(aux);
        }
    }

}
