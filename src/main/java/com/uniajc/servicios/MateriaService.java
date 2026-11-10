package com.uniajc.servicios;

import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Materia;
import java.util.List;
import java.util.Optional;

public class MateriaService {
    private final MateriaDao dao;
    private final GrupoDao grupoDao;

    public MateriaService(MateriaDao dao, GrupoDao grupoDao) { this.dao = dao; this.grupoDao = grupoDao; }

    public void registrarMateria(Materia m) {
        validar(m);
        if (dao.buscarPorCampo(m.getNombreMateria()).isPresent())
            throw new IllegalArgumentException("Ya existe una materia con ese nombre.");
        dao.guardar(m);
    }
    public List<Materia> obtenerTodasLasMaterias() { return dao.obtenerTodos(); }
    public Optional<Materia> buscarMateriaPorId(int id) { return dao.buscarPorId(id); }

    public void actualizarMateria(Materia m) {
        if (dao.buscarPorId(m.getIdMateria()).isEmpty()) throw new IllegalArgumentException("La materia no existe.");
        validar(m);
        dao.actualizar(m);
    }

    public void eliminarMateria(int id) {
        if (grupoDao.existePorMateria(id))
            throw new IllegalStateException("No se puede eliminar: la materia tiene grupos asociados.");
        dao.eliminar(id);
    }

    private void validar(Materia m) {
        if (m.getNombreMateria() == null || m.getNombreMateria().isBlank()) throw new IllegalArgumentException("El nombre de la materia es obligatorio.");
        if (m.getCreditos() <= 0 || m.getCreditos() > 10) throw new IllegalArgumentException("Los créditos deben estar entre 1 y 10.");
    }
}
