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
                cargarDatos();
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

    private static void cargarDatos() {
        //x hacer
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