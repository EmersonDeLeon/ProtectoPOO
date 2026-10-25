package controller;

import java.io.IOException;
import javax.swing.JOptionPane;
import modelo.Usuario;
import repositorio.RepositorioUsuarios;
import view.VentanaPrincipal;

public class GestorPantallas {

    public enum Pantalla {
        MENU,
        TETRIS,
        REGISTRO,
        PUNTUACIONES,
        CREDITOS
    }

    private final VentanaPrincipal ventanaPrincipal;
    private final RepositorioUsuarios repositorioUsuarios;
    private Pantalla pantallaActual = Pantalla.MENU;

    public GestorPantallas(
            VentanaPrincipal ventanaPrincipal,
            RepositorioUsuarios repositorioUsuarios
    ) {
        if (ventanaPrincipal == null || repositorioUsuarios == null) {
            throw new IllegalArgumentException(
                    "La ventana y el repositorio de usuarios son obligatorios."
            );
        }
        this.ventanaPrincipal = ventanaPrincipal;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    public void mostrarMenu() {
        ventanaPrincipal.mostrarMenu();
        pantallaActual = Pantalla.MENU;
    }

    public void mostrarTetris() {
        ventanaPrincipal.mostrarJuego();
        pantallaActual = Pantalla.TETRIS;
    }

    public Usuario mostrarRegistro() {
        pantallaActual = Pantalla.REGISTRO;
        String nombre = JOptionPane.showInputDialog(
                ventanaPrincipal,
                "Escribe tu nombre:",
                "Registro de jugador",
                JOptionPane.QUESTION_MESSAGE
        );

        if (nombre == null) {
            mostrarMenu();
            return null;
        }

        try {
            Usuario usuario = repositorioUsuarios.registrarUsuario(nombre);
            try {
                repositorioUsuarios.guardarUsuarios();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        ventanaPrincipal,
                        "El usuario quedó registrado en esta sesión, pero "
                        + "no se pudo guardar en el archivo. " + e.getMessage(),
                        "Error al guardar",
                        JOptionPane.ERROR_MESSAGE
                );
                mostrarMenu();
                return usuario;
            }

            JOptionPane.showMessageDialog(
                    ventanaPrincipal,
                    "Usuario registrado: " + usuario.getNombre(),
                    "Registro completado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            mostrarMenu();
            return usuario;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    ventanaPrincipal,
                    e.getMessage(),
                    "No se pudo registrar",
                    JOptionPane.ERROR_MESSAGE
            );
            mostrarMenu();
            return null;
        }
    }

    public void mostrarPuntuaciones() {
        ventanaPrincipal.mostrarPuntuaciones();
        pantallaActual = Pantalla.PUNTUACIONES;
    }

    public void mostrarCreditos() {
        ventanaPrincipal.mostrarCreditos();
        pantallaActual = Pantalla.CREDITOS;
    }

    public void volverAlMenu() {
        mostrarMenu();
    }

    public Pantalla getPantallaActual() {
        return pantallaActual;
    }
}
