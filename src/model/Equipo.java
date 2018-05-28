/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author victor
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
        boolean todoOk = true;
        try (Connection conn = ConexionBd.obtener()) {
            String sql = "INSERT INTO equipo(nombre, ciudad, pais) "
                    + "VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, this.getNombre());
                stmt.setString(2, this.getCiudad());
                stmt.setString(3, this.getPais());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            todoOk = false;
            ex.printStackTrace();
        }
        return todoOk;
    }

    public boolean retrieve() {
        // POR HACER
        setId(33);
        setNombre("Equipo ejemplo");
        setCiudad("Ciudad ejemplo");
        setPais("Pais ejemplo");
        return true;
    }

    public boolean update() {
        return true;
    }

    public boolean delete() {
        return true;
    }

    // ----------- Otras, de instancia, relacionadas con la fk
    public List<Jugador> getJugadores() {
        // POR HACER.
        List<Jugador> resultado = new ArrayList<>();
        resultado.add(new Jugador(1, "Paco", "López", 19));
        resultado.add(new Jugador(2, "Luisa", "Martínez", 21));
        return resultado;
    }

    // ----------- Otras, de clase, no relacionadas con ÉSTE (this) objeto
    public static List<Equipo> obtenerEquipos(String busqueda, int orden) {
        // Orden es una de las dos constantes de arriba: nombre o pais
        if (!(orden >= 0 && orden <= 1)) {
            throw new IllegalArgumentException("Parámetro de orden de equipos no admitido");
        }
        List<Equipo> resultado = null;
        boolean todoOk = true;
        try (Connection conn = ConexionBd.obtener()) {
            resultado = new ArrayList<>();
            String sql = "SELECT id, nombre, ciudad, pais FROM equipo ";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        resultado.add(new Equipo(
                                rs.getInt("id"), rs.getString("nombre"), rs.getString("ciudad"), rs.getString("pais")
                        ));
                    }
                }
            }
        } catch (SQLException ex) {
            todoOk = false;
            ex.printStackTrace();
        }
        return resultado;
    }

}


