package com.uniajc.dao;

import com.uniajc.modelo.Estudiante;
import com.uniajc.db.ConexionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public boolean insertar(Estudiante estudiante) {
        String sql = "INSERT INTO Estudiante (name, program, email) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, estudiante.getName());
            ps.setString(2, estudiante.getProgram());
            ps.setString(3, estudiante.getEmail());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar estudiante: " + e.getMessage());
            return false;
        }
    }

    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM Estudiante";
        
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Estudiante est = new Estudiante();
                est.setId(rs.getInt("id"));
                est.setName(rs.getString("name"));
                est.setProgram(rs.getString("program"));
                est.setEmail(rs.getString("email"));
                if (rs.getTimestamp("created_at") != null) {
                    est.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                lista.add(est);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estudiantes: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Estudiante estudiante) {
        String sql = "UPDATE Estudiante SET name=?, program=?, email=? WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, estudiante.getName());
            ps.setString(2, estudiante.getProgram());
            ps.setString(3, estudiante.getEmail());
            ps.setInt(4, estudiante.getId());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estudiante: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Estudiante WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
            return false;
        }
    }
}