/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;
import util.TipoCruce;
import util.TipoMutacion;

/**
 *
 * @author Ederson
 */
public class Problema {

    protected int[][] f;
    protected int[][] d;
    protected CromosomaAsigC best;
    protected double sumaPob;
    protected TipoCruce cruce;
    protected TipoMutacion mut;
    protected double mejorAptitud = 0;          // Para la mutacion heuristica
    protected ArrayList<Integer> mejorMutacion; // Para la mutacion heuristica

    protected CromosomaAsigC[] elite;

    public Problema(TipoCruce c,TipoMutacion m, int[][] f, int[][] d) {
        sumaPob = 0;
        best = null;
        this.f = f;
        this.d = d;
        this.cruce = c;
        this.mut = m;
    }

    /**
     * calcula el fitness desplazado, las puntuaciones y las puntuaciones
     * acomuladas
     *
     * @param pob
     * @return
     */
    public CromosomaAsigC evaluacion(CromosomaAsigC[] pob) {
        CromosomaAsigC bestPobActual = pob[0];
        double maxApt = 0, puntAcomulada = 0;  // Se debe calcular fuera, y entrar como parametro de la funcion
        double sum = 0;
        double sumDefault = 0;
        maxApt = pob[0].getAptitud();
        for (int j = 1; j < pob.length; j++) {
            if (pob[j].getAptitud() > maxApt) {
                maxApt = pob[j].getAptitud();
            }
        }
        for (int i = 0; i < pob.length; i++) {
            pob[i].setAptitudDesplazada(aptitudDesplazada(pob[i], maxApt));
            sumDefault += pob[i].getAptitud();
            sum += pob[i].getAptitudDesplazada();
            if (pob[i].getAptitudDesplazada() > bestPobActual.getAptitudDesplazada()) {
                bestPobActual = pob[i];
            }
        }
        sumaPob = sumDefault;
        for (int k = 0; k < pob.length; k++) {
            pob[k].setPuntuacion(sum);
            pob[k].setPuntAcomulada(pob[k].getPuntuacion() + puntAcomulada);
            puntAcomulada += pob[k].getPuntuacion();
        }
        if (best == null) {
            best = bestPobActual.copy2();
        }
        if (bestPobActual.getAptitud() < best.getAptitud()) {
            best = bestPobActual.copy2();
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return bestPobActual;
    }	// Debe devolver la mejor aptitud, y una array de las aptitudes

    /**
     * calcula el valor del fitness desplazado
     *
     * @param individuo
     * @param maxApt
     * @return
     */
    public double aptitudDesplazada(Cromosoma individuo, double maxApt) {
        return maxApt - individuo.getAptitud();
    }

    /**
     * calcula la media de la poblacion
     *
     * @param tam
     * @return
     */
    public double media(int tam) {
        return sumaPob / tam;
    }

    /**
     * devuelve una nueva poblacion con cromosomas cruzados.
     *
     * @param pob
     * @param probCruce
     * @return
     */
    public CromosomaAsigC[] reproduccion(CromosomaAsigC[] pob, double probCruce) {
        Random r = new Random();
        ArrayList<Integer> elegidos = new ArrayList<>();
        for (int i = 0; i < pob.length; i++) {
            if (r.nextDouble() < probCruce) {
                elegidos.add(i);
            }
        }

        if (elegidos.size() % 2 != 0) {
            elegidos.remove(elegidos.size() - 1);
        }

        for (int i = 0; i < elegidos.size(); i += 2) {
            cruce(pob[elegidos.get(i)], pob[elegidos.get(i + 1)]);
        }
        return pob;
    }

    /**
     * Cruza 2 cromosomas - obtenemos 2 hijos Los cromosomas padre se pasan por
     * referencia y se convierten en los hijos posteriormente.
     *
     * @param pob
     * @param son1
     * @param son2
     */
    protected void cruce(CromosomaAsigC new1, CromosomaAsigC new2) {
        switch (cruce) {
            case CICLOS:
                cruceCiclos(new1,new2);
                break;
            case OX:
                cruceOX(new1,new2);
                break;
            case ERX:
                cruceERX(new1,new2);
                break;
            case CODORD:
                cruceOrdinal(new1,new2);
                break;
            case PMX:
                crucePMX(new1,new2);
                break;
            default:
                cruceOX(new1,new2);
        }
    }

    /**
     * Cruza 2 cromosomas - obtenemos 2 hijos El cruce aplicado en este caso es
     * por ruleta
     *
     * @param pob
     * @param son1
     * @param son2
     */
    private void cruceCiclos(CromosomaAsigC new1, CromosomaAsigC new2) {
        int[] parent1 = Functions.toArrayInt(new1.toArray()); // = pob[pos1].getGenes();
        int[] parent2 = Functions.toArrayInt(new2.toArray());   // = pob[pos2].getGenes();
        int[] son1 = new int[new1.getTamanio()];
        int[] son2 = new int[new2.getTamanio()];
        ArrayList<Integer> array = new ArrayList<>();
        
        for (int i = 0; i < parent1.length; i++) {
            array.add(i);
        }
        //hacemos el ciclo completo.
        int i = 0;
        son1[i] = parent1[i];
        son2[i] = parent2[i];
        i = parent2[i];
        array.remove(new Integer(i));
        while (i!=0) {
            son1[i] = parent1[i];
            son2[i] = parent2[i];
            i = parent2[i];
            array.remove(new Integer(i)); 
        }
        //copiamos los elementos dle otro padre
        for (Integer x : array) {
            son1[x] = parent2[x];
            son2[x] = parent1[x];
        }
        //si hay elementos duplicados en un hijo los intercambiamos
        int dup1,dup2;
        dup1 = duplicado(son1);
        dup2 = duplicado(son2);
        while(dup1 != -1 || dup2 != -1){
            int aux = son1[dup1];
            son1[dup1] = son2[dup2];
            son2[dup2] = aux;
            dup1 = duplicado(son1);
            dup2 = duplicado(son2);
        }
        
        new1.setGenes(son1,f,d);
        new2.setGenes(son2,f,d);
    }
    
    private void cruceERX(CromosomaAsigC new1, CromosomaAsigC new2){
        int[] parent1 = Functions.toArrayInt(new1.toArray()); // = pob[pos1].getGenes();
        int[] parent2 = Functions.toArrayInt(new2.toArray());   // = pob[pos2].getGenes();
        int[] son1 = new int[new1.getTamanio()];
        int[] son2 = new int[new2.getTamanio()];
        int[][] tabla = new int[2][new1.getTamanio()];
        Random r = new Random();
        boolean[] cogido = new boolean[new1.getTamanio()];
        boolean[] cogido2 = new boolean[new1.getTamanio()];
        for(int i = 0; i < cogido.length; i++){
            cogido[i] = false;
            cogido2[i] = false;
            son1[i] = -1;
            son2[i] = -1;
        }
        for(int i = 0;i < 2;i++)
            for(int j = 0;j < parent1.length;j++){
                if(r.nextDouble()<0.5 && !cogido[j]){
                    tabla[i][j] = parent1[j];
                    cogido[j] = true;
                }else if(!cogido2[j]){
                    tabla[i][j] = parent2[j];
                    cogido2[j] = true;
                }else{
                    tabla[i][j] = parent1[j];
                    cogido[j] = true;
                }
            }
        int size1 = 0, size2 = 0;
        for(int i = 0;i < 2;i++)
            for(int j = 0;j < parent1.length;j++){
                if(!contiene(son1,tabla[i][j]) && size1 < parent1.length){
                    son1[j] = tabla[i][j];
                    size1++;
                }else if(i == 0){
                    son1[j] = tabla[i+1][j];
                    size1++;
                }
                if(i == 0 && !contiene(son2,tabla[i+1][j])){
                    son2[j] = tabla[i+1][j];
                    size2++;
                }else if(size2 < parent1.length) {
                    son2[j] = tabla[i-1][j];
                    size2++;
                }
            }
        new1.setGenes(son1,f,d);
        new2.setGenes(son2,f,d);
    }

    private void crucePMX(CromosomaAsigC new1, CromosomaAsigC new2) {
        int[] parent1 = Functions.toArrayInt(new1.toArray());
        int[] parent2 = Functions.toArrayInt(new2.toArray());
        ArrayList<Integer> segmento1 = new ArrayList<>();
        ArrayList<Integer> segmento2 = new ArrayList<>();
        int[] son1 = new int[new1.getTamanio()];
        int[] son2 = new int[new2.getTamanio()];
        Random r = new Random();
        int puntoUno = r.nextInt(new1.getTamanio() - 1) + 1;
        int puntoDos = r.nextInt(new1.getTamanio() - 1) + 1;

        ArrayList<Integer> pos1 = new ArrayList<>();
        ArrayList<Integer> pos2 = new ArrayList<>();
        for (int i = 0; i < new1.getTamanio(); i++) {
            pos1.add(i);
            pos2.add(i);
        }

        // Repetimos hasta conseguir 2 puntos separados
        while (puntoUno >= puntoDos) {
            puntoUno = r.nextInt(new1.getTamanio() - 1) + 1;
            puntoDos = r.nextInt(new1.getTamanio() - 1) + 1;
        }

        // Hacemos el intercambio de ambos segmentos
        for (int i = puntoUno; i < puntoDos; i++) {
            son1[i] = parent2[i];
            son2[i] = parent1[i];
            segmento1.add(parent2[i]);
            segmento2.add(parent1[i]);
            pos1.remove(new Integer(i));
            pos2.remove(new Integer(i));
        }

        // Dejamos los numeros originales que no generan conflicto (Derecha)
        for (int i = puntoDos; i < new1.getTamanio(); i++) {
            if (!segmento1.contains(parent1[i])) {
                son1[i] = parent1[i];
                pos1.remove(new Integer(i));
            }
            if (!segmento2.contains(parent2[i])) {
                son2[i] = parent2[i];
                pos2.remove(new Integer(i));
            }
        }

        // Dejamos los numeros originales que no generan conflicto (Izquierda)       
        for (int i = 0; i < puntoUno; i++) {
            if (!segmento1.contains(parent1[i])) {
                son1[i] = parent1[i];
                pos1.remove(new Integer(i));
            }
            if (!segmento2.contains(parent2[i])) {
                son2[i] = parent2[i];
                pos2.remove(new Integer(i));
            }
        }

        // Rellenamos los que faltan
        for (int i = 0; i < pos1.size(); i++) {
            son1[pos1.get(i)] = segmento1.get(i);
        }
        for (int i = 0; i < pos2.size(); i++) {
            son2[pos2.get(i)] = segmento2.get(i);
        }
        new1.setGenes(son1,f,d);
        new2.setGenes(son2,f,d);
    }

    private void cruceOX(CromosomaAsigC new1, CromosomaAsigC new2) {
        int[] parent1 = Functions.toArrayInt(new1.toArray()); 
        int[] parent2 = Functions.toArrayInt(new2.toArray());   
        int[] son1 = new int[new1.getTamanio()];
        int[] son2 = new int[new2.getTamanio()];
        Random r = new Random();
        int puntoUno = 1, puntoDos = 0; 
        ArrayList<Integer> pos1 = new ArrayList<>();
        ArrayList<Integer> pos2 = new ArrayList<>();
        
        for (int i = 0; i < new1.getTamanio(); i++) {
            pos1.add(i);
            pos2.add(i);
            son1[i] = -1;
            son2[i] = -1;
        }
        
        // Repetimos hasta conseguir 2 puntos separados
        while (puntoUno >= puntoDos) {
            puntoUno = r.nextInt(new1.getTamanio() - 1) + 1;
            puntoDos = r.nextInt(new1.getTamanio() - 1) + 1;
        }

        // Hacemos el intercambio de ambos segmentos
        for (int i = puntoUno; i < puntoDos; i++) {
            son1[i] = parent2[i];
            son2[i] = parent1[i];
            pos1.remove(pos1.indexOf(i));
            pos2.remove(pos2.indexOf(i));
        }

        // Dejamos los numeros originales que no generan conflicto (Primer Hijo)
        int ii = puntoDos, jj = puntoDos;
        int k = puntoDos, h = puntoDos;
        
        while (!pos1.isEmpty() || !pos2.isEmpty()) {
            if (ii == parent1.length) ii = 0;
            if (jj == parent1.length) jj = 0;
            if (k == parent2.length) k = 0;
            if (h == parent2.length) h = 0;
          
            // Dejamos los numeros originales que no generan conflicto (Primer Hijo)
            if (pos1.contains(ii) && !pos1.isEmpty()) {              // Si la posicion ii esta disponible
                int val = parent1[jj];
                if (contiene(son1,val)) {
                    jj++;
                } else {
                    son1[ii] = val;
                    pos1.remove(pos1.indexOf(ii));
                    ii++;
                }
            } else {
                ii++;
            }
            
            // Dejamos los numeros originales que no generan conflicto (Segundo Hijo)
            if (pos2.contains(k) && !pos2.isEmpty()) {              // Si la posicion k esta disponible
                int val = parent2[h];
                if (contiene(son2,val)) {
                    h++;
                } else {
                    son2[k] = val;
                    pos2.remove(pos2.indexOf(k));
                    k++;
                }
            } else {
                k++;
            }
        }
        if(duplicado(son1) != -1){
            
        }
        if(duplicado(son1) != -1){
            
        }
        new1.setGenes(son1,f,d);
        new2.setGenes(son2,f,d);
    }

    private void cruceOrdinal(CromosomaAsigC new1, CromosomaAsigC new2) {
        Random r = new Random();
        int[] son1 = new int[new1.getTamanio()];
        int[] son2 = new int[new2.getTamanio()];
             
        ArrayList<Integer> recorrido1 = new ArrayList<>(new1.getGenes());
        ArrayList<Integer> recorrido2 = new ArrayList<>(new2.getGenes());
        ArrayList<Integer> listaDinamica1 = new ArrayList<>();
        ArrayList<Integer> listaDinamica2 = new ArrayList<>();
        ArrayList<Integer> listaPos1 = new ArrayList<>();
        ArrayList<Integer> listaPos2 = new ArrayList<>();
        
        for(int i = 1; i <= new1.getTamanio(); i++){
            listaDinamica1.add(i);  
            listaDinamica2.add(i);
        }      
        
        for(int i = 0; i < recorrido1.size(); i++){
            int val1 = recorrido1.get(i);
            int val2 = recorrido2.get(i);
            int pos1 = listaDinamica1.indexOf(val1);
            int pos2 = listaDinamica2.indexOf(val2);
            listaPos1.add(pos1);
            listaPos2.add(pos2);
            listaDinamica1.remove(new Integer(pos1));
            listaDinamica2.remove(new Integer(pos2));
        }
        
        int puntoCorte = r.nextInt(new1.getTamanio() - 1) + 1;
        
        ArrayList<Integer> listaCruce1 = new ArrayList<>(listaPos1);
        ArrayList<Integer> listaCruce2 = new ArrayList<>(listaPos2);
        
        // Hacemos el cruce clasico con la lista de posiciones
        for (int i = puntoCorte; i < listaPos1.size(); i++) {
            listaCruce1.set(i, listaPos2.get(i));
            listaCruce2.set(i, listaPos1.get(i));
        }
        
        // Volvemos a inicializar la lista dinamica
        for(int i = 1; i <= new1.getTamanio(); i++){
            listaDinamica1.add(i);  
            listaDinamica2.add(i);
        }
        
        for(int i = 0; i < new2.getTamanio(); i++){
            int pos1 = listaCruce1.get(i);
            int pos2 = listaCruce2.get(i);
            son1[i] = listaDinamica1.get(pos1);
            son2[i] = listaDinamica2.get(pos2);
            listaDinamica1.remove(new Integer(pos1));
            listaDinamica2.remove(new Integer(pos2));
        }

        new1.setGenes(son1,f,d);
        new2.setGenes(son2,f,d);
    }

    
    private void crucePropio(CromosomaAsigC new1, CromosomaAsigC new2) {
	ArrayList<Integer> parent1 = new ArrayList<>(new1.getGenes());
	ArrayList<Integer> parent2 = new ArrayList<>(new2.getGenes());
	ArrayList<Integer> sonOne = new ArrayList<>();
	ArrayList<Integer> sonTwo = new ArrayList<>();
	int[] son1 = new int[new1.getTamanio()];
	int[] son2 = new int[new2.getTamanio()];

	int contUno = 0;
	int contDos = 0;
	boolean intercalate = false;
	while (sonOne.size() < new1.getTamanio()) {
		if (!intercalate) {
			if (!parent1.isEmpty() && !sonOne.contains(parent1.get(contUno))) {
				sonOne.add(parent1.get(contUno));
				parent1.remove(contUno);
				intercalate = true;
			} else {
				contUno++;
			}
		}

		if (intercalate) {
			if (!parent2.isEmpty() && !sonOne.contains(parent2.get(contDos))) {
				sonOne.add(parent2.get(contDos));
				parent2.remove(contDos);
				intercalate = false;
			} else {
				contDos++;
			}
		}

		if (contUno >= parent1.size()) {
			contUno = 0;
		}
		if (contDos >= parent2.size()) {
			contDos = 0;
		}

	}

	contUno = 0;
	contDos = 0;
	intercalate = false;
	while (sonTwo.size() < new1.getTamanio()) {
		if (!intercalate) {
			if (!parent2.isEmpty() && !sonTwo.contains(parent2.get(contUno))) {
				sonTwo.add(parent2.get(contUno));
				parent2.remove(contUno);
				intercalate = true;
			} else {
				contUno++;
			}
		}

		if (intercalate) {
			if (!parent1.isEmpty() && !sonTwo.contains(parent1.get(contDos))) {
				sonTwo.add(parent1.get(contDos));
				parent1.remove(contDos);
				intercalate = false;
			} else {
				contDos++;
			}
		}

		if (contUno >= parent1.size()) {
			contUno = 0;
		}
		if (contDos >= parent2.size()) {
			contDos = 0;
		}

	}

		// Rellenamos
	for (int i = 0; i < sonOne.size(); i++) {
		son1[i] = sonOne.get(i);
	}
	for (int i = 0; i < sonTwo.size(); i++) {
		son2[i] = sonTwo.get(i);
	}
	new1.setGenes(son1, f, d);
	new2.setGenes(son2, f, d);
    }

    /**
     * Recorre la pob. y cada individuo, y segun la probabilidad cambia un gen o
     * no
     *
     * @param pob
     * @param probMutacion
     */
    public void mutacion(Cromosoma[] pob, double probMutacion) {
        switch(mut){
            case INS:
                mutacionInsercion(pob,probMutacion);
                break;
            case HEU:
                mutacionHeuristica(pob,probMutacion,pob[0].getTamanio());
                break;
            case INV:
                mutacionInversion(pob,probMutacion);
                break;
            case INT:
                mutacionIntercambio(pob,probMutacion);
            default:
                mutacionInversion(pob,probMutacion);
                break;
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * ya funciona
     * @param pob
     * @param probMutacion 
     */
    public void mutacionIntercambio(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();
        CromosomaAsigC[] pobAux = new CromosomaAsigC[pob.length];
        for (int i = 0; i < pob.length; i++)
            pobAux[i] = (CromosomaAsigC) pob[i];
        
        int puntoUno,puntoDos;
        for (int j = 0; j < pobAux.length; j++) {
            if (r.nextDouble() < probMutacion) {
                puntoUno = r.nextInt(pobAux[j].getTamanio()-1);
                puntoDos = r.nextInt(pobAux[j].getTamanio()-1);
                // Repetimos hasta conseguir 2 puntos separados
                while (puntoUno >= puntoDos) {
                    puntoUno = r.nextInt(pobAux[j].getTamanio() - 1);
                    puntoDos = r.nextInt(pobAux[j].getTamanio() - 1);
                }
                ArrayList<Integer> mutado1 = pobAux[j].getGenes();
                Integer elem1 = pobAux[j].getGenes().get(puntoUno);
                Integer elem2 = pobAux[j].getGenes().get(puntoDos);
                mutado1.set(puntoUno , new Integer(elem2));
                mutado1.set(puntoDos, new Integer(elem1));
                pob[j] =  new CromosomaAsigC(mutado1);
            }
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * MUTACION POR INVERSION.
     * @param pob
     * @param probMutacion 
     */
    public void mutacionInversion(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();
        CromosomaAsigC[] pobAux = new CromosomaAsigC[pob.length];
        for (int i = 0; i < pob.length; i++) {
            pobAux[i] = (CromosomaAsigC) pob[i];
        }
        int puntoUno,puntoDos;
        for (int j = 0; j < pobAux.length; j++) {
            puntoUno = r.nextInt(pobAux[j].getTamanio() - 1) + 1;
            puntoDos = r.nextInt(pobAux[j].getTamanio() - 1) + 1;
            // Repetimos hasta conseguir 2 puntos separados
            while (puntoUno >= puntoDos) {
                puntoUno = r.nextInt(pobAux[j].getTamanio() - 1) + 1;
                puntoDos = r.nextInt(pobAux[j].getTamanio() - 1) + 1;
            }

            ArrayList<Integer> original = pobAux[j].getGenes();
            ArrayList<Integer> mutado = pobAux[j].getGenes();

            if (r.nextDouble() < probMutacion) {
                for (int i = puntoUno; i < puntoDos; i++) {
                    mutado.add(puntoDos - i, original.get(i));
                    mutado.remove(puntoDos);
                }
                CromosomaAsigC nuevo = new CromosomaAsigC(mutado);
                pob[j] = nuevo;
            }
        }
    }

    public void mutacionInsercion(Cromosoma[] pob, double probMutacion) {
        Random r = new Random();

        CromosomaAsigC[] pobAux = new CromosomaAsigC[pob.length];
        for (int i = 0; i < pob.length; i++) {
            pobAux[i] = (CromosomaAsigC) pob[i];
        }

        for (int j = 0; j < pobAux.length; j++) {
            if (r.nextDouble() < probMutacion) {
                ArrayList<Integer> mutado = pobAux[j].getGenes();
                ArrayList<Integer> posiciones = new ArrayList<>();
                ArrayList<Integer> valores = new ArrayList<>();

                int cont = 0;
                for (int i = 0; i < pobAux.length; i++) {
                    if (r.nextDouble() < probMutacion) {
                        cont++;
                        valores.add(mutado.get(i));
                    }
                }

                for (int i = 0; i < cont; i++) {
                    int pos = r.nextInt(pobAux.length - 1) + 1;
                    posiciones.add(pos);
                }

                for (int i = 0; i < posiciones.size(); i++) {
                    int val = valores.get(i);
                    int posOriginal = mutado.indexOf(val);
                    int posNueva = posiciones.get(i);
                    mutado.add(posNueva, val);
                    if (posOriginal < posNueva) {
                        mutado.remove(posOriginal);
                    } else {
                        mutado.remove(posOriginal + 1);
                    }
                }

                CromosomaAsigC nuevo = new CromosomaAsigC(mutado);
                pob[j] = nuevo;
            }
        }
    }

    public void mutacionHeuristica(Cromosoma[] pob, double probMutacion, int n) {
        Random r = new Random();
        CromosomaAsigC[] pobAux = new CromosomaAsigC[pob.length];
        for (int i = 0; i < pob.length; i++) {
            pobAux[i] = (CromosomaAsigC) pob[i];
        }

        for (int j = 0; j < pobAux.length; j++) {
            if (r.nextDouble() < probMutacion) {

                ArrayList<Integer> original = pobAux[j].getGenes();
                ArrayList<Integer> pos = new ArrayList<>();
                ArrayList<Integer> val = new ArrayList<>();

                while (pos.size() < n) {
                    int aux = r.nextInt(pobAux[j].getTamanio() - 1) + 1;
                    if (!pos.contains(aux)) {
                        pos.add(aux);
                        val.add(original.get(aux));
                    }
                }

                backtracking(pos, val, original);

                CromosomaAsigC nuevo = new CromosomaAsigC(mejorMutacion);
                pob[j] = nuevo;
            }

        }
    }

    private void backtracking(ArrayList<Integer> pos, ArrayList<Integer> valores,
            ArrayList<Integer> mutado) {
        
        ArrayList<Integer> nuevo = new ArrayList<Integer>(mutado);
        ArrayList<Integer> pos2 = new ArrayList<Integer>(pos);
        ArrayList<Integer> val2 = new ArrayList<Integer>(valores);
        for (int i = 0; i < val2.size(); i++) {
            for (int j = 0; j < pos2.size(); j++) {
                nuevo.set(pos2.get(j), val2.get(i));
                val2.remove(i);
                pos2.remove(j);
                backtracking(pos2, val2, nuevo);
                pos2 = new ArrayList<Integer>(pos);
                val2 = new ArrayList<Integer>(valores);
            }
        }

        if (esValido(nuevo)) {
            CromosomaAsigC aux = new CromosomaAsigC(nuevo);
            double apt = aux.getAptitud();
            if (apt > mejorAptitud) {
                mejorMutacion = new ArrayList<Integer>(nuevo);
                mejorAptitud = apt;
            }
        }
    }
    
    public void mutacionPropia(Cromosoma[] pob, double probMutacion) {
	Random r = new Random();
	CromosomaAsigC[] pobAux = new CromosomaAsigC[pob.length];
	for (int i = 0; i < pob.length; i++) {
		pobAux[i] = (CromosomaAsigC) pob[i];
	}

	for (int j = 0; j < pobAux.length; j++) {
		if (r.nextDouble() < probMutacion) {
			int puntoUno = r.nextInt(pobAux[j].getTamanio() - 1) + 1;
			int puntoDos = r.nextInt(pobAux[j].getTamanio() - 1) + 1;

				// Repetimos hasta conseguir 2 puntos separados
			while (puntoUno >= puntoDos) {
				puntoUno = r.nextInt(pobAux[j].getTamanio() - 1) + 1;
				puntoDos = r.nextInt(pobAux[j].getTamanio() - 1) + 1;
			}

			ArrayList<Integer> mutado = new ArrayList<Integer>(pobAux[j].getGenes());
			ArrayList<Integer> segmento = new ArrayList<Integer>();

				// Seleccionamos el segmento que queremos mover
			for (int i = puntoUno; i < puntoDos; i++) {
				segmento.add(mutado.get(i));
			}

			for (int i = 0; i < segmento.size(); i++) {
				mutado.remove(segmento.get(i));
			}

			while (!segmento.isEmpty()) {
				mutado.add(0, segmento.get(0));
				segmento.remove(0);
			}

			CromosomaAsigC nuevo = new CromosomaAsigC(mutado);
			pob[j] = nuevo;

		}
	}
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
    }

    private boolean esValido(ArrayList<Integer> crom) {
        boolean exit = true;
        for (int i = 0; i < crom.size() && exit; i++) {
            if (!crom.contains(i)) {
                exit = false;
            }
        }
        return exit;
    }

    /**
     * devuelve el mejor de la poblacion
     *
     * @return
     */
    public CromosomaAsigC getBest() {
        return best.copy2();
    }

    /**
     * llama a la funcion elitismo maximizacion o minimizacion segun el problema
     *
     * @param pob
     * @param elite
     */
    public void elitismo(CromosomaAsigC[] pob, int elite) {
        elitismoMinimizacion(pob, elite);
    }

    /**
     * inicializa el valor de la elite
     *
     * @param pob
     * @param numElite
     */
    public void iniElite(CromosomaAsigC[] pob, int numElite) {
        elite = new CromosomaAsigC[numElite];
        for (int i = 0; i < numElite; i++) {
            elite[i] = pob[i].copy2();
        }
    }

    /**
     * actualiza la elite en problemas de minimizacion
     *
     * @param pob
     * @param numElite
     */
    public void elitismoMinimizacion(CromosomaAsigC[] pob, int numElite) {
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = maximosPoblacion(pob, numElite);
        int j = 0;
        for (int i : posPeores) {
            if (elite[j].getAptitud() < pob[i].getAptitud()) {
                pob[i] = elite[j].copy2();
            }
            j++;
        }
        posElites = minimosPoblacion(pob, numElite);
        j = 0;
        for (int i : posElites) {
            if (elite[j].getAptitud() > pob[i].getAptitud()) {
                elite[j] = pob[i].copy2();
            }
            j++;
        }
    }

    /**
     * actualiza la elite en problemas de maximizacion
     *
     * @param pob
     * @param numElite
     */
    public void elitismoMaximizacion(CromosomaAsigC[] pob, int numElite) {
        ArrayList<Integer> posElites = new ArrayList<>();
        ArrayList<Integer> posPeores = new ArrayList<>();
        posPeores = minimosPoblacion(pob, numElite);
        int j = 0;
        for (int i : posPeores) {
            if (elite[j].getAptitud() > pob[i].getAptitud()) {
                pob[i] = elite[j].copy2();
            }
            j++;
        }
        posElites = maximosPoblacion(pob, numElite);
        j = 0;
        for (int i : posElites) {
            if (elite[j].getAptitud() < pob[i].getAptitud()) {
                elite[j] = pob[i].copy2();
            }
            j++;
        }
    }

    /**
     * devuelve un array con las posiciones de los minimos de la poblacion
     *
     * @param pob
     * @param numElemElite
     * @return
     */
    private ArrayList<Integer> minimosPoblacion(CromosomaAsigC[] pob, int numElemElite) {
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Double> apt = new ArrayList<>();

        for (int j = 0; j < numElemElite; j++) {
            pos.add(j);
            apt.add(elite[j].getAptitud());
        }

        if (numElemElite > pob.length) {
            return pos;
        }

        boolean salir = false;
        for (int i = 0; i < pob.length; i++) {
            for (int j = 0; j < numElemElite && !salir; j++) {
                if (apt.get(j) > pob[i].getAptitud()) {
                    apt.add(j, pob[i].getAptitud());
                    pos.add(j, i);
                    apt.remove(numElemElite);
                    pos.remove(numElemElite);
                    salir = true;
                }
            }
            salir = false;
        }
        return pos;
    }

    /**
     * devuelve un array con las posiciones de los maximos de la poblacion
     *
     * @param pob
     * @param numElemElite
     * @return
     */
    private ArrayList<Integer> maximosPoblacion(CromosomaAsigC[] pob, int numElemElite) {
        ArrayList<Integer> pos = new ArrayList<>();
        ArrayList<Double> apt = new ArrayList<>();

        for (int j = 0; j < numElemElite; j++) {
            pos.add(j);
            apt.add(elite[j].getAptitud());
        }

        if (numElemElite > pob.length) {
            return pos;
        }
        boolean salir = false;
        for (int i = 0; i < pob.length; i++) {
            for (int j = 0; j < numElemElite && !salir; j++) {
                if (apt.get(j) < pob[i].getAptitud()) {
                    apt.add(j, pob[i].getAptitud());
                    pos.add(j, i);
                    apt.remove(numElemElite);
                    pos.remove(numElemElite);
                    salir = true;
                }
            }
            salir = false;
        }
        return pos;
    }
    
    private boolean contiene(int[] x,int elem){
        for(int y : x)
            if(y == elem)
                return true;
        return false;
    }
    
    private int duplicado(int[] array){
        int ret = -1;
        for(int i = 0; i < array.length;i++)
            for(int j = 0; j < array.length;j++)
                if(array[j] == array[i] && (i != j))
                    ret = j;
        return ret;
    }

}
