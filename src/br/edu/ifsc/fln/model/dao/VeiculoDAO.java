package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VeiculoDAO{

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Veiculo veiculo) throws DAOException {
        String sql = "INSERT INTO veiculo(id, placa, observacoes, id_modelo, id_cor, id_cliente) VALUES(?,?,?,?,?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.setString(2, veiculo.getPlaca());
            stmt.setString(3, veiculo.getObservacoes());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getCor().getId());
            stmt.setInt(6, veiculo.getCliente().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível salvar o registro do veículo no banco de dados!", ex);
        }
    }

    public void alterar(Veiculo veiculo) throws DAOException {
        String sql = "UPDATE veiculo SET placa=?, observacoes=?, id_modelo=?, id_cor=? , id_cliente=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setInt(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o registro do veículo no banco de dados!", ex);
        }
    }

    public void remover(Veiculo veiculo) throws DAOException {
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi excluir o registro do veículo no banco de dados!", ex);
        }
    }

    public List<Veiculo> listar() throws DAOException {
        String sql =  "SELECT v.id as veiculo_id, v.placa as veiculo_placa, v.observacoes as veiculo_observacoes, m.id as modelo_id, m.descricao as modelo_descricao, m.categoria as modelo_categoria, mm.id as marca_id, mm.nome as marca_nome, mt.potencia as motor_potencia, mt.tipoCombustivel as motor_tipoCombustivel, c.id as cor_id, c.nome as cor_nome, cl.id as cliente_id, cl.nome as cliente_nome, cl.celular as cliente_celular, cl.email as cliente_email, cl.endereco as cliente_endereco, cl.dataCadastro as cliente_dataCadastro, cl.dataNascimento cliente_dataNascimento, pf.cpf as cliente_cpf, pj.cnpj as cliente_cnpj, pj.inscricaoEstadual as cliente_inscricaoEstadual "
                + "FROM veiculo v "
                + "INNER JOIN modelo m ON m.id = v.id_modelo "
                + "INNER JOIN marca mm ON m.id_marca = mm.id "
                + "INNER JOIN motor mt ON m.id = mt.id_modelo "
                + "JOIN cor c ON c.id = v.id_cor "
                + "INNER JOIN cliente cl on cl.id=v.id_cliente "
                + "LEFT JOIN pessoa_fisica pf on pf.id_cliente = cl.id "
                + "LEFT JOIN pessoa_juridica pj on pj.id_cliente = cl.id";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem dos veículos do banco de dados!", ex);
        }
        return retorno;
    }
    
    private Veiculo populateVO(ResultSet rs) throws SQLException {
        
        Veiculo veiculo = new Veiculo();
        Modelo modelo = new Modelo();
        Motor motor = new Motor();
        Marca marca = new Marca();
        Cor cor = new Cor();  
        Cliente cliente;

        modelo.setMarca(marca);
        modelo.setMotor(motor);
        motor.setModelo(modelo);
        
        veiculo.setModelo(modelo);
        veiculo.setCor(cor);

      
        
        modelo.setId(rs.getInt("modelo_id"));
        modelo.setDescricao(rs.getString("modelo_descricao")); 
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("modelo_categoria")));
        
        marca.setId(rs.getInt("marca_id"));
        marca.setNome(rs.getString("marca_nome"));
        
        motor.setPotencia(rs.getInt("motor_potencia"));
        motor.setTipoCombustivel(Enum.valueOf(ETipoCombustivel.class, rs.getString("motor_tipoCombustivel")));

        
        cor.setId(rs.getInt("cor_id"));
        cor.setNome(rs.getString("cor_nome"));
        
        veiculo.setId(rs.getInt("veiculo_id"));
        veiculo.setPlaca(rs.getString("veiculo_placa"));
        veiculo.setObservacoes(rs.getString("veiculo_observacoes"));
        if (rs.getString("cliente_cnpj") == null || rs.getString("cliente_cnpj").length() <= 0) 
        {
            //é um cliente PessoaFisica
            cliente =  new PessoaFisica();
            ((PessoaFisica)cliente).setCpf(rs.getString("cliente_cpf"));
        }
        else
        {
           //é um cliente PessoaJuridica
           cliente =  new PessoaJuridica();
           ((PessoaJuridica)cliente).setCnpj(rs.getString("cliente_cnpj"));
           ((PessoaJuridica)cliente).setInscricaoEstadual(rs.getString("cliente_inscricaoEstadual"));
        }
        veiculo.setCliente(cliente);
        cliente.setId(rs.getInt("cliente_id"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setCelular(rs.getString("cliente_celular"));
        cliente.setEmail(rs.getString("cliente_email"));
        cliente.setEndereco(rs.getString("cliente_endereco"));
        cliente.setDataCadastro(rs.getDate("cliente_dataCadastro").toLocalDate());
        cliente.setDataNascimento(rs.getDate("cliente_dataNascimento").toLocalDate());
        cliente.add(veiculo);
        return veiculo;
    }    
    
    
    public Veiculo buscar(Veiculo veiculo) {
         String sql =  "SELECT v.id as veiculo_id, v.placa as veiculo_placa, v.observacoes as veiculo_observacoes, m.id as modelo_id, m.descricao as modelo_descricao, m.categoria as modelo_categoria, mm.id as marca_id, mm.nome as marca_nome, mt.potencia as motor_potencia, mt.tipoCombustivel as motor_tipoCombustivel, c.id as cor_id, c.nome as cor_nome, cl.id as cliente_id, cl.nome as cliente_nome, cl.celular as cliente_celular, cl.email as cliente_email, cl.endereco as cliente_endereco, cl.dataCadastro as cliente_dataCadastro, cl.dataNascimento cliente_dataNascimento, pf.cpf as cliente_cpf, pj.cnpj as cliente_cnpj, pj.inscricaoEstadual as cliente_inscricaoEstadual "
                + "FROM veiculo v "
                + "INNER JOIN modelo m ON m.id = v.id_modelo "
                + "INNER JOIN marca mm ON m.id_marca = mm.id "
                + "INNER JOIN motor mt ON m.id = mt.id_modelo "
                + "JOIN cor c ON c.id = v.id_cor "
                + "INNER JOIN cliente cl on cl.id=v.id_cliente "
                + "LEFT JOIN pessoa_fisica pf on pf.id_cliente = cl.id "
                + "LEFT JOIN pessoa_juridica pj on pj.id_cliente = cl.id "
                + "WHERE v.id = ?";
        Veiculo retorno = new Veiculo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateSingleVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    private Veiculo populateSingleVO(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        Modelo modelo = new Modelo();
        Motor motor = new Motor();
        Marca marca = new Marca();
        Cor cor = new Cor();  
        Cliente cliente;

        modelo.setMarca(marca);
        modelo.setMotor(motor);
        motor.setModelo(modelo);
        
        veiculo.setModelo(modelo);
        veiculo.setCor(cor);

      
        
        modelo.setId(rs.getInt("modelo_id"));
        modelo.setDescricao(rs.getString("modelo_descricao")); 
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("modelo_categoria")));
        
        marca.setId(rs.getInt("marca_id"));
        marca.setNome(rs.getString("marca_nome"));
        
        motor.setPotencia(rs.getInt("motor_potencia"));
        motor.setTipoCombustivel(Enum.valueOf(ETipoCombustivel.class, rs.getString("motor_tipoCombustivel")));

        
        cor.setId(rs.getInt("cor_id"));
        cor.setNome(rs.getString("cor_nome"));
        
        veiculo.setId(rs.getInt("veiculo_id"));
        veiculo.setPlaca(rs.getString("veiculo_placa"));
        veiculo.setObservacoes(rs.getString("veiculo_observacoes"));
        if (rs.getString("cliente_cnpj") == null || rs.getString("cliente_cnpj").length() <= 0) 
        {
            //é um cliente PessoaFisica
            cliente =  new PessoaFisica();
            ((PessoaFisica)cliente).setCpf(rs.getString("cliente_cpf"));
        }
        else
        {
           //é um cliente PessoaJuridica
           cliente =  new PessoaJuridica();
           ((PessoaJuridica)cliente).setCnpj(rs.getString("cliente_cnpj"));
           ((PessoaJuridica)cliente).setInscricaoEstadual(rs.getString("cliente_inscricaoEstadual"));
        }
        veiculo.setCliente(cliente);
        cliente.setId(rs.getInt("cliente_id"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setCelular(rs.getString("cliente_celular"));
        cliente.setEmail(rs.getString("cliente_email"));
        cliente.setEndereco(rs.getString("cliente_endereco"));
        cliente.setDataCadastro(rs.getDate("cliente_dataCadastro").toLocalDate());
        cliente.setDataNascimento(rs.getDate("cliente_dataNascimento").toLocalDate());
        cliente.add(veiculo);
        return veiculo;
    }
}
