package com.uniajc.dao;

import com.uniajc.config.ConexionDatabase;
import com.uniajc.modelo.Docente;
import java.sql.*;
import java.util.*;

public class DocenteDao {

    // CREATE
    public void guardar(Docente d) {
        String sql = "INSERT INTO docente(nombre, especialidad) VALUES (?, ?)";
        try (Connection c = ConexionDatabase.getConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getEspecialidad());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) d.setIdDocente(k.getInt(1)); }
        } catch (SQLException e) { throw new DaoException("Error al guardar docente: " + e.getMessage(), e); }
    }

    // READ - All
    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        try (Connection c = ConexionDatabase.getConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM docente ORDER BY id_docente")) {
            while (rs.next()) lista.add(mapearDocente(rs));
        } catch (SQLException e) { throw new DaoException("Error al listar docentes: " + e.getMessage(), e); }
        return lista;
    }

    // READ - By ID
    public Optional<Docente> buscarPorId(int id) {
        return consultarUno("SELECT * FROM docente WHERE id_docente = ?", ps -> ps.setInt(1, id));
    }

    // READ - By Field (nombre)
    public Optional<Docente> buscarPorCampo(String campo) {
        return consultarUno("SELECT * FROM docente WHERE LOWER(nombre) = LOWER(?) LIMIT 1", ps -> ps.setString(1, campo));
    }

    // UPDATE
    public void actualizar(Docente d) {
        String sql = "UPDATE docente SET nombre = ?, especialidad = ? WHERE id_docente = ?";
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getEspecialidad());
            ps.setInt(3, d.getIdDocente());
            ps.executeUpdate();
        } catch (SQLException e) { throw new DaoException("Error al actualizar docente: " + e.getMessage(), e); }
    }

    // DELETE
    public void eliminar(int id) {
        try (Connection c = ConexionDatabase.getConexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM docente WHERE id_docente = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new DaoException("Error al eliminar docente: " + e.getMessage(), e); }
    }

    // Helper: ResultSet -> objeto
    private Docente mapearDocente(ResultSet rs) throws SQLException {
        return new Docente(rs.getInt("id_docente"), rs.getString("nombre"), rs.getString("especialidad"));
    }

    private interface Setter { void set(PreparedStatement ps) throws SQLException; }

    private Optional<Docente> consultarUno(String sql, Setter setter) {
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            setter.set(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearDocente(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new DaoException("Error al consultar docente: " + e.getMessage(), e); }
    }
}
