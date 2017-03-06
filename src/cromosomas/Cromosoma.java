package cromosomas;

public class Cromosoma {
	private boolean[] genes;
	private double fenotipo;
	private double aptitud;
	private double puntuacion;
	private double punt_acum;
	
	public Cromosoma(boolean[] gen){
		genes = gen;
	}
	
	
	public double evalua(){
		return 0;
	}
	
	public double getFenotipo() {
		return fenotipo;
	}
	public void setFenotipo(double fenotipo) {
		this.fenotipo = fenotipo;
	}
	public double getAptitud() {
		return aptitud;
	}
	public void setAptitud(double aptitud) {
		this.aptitud = aptitud;
	}
	public double getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}
	public double getPunt_acum() {
		return punt_acum;
	}
	public void setPunt_acum(double punt_acum) {
		this.punt_acum = punt_acum;
	}

	private int bin2dec(){
            int ret = 0;
            for(int i = 0; i < genes.length;i++){
                if(genes[i]){
                    ret += Math.pow(2, i);
                }
            }
            return ret;
        } 
        
	
}
