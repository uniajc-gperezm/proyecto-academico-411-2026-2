package com.uniajc.vista;

import com.uniajc.modelo.Grupo;
import com.uniajc.servicios.GrupoService;
import java.util.List;

public class VistaGrupoSwing extends PanelCrud<Grupo> {
    private final GrupoService service;

    public VistaGrupoSwing(GrupoService service) {
        super("Gestión de Grupos", new String[]{"ID", "ID Materia", "ID Docente", "Aula", "Horario"});
        this.service = service;
        refrescar();
    }

    @Override protected List<Grupo> listar() { return service.obtenerTodosLosGrupos(); }
    @Override protected Object[] aFila(Grupo g) {
        return new Object[]{g.getIdGrupo(), g.getIdMateria(), g.getIdDocente(), g.getAula(), g.getHorario()};
    }
    @Override protected String[] etiquetas() { return new String[]{"ID Materia", "ID Docente", "Aula", "Horario"}; }
    @Override protected String[] valores(Grupo g) {
        return new String[]{String.valueOf(g.getIdMateria()), String.valueOf(g.getIdDocente()), g.getAula(), g.getHorario()};
    }

    @Override protected Grupo construir(Grupo existente, String[] v) {
        Grupo g = existente == null ? new Grupo() : existente;
        g.setIdMateria(entero(v[0], "ID Materia"));
        g.setIdDocente(entero(v[1], "ID Docente"));
        g.setAula(v[2]);
        g.setHorario(v[3]);
        return g;
    }
    @Override protected void guardarRegistro(Grupo g, boolean esNuevo) {
        if (esNuevo) service.registrarGrupo(g); else service.actualizarGrupo(g);
    }
    @Override protected void eliminarRegistro(Grupo g) { service.eliminarGrupo(g.getIdGrupo()); }
}
