package modelo;

public abstract class Cromosoma {
	abstract double fenotipo();
	abstract double getAptitud();
	abstract double getPuntuacion();
	abstract void setPuntuacion(double puntuacion);
}
