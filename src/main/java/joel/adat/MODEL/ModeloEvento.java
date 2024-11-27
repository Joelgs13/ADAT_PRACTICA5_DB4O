package joel.adat.MODEL;

import java.util.Objects;

/**
 * Clase que representa un evento deportivo en el contexto de una olimpiada, 
 * incluyendo el nombre del evento, el deporte al que pertenece y la olimpiada 
 * en la que se realizó.
 */
public class ModeloEvento {

	private String nombreEvento;
	private ModeloDeporte deporte;
	private ModeloOlimpiada olimpiada;

	/**
	 * Constructor para inicializar un evento con su nombre, deporte y olimpiada asociada.
	 * 
	 * @param nombreEvento El nombre del evento deportivo.
	 * @param deporte El deporte al que pertenece el evento.
	 * @param olimpiada La olimpiada en la que se realizó el evento.
	 */
	public ModeloEvento(String nombreEvento, ModeloDeporte deporte, ModeloOlimpiada olimpiada) {
		this.nombreEvento = nombreEvento;
		this.deporte = deporte;
		this.olimpiada = olimpiada;
	}

	/**
	 * Obtiene el nombre del evento deportivo.
	 * 
	 * @return Una cadena que representa el nombre del evento.
	 */
	public String getNombreEvento() {
		return nombreEvento;
	}

	/**
	 * Obtiene el deporte al que pertenece el evento.
	 * 
	 * @return Una instancia de ModeloDeporte que representa el deporte del evento.
	 */
	public ModeloDeporte getDeporte() {
		return deporte;
	}

	/**
	 * Obtiene la olimpiada en la que se realizó el evento.
	 * 
	 * @return Una instancia de ModeloOlimpiada que representa la olimpiada del evento.
	 */
	public ModeloOlimpiada getOlimpiada() {
		return olimpiada;
	}

	/**
	 * Devuelve una representación en forma de cadena del evento, que incluye 
	 * el nombre del evento, el deporte y la olimpiada.
	 * 
	 * @return Una cadena con los detalles del evento.
	 */
	@Override
	public String toString() {
		return this.nombreEvento + "," + this.deporte.getNombreDeporte() + "," + this.olimpiada.getNombreOlimpiada();
	}

	/**
	 * Calcula el código hash para el evento, basado en el nombre del evento, 
	 * el deporte y la olimpiada.
	 * 
	 * @return El código hash de la instancia de ModeloEvento.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(deporte, nombreEvento, olimpiada);
	}

	/**
	 * Compara esta instancia de ModeloEvento con otro objeto para determinar si 
	 * son iguales, basándose en el nombre del evento, el deporte y la olimpiada.
	 * 
	 * @param obj El objeto a comparar con la instancia actual.
	 * @return true si los objetos son iguales; false en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModeloEvento other = (ModeloEvento) obj;
		return Objects.equals(deporte, other.deporte) && Objects.equals(nombreEvento, other.nombreEvento)
				&& Objects.equals(olimpiada, other.olimpiada);
	}
}
