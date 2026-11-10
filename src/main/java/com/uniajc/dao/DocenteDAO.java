package com.uniajc.dao;

import com.uniajc.modelo.Docente;
import com.uniajc.db.ConexionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocenteDAO {

    public boolean insertar(Docente docente) {
        String sql = "INSERT INTO Docente (name, specialty, email) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, docente.getName());
            ps.setString(2, docente.getSpecialty());
            ps.setString(3, docente.getEmail());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar docente: " + e.getMessage());
            return false;
        }
    }

    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Docente";
        
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Docente doc = new Docente();
                doc.setId(rs.getInt("id"));
                doc.setName(rs.getString("name"));
                doc.setSpecialty(rs.getString("specialty"));
                doc.setEmail(rs.getString("email"));
                if (rs.getTimestamp("created_at") != null) {
                    doc.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                lista.add(doc);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener docentes: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Docente docente) {
        String sql = "UPDATE Docente SET name=?, specialty=?, email=? WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, docente.getName());
            ps.setString(2, docente.getSpecialty());
            ps.setString(3, docente.getEmail());
            ps.setInt(4, docente.getId());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar docente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Docente WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar docente: " + e.getMessage());
            return false;
        }
    }
}