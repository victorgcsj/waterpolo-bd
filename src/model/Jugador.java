package model;

import java.util.*;
import java.sql.*;

/**
 *
 * @author Victor Gomez-C
 */
public class Jugador {

    private int id;
    private String nombre;
    private String apellidos;
    private int edad;
    private int idEquipo;

    public Jugador() {

    }

    public Jugador(int id) {
        this.id = id;
    }

    public Jugador(String nombre, String apellidos, int edad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
    }

    public Jugador(int id, String nombre, String apellidos, int edad) {
        this(nombre, apellidos, edad);
        this.id = id;
    }

    public Jugador(String nombre, String apellidos, int edad, int idEquipo) {
        this(nombre, apellidos, edad);
        this.idEquipo = idEquipo;
    }

    public Jugador(int id, String nombre, String apellidos, int edad, int idEquipo) {
        this(id, nombre, apellidos, edad);
        this.idEquipo = idEquipo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    // --------- OPERACIONES BD ----------------------------------------
    // ---------- CRUD BÁSICO
    public boolean create() {
        boolean exito = true;
        try (Connection conn = ConexionBd.obtener()) {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO jugador (nombre, apellidos, edad, idequipo) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, getNombre());
                stmt.setString(2, getApellidos());
                stmt.setInt(3, getEdad());
                stmt.setInt(4, getIdEquipo());

                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            exito = false;
            ex.printStackTrace();
        }

        return exito;
    }

    public boolean retrieve() {
        boolean exito = true;
        try (Connection conn = ConexionBd.obtener()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT nombre, apellidos, edad, idequipo FROM jugador WHERE id = ?")) {
                stmt.setInt(1, getId());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        setNombre(rs.getString("nombre"));
                        setApellidos(rs.getString("apellidos"));
                        setEdad(rs.getInt("edad"));
                        setIdEquipo(rs.getInt("idequipo"));
                    }
                }
            }
        } catch (SQLException ex) {
            exito = false;
            ex.printStackTrace();
        }

        return exito;
    }

    public boolean update() {
        boolean exito = true;
        try (Connection conn = ConexionBd.obtener()) {
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE jugador SET nombre = ?, apellidos = ?, idequipo = ? WHERE id = ?")) {
                stmt.setString(1, getNombre());
                stmt.setString(2, getApellidos());
                stmt.setInt(3, getIdEquipo());
                stmt.setInt(4, getId());

                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            exito = false;
            ex.printStackTrace();
        }
        return exito;
    }

    public boolean delete() {
        boolean exito = true;
        try (Connection conn = ConexionBd.obtener()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM jugador WHERE id = ?")) {
                stmt.setInt(1, getId());

                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            exito = false;
            ex.printStackTrace();
        }
        return exito;
    }

    // ----------- Otras, de clase, no relacionadas con ÉSTE (this) objeto
    public static List<Jugador> obtenerJugadores(String busqueda, boolean esJunior, boolean esClass, boolean esMaster) {
        /* Junior:  14 años o más y menos de 18.
        Class: 18 o más y menos de 26.
        Master: 26 años o más. */
        List<Jugador> resultado = new ArrayList<>();
        boolean todoOk = true;
        try (Connection conn = ConexionBd.obtener()) {

            String sql = "SELECT id, nombre, apellidos, edad, idequipo FROM jugador";
            
                sql = sql + " WHERE LOWER(nombre) LIKE ? OR LOWER(apellidos) LIKE ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    String busquedaSql = "%" + busqueda.toLowerCase() + "%";
                    stmt.setString(1, busquedaSql);
                    stmt.setString(2, busquedaSql);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            if (esJunior && rs.getInt("edad") >= 14 && rs.getInt("edad") < 18) {
                                resultado.add(new Jugador(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellidos"), rs.getInt("edad"), rs.getInt("idequipo")));
                            } else if (esClass && rs.getInt("edad") >= 18 && rs.getInt("edad") < 26) {
                                resultado.add(new Jugador(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellidos"), rs.getInt("edad"), rs.getInt("idequipo")));
                            } else if (esMaster && rs.getInt("edad") > 26) {
                                resultado.add(new Jugador(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellidos"), rs.getInt("edad"), rs.getInt("idequipo")));
                            } else if (!(esJunior) && !(esClass) && !(esMaster)) {
                                resultado.add(new Jugador(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellidos"), rs.getInt("edad"), rs.getInt("idequipo")));
                            }
                        }
                    }
                }
        } catch (SQLException ex) {
            todoOk = false;
            ex.printStackTrace();
        }
        if (todoOk) {
            return resultado;
        } else {
            return null;
        }
    }
}
