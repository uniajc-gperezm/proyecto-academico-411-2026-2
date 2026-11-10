package com.uniajc.dao;

import com.uniajc.modelo.Inscripcion;
import com.uniajc.db.ConexionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {

    public boolean insertar(Inscripcion inscripcion) {
        String sql = "INSERT INTO Inscripcion (id_estudiante, id_grupo, final_grade, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, inscripcion.getIdEstudiante());
            ps.setInt(2, inscripcion.getIdGrupo());
            ps.setDouble(3, inscripcion.getFinalGrade());
            ps.setString(4, inscripcion.getStatus());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar inscripción: " + e.getMessage());
            return false;
        }
    }

    public List<Inscripcion> obtenerTodas() {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Inscripcion";
        
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Inscripcion ins = new Inscripcion();
                ins.setId(rs.getInt("id"));
                ins.setIdEstudiante(rs.getInt("id_estudiante"));
                ins.setIdGrupo(rs.getInt("id_grupo"));
                ins.setFinalGrade(rs.getDouble("final_grade"));
                ins.setStatus(rs.getString("status"));
                if (rs.getTimestamp("created_at") != null) {
                    ins.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                lista.add(ins);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener inscripciones: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Inscripcion inscripcion) {
        String sql = "UPDATE Inscripcion SET id_estudiante=?, id_grupo=?, final_grade=?, status=? WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, inscripcion.getIdEstudiante());
            ps.setInt(2, inscripcion.getIdGrupo());
            ps.setDouble(3, inscripcion.getFinalGrade());
            ps.setString(4, inscripcion.getStatus());
            ps.setInt(5, inscripcion.getId());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar inscripción: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Inscripcion WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar inscripción: " + e.getMessage());
            return false;
        }
    }
}