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

    private static void listar(Scanner input, ObjectContainer db) {
        int resp=0;
        String temporada="Summer";
        do {
            System.out.println("Dime la temporada:\n1 Winter\n2 Summer");
            resp=input.nextInt();
            input.nextLine();
        }while(resp!=1&&resp!=2);
        if(resp==1) {
            temporada="Winter";
        }
        List<ModeloOlimpiada> olimpiadas=DaoOlimpiada.conseguirPorTemporada(temporada, db);
        do {
            System.out.println("Elige la edición olímpica:");
            for(int i=0;i<olimpiadas.size();i++) {
                System.out.println((i+1)+" "+olimpiadas.get(i).getNombreOlimpiada());
            }
            resp=input.nextInt();
            input.nextLine();
        }while(resp<1||resp>olimpiadas.size());
        ModeloOlimpiada olimpiada=olimpiadas.get(resp-1);
        List<ModeloEvento> eventos=DaoEvento.conseguirPorOlimpiada(olimpiada, db);
        if(eventos.size()==0) {
            System.out.println("No hay deportes en esa olimpiada");
        }else {
            ArrayList<ModeloDeporte> deportesDisponibles=new ArrayList<ModeloDeporte>();
            for(ModeloEvento e:eventos) {
                if(!deportesDisponibles.contains(e.getDeporte())) {
                    deportesDisponibles.add(e.getDeporte());
                }
            }
            do {
                System.out.println("Elige el deporte");
                for(int i=0;i<deportesDisponibles.size();i++) {
                    System.out.println((i+1)+" "+deportesDisponibles.get(i).getNombreDeporte());
                }
                resp=input.nextInt();
                input.nextLine();
            }while(resp<1||resp>deportesDisponibles.size());
            ModeloDeporte deporte=deportesDisponibles.get(resp-1);
            List<ModeloEvento>eventosConFiltro=
                    DaoEvento.conseguirPorOlimpiadaDeporte(olimpiada, deporte, db);
            do {
                System.out.println("Elige el evento");
                for(int i=0;i<eventosConFiltro.size();i++) {
                    System.out.println((i+1)+" "+eventosConFiltro.get(i).getNombreEvento());
                }
                resp=input.nextInt();
                input.nextLine();
            }while(resp<1||resp>eventosConFiltro.size());
            ModeloEvento evento=eventosConFiltro.get(resp-1);
            List<ModeloParticipacion> participaciones=
                    DaoParticipacion.conseguirPorEvento(evento, db);
            ArrayList<ModeloDeportista> deportistas=new ArrayList<ModeloDeportista>();
            for(ModeloParticipacion par:participaciones) {
                if(!deportistas.contains(par.getDeportista())) {
                    deportistas.add(par.getDeportista());
                }
            }
            for(int i=0;i<deportistas.size();i++) {
                ModeloDeportista dep=deportistas.get(i);
                ModeloParticipacion par=
                        DaoParticipacion.conseguirPorDeportistaEvento(dep, evento, db);
                System.out.println("Nombre: "+dep.getNombreDeportista()+", Altura: "+
                        dep.getAltura()+", Peso: "+dep.getPeso()+", Edad: "+
                        par.getEdad()+", Equipo: "+par.getEquipo().getNombreEquipo()+
                        ", Medalla: "+par.getMedalla());
            }
        }

    }
}