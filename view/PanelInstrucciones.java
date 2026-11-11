package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelInstrucciones extends JPanel {

    public PanelInstrucciones() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String instrucciones = String.join("\n",
                "OBJETIVO DEL JUEGO",
                "Representa el objetivo decimal colocando bloques",
                "en las posiciones binarias correctas.",
                "",
                "CONTROLES",
                "Flecha izquierda: mover el bloque a la izquierda.",
                "Flecha derecha: mover el bloque a la derecha.",
                "SPACE: soltar el bloque inmediatamente.",
                "El bloque también cae automáticamente.",
                "",
                "CÓMO FORMAR EL NÚMERO",
                "Cada bloque colocado representa un 1.",
                "Las posiciones vacías representan un 0.",
                "Los valores, de izquierda a derecha, son:",
                "128, 64, 32, 16, 8, 4, 2 y 1.",
                "Suma los valores de las posiciones con un 1",
                "para obtener el objetivo decimal.",
                "Ejemplo: 5 se representa como 00000101.",
                "",
                "FILAS Y ERRORES",
                "Completa la fila activa con los unos necesarios.",
                "Si colocas un bloque donde debía haber un cero,",
                "la fila y su objetivo se marcan en rojo.",
                "Después aparece una nueva fila activa encima.",
                "Al completarla, se elimina y recuperas la anterior,",
                "que se reinicia para intentarlo nuevamente.",
                "Si despejas todo el tablero, aparece otro objetivo",
                "y la partida continúa.",
                "",
                "PISTAS",
                "Tienes tres pistas por partida.",
                "Actívalas con los botones de idea o las teclas 1, 2 y 3.",
                "Muestran los valores de las posiciones durante",
                "cinco segundos. Cada pista se usa una sola vez.",
                "",
                "EL ROBOT",
                "Feliz: al comenzar o completar una fila.",
                "Nervioso: cuando cometes un error.",
                "Enojado: cuando pierdes.",
                "",
                "PUNTUACIÓN",
                "Cada bloque colocado correctamente suma un punto.",
                "",
                "FIN DE LA PARTIDA",
                "Pierdes al alcanzar diez filas infectadas",
                "al mismo tiempo.",
                "Escribe tu nombre y guarda la puntuación.",
                "Puedes consultar el ranking desde el menú.",
                "",
                "VOLVER AL MENÚ",
                "El botón Volver al menú detiene la partida actual."
        );

        JTextArea texto = new JTextArea(instrucciones);
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        texto.setMargin(new Insets(12, 12, 12, 12));
        texto.setCaretPosition(0);

        JScrollPane desplazamiento = new JScrollPane(texto);
        desplazamiento.setPreferredSize(new Dimension(500, 430));

        add(desplazamiento, BorderLayout.CENTER);
    }
}