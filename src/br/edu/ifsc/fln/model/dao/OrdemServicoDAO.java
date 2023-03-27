package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.exception.ExceptionLavacao;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Pontuacao;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class OrdemServicoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(OrdemServico ordemServico) throws DAOException {
        String sql = "INSERT INTO ordem_servico(total, agenda, desconto, id_veiculo, status) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            stmt.setBigDecimal(1, ordemServico.getTotal());
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getDesconto());
            stmt.setInt(4, ordemServico.getVeiculo().getId());
            if  (ordemServico.getStatus() != null) {
                stmt.setString(5, ordemServico.getStatus().name());
            } else {
                //TODO apresentar situação clara de inconsistência de dados
                //tratamento de exceções e a necessidade de uso de commit e rollback
                //stmt.setString(6, "teste");
                stmt.setString(5, EStatus.ABERTA.name());
            }
            stmt.execute();
            ItemOS_DAO itemOS_DAO = new ItemOS_DAO();
            itemOS_DAO.setConnection(connection);
            
            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(connection);
            int quantidade = 0;
            for (ItemOS itemOS: ordemServico.getItensOS()) {
                Servico servico = itemOS.getServico();
                itemOS.setOrdemServico(this.buscarUltimaOrdemServico());
                itemOS_DAO.inserir(itemOS);
                quantidade += servico.getPontos();
            }
            PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
            pontuacaoDAO.setConnection(connection);
            ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(quantidade + pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao()));
            pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                //TODO Mensagem relacionada ao método rollback
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            throw new DAOException("Não foi possível salvar o registro da ordem de serviço no banco de dados!", ex);
        }
    }

 public boolean alterar(OrdemServico ordemServico) throws DAOException, ExceptionLavacao {
        String sql = "UPDATE ordem_servico SET total=?, agenda=?, desconto=?, id_veiculo=?, status=? WHERE numero=?";
        try {
            //antes de atualizar a nova venda, a anterior terá seus itens de venda removidos
            // e o estoque dos produtos da venda sofrerão um estorno
            connection.setAutoCommit(false);
            ItemOS_DAO itemOS_DAO = new ItemOS_DAO();
            itemOS_DAO.setConnection(connection);
            PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
            pontuacaoDAO.setConnection(connection);
            
            //Venda vendaAnterior = buscar(venda.getCdVenda());
            OrdemServico ordemAnterior = buscar(ordemServico);

            List<ItemOS> listaItensOS = itemOS_DAO.listarPorOrdemServico(ordemServico);

            int quantidadeAnterior = 0;
            for (ItemOS itemOS : listaItensOS) {
                itemOS_DAO.remover(itemOS);
                quantidadeAnterior += itemOS.getServico().getPontos();
            }
            int quantidade = pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao());

            ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(quantidade);
            ordemServico.getVeiculo().getCliente().getPontuacao().subtrair(quantidadeAnterior);
            pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
            
            //atualiza os dados da ordem de serviço
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, ordemServico.getTotal());
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getDesconto());
            stmt.setInt(4, ordemServico.getVeiculo().getId());
            if  (ordemServico.getStatus() != null) {
                stmt.setString(5, ordemServico.getStatus().name());
            } else {
                stmt.setString(5, EStatus.ABERTA.name());
            }
            stmt.setInt(6, ordemServico.getNumero());
            stmt.execute();
            
            int quantidadeAtual = pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao());
            for (ItemOS itemOS: ordemServico.getItensOS()) {
                itemOS_DAO.inserir(itemOS);
                quantidadeAtual += itemOS.getServico().getPontos();
            }
            ordemServico.getVeiculo().getCliente().getPontuacao().adicionar(quantidadeAtual);
            pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
            connection.commit();
            return true;
        } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
                }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void remover(OrdemServico ordemServico) throws DAOException, ExceptionLavacao {
        String sql = "DELETE FROM ordem_servico WHERE numero=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            try {
                connection.setAutoCommit(false);
                ItemOS_DAO itemOS_DAO = new ItemOS_DAO();
                itemOS_DAO.setConnection(connection);
                
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                int quantidade = 0;
                for (ItemOS itemOS : ordemServico.getItensOS()) {
                    itemOS_DAO.remover(itemOS);
                    quantidade += itemOS.getServico().getPontos();
                }
                PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
                pontuacaoDAO.setConnection(connection);
                ordemServico.getVeiculo().getCliente().getPontuacao().setQuantidade(pontuacaoDAO.buscarQuantidade(ordemServico.getVeiculo().getCliente().getPontuacao()));
                
                ordemServico.getVeiculo().getCliente().getPontuacao().subtrair(quantidade);
                
                pontuacaoDAO.alterar(ordemServico.getVeiculo().getCliente().getPontuacao());
                
                stmt.setInt(1, ordemServico.getNumero());
                stmt.execute();
                connection.commit();
            } catch (SQLException exc) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    //TODO Mensagem relacionada ao método rollback
                    Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
                }
            }            
        } catch (SQLException ex) {
          throw new DAOException("Não foi excluir o registro da ordem de serviço no banco de dados!", ex);
        }
    }

    public List<OrdemServico> listar() throws DAOException {
        String sql = "SELECT * FROM ordem_servico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Veiculo veiculo = new Veiculo();
                List<ItemOS> itensOS = new ArrayList();

                ordemServico.setNumero(resultado.getInt("numero"));
                //ordemServico.setTotal(resultado.getBigDecimal("total"));
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServico.setDesconto(resultado.getDouble("desconto"));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServico.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
               
                //Obtendo os dados completos do Veículo associado à Ordem de Serviço
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(connection);
                veiculo = veiculoDAO.buscar(veiculo);

                //Obtendo os dados completos dos Itens de Venda associados à Venda
                ItemOS_DAO itemOS_DAO = new ItemOS_DAO();
                itemOS_DAO.setConnection(connection);
                itensOS = itemOS_DAO.listarPorOrdemServico(ordemServico);

                ordemServico.setVeiculo(veiculo);
                ordemServico.setItensOS(itensOS);
                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem das ordens de serviço no banco de dados!", ex);
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) throws DAOException {
        String sql = "SELECT * FROM ordem_servico WHERE numero=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, (int) ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setNumero(resultado.getInt("numero"));
                //ordemServicoRetorno.setTotal(resultado.getBigDecimal("total"));
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a pesquisa da ordem de serviço no banco de dados!", ex);
        }
        return ordemServicoRetorno;
    }
    
    public OrdemServico buscar(int id) throws DAOException {
        /*
            Método necessário para evitar que a instância de retorno seja 
            igual a instância a ser atualizada.
        */
        String sql = "SELECT * FROM ordem_servico WHERE numero=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setNumero(resultado.getInt("numero"));
                //ordemServicoRetorno.setTotal(resultado.getBigDecimal("total"));
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a pesquisa da ordem de serviço no banco de dados!", ex);           
        }
        return ordemServicoRetorno;
    }    
    public OrdemServico buscarUltimaOrdemServico() throws DAOException {
        String sql = "SELECT max(numero) as max FROM ordem_servico";
        
        OrdemServico retorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                retorno.setNumero(resultado.getInt("max"));
                return retorno;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a pesquisa da última ordem de serviço no banco de dados!", ex);           
        }
        return retorno;
    }
    
    public Map<Integer, ArrayList> listarQuantidadeOSPorMes() throws DAOException {
        String sql = "select count(numero) as count, extract(year from agenda) as ano, "
                + " extract(month from agenda) as mes from ordem_servico group by ano, "
                + "mes order by ano, mes";
        Map<Integer, ArrayList> retorno = new HashMap();
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                ArrayList linha = new ArrayList();
                if (!retorno.containsKey(resultado.getInt("ano")))
                {
                    linha.add(resultado.getInt("mes"));
                    linha.add(resultado.getInt("count"));
                    retorno.put(resultado.getInt("ano"), linha);
                }else{
                    ArrayList linhaNova = retorno.get(resultado.getInt("ano"));
                    linhaNova.add(resultado.getInt("mes"));
                    linhaNova.add(resultado.getInt("count"));
                }
            }
            if (retorno.size() > 0) {
                retorno = ordenar(retorno);
            }
            return retorno;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem da quantidade de ordens de serviço por mês no banco de dados!", ex);        
        }
    }
    
    private Map<Integer, ArrayList> ordenar(Map<Integer, ArrayList> ordensServico) {
        LinkedHashMap<Integer, ArrayList> orderedMap = ordensServico.entrySet() 
            .stream() 
            .sorted(Map.Entry.comparingByKey()) 
                .collect(Collectors.toMap(Map.Entry::getKey, 
                    Map.Entry::getValue, //
                    (key, content) -> content, //
                    LinkedHashMap::new));
        return orderedMap;
    }

}
