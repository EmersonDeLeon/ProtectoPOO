package view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class PanelCaida extends JPanel {

    private static final int COLUMNAS = 8;
    private static final int SEPARACION = 5;

    private int columna;
    private double progreso;
    private boolean bloqueVisible;

    //Define el color del panel

    public PanelCaida() {
        setBackground(new Color(30, 40, 55));
    }

    //Muestra el bloque a caer

    public void mostrarBloque(int columna, double progreso) {
        if (columna < 0 || columna >= COLUMNAS) {
            throw new IllegalArgumentException(
                    "La columna debe estar entre 0 y 7."
            );
        }

        if (Double.isNaN(progreso) || progreso < 0 || progreso > 1) {
            throw new IllegalArgumentException(
                    "El progreso debe estar entre 0 y 1."
            );
        }

        this.columna = columna;
        this.progreso = progreso;
        bloqueVisible = true;

        repaint();
    }

    public void ocultarBloque() {
        bloqueVisible = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Misma distribucion que la fila de ocho casillas.
        int anchoColumna =
                (getWidth() - SEPARACION * (COLUMNAS - 1)) / COLUMNAS;

        if (anchoColumna <= 6 || getHeight() <= 0) {
            return;
        }

        int anchoUsado =
                anchoColumna * COLUMNAS + SEPARACION * (COLUMNAS - 1);

        int margen = (getWidth() - anchoUsado) / 2;
        Graphics2D dibujo = (Graphics2D) g.create();

        try {
            // Dibujar las ocho columnas.
            dibujo.setColor(new Color(40, 54, 72));

            for (int i = 0; i < COLUMNAS; i++) {
                int x = margen + i * (anchoColumna + SEPARACION);
                dibujo.fillRect(x, 0, anchoColumna, getHeight());
            }

            if (!bloqueVisible) {
                return;
            }

            // Calcular el tamaño y la posicion del bloque.
            int lado = Math.min(
                    40, Math.min(anchoColumna - 6, getHeight())
            );

            int x = margen + columna * (anchoColumna + SEPARACION)
                    + (anchoColumna - lado) / 2;

            int y = (int) Math.round(
                    progreso * (getHeight() - lado)
            );

            // Dibujar el bloque.
            // Relleno azul.
            dibujo.setColor(new Color(45, 110, 220));
            dibujo.fillRect(x, y, lado, lado);

            // Borde azul oscuro.
            dibujo.setColor(new Color(20, 60, 130));
            dibujo.drawRect(x, y, lado - 1, lado - 1);

            // Numero blanco.
            dibujo.setFont(Fuentes.pixel(20f));
            dibujo.setColor(Color.WHITE);

            FontMetrics medidas = dibujo.getFontMetrics();

            int textoX = x + (lado - medidas.stringWidth("1")) / 2;
            int textoY = y + (lado - medidas.getHeight()) / 2
                    + medidas.getAscent();

            dibujo.drawString("1", textoX, textoY);
        } finally {
            dibujo.dispose();
        }
    }
}