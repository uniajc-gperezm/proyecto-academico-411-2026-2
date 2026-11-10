package com.uniajc.dao;

import com.uniajc.config.ConexionDatabase;
import com.uniajc.modelo.InscripcionCurso;
import java.sql.*;
import java.util.*;

public class InscripcionCursoDao {

    // CREATE
    public void guardar(InscripcionCurso i) {
        String sql = "INSERT INTO inscripcion_curso(id_estudiante, id_grupo, nota_final, estado) VALUES (?, ?, ?, ?)";
        try (Connection c = ConexionDatabase.getConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getIdEstudiante());
            ps.setInt(2, i.getIdGrupo());
            ps.setFloat(3, i.getNotaFinal());
            ps.setString(4, i.getEstado());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) i.setIdInscripcion(k.getInt(1)); }
        } catch (SQLException e) { throw new DaoException("Error al guardar inscripción: " + e.getMessage(), e); }
    }

    // READ - All
    public List<InscripcionCurso> obtenerTodos() {
        List<InscripcionCurso> lista = new ArrayList<>();
        try (Connection c = ConexionDatabase.getConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM inscripcion_curso ORDER BY id_inscripcion")) {
            while (rs.next()) lista.add(mapearInscripcion(rs));
        } catch (SQLException e) { throw new DaoException("Error al listar inscripciones: " + e.getMessage(), e); }
        return lista;
    }

    // READ - By ID
    public Optional<InscripcionCurso> buscarPorId(int id) {
        return consultarUno("SELECT * FROM inscripcion_curso WHERE id_inscripcion = ?", ps -> ps.setInt(1, id));
    }

    // READ - By Field (estado)
    public Optional<InscripcionCurso> buscarPorCampo(String campo) {
        return consultarUno("SELECT * FROM inscripcion_curso WHERE LOWER(estado) = LOWER(?) LIMIT 1", ps -> ps.setString(1, campo));
    }

    // UPDATE
    public void actualizar(InscripcionCurso i) {
        String sql = "UPDATE inscripcion_curso SET id_estudiante = ?, id_grupo = ?, nota_final = ?, estado = ? WHERE id_inscripcion = ?";
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, i.getIdEstudiante());
            ps.setInt(2, i.getIdGrupo());
            ps.setFloat(3, i.getNotaFinal());
            ps.setString(4, i.getEstado());
            ps.setInt(5, i.getIdInscripcion());
            ps.executeUpdate();
        } catch (SQLException e) { throw new DaoException("Error al actualizar inscripción: " + e.getMessage(), e); }
    }

    // DELETE
    public void eliminar(int id) {
        try (Connection c = ConexionDatabase.getConexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM inscripcion_curso WHERE id_inscripcion = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new DaoException("Error al eliminar inscripción: " + e.getMessage(), e); }
    }

    // Consultas de integridad
    public boolean existePorGrupo(int idGrupo) { return existe("SELECT 1 FROM inscripcion_curso WHERE id_grupo = ? LIMIT 1", idGrupo); }
    public boolean existeEstudiante(int idEstudiante) { return existe("SELECT 1 FROM estudiante WHERE id_estudiante = ?", idEstudiante); }

    public boolean existeInscripcion(int idEstudiante, int idGrupo) {
        String sql = "SELECT 1 FROM inscripcion_curso WHERE id_estudiante = ? AND id_grupo = ?";
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idGrupo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new DaoException("Error de consulta: " + e.getMessage(), e); }
    }

    private boolean existe(String sql, int valor) {
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, valor);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new DaoException("Error de consulta: " + e.getMessage(), e); }
    }

    private InscripcionCurso mapearInscripcion(ResultSet rs) throws SQLException {
        return new InscripcionCurso(rs.getInt("id_inscripcion"), rs.getInt("id_estudiante"),
                rs.getInt("id_grupo"), rs.getFloat("nota_final"), rs.getString("estado"));
    }

    private interface Setter { void set(PreparedStatement ps) throws SQLException; }

    private Optional<InscripcionCurso> consultarUno(String sql, Setter setter) {
        try (Connection c = ConexionDatabase.getConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            setter.set(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearInscripcion(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new DaoException("Error al consultar inscripción: " + e.getMessage(), e); }
    }
}
