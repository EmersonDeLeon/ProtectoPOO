package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class PanelPuntuaciones extends JPanel {

    private final JButton botonVolver =
            new JButton("Volver al menú");

    private final JLabel estado =
            new JLabel("Sin resultados todavía.");

    private final DefaultTableModel datos = new DefaultTableModel(
            new Object[]{"Nombre", "Puntuación"}, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columna) {
            return columna == 1 ? Integer.class : String.class;
        }
    };

    public PanelPuntuaciones() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel titulo = new JLabel(
                "Puntuaciones", SwingConstants.CENTER
        );
        titulo.setFont(Fuentes.pixel(20f));
        add(titulo, BorderLayout.NORTH);

        JTable tabla = new JTable(datos);
        tabla.setFont(Fuentes.pixel(12f));
        tabla.setRowHeight(32);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setFont(Fuentes.pixel(10f));
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.setAutoCreateRowSorter(true);
        tabla.getRowSorter().setSortKeys(Collections.singletonList(
                new RowSorter.SortKey(1, SortOrder.DESCENDING)
        ));

        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setColumnHeaderView(tabla.getTableHeader());
        add(desplazamiento, BorderLayout.CENTER);

        JPanel controles = new JPanel(new BorderLayout(10, 0));
        controles.add(estado, BorderLayout.CENTER);
        controles.add(botonVolver, BorderLayout.EAST);
        add(controles, BorderLayout.SOUTH);
    }

    public void limpiarResultados() {
        datos.setRowCount(0);
        estado.setForeground(Color.DARK_GRAY);
        estado.setText("Sin resultados todavía.");
    }

    public void agregarResultado(String nombre, int puntuacion) {
        datos.addRow(new Object[]{nombre, puntuacion});
        estado.setForeground(Color.DARK_GRAY);
        estado.setText("Resultados: " + datos.getRowCount());
    }

    public void mostrarError(String mensaje) {
        estado.setForeground(new Color(180, 40, 40));
        estado.setText(mensaje);
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }
}