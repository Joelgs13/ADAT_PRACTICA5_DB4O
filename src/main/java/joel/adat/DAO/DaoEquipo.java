package joel.adat.DAO;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import joel.adat.MODEL.ModeloEquipo;

public class DaoEquipo {

	public static void insertar(ModeloEquipo e, ObjectContainer db) {
		db.store(e);
	}

	public static ModeloEquipo conseguirPorNombre(String nombre, ObjectContainer db) {
		ModeloEquipo dep = new ModeloEquipo();
		dep.setNombreEquipo(nombre);
		ObjectSet<ModeloEquipo> set = db.queryByExample(dep);
		return set.hasNext() ? set.next() : null;
	}
}