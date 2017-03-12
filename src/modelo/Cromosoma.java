package modelo;

public abstract class Cromosoma {
	public abstract void inicializa();
        abstract double fenotipo();
	abstract double getAptitud();
	abstract double getPuntuacion();
	abstract void setPuntuacion(double puntuacion);
        abstract int getTamaño();
}
