package entidades;

//CLASE HIJA - Administrador
//Hereda de Usuario (clase padre abstracta)
//Representa a un usuario con permisos completos en el sistema

//RELACIÓN CON OTRAS CLASES:
//- Hereda de Usuario (extends)
//- Tiene acceso completo: puede gestionar estadios, ubicaciones, espectáculos, usuarios y reportes


public class Administrador extends Usuario {

    //CONSTRUCTORES ------

    //constructor vacío
    //llama al constructor vacío del padre con super()
    public Administrador() {
        super(); //llama a Usuario()
    }

    //constructor con parámetros
    //llama al constructor con parámetros del padre
    public Administrador(String nombre, String apellido, String username, String password) {
        super(nombre, apellido, username, password); //llama a Usuario(nombre, apellido, username, password)
    }


    //MÉTODO OBLIGATORIO ------
    //Como Usuario tiene un método abstracto getRol(),
    //esta clase TIENE que implementarlo

    @Override
    public String getRol() {
        return "ADMINISTRADOR"; //este es el rol de esta clase
    }


    //TOSTRING ------
    //Hereda el toString de Usuario, pero podemos sobrescribirlo si queremos
    //En este caso lo dejamos como está (hereda el del padre)
    //Si querés agregar algo específico de Administrador, podés hacerlo acá

    @Override
    public String toString() {
        return super.toString(); //usa el toString de Usuario
        //Si quisieras agregar algo: return super.toString() + "\nNivel: Alto";
    }
}