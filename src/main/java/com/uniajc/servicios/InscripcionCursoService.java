package com.uniajc.servicios;

import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionCursoDao;
import com.uniajc.modelo.InscripcionCurso;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class InscripcionCursoService {
    private static final Set<String> ESTADOS = Set.of("INSCRITO", "APROBADO", "REPROBADO", "RETIRADO");

    private final InscripcionCursoDao dao;
    private final GrupoDao grupoDao;

    public InscripcionCursoService(InscripcionCursoDao dao, GrupoDao grupoDao) { this.dao = dao; this.grupoDao = grupoDao; }

    public void registrarInscripcion(InscripcionCurso i) {
        normalizar(i);
        validar(i);
        if (dao.existeInscripcion(i.getIdEstudiante(), i.getIdGrupo()))
            throw new IllegalArgumentException("El estudiante ya está inscrito en ese grupo.");
        dao.guardar(i);
    }
    public List<InscripcionCurso> obtenerTodasLasInscripciones() { return dao.obtenerTodos(); }
    public Optional<InscripcionCurso> buscarInscripcionPorId(int id) { return dao.buscarPorId(id); }

    public void actualizarInscripcion(InscripcionCurso i) {
        if (dao.buscarPorId(i.getIdInscripcion()).isEmpty()) throw new IllegalArgumentException("La inscripción no existe.");
        normalizar(i);
        validar(i);
        dao.actualizar(i);
    }

    public void eliminarInscripcion(int id) { dao.eliminar(id); }

    private void normalizar(InscripcionCurso i) {
        if (i.getEstado() != null) i.setEstado(i.getEstado().trim().toUpperCase());
    }

    private void validar(InscripcionCurso i) {
        if (!dao.existeEstudiante(i.getIdEstudiante())) throw new IllegalArgumentException("El estudiante indicado no existe.");
        if (grupoDao.buscarPorId(i.getIdGrupo()).isEmpty()) throw new IllegalArgumentException("El grupo indicado no existe.");
        if (i.getNotaFinal() < 0 || i.getNotaFinal() > 5) throw new IllegalArgumentException("La nota debe estar entre 0.0 y 5.0.");
        if (i.getEstado() == null || !ESTADOS.contains(i.getEstado()))
            throw new IllegalArgumentException("Estado inválido. Use: " + String.join(", ", ESTADOS));
    }
}
