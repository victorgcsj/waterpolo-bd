
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Victor Gomez-C
 */
public class ConexionBd {

    private static String URL = "jdbc:h2:/./waterpolo";
    private static String USER = "sa";
    private static String PASS = "";
    private static String CREATION_STATEMENT
            = "CREATE TABLE equipo (\n"
            + "   id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "   nombre VARCHAR(30),\n"
            + "   ciudad VARCHAR(30),\n"
            + "   pais VARCHAR(30)\n"
            + ");\n"
            + "CREATE TABLE jugador (\n"
            + "   id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "   nombre VARCHAR(30),\n"
            + "   apellidos VARCHAR(30),\n"
            + "   edad INT,\n"
            + "   idequipo int NOT NULL,\n"
            + "   FOREIGN KEY (idequipo) REFERENCES equipo(id) ON DELETE CASCADE ON UPDATE CASCADE\n"
            + ")";

    static {
        inicializar();
    }

    /**
     *
     */
    private static void inicializar() {
        boolean hayErrorGrave = false;
        // Cargar el driver
        try {
            System.err.println("INFO: Cargando driver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("ERROR: El Driver de h2 no está disponible");
            hayErrorGrave = true;
        }

        //Comprobar si la Bd ya esta creada.
        System.err.println("INFO: Comprobando si la BD ya está creada");
        boolean bdExiste = true;
        try {
            /* H2 crea la BD si no existe al acceder por primera vez.
               Añadiendo ;ifexists=true al abrir una conexión de h2, no la crea
               si no existe y lanza una excepción.
               Intentamos conectarnos con ifexists=true y si hay excepción es que
               la BD no existe. Cerramos la conexión inmediatamente.
             */
            DriverManager.getConnection(URL + ";IFEXISTS=TRUE", USER, PASS).close();
        } catch (SQLException ex) {
            bdExiste = false;
        }

        // Si no existe la BD nos conectamos normalmente para que la cree, y lanzamos la
        // sentencia de creacion de la tabla.
        if (!bdExiste) {
            try {
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                conn.prepareStatement(CREATION_STATEMENT).executeUpdate();
                System.err.println("INFO: Creando BD + tabla vacía");
            } catch (SQLException ex) {
                System.err.println("ERROR: no se pudo crear la tabla");
                ex.printStackTrace();
                hayErrorGrave = true;
            }
        }
        if (hayErrorGrave) {
            System.err.println("*** Se encontaron errores graves relacionados con la BD. Abortando la ejecución");
            System.exit(1); // Aborta la ejecución de la app.
        }
    }

    /**
     * Crea una conexion con los parámetros obtenidos de las constantes de
     * arriba
     *
     * @return la Conexión a la DB
     * @throws SQLException
     */
    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(
                URL, USER, PASS
        );
    }

}
