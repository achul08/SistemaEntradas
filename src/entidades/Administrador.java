package entidades;


public class Administrador extends Usuario {

    //CONSTRUCTORES ------
    public Administrador() {
        super(); //llama a Usuario()
    }

    //constructor con parámetros
    public Administrador(String nombre, String apellido, String username, String password) {
        super(nombre, apellido, username, password); //llama a Usuario(nombre, apellido, username, password)
    }

    @Override
    public String getRol() {
        return "ADMINISTRADOR"; //este es el rol de esta clase
    }


    @Override
    public String toString() {
        return super.toString();
    }
}