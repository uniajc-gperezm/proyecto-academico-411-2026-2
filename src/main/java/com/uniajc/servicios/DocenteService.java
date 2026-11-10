package com.uniajc.servicios;

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.modelo.Docente;
import java.util.List;
import java.util.Optional;

public class DocenteService {
    private final DocenteDao dao;
    private final GrupoDao grupoDao;

    public DocenteService(DocenteDao dao, GrupoDao grupoDao) { this.dao = dao; this.grupoDao = grupoDao; }

    public void registrarDocente(Docente d) {
        validar(d);
        dao.guardar(d);
    }
    public List<Docente> obtenerTodosLosDocentes() { return dao.obtenerTodos(); }
    public Optional<Docente> buscarDocentePorId(int id) { return dao.buscarPorId(id); }

    public void actualizarDocente(Docente d) {
        if (dao.buscarPorId(d.getIdDocente()).isEmpty()) throw new IllegalArgumentException("El docente no existe.");
        validar(d);
        dao.actualizar(d);
    }

    public void eliminarDocente(int id) {
        if (grupoDao.existePorDocente(id))
            throw new IllegalStateException("No se puede eliminar: el docente tiene grupos asignados.");
        dao.eliminar(id);
    }

    private void validar(Docente d) {
        if (d.getNombre() == null || d.getNombre().isBlank()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (d.getEspecialidad() == null || d.getEspecialidad().isBlank()) throw new IllegalArgumentException("La especialidad es obligatoria.");
    }
}
