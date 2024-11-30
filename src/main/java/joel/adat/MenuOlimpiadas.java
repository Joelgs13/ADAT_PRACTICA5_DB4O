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
                    modificar(scanner,bbdd);
                    break;
                case 3:
                    aniadir(scanner,bbdd);
                    break;
                case 4:
                    eliminar(scanner,bbdd);
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

    private static void modificar(Scanner scanner, ObjectContainer bbdd) {
        int resp = 0;
        List<ModeloDeportista> deportistas;

        System.out.println("=== Modificar Medalla de Participación ===");

        do {
            System.out.print("Introduce el nombre o parte del nombre del deportista: ");
            String nombre = scanner.nextLine();
            deportistas = DaoDeportista.conseguirPorFragmentoNombre(nombre, bbdd);

            if (deportistas == null || deportistas.isEmpty()) {
                System.out.println(" No se encontraron deportistas con ese nombre. Inténtalo de nuevo.");
            } else {
                System.out.println("\nDeportistas encontrados:");
                for (int i = 0; i < deportistas.size(); i++) {
                    System.out.println((i + 1) + ". " + deportistas.get(i).getNombreDeportista());
                }
                System.out.print("Selecciona el número del deportista: ");
                resp = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
            }
        } while (resp < 1 || resp > (deportistas == null ? 0 : deportistas.size()));

        ModeloDeportista deportista = deportistas.get(resp - 1);
        List<ModeloParticipacion> participaciones = DaoParticipacion.conseguirPorDeportista(deportista, bbdd);
        ArrayList<ModeloEvento> eventos = new ArrayList<>();

        for (ModeloParticipacion p : participaciones) {
            if (!eventos.contains(p.getEvento())) {
                eventos.add(p.getEvento());
            }
        }

        System.out.println("\n=== Selección de Evento ===");
        do {
            System.out.println("Eventos disponibles:");
            for (int i = 0; i < eventos.size(); i++) {
                System.out.println((i + 1) + ". " + eventos.get(i).getNombreEvento());
            }
            System.out.print("Elige el número del evento: ");
            resp = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
        } while (resp < 1 || resp > eventos.size());

        ModeloEvento evento = eventos.get(resp - 1);

        System.out.println("\n=== Selección de Medalla ===");
        do {
            System.out.println("Opciones disponibles:");
            System.out.println("1.  Oro (Gold)");
            System.out.println("2.  Plata (Silver)");
            System.out.println("3.  Bronce (Bronze)");
            System.out.println("4. Sin Medalla (NA)");
            System.out.print("Selecciona la medalla: ");
            resp = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
        } while (resp < 1 || resp > 4);

        String medalla = switch (resp) {
            case 1 -> "Gold";
            case 2 -> "Silver";
            case 3 -> "Bronze";
            default -> "NA";
        };

        DaoParticipacion.actualizarMedallas(medalla, deportista, evento, bbdd);

        System.out.println("\n Medalla actualizada correctamente:");
        System.out.println("Deportista: " + deportista.getNombreDeportista());
        System.out.println("Evento: " + evento.getNombreEvento());
        System.out.println("Nueva Medalla: " + medalla);
    }

    private static void aniadir(Scanner scanner, ObjectContainer bbdd) {
        int resp = 0;
        List<ModeloDeportista> deportistas = null;
        boolean nuevoDeportista = false;
        ModeloDeportista deportista = null;

        do {
            System.out.println("Introduce el nombre del deportista a buscar:");
            String nombre = scanner.nextLine();
            deportistas = DaoDeportista.conseguirPorFragmentoNombre(nombre, bbdd);

            if (deportistas == null || deportistas.isEmpty()) {
                System.out.println("No se encontró al deportista, creando uno nuevo...");
                deportista = new ModeloDeportista();
                deportista.setNombreDeportista(nombre);
                DaoDeportista.insertar(deportista, bbdd);
                nuevoDeportista = true;
            } else {
                System.out.println("\nDeportistas encontrados:");
                for (int i = 0; i < deportistas.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, deportistas.get(i).getNombreDeportista());
                }
                System.out.print("Selecciona el número del deportista: ");
                resp = scanner.nextInt();
                scanner.nextLine();
            }
        } while ((resp < 1 || resp > deportistas.size()) && !nuevoDeportista);

        if (!nuevoDeportista) {
            deportista = deportistas.get(resp - 1);
        }

        String temporada = seleccionarTemporada(scanner);
        ModeloOlimpiada olimpiada = seleccionarOlimpiada(temporada, bbdd, scanner);
        ModeloEvento evento = seleccionarEvento(olimpiada, bbdd, scanner);

        if (evento != null) {
            DaoParticipacion.insertar(new ModeloParticipacion(deportista, evento,
                    new ModeloEquipo("EjemploEquipo", "EEE"), 0, temporada), bbdd);
            System.out.println("\n Participación añadida exitosamente.");
        } else {
            System.out.println("\n No se ha podido añadir la participación, ya que no hay eventos disponibles.");
        }
    }

    private static String seleccionarTemporada(Scanner scanner) {
        int resp;
        String temporada = "Summer";

        do {
            System.out.println("\nSelecciona la temporada:");
            System.out.println("1. Invierno (Winter)");
            System.out.println("2. Verano (Summer)");
            System.out.print("Tu elección: ");
            resp = scanner.nextInt();
            scanner.nextLine();
        } while (resp != 1 && resp != 2);

        if (resp == 1) {
            temporada = "Winter";
        }

        return temporada;
    }

    private static ModeloOlimpiada seleccionarOlimpiada(String temporada, ObjectContainer db, Scanner input) {
        int resp;
        List<ModeloOlimpiada> olimpiadas = DaoOlimpiada.conseguirPorTemporada(temporada, db);

        if (olimpiadas.isEmpty()) {
            System.out.println(" No se encontraron olimpiadas para la temporada " + temporada);
            return null;
        }

        System.out.println("\nElige la edición olímpica:");
        for (int i = 0; i < olimpiadas.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, olimpiadas.get(i).getNombreOlimpiada());
        }

        do {
            System.out.print("Tu elección: ");
            resp = input.nextInt();
            input.nextLine();
        } while (resp < 1 || resp > olimpiadas.size());

        return olimpiadas.get(resp - 1);
    }

    private static ModeloEvento seleccionarEvento(ModeloOlimpiada olimpiada, ObjectContainer bbdd, Scanner scanner) {
        int resp;
        List<ModeloEvento> eventos = DaoEvento.conseguirPorOlimpiada(olimpiada, bbdd);

        if (eventos.isEmpty()) {
            System.out.println(" No hay eventos disponibles para esta olimpiada.");
            return null;
        }

        List<ModeloDeporte> deportesDisponibles = obtenerDeportesDisponibles(eventos);

        System.out.println("\nSelecciona el deporte:");
        for (int i = 0; i < deportesDisponibles.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, deportesDisponibles.get(i).getNombreDeporte());
        }

        do {
            System.out.print("Tu elección: ");
            resp = scanner.nextInt();
            scanner.nextLine();
        } while (resp < 1 || resp > deportesDisponibles.size());

        ModeloDeporte deporte = deportesDisponibles.get(resp - 1);

        List<ModeloEvento> eventosConFiltro = DaoEvento.conseguirPorOlimpiadaDeporte(olimpiada, deporte, bbdd);

        System.out.println("\nSelecciona el evento:");
        for (int i = 0; i < eventosConFiltro.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, eventosConFiltro.get(i).getNombreEvento());
        }

        do {
            System.out.print("Tu elección: ");
            resp = scanner.nextInt();
            scanner.nextLine();
        } while (resp < 1 || resp > eventosConFiltro.size());

        return eventosConFiltro.get(resp - 1);
    }

    private static List<ModeloDeporte> obtenerDeportesDisponibles(List<ModeloEvento> eventos) {
        List<ModeloDeporte> deportesDisponibles = new ArrayList<>();
        for (ModeloEvento e : eventos) {
            if (!deportesDisponibles.contains(e.getDeporte())) {
                deportesDisponibles.add(e.getDeporte());
            }
        }
        return deportesDisponibles;
    }

    private static void eliminar(Scanner scanner, ObjectContainer bbdd) {
        int resp = 0;
        List<ModeloDeportista> deportistas = null;

        do {
            System.out.println("Introduce el nombre del deportista a buscar:");
            String nombre = scanner.nextLine();
            deportistas = DaoDeportista.conseguirPorFragmentoNombre(nombre, bbdd);

            if (deportistas == null || deportistas.isEmpty()) {
                System.out.println(" No se encontraron deportistas con ese nombre.");
            } else {
                System.out.println("\nDeportistas encontrados:");
                for (int i = 0; i < deportistas.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, deportistas.get(i).getNombreDeportista());
                }
                System.out.print("Selecciona el número del deportista: ");
                resp = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
            }
        } while (resp < 1 || resp > deportistas.size());

        ModeloDeportista deportista = deportistas.get(resp - 1);
        List<ModeloParticipacion> participaciones = DaoParticipacion.conseguirPorDeportista(deportista, bbdd);

        if (participaciones.isEmpty()) {
            System.out.println(" El deportista seleccionado no tiene participaciones registradas.");
            return;
        }

        System.out.println("\nSelecciona la participación que deseas eliminar:");
        for (int i = 0; i < participaciones.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, participaciones.get(i).getEvento().getNombreEvento());
        }

        do {
            System.out.print("Tu elección: ");
            resp = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
        } while (resp < 1 || resp > participaciones.size());

        ModeloParticipacion participacionSeleccionada = participaciones.get(resp - 1);
        DaoParticipacion.eliminar(deportista, participacionSeleccionada.getEvento(), bbdd);

        System.out.println("\n Participación eliminada correctamente.");
    }



}