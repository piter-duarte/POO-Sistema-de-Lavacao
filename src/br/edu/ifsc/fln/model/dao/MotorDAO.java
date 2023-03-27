package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Motor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotorDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Motor motor) throws DAOException {
        
       String sql = "INSERT INTO motor(id_modelo) VALUES(?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, motor.getModelo().getId());
            stmt.execute();
            alterar(motor);
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível salvar o registro do motor no banco de dados!", ex);
        }
    }

    public void alterar(Motor motor) throws DAOException {
        String sql = "UPDATE motor SET potencia=?, tipoCombustivel=? WHERE id_modelo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, motor.getPotencia());
            stmt.setString(2, motor.getTipoCombustivel().name());
            stmt.setInt(3, motor.getModelo().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o registro do modelo no banco de dados!", ex);
        }
    }

    public void remover(Motor motor) throws DAOException {
        String sql = "DELETE FROM motor WHERE id_modelo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, motor.getModelo().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi excluir o registro do motor no banco de dados!", ex);
        }
    }
    
    public List<Motor> listar() throws DAOException {
        String sql = "SELECT * FROM motor";
        List<Motor> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Motor motor = new Motor();
                motor.getModelo().setId(resultado.getInt("id_modelo"));
                motor.setPotencia(resultado.getInt("potencia"));
                motor.setTipoCombustivel(Enum.valueOf(ETipoCombustivel.class, resultado.getString("tipoCombustivel")));
                retorno.add(motor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem dos motores no banco de dados!", ex);
        }
        return retorno;
    }
    
}
