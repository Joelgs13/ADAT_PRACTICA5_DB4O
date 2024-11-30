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
import joel.adat.DAO.*;
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

            switch (opcion){
                case 1:
                    listar(scanner,bbdd);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 0:
                    System.out.print("Terminando programa");
                    break;
                default:
                    break;
            }

        } while (opcion!=0);


    }

    public static void cargarDatos(File ruta,ObjectContainer db) {
        try (CSVReader reader = new CSVReader(new FileReader(ruta))) {
            List<String[]> lineas = reader.readAll();
            lineas.removeFirst(); // Remover cabecera
            for (String[] linea : lineas) {
                ModeloDeporte deporte= DaoDeporte.conseguirPorNombre(linea[12],db);
                if(deporte==null) {
                    deporte=new ModeloDeporte(linea[12]);
                }
                ModeloDeportista deportista= DaoDeportista.conseguirPorNombre(linea[1], db);
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
                ModeloEquipo equipo= DaoEquipo.conseguirPorNombre(linea[6], db);
                if(equipo==null) {
                    equipo=new ModeloEquipo(linea[6], linea[7]);
                }
                ModeloOlimpiada olimpiada= DaoOlimpiada.conseguirPorNombre(linea[8], db);
                if(olimpiada==null) {
                    int i;
                    try {
                        i=Integer.parseInt(linea[9]);
                    }catch(NumberFormatException e) {
                        i=0;
                    }
                    olimpiada=new ModeloOlimpiada(linea[8],i,linea[10],linea[11]);
                }
                ModeloEvento evento= DaoEvento.conseguirPorNombre(linea[13], db);
                if(evento==null) {
                    evento=new ModeloEvento(linea[13], deporte, olimpiada);
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

    private static void listar(Scanner scanner, ObjectContainer bbdd) {
        int resp;
        String temporada = "Summer";

        System.out.println("Selecciona la temporada olimpica:");
        System.out.println("1. Winter");
        System.out.println("2. Summer");
        do {
            System.out.print("Tu elección: ");
            resp = scanner.nextInt();
            scanner.nextLine();
        } while (resp != 1 && resp != 2);

        if (resp == 1) {
            temporada = "Winter";
        }

        List<ModeloOlimpiada> olimpiadas = DaoOlimpiada.conseguirPorTemporada(temporada, bbdd);
        if (olimpiadas.isEmpty()) {
            System.out.println("No se encontraron olimpiadas para la temporada seleccionada.");
            return;
        }

        System.out.println("\nEdiciones olímpicas:");
        for (int i = 0; i < olimpiadas.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, olimpiadas.get(i).getNombreOlimpiada());
        }

        do {
            System.out.print("Selecciona una edición olímpica de las anteriores: ");
            resp = scanner.nextInt();
            scanner.nextLine();
        } while (resp < 1 || resp > olimpiadas.size());

        ModeloOlimpiada olimpiada = olimpiadas.get(resp - 1);
        List<ModeloEvento> eventos = DaoEvento.conseguirPorOlimpiada(olimpiada, bbdd);

        if (eventos.isEmpty()) {
            System.out.println("No hay eventos disponibles para esta olimpiada.");
            return;
        }

        List<ModeloDeporte> deportesDisponibles = new ArrayList<>();
        for (ModeloEvento e : eventos) {
            if (!deportesDisponibles.contains(e.getDeporte())) {
                deportesDisponibles.add(e.getDeporte());
            }
        }

        System.out.println("\nDeportes disponibles:");
        for (int i = 0; i < deportesDisponibles.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, deportesDisponibles.get(i).getNombreDeporte());
        }

        do {
            System.out.print("Selecciona un deporte de los anteriores: ");
            resp = scanner.nextInt();
            scanner.nextLine();
        } while (resp < 1 || resp > deportesDisponibles.size());

        ModeloDeporte deporte = deportesDisponibles.get(resp - 1);
        List<ModeloEvento> eventosConFiltro = DaoEvento.conseguirPorOlimpiadaDeporte(olimpiada, deporte, bbdd);

        System.out.println("\nEventos disponibles:");
        for (int i = 0; i < eventosConFiltro.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, eventosConFiltro.get(i).getNombreEvento());
        }

        do {
            System.out.print("Selecciona un evento de los anteriores: ");
            resp = scanner.nextInt();
            scanner.nextLine();
        } while (resp < 1 || resp > eventosConFiltro.size());

        ModeloEvento evento = eventosConFiltro.get(resp - 1);
        List<ModeloParticipacion> participaciones = DaoParticipacion.conseguirPorEvento(evento, bbdd);

        if (participaciones.isEmpty()) {
            System.out.println("No hay participantes en este evento.");
            return;
        }

        System.out.println("\nParticipantes:");
        for (ModeloParticipacion par : participaciones) {
            ModeloDeportista dep = par.getDeportista();
            System.out.printf(
                    "Nombre: %s | Altura: %.2f m | Peso: %.2f kg | Edad: %d años | Equipo: %s | Medalla: %s\n",
                    dep.getNombreDeportista(),
                    dep.getAltura(),
                    dep.getPeso(),
                    par.getEdad(),
                    par.getEquipo().getNombreEquipo(),
                    par.getMedalla() != null ? par.getMedalla() : "Sin medalla"
            );
        }
    }

}