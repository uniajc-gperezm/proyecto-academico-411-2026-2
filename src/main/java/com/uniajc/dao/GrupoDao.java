package com.uniajc.dao;

import com.uniajc.config.ConexionDatabase;
import com.uniajc.modelo.Grupo;
import java.sql.*;
import java.util.*;

public class GrupoDao {

    // CREATE
    public void guardar(Grupo g) {
        String sql = "INSERT INTO grupo(id_materia, id_docente, aula, horario) VALUES (?, ?, ?, ?)";
        try (Connection c = ConexionDatabase.getConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, g.getIdMateria());
            ps.setInt(2, g.getIdDocente());
            ps.setString(3, g.getAula());
            ps.setString(4, g.getHorario());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) g.setIdGrupo(k.getInt(1)); }
        } catch (SQLException e) { throw new DaoException("Error al guardar grupo: " + e.getMessage(), e); }
    }

    // READ - All
    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        try (Connection c = ConexionDatabase.getConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM grupo ORDER BY id_grupo")) {
            while (rs.next()) lista.add(mapearGrupo(rs));
        } catch (SQLException e) { throw new DaoException("Error al listar grupos: " + e.getMessage(), e); }
        return lista;
    }

    // READ - By ID
    public Optional<Grupo> buscarPorId(int id) {
        return consultarUno("SELECT * FROM grupo WHERE id_grupo = ?", ps -> ps.setInt(1, id));
    }

    // READ - By Field (aula)
    public Optional<Grupo> buscarPorCampo(String campo) {
        return consultarUno("SELECT * FROM grupo WHERE LOWER(aula) = LOWER(?) LIMIT 1", ps -> ps.setString(1, campo));
    }

    // UPDATE
    public void actualizar(Grupo g) {
        String sql = "UPDATE grupo SET id_materia = ?, id_docente = ?, aula = ?, horario = ? WHERE id_grupo = ?";
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, g.getIdMateria());
            ps.setInt(2, g.getIdDocente());
            ps.setString(3, g.getAula());
            ps.setString(4, g.getHorario());
            ps.setInt(5, g.getIdGrupo());
            ps.executeUpdate();
        } catch (SQLException e) { throw new DaoException("Error al actualizar grupo: " + e.getMessage(), e); }
    }

    // DELETE
    public void eliminar(int id) {
        try (Connection c = ConexionDatabase.getConexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM grupo WHERE id_grupo = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new DaoException("Error al eliminar grupo: " + e.getMessage(), e); }
    }

    // Consultas de integridad (las usan los servicios de Docente y Materia)
    public boolean existePorDocente(int idDocente) { return existe("SELECT 1 FROM grupo WHERE id_docente = ? LIMIT 1", idDocente); }
    public boolean existePorMateria(int idMateria) { return existe("SELECT 1 FROM grupo WHERE id_materia = ? LIMIT 1", idMateria); }

    private boolean existe(String sql, int valor) {
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, valor);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new DaoException("Error de consulta: " + e.getMessage(), e); }
    }

    private Grupo mapearGrupo(ResultSet rs) throws SQLException {
        return new Grupo(rs.getInt("id_grupo"), rs.getInt("id_materia"), rs.getInt("id_docente"),
                rs.getString("aula"), rs.getString("horario"));
    }

    private interface Setter { void set(PreparedStatement ps) throws SQLException; }

    private Optional<Grupo> consultarUno(String sql, Setter setter) {
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            setter.set(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearGrupo(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new DaoException("Error al consultar grupo: " + e.getMessage(), e); }
    }
}
