package model;

import java.util.*;
import java.sql.*;

/**
 *
 * @author Victor Gomez-C
 */
public class Equipo {

    public static int ORDEN_NOMBRE = 0;
    public static int ORDEN_PAIS = 1;

    private int id;
    private String nombre;
    private String ciudad;
    private String pais;

    public Equipo() {
    }

    public Equipo(int id) {
        this.id = id;
    }

    public Equipo(String nombre, String ciudad, String pais) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public Equipo(int id, String nombre, String ciudad, String pais) {
        this(nombre, ciudad, pais);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    // --------- OPERACIONES BD ----------------------------------------

    // ---------- CRUD BÁSICO
    public boolean create() {
        boolean exito = true;
        try (Connection conn = ConexionBd.obtener()) {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO equipo (nombre, ciudad, pais) VALUES (?, ?, ?)")) {
                stmt.setString(1, getNombre());
                stmt.setString(2, getCiudad());
                stmt.setString(3, getPais());

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
            try (PreparedStatement stmt = conn.prepareStatement("SELECT nombre, ciudad, pais FROM equipo WHERE id = ?")) {
                stmt.setInt(1, getId());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        setNombre(rs.getString("nombre"));
                        setCiudad(rs.getString("ciudad"));
                        setPais(rs.getString("pais"));
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
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE equipo SET nombre = ?, ciudad = ?, pais = ? WHERE id = ?")) {
                stmt.setString(1, getNombre());
                stmt.setString(2, getCiudad());
                stmt.setString(3, getPais());
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
                    "DELETE FROM equipo WHERE id = ?")) {
                stmt.setInt(1, getId());

                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            exito = false;
            ex.printStackTrace();
        }
        return exito;
    }

    // ----------- Otras, de instancia, relacionadas con la fk
    public List<Jugador> getJugadores() {
        List<Jugador> resultado = new ArrayList<>();
        boolean exito = true;
        try (Connection conn = ConexionBd.obtener()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT nombre, apellidos, edad FROM jugador WHERE idequipo = ?")) {
                stmt.setInt(1, getId());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        resultado.add(new Jugador(rs.getString("nombre"), rs.getString("apellidos"), rs.getInt("edad"), getId()));
                    }
                }
            }
        } catch (SQLException ex) {
            exito = false;
            ex.printStackTrace();
        }
        return resultado;
    }

    // ----------- Otras, de clase, no relacionadas con ÉSTE (this) objeto
    public static List<Equipo> obtenerEquipos(String busqueda, int orden) {
        // Orden es una de las dos constantes de arriba: nombre o pais
        if (!(orden >= 0 && orden <= 1)) {
            throw new IllegalArgumentException("Parámetro de orden de equipos no admitido");
        }
        List<Equipo> resultado = null;
        boolean exito = true;
        try (Connection conn = ConexionBd.obtener()) {
            resultado = new ArrayList<>();
            String sql = "SELECT id, nombre, ciudad, pais FROM EQUIPO";
            if (!(busqueda.equals(""))) {
                sql = sql + " WHERE LOWER(nombre) LIKE ? OR LOWER(ciudad) LIKE ? OR LOWER(pais) LIKE ?";
                sql = orden == 0 ? sql + " ORDER BY nombre" : sql + " ORDER BY pais";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    String busquedaSql = "%" + busqueda.toLowerCase() + "%";
                    stmt.setString(1, busquedaSql);
                    stmt.setString(2, busquedaSql);
                    stmt.setString(3, busquedaSql);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            resultado.add(new Equipo(rs.getInt("id"), rs.getString("nombre"), rs.getString("ciudad"), rs.getString("pais")));
                        }
                    }
                }
            } else {
                sql = orden == 0 ? sql + " ORDER BY nombre" : sql + " ORDER BY pais";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            resultado.add(new Equipo(rs.getInt("id"), rs.getString("nombre"), rs.getString("ciudad"), rs.getString("pais")));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            exito = false;
            ex.printStackTrace();
        }

        if (exito) {
            return resultado;
        } else {
            return null;
        }
    }

}
