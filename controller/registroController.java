package controller;

import java.awt.Component;
import java.io.IOException;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import modelo.Usuario;
import repositorio.RepositorioUsuarios;

public class RegistroController {

    private final RepositorioUsuarios repositorio;
    private final Component componentePadre;
    private final Consumer<Usuario> alRegistrar;

    public RegistroController(
            RepositorioUsuarios repositorio,
            Component componentePadre,
            Consumer<Usuario> alRegistrar
    ) {
        if (repositorio == null || alRegistrar == null) {
            throw new IllegalArgumentException(
                    "El repositorio y la acción de registro son obligatorios."
            );
        }
        this.repositorio = repositorio;
        this.componentePadre = componentePadre;
        this.alRegistrar = alRegistrar;
    }

    public Usuario registrarUsuario() {
        while (true) {
            String nombre = JOptionPane.showInputDialog(
                    componentePadre,
                    "Escribe tu nombre:",
                    "Registro de jugador",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (nombre == null) {
                return null;
            }

            final Usuario usuario;
            try {
                usuario = repositorio.registrarUsuario(nombre);
            } catch (IllegalArgumentException e) {
                mostrarMensaje(
                        e.getMessage(),
                        "Nombre no válido",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            try {
                repositorio.guardarUsuarios();
            } catch (IOException e) {
                alRegistrar.accept(usuario);
                mostrarMensaje(
                        "El usuario quedó registrado en esta sesión, pero no "
                        + "se pudo guardar en el archivo. " + e.getMessage(),
                        "Error al guardar",
                        JOptionPane.ERROR_MESSAGE
                );
                return usuario;
            }

            alRegistrar.accept(usuario);
            mostrarMensaje(
                    "Usuario registrado: " + usuario.getNombre(),
                    "Registro completado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return usuario;
        }
    }

    private void mostrarMensaje(String texto, String titulo, int tipo) {
        JOptionPane.showMessageDialog(
                componentePadre,
                texto,
                titulo,
                tipo
        );
    }
}
