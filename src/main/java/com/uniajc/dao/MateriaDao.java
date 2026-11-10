package com.uniajc.dao;

import com.uniajc.config.ConexionDatabase;
import com.uniajc.modelo.Materia;
import java.sql.*;
import java.util.*;

public class MateriaDao {

    // CREATE
    public void guardar(Materia m) {
        String sql = "INSERT INTO materia(nombre_materia, creditos) VALUES (?, ?)";
        try (Connection c = ConexionDatabase.getConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombreMateria());
            ps.setInt(2, m.getCreditos());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) m.setIdMateria(k.getInt(1)); }
        } catch (SQLException e) { throw new DaoException("Error al guardar materia: " + e.getMessage(), e); }
    }

    // READ - All
    public List<Materia> obtenerTodos() {
        List<Materia> lista = new ArrayList<>();
        try (Connection c = ConexionDatabase.getConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM materia ORDER BY id_materia")) {
            while (rs.next()) lista.add(mapearMateria(rs));
        } catch (SQLException e) { throw new DaoException("Error al listar materias: " + e.getMessage(), e); }
        return lista;
    }

    // READ - By ID
    public Optional<Materia> buscarPorId(int id) {
        return consultarUno("SELECT * FROM materia WHERE id_materia = ?", ps -> ps.setInt(1, id));
    }

    // READ - By Field (nombre_materia)
    public Optional<Materia> buscarPorCampo(String campo) {
        return consultarUno("SELECT * FROM materia WHERE LOWER(nombre_materia) = LOWER(?) LIMIT 1", ps -> ps.setString(1, campo));
    }

    // UPDATE
    public void actualizar(Materia m) {
        String sql = "UPDATE materia SET nombre_materia = ?, creditos = ? WHERE id_materia = ?";
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNombreMateria());
            ps.setInt(2, m.getCreditos());
            ps.setInt(3, m.getIdMateria());
            ps.executeUpdate();
        } catch (SQLException e) { throw new DaoException("Error al actualizar materia: " + e.getMessage(), e); }
    }

    // DELETE
    public void eliminar(int id) {
        try (Connection c = ConexionDatabase.getConexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM materia WHERE id_materia = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new DaoException("Error al eliminar materia: " + e.getMessage(), e); }
    }

    private Materia mapearMateria(ResultSet rs) throws SQLException {
        return new Materia(rs.getInt("id_materia"), rs.getString("nombre_materia"), rs.getInt("creditos"));
    }

    private interface Setter { void set(PreparedStatement ps) throws SQLException; }

    private Optional<Materia> consultarUno(String sql, Setter setter) {
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            setter.set(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearMateria(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new DaoException("Error al consultar materia: " + e.getMessage(), e); }
    }
}
