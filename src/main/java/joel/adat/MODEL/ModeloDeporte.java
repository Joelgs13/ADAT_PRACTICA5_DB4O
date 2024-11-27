package joel.adat.MODEL;

import java.util.Objects;

public class ModeloDeporte {

	private String nombreDeporte;


	public ModeloDeporte(String nombre) {
		this.nombreDeporte = nombre;
	}
	

	public String getNombreDeporte() {
		return nombreDeporte;
	}
	

	@Override
	public String toString() {
		return this.nombreDeporte;
	}


	@Override
	public int hashCode() {
		return Objects.hash(nombreDeporte);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModeloDeporte other = (ModeloDeporte) obj;
		return Objects.equals(nombreDeporte, other.nombreDeporte);
	}
}
