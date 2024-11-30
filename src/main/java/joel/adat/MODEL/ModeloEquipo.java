package joel.adat.MODEL;

/**
 * Clase que representa un equipo en el contexto de eventos deportivos, 
 * con su nombre oficial e iniciales.
 */
public class ModeloEquipo {
	
	private String nombreEquipo;
	private String iniciales;

	public ModeloEquipo() {

	}

	public void setNombreEquipo(String nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}

	/**
	 * Constructor que inicializa el equipo con su nombre e iniciales.
	 * 
	 * @param nombreEquipo El nombre oficial del equipo.
	 * @param iniciales Las iniciales que representan al equipo.
	 */
	public ModeloEquipo(String nombreEquipo, String iniciales) {
		this.nombreEquipo = nombreEquipo;
		this.iniciales = iniciales;
	}

	/**
	 * Obtiene el nombre oficial del equipo.
	 * 
	 * @return Una cadena que representa el nombre del equipo.
	 */
	public String getNombreEquipo() {
		return nombreEquipo;
	}

	/**
	 * Obtiene las iniciales del equipo.
	 * 
	 * @return Una cadena que representa las iniciales del equipo.
	 */
	public String getIniciales() {
		return iniciales;
	}
	
}
