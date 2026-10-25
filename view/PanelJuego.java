package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Point;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;



public class PanelJuego extends JPanel {

    //Filas generales
    private final List<FilaVista> filas = new ArrayList<>();

    private final JPanel contenedorFilas =
        new JPanel(new GridLayout(0, 1, 0, 6));

    private final JPanel contenedorObjetivos = new JPanel();

    private final JScrollPane desplazamientoObjetivos =
        new JScrollPane();

    // Valores
    private final JLabel etiquetaPuntuacion = new JLabel();
    private final JLabel etiquetaEstado = new JLabel();

    //Botones de pistas
    private final JButton botonVolver =
            new JButton("Volver al menú");

    private final JButton[] botonesPista = new JButton[3];
    private final boolean[] pistasDisponibles = {
        true, true, true
    };

    private boolean pistaVisible;
    
    private final Timer temporizadorPista;

    //Potencias inferiores
    private final JLabel[] etiquetasPotencias = new JLabel[8];

    //Area de juego
    private final PanelCaida zonaCaida = new PanelCaida();

    //Constructor
    public PanelJuego() {

        

        //Diseño del tablero

        contenedorObjetivos.setLayout(
        new BoxLayout(contenedorObjetivos, BoxLayout.Y_AXIS)
        );

        setLayout(new BorderLayout(15, 15));
        setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JPanel informacion = new JPanel(new BorderLayout());

        informacion.add(new JLabel("Tetris binario"), BorderLayout.WEST);

        //Tablero información
        informacion.add(etiquetaPuntuacion, BorderLayout.EAST);
        add(informacion, BorderLayout.NORTH);

        JPanel tablero = new JPanel(new BorderLayout());
        tablero.setBorder(
                BorderFactory.createTitledBorder("Tablero binario")
        );

        // Izquierda: caída y filas binarias apoyadas abajo.
        JPanel zonaIzquierda = new JPanel(new BorderLayout());

        zonaIzquierda.add(zonaCaida, BorderLayout.CENTER);

        JPanel zonaInferior = new JPanel(new BorderLayout(0, 4));
        zonaInferior.add(contenedorFilas, BorderLayout.CENTER);
        zonaInferior.add(crearGuiaPotencias(), BorderLayout.SOUTH);

        zonaIzquierda.add(zonaInferior, BorderLayout.SOUTH);

        // Los objetivos se apoyan abajo cuando hay pocos.
        JPanel soporteObjetivos = new JPanel(new BorderLayout());
        soporteObjetivos.add(contenedorObjetivos, BorderLayout.NORTH);

        desplazamientoObjetivos.setViewportView(soporteObjetivos);
        desplazamientoObjetivos.setPreferredSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 180)
        );
        desplazamientoObjetivos.setBorder(
                BorderFactory.createEmptyBorder()
        );
        desplazamientoObjetivos.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_NEVER
        );
        desplazamientoObjetivos.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        desplazamientoObjetivos.setWheelScrollingEnabled(false);
        desplazamientoObjetivos.setFocusable(false);

        // Derecha: imagen y pistas independientes de las filas binarias.
        JPanel zonaDerecha = new JPanel(new BorderLayout());
        zonaDerecha.setPreferredSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 0)
        );

        JPanel cabeceraDerecha = crearColumnaDerecha();
        cabeceraDerecha.setPreferredSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 240)
        );

        JPanel contenidoDerecho = new JPanel(new BorderLayout(0, 8));
        contenidoDerecho.add(cabeceraDerecha, BorderLayout.NORTH);
        contenidoDerecho.add(desplazamientoObjetivos, BorderLayout.CENTER);

        zonaDerecha.add(contenidoDerecho, BorderLayout.CENTER);

        tablero.add(zonaIzquierda, BorderLayout.CENTER);
        tablero.add(zonaDerecha, BorderLayout.EAST);

        // Separación entre las dos columnas.
        ((BorderLayout) tablero.getLayout()).setHgap(10);

        add(tablero, BorderLayout.CENTER);

        JPanel controles = new JPanel(new BorderLayout(10, 0));
        controles.add(etiquetaEstado, BorderLayout.CENTER);
        controles.add(botonVolver, BorderLayout.EAST);

        add(controles, BorderLayout.SOUTH);

        //Temporizador de activación de la pista 1000 ms es 1s.
        temporizadorPista = new Timer(5000, e -> mostrarPista(false));
        temporizadorPista.setRepeats(false);


        reiniciarFilas(5);
        mostrarPuntuacion(0);
    }
    

    private JPanel crearColumnaDerecha() {
        JPanel columna = new JPanel(new GridBagLayout());

        columna.setPreferredSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 0)
        );

        // Imagen.
        PanelImagen espacioImagen =
                new PanelImagen("/imagenes/images.jpg");

        espacioImagen.setBackground(Color.WHITE);
        espacioImagen.setBorder(
                BorderFactory.createLineBorder(Color.GRAY)
        );

        espacioImagen.setPreferredSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 0)
        );
        espacioImagen.setMinimumSize(new Dimension(0, 0));

        // Tres pistas en una fila horizontal.
        JPanel panelPistas = new JPanel(new GridLayout(1, 3, 6, 0));

        panelPistas.setBorder(
                BorderFactory.createTitledBorder("Pistas")
        );

        panelPistas.setPreferredSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 60)
        );

        for (int i = 0; i < botonesPista.length; i++) {
            botonesPista[i] = new JButton(String.valueOf(i + 1));
            botonesPista[i].setMargin(new Insets(4, 2, 4, 2));
            botonesPista[i].setToolTipText("Pista " + (i + 1));
            botonesPista[i].setFocusable(false);

            panelPistas.add(botonesPista[i]);
        }

        // Configurar ANTES de agregar los componentes.
        GridBagConstraints posicion = new GridBagConstraints();
        posicion.gridx = 0;
        posicion.weightx = 1;
        posicion.fill = GridBagConstraints.BOTH;

        // Primera posición: imagen.
        posicion.gridy = 0;
        posicion.weighty = 0.4;
        posicion.insets = new Insets(0, 0, 0, 0);

        columna.add(espacioImagen, posicion);

        // Segunda posición: pistas.
        posicion.gridy = 1;
        posicion.weighty = 0;
        posicion.insets = new Insets(12, 0, 0, 0);

        columna.add(panelPistas, posicion);

        return columna;
    }

    private JPanel crearGuiaPotencias() {
        JPanel potencias =
                new JPanel(new GridLayout(1, 8, 5, 0));

        for (int i = 0; i < etiquetasPotencias.length; i++) {
            etiquetasPotencias[i] =
                    new JLabel("", SwingConstants.CENTER);

            etiquetasPotencias[i].setFont(Fuentes.pixel(10f));
            etiquetasPotencias[i].setPreferredSize(
                    new Dimension(55, 22)
            );

            potencias.add(etiquetasPotencias[i]);
        }

        // Mantiene la guía alineada con las ocho casillas.
            return potencias;
    }

    public void mostrarPista(boolean mostrar) {

    // Evita prolongar la pista mientras ya está visible.
    if (mostrar && pistaVisible) {
        return;
    }

    temporizadorPista.stop();
    pistaVisible = mostrar;

    for (int i = 0; i < etiquetasPotencias.length; i++) {
        int exponente = 7 - i;

        String texto = mostrar
                ? String.valueOf(1 << exponente)
                : "<html>2<sup>" + exponente + "</sup></html>";

        etiquetasPotencias[i].setText(texto);
    }

    // Se habilita nuevamente cuando termina la pista.
    actualizarBotonesPista();

    if (mostrar) {
        temporizadorPista.start();
    }
    }

    public void agregarFila(int objetivo) {
        if (!filas.isEmpty()) {
            filas.get(0).setActiva(false);
        }

        FilaVista nueva = new FilaVista(objetivo);

        nueva.setPreferredSize(new Dimension(0, 55));
        nueva.setMinimumSize(new Dimension(0, 55));
        nueva.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        nueva.setActiva(true);

        filas.add(0, nueva);
        contenedorFilas.add(nueva, 0);

        JPanel objetivoVisual = nueva.getPanelObjetivo();

        objetivoVisual.setPreferredSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 55)
        );
        objetivoVisual.setMinimumSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 55)
        );
        objetivoVisual.setMaximumSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 55)
        );

        contenedorObjetivos.add(objetivoVisual, 0);
        actualizarAreaFilas();

        actualizarEstado();
        revalidate();
        repaint();
    }

    public void retirarFilaActiva() {
        if (filas.size() < 2) {
            throw new IllegalStateException(
                    "No hay una fila anterior para recuperar."
            );
        }

        // Retira también el objetivo decimal de esa fila.
        FilaVista retirada = filas.remove(0);
        contenedorFilas.remove(retirada);
        contenedorObjetivos.remove(retirada.getPanelObjetivo());
        actualizarAreaFilas();

        filas.get(0).setActiva(true);

        actualizarEstado();
        revalidate();
        repaint();
    }

    public void reiniciarFilas(int objetivo) {
        ocultarBloque();

        filas.clear();
        contenedorFilas.removeAll();
        contenedorObjetivos.removeAll();

        agregarFila(objetivo);
        mostrarPista(false);
    }

    public void mostrarBits(int[] bits) {
        filas.get(0).mostrarBits(bits);
    }

    public void mostrarObjetivo(int numero) {
        filas.get(0).mostrarObjetivo(numero);
    }

    public void mostrarPuntuacion(int puntos) {
        etiquetaPuntuacion.setText("Puntuación: " + puntos);
    }

    public void mostrarInfeccion(boolean infectada) {
        filas.get(0).mostrarInfeccion(infectada);
        actualizarEstado();
    }

    private void actualizarEstado() {
        boolean infectada = filas.get(0).estaInfectada();

        mostrarEstado(
                infectada ? "Fila infectada" : "Fila activa"
        );

        etiquetaEstado.setForeground(
                infectada ? new Color(160, 0, 0) : Color.BLACK
        );
    }

    private void actualizarBotonesPista() {
    for (int i = 0; i < botonesPista.length; i++) {
        botonesPista[i].setEnabled(
                pistasDisponibles[i] && !pistaVisible
        );
    }
}

    public void mostrarPistaDisponible(int indice, boolean disponible) {
        pistasDisponibles[indice] = disponible;
        actualizarBotonesPista();
    }

    public JButton getBotonPista(int indice) {
        return botonesPista[indice];
    }

    public void mostrarEstado(String mensaje) {
        etiquetaEstado.setText(mensaje);
        etiquetaEstado.setForeground(Color.BLACK);
    }

    public void mostrarBloque(int columna, double progreso) {
        zonaCaida.mostrarBloque(columna, progreso);
    }

    public void ocultarBloque() {
        zonaCaida.ocultarBloque();
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }

    private void actualizarAreaFilas() {
        contenedorFilas.revalidate();
        contenedorFilas.repaint();

        contenedorObjetivos.revalidate();
        contenedorObjetivos.repaint();

        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> {
            // Mostrar el objetivo más reciente cuando haya desbordamiento.
            desplazamientoObjetivos.getViewport()
                    .setViewPosition(new Point(0, 0));
        });
    }
}