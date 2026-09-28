package Consultas;

import Conexion.ConexionDB;
import Clase.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDB {

    //constructor automatico al abrir el programa para que aparezcan todos los vehiculos
    public VehiculoDB() {
        reiniciarBaseDeDatos();
    }

    private void reiniciarBaseDeDatos() {
        String sqlLimpiarAlquileres = "DELETE FROM alquileres";
        String sqlRestablecerCoches = "UPDATE vehiculos SET disponible = true";

        try (Connection con = ConexionDB.getConexion();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(sqlLimpiarAlquileres);
            stmt.executeUpdate(sqlRestablecerCoches);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //leer los vehículos
    public List<Vehiculo> obtenerDisponibles() {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT id, marca, modelo, combustible, precio, disponible FROM vehiculos WHERE disponible = true";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Vehiculo(
                        rs.getInt("id"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("combustible"),
                        rs.getInt("precio"),
                        rs.getBoolean("disponible")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener vehículos: " + e.getMessage());
        }
        return lista;
    }

    //alquiler vehículo (elimina el vehiculo, al abrir de nuevo reaparece)
    public boolean alquilarVehiculo(int idVehiculo, String dniCliente) {
        String cliente = "SELECT id FROM clientes WHERE dni = ?";
        String alquiler = "INSERT INTO alquileres (id_vehiculo, id_cliente, fecha) VALUES (?, ?, NOW())";
        String vehiculo = "UPDATE vehiculos SET disponible = false WHERE id = ?";

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false); //si sale bien se guardan todos los cambios a la vez, en false para decidir cuando se realiza

            int idCliente = -1;
            try (PreparedStatement preparedStatement1 = con.prepareStatement(cliente)) {
                preparedStatement1.setString(1, dniCliente);
                try (ResultSet rs = preparedStatement1.executeQuery()) {
                    if (rs.next()) {
                        idCliente = rs.getInt("id");
                    }
                }
            }

            if (idCliente == -1) {
                con.rollback(); //devolverlo a su estado inicial
                return false;
            }

            try (PreparedStatement prepareStatement2 = con.prepareStatement(alquiler)) {
                prepareStatement2.setInt(1, idVehiculo);
                prepareStatement2.setInt(2, idCliente);
                prepareStatement2.executeUpdate();
            }

            try (PreparedStatement preparedStatement3 = con.prepareStatement(vehiculo)) {
                preparedStatement3.setInt(1, idVehiculo);
                preparedStatement3.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error en la operación de alquiler: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        }
    }

    public boolean eliminarVehiculo(int idVehiculo) {
        String sql = "DELETE FROM vehiculos WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVehiculo);
            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
