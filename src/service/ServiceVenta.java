package service;

//SERVICE VENTA - Lógica de negocio para la entidad Venta
//Es la clase más compleja porque necesita validar:
//1. Que el espectáculo exista y esté activo
//2. Que la ubicación exista y pertenezca al estadio del espectáculo
//3. Que haya capacidad disponible en esa ubicación
//4. Que el vendedor exista

import dao.*;
import entidades.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public class ServiceVenta {
    //ATRIBUTOS -----
    private DaoVenta daoVenta;
    private DaoEspectaculo daoEspectaculo;
    private DaoUbicacion daoUbicacion;
    private DaoUsuario daoUsuario;

    //CONSTRUCTOR -----
    public ServiceVenta() {
        daoVenta = new DaoVenta();
        daoEspectaculo = new DaoEspectaculo();
        daoUbicacion = new DaoUbicacion();
        daoUsuario = new DaoUsuario();
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 1: INSERTAR (Registrar una venta nueva)
    //═════════════════════════════════════════════════════════════════════
    //Este es el método más importante porque aquí se valida TODO antes de vender
    public void insertar(Venta venta) throws ServiceException {
        try {
            //VALIDACIÓN 1: Verificar que el espectáculo EXISTA
            Espectaculo espectaculo = daoEspectaculo.consultar(venta.getIdEspectaculo());
            if(espectaculo == null || espectaculo.getIdEspectaculo() == 0) {
                throw new ServiceException("El espectáculo con ID " + venta.getIdEspectaculo() + " no existe");
            }

            //VALIDACIÓN 2: Verificar que el espectáculo esté ACTIVO
            //No se pueden vender entradas para espectáculos inactivos
            if(!espectaculo.isActivo()) {
                throw new ServiceException("El espectáculo no está activo. No se pueden vender entradas");
            }

            //VALIDACIÓN 3: Verificar que la fecha del espectáculo no haya pasado
            //No se pueden vender entradas para espectáculos del pasado
            Date hoy = new Date();
            if(espectaculo.getFecha().before(hoy)) {
                throw new ServiceException("El espectáculo ya pasó. No se pueden vender entradas");
            }

            //VALIDACIÓN 4: Verificar que la ubicación EXISTA
            Ubicacion ubicacion = daoUbicacion.consultar(venta.getIdUbicacion());
            if(ubicacion == null || ubicacion.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación con ID " + venta.getIdUbicacion() + " no existe");
            }

            //VALIDACIÓN 5: Verificar que la ubicación pertenezca al estadio del espectáculo
            //No se puede vender una entrada de "Platea del Luna Park" para un espectáculo en "River"
            if(ubicacion.getIdEstadio() != espectaculo.getIdEstadio()) {
                throw new ServiceException("La ubicación seleccionada no pertenece al estadio del espectáculo");
            }

            //VALIDACIÓN 6: Verificar que haya CAPACIDAD DISPONIBLE
            //Esta es la validación más importante para el negocio
            int capacidadDisponible = verificarCapacidadDisponible(
                    venta.getIdEspectaculo(),
                    venta.getIdUbicacion()
            );

            if(capacidadDisponible <= 0) {
                throw new ServiceException("No hay capacidad disponible en esta ubicación. Entradas agotadas");
            }

            //VALIDACIÓN 7: Verificar que el vendedor EXISTA
            Usuario vendedor = daoUsuario.consultar(venta.getIdVendedor());
            if(vendedor == null || vendedor.getIdUsuario() == 0) {
                throw new ServiceException("El vendedor con ID " + venta.getIdVendedor() + " no existe");
            }

            //VALIDACIÓN 8: Verificar que el usuario sea realmente un VENDEDOR
            //Un administrador no debería poder registrar ventas (según el diseño)
            if(!vendedor.getRol().equals("VENDEDOR")) {
                throw new ServiceException("El usuario no es un vendedor");
            }

            //VALIDACIÓN 9: Nombre del cliente obligatorio
            if(venta.getNombreCliente() == null || venta.getNombreCliente().trim().isEmpty()) {
                throw new ServiceException("El nombre del cliente es obligatorio");
            }

            //VALIDACIÓN 10: DNI del cliente obligatorio
            if(venta.getDniCliente() == null || venta.getDniCliente().trim().isEmpty()) {
                throw new ServiceException("El DNI del cliente es obligatorio");
            }

            //VALIDACIÓN 11: Precio final mayor a 0
            if(venta.getPrecioFinal() <= 0) {
                throw new ServiceException("El precio final debe ser mayor a 0");
            }

            //VALIDACIÓN 12: Verificar que el precio final coincida con el precio de la ubicación
            //(esto es para evitar que alguien modifique el precio manualmente)
            //NOTA: Esta validación se relaja si hay descuentos o abonos
            if(venta.getPrecioFinal() != ubicacion.getPrecio()) {
                //Por ahora lanzamos advertencia, más adelante permitiremos descuentos
                System.out.println("ADVERTENCIA: El precio de la venta no coincide con el precio de la ubicación");
            }

            //Si pasó TODAS las validaciones, registrar la venta
            daoVenta.insertar(venta);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 2: MODIFICAR (Actualizar una venta existente)
    //═════════════════════════════════════════════════════════════════════
    //NOTA: Modificar ventas es poco común en el mundo real
    //Por lo general, se cancelan (eliminan) y se crean nuevas
    //Pero lo implementamos por completitud del CRUD
    public void modificar(Venta venta) throws ServiceException {
        try {
            //VALIDACIÓN 0: Verificar que la venta EXISTA
            Venta existente = daoVenta.consultar(venta.getIdVenta());
            if(existente == null || existente.getIdVenta() == 0) {
                throw new ServiceException("La venta con ID " + venta.getIdVenta() + " no existe");
            }

            //VALIDACIÓN 1: Verificar que el espectáculo EXISTA
            Espectaculo espectaculo = daoEspectaculo.consultar(venta.getIdEspectaculo());
            if(espectaculo == null || espectaculo.getIdEspectaculo() == 0) {
                throw new ServiceException("El espectáculo con ID " + venta.getIdEspectaculo() + " no existe");
            }

            //VALIDACIÓN 2: Verificar que el espectáculo esté ACTIVO
            if(!espectaculo.isActivo()) {
                throw new ServiceException("El espectáculo no está activo");
            }

            //VALIDACIÓN 3: Verificar que la ubicación EXISTA
            Ubicacion ubicacion = daoUbicacion.consultar(venta.getIdUbicacion());
            if(ubicacion == null || ubicacion.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación con ID " + venta.getIdUbicacion() + " no existe");
            }

            //VALIDACIÓN 4: Verificar que la ubicación pertenezca al estadio del espectáculo
            if(ubicacion.getIdEstadio() != espectaculo.getIdEstadio()) {
                throw new ServiceException("La ubicación no pertenece al estadio del espectáculo");
            }

            //VALIDACIÓN 5: Si cambió la ubicación, verificar capacidad
            if(existente.getIdUbicacion() != venta.getIdUbicacion()) {
                int capacidadDisponible = verificarCapacidadDisponible(
                        venta.getIdEspectaculo(),
                        venta.getIdUbicacion()
                );

                if(capacidadDisponible <= 0) {
                    throw new ServiceException("No hay capacidad disponible en la nueva ubicación");
                }
            }

            //VALIDACIÓN 6: Verificar que el vendedor EXISTA
            Usuario vendedor = daoUsuario.consultar(venta.getIdVendedor());
            if(vendedor == null || vendedor.getIdUsuario() == 0) {
                throw new ServiceException("El vendedor con ID " + venta.getIdVendedor() + " no existe");
            }

            //VALIDACIÓN 7: Nombre del cliente obligatorio
            if(venta.getNombreCliente() == null || venta.getNombreCliente().trim().isEmpty()) {
                throw new ServiceException("El nombre del cliente es obligatorio");
            }

            //VALIDACIÓN 8: DNI del cliente obligatorio
            if(venta.getDniCliente() == null || venta.getDniCliente().trim().isEmpty()) {
                throw new ServiceException("El DNI del cliente es obligatorio");
            }

            //VALIDACIÓN 9: Precio final mayor a 0
            if(venta.getPrecioFinal() <= 0) {
                throw new ServiceException("El precio final debe ser mayor a 0");
            }

            //Si pasó todas las validaciones, modificar
            daoVenta.modificar(venta);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 3: ELIMINAR (Cancelar una venta)
    //═════════════════════════════════════════════════════════════════════
    public void eliminar(int id) throws ServiceException {
        try {
            //VALIDACIÓN: Verificar que la venta EXISTA
            Venta venta = daoVenta.consultar(id);
            if(venta == null || venta.getIdVenta() == 0) {
                throw new ServiceException("La venta con ID " + id + " no existe");
            }

            //Si existe, eliminar
            daoVenta.eliminar(id);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 4: CONSULTAR (Buscar una venta por ID)
    //═════════════════════════════════════════════════════════════════════
    public Venta consultar(int id) throws ServiceException {
        try {
            return daoVenta.consultar(id);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 5: CONSULTAR TODOS (Obtener todas las ventas)
    //═════════════════════════════════════════════════════════════════════
    public List<Venta> consultarTodos() throws ServiceException {
        try {
            return daoVenta.consultarTodos();
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 6: CONSULTAR POR VENDEDOR (Ventas de un vendedor específico)
    //═════════════════════════════════════════════════════════════════════
    //Este método usa IVentaDAO que ya tiene consultarPorVendedor()
    //Es útil para que cada vendedor vea solo sus propias ventas
    public List<Venta> consultarPorVendedor(int idVendedor) throws ServiceException {
        try {
            return daoVenta.consultarPorVendedor(idVendedor);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 7: CONSULTAR POR ESPECTÁCULO (Ventas de un espectáculo)
    //═════════════════════════════════════════════════════════════════════
    //Útil para reportes: "¿Cuánto se vendió del concierto de X?"
    public List<Venta> consultarPorEspectaculo(int idEspectaculo) throws ServiceException {
        try {
            return daoVenta.consultarPorEspectaculo(idEspectaculo);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
// MÉTODO 8: CONSULTAR POR UBICACIÓN (Ventas de una ubicación)
//═════════════════════════════════════════════════════════════════════
//Útil para validar si se puede eliminar una ubicación
    public List<Venta> consultarPorUbicacion(int idUbicacion) throws ServiceException {
        try {
            return daoVenta.consultarPorUbicacion(idUbicacion);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 8: CONSULTAR POR FECHA (Ventas en un rango de fechas)
    //═════════════════════════════════════════════════════════════════════
    //Útil para reportes: "¿Cuánto se vendió en marzo?"
    public List<Venta> consultarPorFecha(Date fechaInicio, Date fechaFin) throws ServiceException {
        try {
            //VALIDACIÓN 1: Fechas no pueden ser null
            if(fechaInicio == null || fechaFin == null) {
                throw new ServiceException("Las fechas no pueden ser nulas");
            }

            //VALIDACIÓN 2: La fecha de inicio no puede ser posterior a la fecha de fin
            if(fechaInicio.after(fechaFin)) {
                throw new ServiceException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }

            return daoVenta.consultarPorFecha(fechaInicio, fechaFin);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 9: CONSULTAR POR ESPECTÁCULO Y FECHA
    //═════════════════════════════════════════════════════════════════════
    //Útil para reportes específicos: "¿Cuánto se vendió del concierto de X en marzo?"
    public List<Venta> consultarPorEspectaculoYFecha(int idEspectaculo, Date fechaInicio, Date fechaFin) throws ServiceException {
        try {
            //VALIDACIÓN 1: Fechas no pueden ser null
            if(fechaInicio == null || fechaFin == null) {
                throw new ServiceException("Las fechas no pueden ser nulas");
            }

            //VALIDACIÓN 2: La fecha de inicio no puede ser posterior a la fecha de fin
            if(fechaInicio.after(fechaFin)) {
                throw new ServiceException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }

            return daoVenta.consultarPorEspectaculoYFecha(idEspectaculo, fechaInicio, fechaFin);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 10: VERIFICAR CAPACIDAD DISPONIBLE (CLAVE DEL NEGOCIO)
    //═════════════════════════════════════════════════════════════════════
    //Este método calcula cuántas entradas quedan disponibles en una ubicación
    //para un espectáculo específico
    //
    //Fórmula: Capacidad Disponible = Capacidad Total - Entradas Vendidas
    //
    //Ejemplo:
    //- Platea del Luna Park tiene capacidad de 500 personas
    //- Ya se vendieron 350 entradas para el concierto del sábado
    //- Capacidad disponible = 500 - 350 = 150
    public int verificarCapacidadDisponible(int idEspectaculo, int idUbicacion) throws ServiceException {
        try {
            //PASO 1: Obtener la ubicación para saber su capacidad total
            Ubicacion ubicacion = daoUbicacion.consultar(idUbicacion);
            if(ubicacion == null || ubicacion.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación no existe");
            }

            int capacidadTotal = ubicacion.getCapacidad();

            //PASO 2: Calcular cuántas entradas ya se vendieron
            int entradasVendidas = calcularVentasDeUbicacion(idEspectaculo, idUbicacion);

            //PASO 3: Restar para obtener la capacidad disponible
            int capacidadDisponible = capacidadTotal - entradasVendidas;

            return capacidadDisponible;

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 11: CALCULAR VENTAS DE UBICACIÓN (AUXILIAR)
    //═════════════════════════════════════════════════════════════════════
    //Este método cuenta cuántas entradas se vendieron para una ubicación
    //en un espectáculo específico
    //
    //IMPORTANTE: Cuenta 1 entrada por cada registro de venta
    //Si en el futuro permitimos comprar múltiples entradas en una venta,
    //habrá que sumar un campo "cantidad" en lugar de contar registros
    private int calcularVentasDeUbicacion(int idEspectaculo, int idUbicacion) throws ServiceException {
        try {
            //Traer TODAS las ventas de la BD
            List<Venta> todasLasVentas = daoVenta.consultarTodos();

            //Contador de ventas
            int contador = 0;

            //Recorrer cada venta
            for(Venta venta : todasLasVentas) {
                //Verificar si la venta es:
                //1. Del espectáculo que nos interesa
                //2. De la ubicación que nos interesa
                if(venta.getIdEspectaculo() == idEspectaculo
                        && venta.getIdUbicacion() == idUbicacion) {
                    contador++; //Sumar 1 al contador
                }
            }

            return contador;

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 12: CALCULAR TOTAL RECAUDADO (Para reportes)
    //═════════════════════════════════════════════════════════════════════
    //Calcula el total de dinero recaudado de una lista de ventas
    //Útil para reportes como: "Se recaudaron $50.000 en marzo"
    public double calcularTotalRecaudado(List<Venta> ventas) throws ServiceException {
        double total = 0.0;

        //Recorrer cada venta y sumar su precio final
        for(Venta venta : ventas) {
            total += venta.getPrecioFinal();
        }

        return total;
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO 13: CALCULAR CANTIDAD VENDIDA (Para reportes)
    //═════════════════════════════════════════════════════════════════════
    //Cuenta cuántas entradas se vendieron de una lista de ventas
    //Útil para reportes como: "Se vendieron 350 entradas en marzo"
    public int calcularCantidadVendida(List<Venta> ventas) throws ServiceException {
        //Por ahora, cada venta = 1 entrada
        //En el futuro, si agregamos cantidad por venta, habría que sumar ese campo
        return ventas.size();
    }
}