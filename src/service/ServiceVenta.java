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


    //METODOS ---------------
    //INSERTAR
    public void insertar(Venta venta) throws ServiceException {
        try {
            if(venta.getCantidad() < 1 || venta.getCantidad() > 5) {
                throw new ServiceException("La cantidad de entradas debe ser entre 1 y 5");
            }
            Espectaculo espectaculo = daoEspectaculo.consultar(venta.getIdEspectaculo());
            if(espectaculo == null || espectaculo.getIdEspectaculo() == 0) {
                throw new ServiceException("El espectáculo con ID " + venta.getIdEspectaculo() + " no existe");
            }

            if(!espectaculo.isActivo()) {
                throw new ServiceException("El espectáculo no está activo. No se pueden vender entradas");
            }

            Date hoy = new Date();
            if(espectaculo.getFecha().before(hoy)) {
                throw new ServiceException("El espectáculo ya pasó. No se pueden vender entradas");
            }

            Ubicacion ubicacion = daoUbicacion.consultar(venta.getIdUbicacion());
            if(ubicacion == null || ubicacion.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación con ID " + venta.getIdUbicacion() + " no existe");
            }

            if(ubicacion.getIdEstadio() != espectaculo.getIdEstadio()) {
                throw new ServiceException("La ubicación seleccionada no pertenece al estadio del espectáculo");
            }

            int capacidadDisponible = verificarCapacidadDisponible(
                    venta.getIdEspectaculo(),
                    venta.getIdUbicacion()
            );

            if(venta.getCantidad() > capacidadDisponible) {
                throw new ServiceException(
                        "No hay capacidad suficiente. " +
                                "Solicitadas: " + venta.getCantidad() + " entrada(s). " +
                                "Disponibles: " + capacidadDisponible + " entrada(s)."
                );
            }
            Usuario vendedor = daoUsuario.consultar(venta.getIdVendedor());
            if(vendedor == null || vendedor.getIdUsuario() == 0) {
                throw new ServiceException("El vendedor con ID " + venta.getIdVendedor() + " no existe");
            }

            if(!vendedor.getRol().equals("VENDEDOR")) {
                throw new ServiceException("El usuario no es un vendedor");
            }

            if(venta.getNombreCliente() == null || venta.getNombreCliente().trim().isEmpty()) {
                throw new ServiceException("El nombre del cliente es obligatorio");
            }

            if(venta.getDniCliente() == null || venta.getDniCliente().trim().isEmpty()) {
                throw new ServiceException("El DNI del cliente es obligatorio");
            }

            if(venta.getPrecioFinal() <= 0) {
                throw new ServiceException("El precio final debe ser mayor a 0");
            }

            double precioUnitario = ubicacion.getPrecio();
            double precioTotalEsperado = precioUnitario * venta.getCantidad();

            double diferencia = Math.abs(venta.getPrecioFinal() - precioTotalEsperado);
            if(diferencia > precioTotalEsperado * 0.5) { // Si difiere más del 50%
                System.out.println("ADVERTENCIA: El precio de la venta difiere significativamente del precio esperado");
                System.out.println("Precio esperado (sin descuentos): $" + precioTotalEsperado);
                System.out.println("Precio final de la venta: $" + venta.getPrecioFinal());
            }

            daoVenta.insertar(venta);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos: " + e.getMessage());
        }
    }



    // MODIFICAR (Actualizar una venta existente)
    public void modificar(Venta venta) throws ServiceException {
        try {
            //VALIDACIONES
            Venta existente = daoVenta.consultar(venta.getIdVenta());
            if(existente == null || existente.getIdVenta() == 0) {
                throw new ServiceException("La venta con ID " + venta.getIdVenta() + " no existe");
            }

            Espectaculo espectaculo = daoEspectaculo.consultar(venta.getIdEspectaculo());
            if(espectaculo == null || espectaculo.getIdEspectaculo() == 0) {
                throw new ServiceException("El espectáculo con ID " + venta.getIdEspectaculo() + " no existe");
            }

            if(!espectaculo.isActivo()) {
                throw new ServiceException("El espectáculo no está activo");
            }

            Ubicacion ubicacion = daoUbicacion.consultar(venta.getIdUbicacion());
            if(ubicacion == null || ubicacion.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación con ID " + venta.getIdUbicacion() + " no existe");
            }

            if(ubicacion.getIdEstadio() != espectaculo.getIdEstadio()) {
                throw new ServiceException("La ubicación no pertenece al estadio del espectáculo");
            }

            if(existente.getIdUbicacion() != venta.getIdUbicacion()) {
                int capacidadDisponible = verificarCapacidadDisponible(
                        venta.getIdEspectaculo(),
                        venta.getIdUbicacion()
                );

                if(capacidadDisponible <= 0) {
                    throw new ServiceException("No hay capacidad disponible en la nueva ubicación");
                }
            }

            Usuario vendedor = daoUsuario.consultar(venta.getIdVendedor());
            if(vendedor == null || vendedor.getIdUsuario() == 0) {
                throw new ServiceException("El vendedor con ID " + venta.getIdVendedor() + " no existe");
            }

            if(venta.getNombreCliente() == null || venta.getNombreCliente().trim().isEmpty()) {
                throw new ServiceException("El nombre del cliente es obligatorio");
            }

            if(venta.getDniCliente() == null || venta.getDniCliente().trim().isEmpty()) {
                throw new ServiceException("El DNI del cliente es obligatorio");
            }

            if(venta.getPrecioFinal() <= 0) {
                throw new ServiceException("El precio final debe ser mayor a 0");
            }

            daoVenta.modificar(venta);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    // ELIMINAR (Cancelar una venta)
    public void eliminar(int id) throws ServiceException {
        try {
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


    // CONSULTAR (Buscar una venta por ID)
    public Venta consultar(int id) throws ServiceException {
        try {
            return daoVenta.consultar(id);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    // CONSULTAR TODOS (Obtener todas las ventas)
    public List<Venta> consultarTodos() throws ServiceException {
        try {
            return daoVenta.consultarTodos();
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    // CONSULTAR POR VENDEDOR (Ventas de un vendedor específico)
    //Este método usa IVentaDAO que ya tiene consultarPorVendedor()
    //Es útil para que cada vendedor vea solo sus propias ventas
    public List<Venta> consultarPorVendedor(int idVendedor) throws ServiceException {
        try {
            return daoVenta.consultarPorVendedor(idVendedor);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    // CONSULTAR POR ESPECTÁCULO (Ventas de un espectáculo)
    public List<Venta> consultarPorEspectaculo(int idEspectaculo) throws ServiceException {
        try {
            return daoVenta.consultarPorEspectaculo(idEspectaculo);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }



    // CONSULTAR POR UBICACIÓN (Ventas de una ubicación)
    //Útil para validar si se puede eliminar una ubicación
    public List<Venta> consultarPorUbicacion(int idUbicacion) throws ServiceException {
        try {
            return daoVenta.consultarPorUbicacion(idUbicacion);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    // CONSULTAR POR FECHA (Ventas en un rango de fechas)
    //Útil para reportes: "¿Cuánto se vendió en marzo?"
    public List<Venta> consultarPorFecha(Date fechaInicio, Date fechaFin) throws ServiceException {
        try {
            if(fechaInicio == null || fechaFin == null) {
                throw new ServiceException("Las fechas no pueden ser nulas");
            }
            if(fechaInicio.after(fechaFin)) {
                throw new ServiceException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }

            return daoVenta.consultarPorFecha(fechaInicio, fechaFin);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }



    // CONSULTAR POR ESPECTÁCULO Y FECHA
    //Útil para reportes específicos: "¿Cuánto se vendió del concierto de X en marzo?"
    public List<Venta> consultarPorEspectaculoYFecha(int idEspectaculo, Date fechaInicio, Date fechaFin) throws ServiceException {
        try {
            if(fechaInicio == null || fechaFin == null) {
                throw new ServiceException("Las fechas no pueden ser nulas");
            }

            if(fechaInicio.after(fechaFin)) {
                throw new ServiceException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }

            return daoVenta.consultarPorEspectaculoYFecha(idEspectaculo, fechaInicio, fechaFin);
        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }



    // VERIFICAR CAPACIDAD DISPONIBLE
    //Este metodo calcula cuántas entradas quedan disponibles en una ubicación para un espectáculo específico
    public int verificarCapacidadDisponible(int idEspectaculo, int idUbicacion) throws ServiceException {
        try {
            //Obtener la ubicación para saber su capacidad total
            Ubicacion ubicacion = daoUbicacion.consultar(idUbicacion);
            if(ubicacion == null || ubicacion.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación no existe");
            }

            int capacidadTotal = ubicacion.getCapacidad();

            //Calcular cuántas entradas ya se vendieron
            int entradasVendidas = calcularEntradasVendidas(idEspectaculo, idUbicacion);

            //Restar para obtener la capacidad disponible
            int capacidadDisponible = capacidadTotal - entradasVendidas;

            return capacidadDisponible;

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }




// Cuenta la cantidad REAL de entradas vendidas (no la cantidad de registros)
// Usa consulta optimizada que filtra directo en la BD
    private int calcularEntradasVendidas(int idEspectaculo, int idUbicacion) throws ServiceException {
        try {
            List<Venta> ventasEspecificas = daoVenta.consultarPorEspectaculoYUbicacion(idEspectaculo, idUbicacion);

            int totalEntradas = 0;
            for(Venta venta : ventasEspecificas) {
                totalEntradas += venta.getCantidad();
            }

            return totalEntradas;

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    // CALCULAR TOTAL RECAUDADO (Para reportes)
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



    // CALCULAR CANTIDAD VENDIDA (Para reportes)
    //Cuenta cuántas entradas se vendieron de una lista de ventas
    //Útil para reportes como: "Se vendieron 350 entradas en marzo"
    public int calcularCantidadVendida(List<Venta> ventas) throws ServiceException {
        return ventas.size();
    }
}