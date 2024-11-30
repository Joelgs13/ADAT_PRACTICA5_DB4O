package joel.adat.DAO;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import joel.adat.MODEL.ModeloOlimpiada;

public class DaoOlimpiada {

    public static void insertar(ModeloOlimpiada o, ObjectContainer db) {
        db.store(o);
    }

    public static List<ModeloOlimpiada> conseguirPorTemporada(String temporada, ObjectContainer db) {
        List<ModeloOlimpiada> olimpiadas = db.query(new Predicate<ModeloOlimpiada>() {

            @Override
            public boolean match(ModeloOlimpiada o) {
                return o.getTemporada().equals(temporada);
            }
        });
        return olimpiadas;
    }

    public static ModeloOlimpiada conseguirPorNombre(String nombre, ObjectContainer db) {
        ModeloOlimpiada dep = new ModeloOlimpiada();
        dep.setNombreOlimpiada(nombre);
        ObjectSet<ModeloOlimpiada> set = db.queryByExample(dep);
        return set.hasNext() ? set.next() : null;
    }
}

