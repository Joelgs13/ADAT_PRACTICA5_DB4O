package joel.adat.DAO;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import joel.adat.MODEL.ModeloDeporte;
import joel.adat.MODEL.ModeloEvento;
import joel.adat.MODEL.ModeloOlimpiada;

public class DaoEvento {

	public static void insertar(ModeloEvento e, ObjectContainer db) {
		db.store(e);
	}

	public static ModeloEvento conseguirPorNombre(String nombre, ObjectContainer db) {
		ModeloEvento e = new ModeloEvento();
		e.setNombreEvento(nombre);
		ObjectSet<ModeloEvento> set = db.queryByExample(e);
		return set.hasNext() ? set.next() : null;
	}

	public static List<ModeloEvento> conseguirPorOlimpiada(ModeloOlimpiada o, ObjectContainer db) {
		List<ModeloEvento> eventos = db.query(new Predicate<ModeloEvento>() {

			@Override
			public boolean match(ModeloEvento e) {
				return e.getOlimpiada().equals(o);
			}
		});
		return eventos;
	}

	public static List<ModeloEvento> conseguirPorOlimpiadaDeporte(ModeloOlimpiada o, ModeloDeporte d, ObjectContainer db) {
		List<ModeloEvento> eventos = db.query(new Predicate<ModeloEvento>() {

			@Override
			public boolean match(ModeloEvento e) {
				return e.getOlimpiada().equals(o) && e.getDeporte().equals(d);
			}
		});
		return eventos;
	}
}
