package joel.adat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.db4o.ObjectContainer;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import com.db4o.ObjectContainer;
import joel.adat.BBDD.ConexionDB;
import joel.adat.MODEL.*;

import java.util.Scanner;

public class MenuOlimpiadas {
    public static void main(String[] args) {
        ObjectContainer bbdd=new ConexionDB().getConnection();
        Scanner scanner = new Scanner(System.in);
        ModeloDeporte modeloDeporte=new ModeloDeporte();
        File file = cargarArchivo(modeloDeporte);
        int opcion;

        System.out.println("antes de cargar el menu, necesito saber si se van a cargar datos o no, se van a cargar datos? (1=si y cualquier otra cosa=no)");
        try {
            opcion = scanner.nextInt();
            if (opcion==1) {
                cargarDatos(file,bbdd);
            }
        } catch (NumberFormatException e) {
            System.out.println("Supondre que no...");
        }



        do {
            // Mostrar el menú
            System.out.println("Menú:");
            System.out.println("1. Listado de deportistas participantes");
            System.out.println("2. Modificar medalla deportista");
            System.out.println("3. Añadir deportista/participación");
            System.out.println("4. Eliminar participación");
            System.out.println("0. Terminar programa");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

        } while (opcion!=0);
    }

    public static void cargarDatos(File ruta,ObjectContainer db) {
        try (CSVReader reader = new CSVReader(new FileReader(ruta))) {
            List<String[]> lineas = reader.readAll();
            lineas.remove(0); // Remover cabecera
            for (String[] linea : lineas) {
                ModeloDeporte deporte=DaoDeporte.conseguirPorNombre(linea[12],db);
                if(deporte==null) {
                    deporte=new ModeloDeporte(linea[12]);
                }
                ModeloDeportista deportista=DaoDeportista.conseguirPorNombre(linea[1], db);
                if(deportista==null) {
                    float f;
                    try {
                        f=Float.parseFloat(linea[5]);
                    }catch (NumberFormatException e) {
                        f=0f;
                    }
                    int i;
                    try {
                        i=Integer.parseInt(linea[4]);
                    }catch (NumberFormatException e) {
                        i=0;
                    }
                    deportista=new ModeloDeportista(linea[1],linea[2].charAt(0), f, i);
                }
                ModeloEquipo equipo=DaoEquipo.conseguirPorNombre(linea[6], db);
                if(equipo==null) {
                    equipo=new ModeloEquipo(linea[6], linea[7]);
                }
                ModeloOlimpiada olimpiada=DaoOlimpiada.conseguirPorNombre(linea[8], db);
                if(olimpiada==null) {
                    int i;
                    try {
                        i=Integer.parseInt(linea[9]);
                    }catch(NumberFormatException e) {
                        i=0;
                    }
                    olimpiada=new ModeloOlimpiada(linea[8],i,linea[10],linea[11]);
                }
                ModeloEvento evento=DaoEvento.conseguirPorNombre(linea[13], db);
                if(evento==null) {
                    evento=new ModeloEvento(linea[13], olimpiada, deporte);
                }
                ModeloParticipacion participacion=DaoParticipacion.conseguirPorDeportistaEvento(deportista, evento, db);
                if(participacion==null) {
                    int i;
                    try {
                        i=Integer.parseInt(linea[3]);
                    }catch(NumberFormatException e) {
                        i=0;
                    }
                    participacion=new ModeloParticipacion(deportista, evento, equipo, i, linea[14]);
                }
                DaoDeporte.insertar(deporte, db);
                DaoDeportista.insertar(deportista, db);
                DaoEquipo.insertar(equipo, db);
                DaoEvento.insertar(evento, db);
                DaoOlimpiada.insertar(olimpiada, db);
                DaoParticipacion.insertar(participacion, db);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }
    }

    private static File cargarArchivo(ModeloDeporte modeloDeporte) {
        try {
            return new File(modeloDeporte.getClass().getResource("/csv/athlete_events-sort.csv").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}