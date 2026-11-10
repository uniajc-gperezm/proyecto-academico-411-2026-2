package com.uniajc.dao;

import com.uniajc.modelo.Materia;
import com.uniajc.db.ConexionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    public boolean insertar(Materia materia) {
        String sql = "INSERT INTO Materia (name, credits) VALUES (?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, materia.getName());
            ps.setInt(2, materia.getCredits());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar materia: " + e.getMessage());
            return false;
        }
    }

    public List<Materia> obtenerTodas() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT * FROM Materia";
        
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Materia mat = new Materia();
                mat.setId(rs.getInt("id"));
                mat.setName(rs.getString("name"));
                mat.setCredits(rs.getInt("credits"));
                if (rs.getTimestamp("created_at") != null) {
                    mat.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                lista.add(mat);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener materias: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Materia materia) {
        String sql = "UPDATE Materia SET name=?, credits=? WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, materia.getName());
            ps.setInt(2, materia.getCredits());
            ps.setInt(3, materia.getId());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar materia: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Materia WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar materia: " + e.getMessage());
            return false;
        }
    }
}