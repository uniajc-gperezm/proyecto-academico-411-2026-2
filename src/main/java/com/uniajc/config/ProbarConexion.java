   package com.uniajc;

   import com.uniajc.config.ConexionDatabase;
   import java.sql.*;

   public class ProbarConexion {
       public static void main(String[] args) throws Exception {
           try (Connection c = ConexionDatabase.getConexion();
                Statement st = c.createStatement()) {
               String[] tablas = {"estudiante", "docente", "materia", "grupo", "inscripcion_curso"};
               for (String t : tablas) {
                   ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + t);
                   rs.next();
                   System.out.println(t + ": " + rs.getInt(1) + " registros");
               }
               System.out.println("Conexión a Neon OK");
           }
       }
   }