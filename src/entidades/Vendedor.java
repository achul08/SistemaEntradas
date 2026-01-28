package entidades;

public class Vendedor extends Usuario {

    //CONSTRUCTORES ------
    public Vendedor() {
        super(); //llama a Usuario()
    }

    public Vendedor(String nombre, String apellido, String username, String password) {
        super(nombre, apellido, username, password); //llama a Usuario(nombre, apellido, username, password)
    }

    @Override
    public String getRol() {
        return "VENDEDOR"; //este es el rol de esta clase
    }

    @Override
    public String toString() {
        return super.toString(); //usa el toString de Usuario
        //Si quisieras agregar algo: return super.toString() + "\nComisión: 5%";
    }
}