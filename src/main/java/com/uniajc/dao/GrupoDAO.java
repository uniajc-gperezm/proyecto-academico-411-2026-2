package com.uniajc.dao;

import com.uniajc.modelo.Grupo;
import com.uniajc.db.ConexionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO {

    public boolean insertar(Grupo grupo) {
        String sql = "INSERT INTO Grupo (id_materia, id_docente, classroom, schedule) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, grupo.getIdMateria());
            ps.setInt(2, grupo.getIdDocente());
            ps.setString(3, grupo.getClassroom());
            ps.setString(4, grupo.getSchedule());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar grupo: " + e.getMessage());
            return false;
        }
    }

    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Grupo";
        
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Grupo grupo = new Grupo();
                grupo.setId(rs.getInt("id"));
                grupo.setIdMateria(rs.getInt("id_materia"));
                grupo.setIdDocente(rs.getInt("id_docente"));
                grupo.setClassroom(rs.getString("classroom"));
                grupo.setSchedule(rs.getString("schedule"));
                if (rs.getTimestamp("created_at") != null) {
                    grupo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                lista.add(grupo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener grupos: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean actualizar(Grupo grupo) {
        String sql = "UPDATE Grupo SET id_materia=?, id_docente=?, classroom=?, schedule=? WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, grupo.getIdMateria());
            ps.setInt(2, grupo.getIdDocente());
            ps.setString(3, grupo.getClassroom());
            ps.setString(4, grupo.getSchedule());
            ps.setInt(5, grupo.getId());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar grupo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Grupo WHERE id=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar grupo: " + e.getMessage());
            return false;
        }
    }
}