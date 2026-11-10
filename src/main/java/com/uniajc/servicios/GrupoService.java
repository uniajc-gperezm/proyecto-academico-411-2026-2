package com.uniajc.servicios;

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionCursoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Grupo;
import java.util.List;
import java.util.Optional;

public class GrupoService {
    private final GrupoDao dao;
    private final MateriaDao materiaDao;
    private final DocenteDao docenteDao;
    private final InscripcionCursoDao inscripcionDao;

    public GrupoService(GrupoDao dao, MateriaDao materiaDao, DocenteDao docenteDao, InscripcionCursoDao inscripcionDao) {
        this.dao = dao; this.materiaDao = materiaDao; this.docenteDao = docenteDao; this.inscripcionDao = inscripcionDao;
    }

    public void registrarGrupo(Grupo g) {
        validar(g);
        dao.guardar(g);
    }
    public List<Grupo> obtenerTodosLosGrupos() { return dao.obtenerTodos(); }
    public Optional<Grupo> buscarGrupoPorId(int id) { return dao.buscarPorId(id); }

    public void actualizarGrupo(Grupo g) {
        if (dao.buscarPorId(g.getIdGrupo()).isEmpty()) throw new IllegalArgumentException("El grupo no existe.");
        validar(g);
        dao.actualizar(g);
    }

    public void eliminarGrupo(int id) {
        if (inscripcionDao.existePorGrupo(id))
            throw new IllegalStateException("No se puede eliminar: el grupo tiene estudiantes inscritos.");
        dao.eliminar(id);
    }

    private void validar(Grupo g) {
        if (materiaDao.buscarPorId(g.getIdMateria()).isEmpty()) throw new IllegalArgumentException("La materia indicada no existe.");
        if (docenteDao.buscarPorId(g.getIdDocente()).isEmpty()) throw new IllegalArgumentException("El docente indicado no existe.");
        if (g.getAula() == null || g.getAula().isBlank()) throw new IllegalArgumentException("El aula es obligatoria.");
        if (g.getHorario() == null || g.getHorario().isBlank()) throw new IllegalArgumentException("El horario es obligatorio.");
    }
}
