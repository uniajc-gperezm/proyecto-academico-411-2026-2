package com.uniajc.vista;

import com.uniajc.modelo.Materia;
import com.uniajc.servicios.MateriaService;
import java.util.List;

public class VistaMateriaSwing extends PanelCrud<Materia> {
    private final MateriaService service;

    public VistaMateriaSwing(MateriaService service) {
        super("Gestión de Materias", new String[]{"ID", "Materia", "Créditos"});
        this.service = service;
        refrescar();
    }

    @Override protected List<Materia> listar() { return service.obtenerTodasLasMaterias(); }
    @Override protected Object[] aFila(Materia m) { return new Object[]{m.getIdMateria(), m.getNombreMateria(), m.getCreditos()}; }
    @Override protected String[] etiquetas() { return new String[]{"Nombre de la materia", "Créditos"}; }
    @Override protected String[] valores(Materia m) { return new String[]{m.getNombreMateria(), String.valueOf(m.getCreditos())}; }

    @Override protected Materia construir(Materia existente, String[] v) {
        Materia m = existente == null ? new Materia() : existente;
        m.setNombreMateria(v[0]);
        m.setCreditos(entero(v[1], "Créditos"));
        return m;
    }
    @Override protected void guardarRegistro(Materia m, boolean esNuevo) {
        if (esNuevo) service.registrarMateria(m); else service.actualizarMateria(m);
    }
    @Override protected void eliminarRegistro(Materia m) { service.eliminarMateria(m.getIdMateria()); }
}
