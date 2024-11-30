package joel.adat.MODEL;

/**
 * Clase que representa un deportista, con su nombre, sexo, altura y peso.
 */
public class ModeloDeportista {
	
	private String nombreDeportista;
	private char sexo;
	private float altura;
	private float peso;

	public ModeloDeportista() {

	}

	public void setNombreDeportista(String nombreDeportista) {
		this.nombreDeportista = nombreDeportista;
	}

	/**
	 * Constructor que inicializa los atributos de un deportista.
	 * 
	 * @param nombreDeportista El nombre completo del deportista.
	 * @param sexo El sexo del deportista ('M' para masculino, 'F' para femenino).
	 * @param altura La altura del deportista en centímetros.
	 * @param peso El peso del deportista en kilogramos.
	 */
	public ModeloDeportista(String nombreDeportista, char sexo, float altura, float peso) {
		this.nombreDeportista = nombreDeportista;
		this.sexo = sexo;
		this.altura = altura;
		this.peso = peso;
	}

	/**
	 * Obtiene el nombre del deportista.
	 * 
	 * @return Una cadena que representa el nombre del deportista.
	 */
	public String getNombreDeportista() {
		return nombreDeportista;
	}

	/**
	 * Obtiene el sexo del deportista.
	 * 
	 * @return El sexo del deportista ('M' para masculino, 'F' para femenino).
	 */
	public char getSexo() {
		return sexo;
	}

	/**
	 * Obtiene la altura del deportista.
	 * 
	 * @return La altura del deportista en centímetros.
	 */
	public float getAltura() {
		return altura;
	}

	/**
	 * Obtiene el peso del deportista.
	 * 
	 * @return El peso del deportista en kilogramos.
	 */
	public float getPeso() {
		return peso;
	}
	
	/**
	 * Devuelve una representación en forma de cadena del deportista, 
	 * incluyendo su nombre, sexo, altura y peso.
	 * 
	 * @return Una cadena que representa al deportista con sus detalles.
	 */
	@Override
	public String toString() {
		return this.nombreDeportista + "," + this.sexo + "," + this.altura + "," + this.peso;
	}
	
}
