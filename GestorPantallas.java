public enum Pantalla {
    MENU,
    TETRIS,
    REGISTRO,
    PUNTUACIONES,
    CREDITOS
}

private Stage ventanaPrincipal;
private Pantalla pantallaActual;

public void mostrarMenu();
public void mostrarTetris();
public void mostrarRegistro();
public void mostrarPuntuaciones();
public void mostrarCreditos();
public void volverAlMenu();