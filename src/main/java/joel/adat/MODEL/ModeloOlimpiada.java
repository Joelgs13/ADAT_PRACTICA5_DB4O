package joel.adat.MODEL;

import java.util.Objects;

/**
 * Clase que representa una olimpiada, con detalles sobre su nombre, año, temporada y ciudad.
 */
public class ModeloOlimpiada {

	private String nombreOlimpiada;
	private int anio;
	private String temporada;
	private String ciudad;
	
	/**
	 * Constructor que inicializa los detalles de una olimpiada.
	 * 
	 * @param nombreOlimpiada El nombre oficial de la olimpiada.
	 * @param anio El año en que se celebró la olimpiada.
	 * @param temporada La temporada en la que se celebró ("Winter" o "Summer").
	 * @param ciudad La ciudad donde tuvo lugar la olimpiada.
	 */
	public ModeloOlimpiada(String nombreOlimpiada, int anio, String temporada, String ciudad) {
		this.nombreOlimpiada = nombreOlimpiada;
		this.anio = anio;
		this.temporada = temporada;
		this.ciudad = ciudad;
	}
	
	/**
	 * Obtiene el nombre oficial de la olimpiada.
	 * 
	 * @return Una cadena que representa el nombre de la olimpiada.
	 */
	public String getNombreOlimpiada() {
		return nombreOlimpiada;
	}

	/**
	 * Obtiene el año de la olimpiada.
	 * 
	 * @return El año en que se celebró la olimpiada.
	 */
	public int getAnio() {
		return anio;
	}

	/**
	 * Obtiene la temporada de la olimpiada.
	 * 
	 * @return Una cadena que representa la temporada de la olimpiada ("Winter" o "Summer").
	 */
	public String getTemporada() {
		return temporada;
	}

	/**
	 * Obtiene la ciudad en la que se celebró la olimpiada.
	 * 
	 * @return Una cadena que representa la ciudad de la olimpiada.
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * Devuelve una representación en forma de cadena de la olimpiada, incluyendo
	 * el nombre, año, temporada y ciudad.
	 * 
	 * @return Una cadena con los detalles de la olimpiada.
	 */
	@Override
	public String toString() {
		return this.nombreOlimpiada + "," + this.anio + "," + this.temporada + "," + this.ciudad;
	}

	/**
	 * Calcula el código hash para la olimpiada, basado en el nombre, año, temporada y ciudad.
	 * 
	 * @return El código hash de la instancia de ModeloOlimpiada.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(anio, ciudad, nombreOlimpiada, temporada);
	}

	/**
	 * Compara esta instancia de ModeloOlimpiada con otro objeto para determinar
	 * si son iguales, basándose en el nombre, año, temporada y ciudad.
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
		ModeloOlimpiada other = (ModeloOlimpiada) obj;
		return anio == other.anio && Objects.equals(ciudad, other.ciudad)
				&& Objects.equals(nombreOlimpiada, other.nombreOlimpiada) && Objects.equals(temporada, other.temporada);
	}
}
