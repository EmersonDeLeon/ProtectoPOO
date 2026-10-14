package view;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.UIManager;

public final class Fuentes {

    private static final Font BASE = cargar();

    private Fuentes() {
    }

    private static Font cargar() {
        try (InputStream archivo = Fuentes.class.getResourceAsStream(
                "/fuentes/PressStart2P-Regular.ttf")) {

            if (archivo == null) {
                throw new IOException(
                        "No se encontro PressStart2P-Regular.ttf."
                );
            }

            return Font.createFont(Font.TRUETYPE_FONT, archivo);

        } catch (IOException | FontFormatException e) {
            System.err.println("AVISO: " + e.getMessage());
            System.err.println(
                    "Se usara Monospaced. Revisa recursos/fuentes "
                    + "y ejecuta con -cp \"salida;recursos\" en Windows."
            );

            return new Font(Font.MONOSPACED, Font.PLAIN, 12);
        }
    }

    public static Font pixel(float tamano) {
        return BASE.deriveFont(Font.PLAIN, tamano);
    }

    // Se llama antes de crear los componentes.
    public static void configurar() {
        Font texto = pixel(10f);

        UIManager.put("Label.font", texto);
        UIManager.put("Button.font", texto);
        UIManager.put("TitledBorder.font", texto);
    }
}

