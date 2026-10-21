package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelImagen extends JPanel {

    private BufferedImage imagen;

    public PanelImagen(String rutaRecurso) {
        setBackground(Color.WHITE);

        // Carga la imagen una sola vez.
        try (InputStream archivo =
                PanelImagen.class.getResourceAsStream(rutaRecurso)) {

            if (archivo == null) {
                throw new IOException(
                        "No se encontró la imagen: " + rutaRecurso
                );
            }

            imagen = ImageIO.read(archivo);

            if (imagen == null) {
                throw new IOException(
                        "No se pudo leer la imagen: " + rutaRecurso
                );
            }

        } catch (IOException e) {
            System.err.println("AVISO: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen == null) {
            g.setColor(Color.GRAY);
            g.drawString("Sin imagen", 10, 20);
            return;
        }

        // Calcula el espacio disponible dentro del borde.
        Insets borde = getInsets();

        int anchoDisponible =
                getWidth() - borde.left - borde.right;

        int altoDisponible =
                getHeight() - borde.top - borde.bottom;

        if (anchoDisponible <= 0 || altoDisponible <= 0) {
            return;
        }

        // Conserva las proporciones originales.
        double escala = Math.min(
                (double) anchoDisponible / imagen.getWidth(),
                (double) altoDisponible / imagen.getHeight()
        );

        int ancho = Math.max(
                1,
                (int) Math.round(imagen.getWidth() * escala)
        );

        int alto = Math.max(
                1,
                (int) Math.round(imagen.getHeight() * escala)
        );

        // Centra la imagen dentro del recuadro.
        int x = borde.left + (anchoDisponible - ancho) / 2;
        int y = borde.top + (altoDisponible - alto) / 2;

        Graphics2D dibujo = (Graphics2D) g.create();

        try {
            dibujo.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );

            dibujo.drawImage(imagen, x, y, ancho, alto, this);

        } finally {
            dibujo.dispose();
        }
    }
}