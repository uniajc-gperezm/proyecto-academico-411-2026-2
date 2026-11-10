package com.uniajc.vista;

import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;
import java.util.List;

public class VistaDocenteSwing extends PanelCrud<Docente> {
    private final DocenteService service;

    public VistaDocenteSwing(DocenteService service) {
        super("Gestión de Docentes", new String[]{"ID", "Nombre", "Especialidad"});
        this.service = service;
        refrescar();
    }

    @Override protected List<Docente> listar() { return service.obtenerTodosLosDocentes(); }
    @Override protected Object[] aFila(Docente d) { return new Object[]{d.getIdDocente(), d.getNombre(), d.getEspecialidad()}; }
    @Override protected String[] etiquetas() { return new String[]{"Nombre", "Especialidad"}; }
    @Override protected String[] valores(Docente d) { return new String[]{d.getNombre(), d.getEspecialidad()}; }

    @Override protected Docente construir(Docente existente, String[] v) {
        Docente d = existente == null ? new Docente() : existente;
        d.setNombre(v[0]);
        d.setEspecialidad(v[1]);
        return d;
    }
    @Override protected void guardarRegistro(Docente d, boolean esNuevo) {
        if (esNuevo) service.registrarDocente(d); else service.actualizarDocente(d);
    }
    @Override protected void eliminarRegistro(Docente d) { service.eliminarDocente(d.getIdDocente()); }
}
