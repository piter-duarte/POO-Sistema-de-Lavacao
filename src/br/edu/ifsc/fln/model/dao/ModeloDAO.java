package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
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


public class ModeloDAO{

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Modelo modelo) throws DAOException {
        String sql = "INSERT INTO modelo(id, descricao, id_marca, categoria) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.setString(2, modelo.getDescricao());
            stmt.setInt(3, modelo.getMarca().getId());
            stmt.setString(4, modelo.getCategoria().name());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível salvar o registro do modelo no banco de dados!", ex);
        }
    }

    public void alterar(Modelo modelo) throws DAOException {
        String sql = "UPDATE modelo SET descricao=?, id_marca=?, categoria=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3, modelo.getCategoria().name());
            stmt.setInt(4, modelo.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o registro do modelo no banco de dados!", ex);
        }
    }

    public void remover(Modelo modelo) throws DAOException {
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi excluir o registro do modelo no banco de dados!", ex);
        }
    }

    public List<Modelo> listar() throws DAOException {
        String sql =  "SELECT m.id as modelo_id, m.descricao as modelo_descricao, m.categoria as modelo_categoria, "
                    + "n.id as marca_id, n.nome as marca_nome, "
                    + "mt.potencia as motor_potencia, mt.tipoCombustivel as motor_tipoCombustivel "
                    + "FROM modelo m "
                    + "INNER JOIN marca n ON n.id = m.id_marca "
                    + "INNER JOIN motor mt ON mt.id_modelo = m.id;";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem dos modelos no banco de dados!", ex);
        }
        return retorno;
    }
    
    public List<Modelo> listarPorMarca(Marca marca) throws DAOException {
        String sql =  "SELECT m.id as modelo_id, m.descricao as modelo_descricao, m.categoria as modelo_categoria, "
                + "n.id as marca_id, n.nome as marca_nome "
                + "FROM modelo m INNER JOIN marca n ON n.id = m.id_marca WHERE n.id = ?;";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, marca.getId());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem dos modelos por marca no banco de dados!", ex);
        }
        return retorno;
    }

    public Modelo buscar(Modelo modelo) throws DAOException {
        String sql =  "SELECT m.id as modelo_id, m.descricao as modelo_descricao, m.categoria as modelo_categoria, "
                + "n.id as marca_id, n.nome as marca_nome "
                + "FROM modelo m INNER JOIN marca n ON n.id = m.id_marca WHERE n.id = ?;";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a pesquisa da modelo no banco de dados!", ex);
        }
        return retorno;
    }
    
    public int getModeloAutoID(Modelo modelo) throws DAOException{
        
        String sql1= "SELECT max(id) as id FROM modelo";
        int id = 0;
        try {         
            PreparedStatement stmt = connection.prepareStatement(sql1);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
            id = resultado.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível recuperar o id do modelo no banco de dados!", ex);
        }
       
        return id;
    }
    
    private Modelo populateVO(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();
        Marca marca = new Marca();
        Motor motor = new Motor();
        modelo.setMarca(marca);
        modelo.setMotor(motor);
        motor.setModelo(modelo);
        
        modelo.setId(rs.getInt("modelo_id"));
        modelo.setDescricao(rs.getString("modelo_descricao"));
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("modelo_categoria")));
        marca.setId(rs.getInt("marca_id"));
        marca.setNome(rs.getString("marca_nome"));
        motor.setPotencia(rs.getInt("motor_potencia"));
        motor.setTipoCombustivel(Enum.valueOf(ETipoCombustivel.class, rs.getString("motor_tipoCombustivel")));
        
        return modelo;
    }
}
