package com.uniajc.vista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Panel base reutilizable: búsqueda, JTable con modelo, botones CRUD y dialog de registro/edición.
 * Cada VistaXxxSwing extiende esta clase (que a su vez es un JPanel).
 */
public abstract class PanelCrud<T> extends JPanel {
    protected static final Color AZUL = new Color(0x1F4E79);
    protected static final Color AZUL_CLARO = new Color(0xEAF1F8);
    protected static final Color VERDE = new Color(0x2E7D32);
    protected static final Color ROJO = new Color(0xC62828);
    protected static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 14);
    protected static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);

    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField txtBuscar = new JTextField(20);
    private final List<T> datos = new ArrayList<>();

    protected PanelCrud(String titulo, String[] columnas) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Panel de búsqueda
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(AZUL);
        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buscar.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(FUENTE);
        txtBuscar.setFont(FUENTE);
        buscar.add(lblBuscar);
        buscar.add(txtBuscar);
        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(lblTitulo, BorderLayout.WEST);
        norte.add(buscar, BorderLayout.EAST);
        add(norte, BorderLayout.NORTH);

        // JTable con modelo
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setFont(FUENTE);
        tabla.setRowHeight(26);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setSelectionBackground(AZUL_CLARO);
        tabla.setSelectionForeground(Color.BLACK);
        JTableHeader h = tabla.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
        h.setBackground(AZUL);
        h.setForeground(Color.WHITE);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones CRUD
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botones.setOpaque(false);
        JButton bNuevo = boton("Nuevo", VERDE);
        JButton bEditar = boton("Editar", AZUL);
        JButton bEliminar = boton("Eliminar", ROJO);
        JButton bRefrescar = boton("Refrescar", Color.GRAY);
        botones.add(bNuevo); botones.add(bEditar); botones.add(bEliminar); botones.add(bRefrescar);
        add(botones, BorderLayout.SOUTH);

        // Listeners
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            void filtrar() {
                String t = txtBuscar.getText().trim();
                sorter.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(t)));
            }
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
        bNuevo.addActionListener(e -> abrirDialogo(null));
        bEditar.addActionListener(e -> { T sel = seleccionado(); if (sel != null) abrirDialogo(sel); });
        bEliminar.addActionListener(e -> {
            T sel = seleccionado();
            if (sel == null) return;
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar el registro seleccionado?", "Confirmar",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            try { eliminarRegistro(sel); refrescar(); } catch (RuntimeException ex) { error(ex.getMessage()); }
        });
        bRefrescar.addActionListener(e -> refrescar());
    }

    // ---- Lo que cada vista concreta debe implementar ----
    protected abstract List<T> listar();
    protected abstract Object[] aFila(T entidad);
    protected abstract String[] etiquetas();
    protected abstract String[] valores(T entidad);
    protected abstract T construir(T existente, String[] valores);
    protected abstract void guardarRegistro(T entidad, boolean esNuevo);
    protected abstract void eliminarRegistro(T entidad);

    // ---- Lógica común ----
    protected void refrescar() {
        try {
            datos.clear();
            datos.addAll(listar());
            modelo.setRowCount(0);
            for (T t : datos) modelo.addRow(aFila(t));
        } catch (RuntimeException ex) { error(ex.getMessage()); }
    }

    private T seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla."); return null; }
        return datos.get(tabla.convertRowIndexToModel(fila));
    }

    private void abrirDialogo(T existente) {
        String[] etq = etiquetas();
        String[] vals = existente == null ? new String[etq.length] : valores(existente);
        Window w = SwingUtilities.getWindowAncestor(this);
        JDialog d = new JDialog(w, existente == null ? "Nuevo registro" : "Editar registro", Dialog.ModalityType.APPLICATION_MODAL);

        JPanel form = new JPanel(new GridLayout(etq.length, 2, 8, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        JTextField[] campos = new JTextField[etq.length];
        for (int i = 0; i < etq.length; i++) {
            JLabel l = new JLabel(etq[i]); l.setFont(FUENTE);
            campos[i] = new JTextField(vals[i] == null ? "" : vals[i]); campos[i].setFont(FUENTE);
            form.add(l); form.add(campos[i]);
        }
        JButton ok = boton("Guardar", VERDE);
        JButton cancelar = boton("Cancelar", Color.GRAY);
        ok.addActionListener(e -> {
            try {
                String[] v = new String[campos.length];
                for (int i = 0; i < v.length; i++) v[i] = campos[i].getText().trim();
                guardarRegistro(construir(existente, v), existente == null);
                d.dispose();
                refrescar();
            } catch (RuntimeException ex) { error(ex.getMessage()); }
        });
        cancelar.addActionListener(e -> d.dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pie.add(ok); pie.add(cancelar);

        d.add(form, BorderLayout.CENTER);
        d.add(pie, BorderLayout.SOUTH);
        d.setSize(420, 90 + etq.length * 45);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(110, 34));
        return b;
    }

    protected void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected static int entero(String texto, String campo) {
        try { return Integer.parseInt(texto.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(campo + " debe ser un número entero."); }
    }

    protected static float decimal(String texto, String campo) {
        try { return Float.parseFloat(texto.trim().replace(',', '.')); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(campo + " debe ser un número."); }
    }
}
