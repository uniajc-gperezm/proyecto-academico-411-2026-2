package com.uniajc.vista;

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionCursoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.servicios.DocenteService;
import com.uniajc.servicios.GrupoService;
import com.uniajc.servicios.InscripcionCursoService;
import com.uniajc.servicios.MateriaService;

import javax.swing.*;
import java.awt.*;

/** Ventana principal: menú + CardLayout que muestra un panel por entidad. */
public class VistaPrincipalSwing extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenedor = new JPanel(cardLayout);

    public VistaPrincipalSwing() {
        super("Sistema de Gestión UNIAJC");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        // DAOs y servicios (inyección por constructor)
        GrupoDao grupoDao = new GrupoDao();
        DocenteDao docenteDao = new DocenteDao();
        MateriaDao materiaDao = new MateriaDao();
        InscripcionCursoDao inscDao = new InscripcionCursoDao();

        // Paneles agregados al CardLayout
        panelContenedor.add(new VistaDocenteSwing(new DocenteService(docenteDao, grupoDao)), "docentes");
        panelContenedor.add(new VistaMateriaSwing(new MateriaService(materiaDao, grupoDao)), "materias");
        panelContenedor.add(new VistaGrupoSwing(new GrupoService(grupoDao, materiaDao, docenteDao, inscDao)), "grupos");
        panelContenedor.add(new VistaInscripcionCursoSwing(new InscripcionCursoService(inscDao, grupoDao)), "inscripciones");

        // Menú
        JMenuBar barra = new JMenuBar();
        JMenu menu = new JMenu("Gestión");
        menu.add(opcion("Docentes", "docentes"));
        menu.add(opcion("Materias", "materias"));
        menu.add(opcion("Grupos", "grupos"));
        menu.add(opcion("Inscripciones", "inscripciones"));
        menu.addSeparator();
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> System.exit(0));
        menu.add(salir);
        barra.add(menu);
        setJMenuBar(barra);

        add(panelContenedor, BorderLayout.CENTER);
        cardLayout.show(panelContenedor, "docentes");
    }

    private JMenuItem opcion(String texto, String clave) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(e -> cardLayout.show(panelContenedor, clave));
        return item;
    }

    /** Para probar solo esta ventana. Si el proyecto ya tiene Main, llámala desde ahí. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaPrincipalSwing().setVisible(true));
    }
}
