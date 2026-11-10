package com.uniajc.vista;

import com.uniajc.modelo.InscripcionCurso;
import com.uniajc.servicios.InscripcionCursoService;
import java.util.List;

public class VistaInscripcionCursoSwing extends PanelCrud<InscripcionCurso> {
    private final InscripcionCursoService service;

    public VistaInscripcionCursoSwing(InscripcionCursoService service) {
        super("Inscripciones a Cursos", new String[]{"ID", "ID Estudiante", "ID Grupo", "Nota final", "Estado"});
        this.service = service;
        refrescar();
    }

    @Override protected List<InscripcionCurso> listar() { return service.obtenerTodasLasInscripciones(); }
    @Override protected Object[] aFila(InscripcionCurso i) {
        return new Object[]{i.getIdInscripcion(), i.getIdEstudiante(), i.getIdGrupo(), i.getNotaFinal(), i.getEstado()};
    }
    @Override protected String[] etiquetas() {
        return new String[]{"ID Estudiante", "ID Grupo", "Nota final (0-5)", "Estado (INSCRITO/APROBADO/REPROBADO/RETIRADO)"};
    }
    @Override protected String[] valores(InscripcionCurso i) {
        return new String[]{String.valueOf(i.getIdEstudiante()), String.valueOf(i.getIdGrupo()),
                String.valueOf(i.getNotaFinal()), i.getEstado()};
    }

    @Override protected InscripcionCurso construir(InscripcionCurso existente, String[] v) {
        InscripcionCurso i = existente == null ? new InscripcionCurso() : existente;
        i.setIdEstudiante(entero(v[0], "ID Estudiante"));
        i.setIdGrupo(entero(v[1], "ID Grupo"));
        i.setNotaFinal(v[2].isEmpty() ? 0f : decimal(v[2], "La nota"));
        i.setEstado(v[3].isEmpty() ? "INSCRITO" : v[3]);
        return i;
    }
    @Override protected void guardarRegistro(InscripcionCurso i, boolean esNuevo) {
        if (esNuevo) service.registrarInscripcion(i); else service.actualizarInscripcion(i);
    }
    @Override protected void eliminarRegistro(InscripcionCurso i) { service.eliminarInscripcion(i.getIdInscripcion()); }
}
