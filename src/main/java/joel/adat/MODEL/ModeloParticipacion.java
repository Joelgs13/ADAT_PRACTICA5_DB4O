package joel.adat.MODEL;

/**
 * Clase que representa la participación de un deportista en un evento específico, 
 * incluyendo su equipo, edad y tipo de medalla obtenida.
 */
public class ModeloParticipacion {
	
	private ModeloDeportista deportista;
	private ModeloEvento evento;
	private ModeloEquipo equipo;
	private int edad;
	private String medalla;
	
	/**
	 * Constructor que inicializa la participación de un deportista en un evento.
	 * 
	 * @param deportista El modelo del deportista que participa.
	 * @param evento El modelo del evento en el que participa el deportista.
	 * @param equipo El modelo del equipo al que pertenece el deportista.
	 * @param edad La edad del deportista en el momento de la participación.
	 * @param medalla La medalla obtenida por el deportista (puede ser "NA" si no se obtuvo medalla).
	 */
	public ModeloParticipacion(ModeloDeportista deportista, ModeloEvento evento, ModeloEquipo equipo, int edad, String medalla) {
		this.deportista = deportista;
		this.evento = evento;
		this.equipo = equipo;
		this.edad = edad;
		this.medalla = medalla;
	}
	
	/**
	 * Devuelve una representación en forma de cadena de la participación, 
	 * incluyendo el nombre del deportista, su altura, peso, edad, nombre del equipo y medalla obtenida.
	 * 
	 * @return Una cadena con los detalles de la participación.
	 */
	@Override
	public String toString() {
		return this.deportista.getNombreDeportista() + "," + this.deportista.getAltura() + "," + this.deportista.getPeso() +
				"," + this.edad + "," + this.equipo.getNombreEquipo() + "," + this.medalla;
	}
	
	/**
	 * Obtiene el deportista que participa.
	 * 
	 * @return Un objeto ModeloDeportista correspondiente al deportista.
	 */
	public ModeloDeportista getDeportista() {
		return deportista;
	}

	/**
	 * Obtiene el evento en el que participa el deportista.
	 * 
	 * @return Un objeto ModeloEvento correspondiente al evento.
	 */
	public ModeloEvento getEvento() {
		return evento;
	}

	/**
	 * Obtiene el equipo al que pertenece el deportista.
	 * 
	 * @return Un objeto ModeloEquipo correspondiente al equipo.
	 */
	public ModeloEquipo getEquipo() {
		return equipo;
	}

	/**
	 * Obtiene la edad del deportista al momento de la participación.
	 * 
	 * @return La edad del deportista.
	 */
	public int getEdad() {
		return edad;
	}

	/**
	 * Obtiene el tipo de medalla obtenida por el deportista en el evento.
	 * 
	 * @return Una cadena que representa la medalla obtenida ("Gold", "Silver", "Bronze" o "NA").
	 */
	public String getMedalla() {
		return medalla;
	}
}
