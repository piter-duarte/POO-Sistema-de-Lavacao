/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoriaS;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroServicoController implements Initializable {

    
    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbServicoCategoria;

    @FXML
    private Label lbServicoDescricao;

    @FXML
    private Label lbServicoId;

    @FXML
    private Label lbServicoPontos;

    @FXML
    private Label lbServicoValor;

    @FXML
    private TableColumn<Servico, String> tableColumnServicoDescricao;

    @FXML
    private TableView<Servico> tableViewServicos;
    
    private List<Servico> listaServicos;
    private ObservableList<Servico> observableListServicos;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoDAO.setConnection(connection);
        carregarTableViewServico();
        
        tableViewServicos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewServicos(newValue)); //lambda ->
    }     
    
    public void carregarTableViewServico() {
        tableColumnServicoDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        try {
            listaServicos = servicoDAO.listar();
        } catch (DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        
        observableListServicos = FXCollections.observableArrayList(listaServicos);
        tableViewServicos.setItems(observableListServicos);
    }
    
    public void selecionarItemTableViewServicos(Servico servico) {
        if (servico != null) {
            lbServicoId.setText(String.valueOf(servico.getId())); 
            lbServicoDescricao.setText(servico.getDescricao());
            lbServicoValor.setText(String.valueOf(servico.getValor()));
            lbServicoPontos.setText(String.valueOf(servico.getPontos()));
            lbServicoCategoria.setText(servico.getCategoria().name());
        } else {
            lbServicoId.setText(""); 
            lbServicoDescricao.setText("");
            lbServicoValor.setText("");
            lbServicoPontos.setText("");
            lbServicoCategoria.setText("");
        }
        
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Servico servico = new Servico();
        servico.setCategoria(ECategoriaS.TODAS);
        servico.setValor(0.0);
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
        if (btConfirmarClicked) 
        {
            
            if(servico.getCategoria().getDescricao() == "TODAS")
            {
                carregarTableViewServico();
            }
            else
            {
                try 
                {
                    servicoDAO.inserir(servico);
                } catch (DAOException ex) 
                {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableViewServico();
            }
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Servico servico = tableViewServicos.getSelectionModel().getSelectedItem();
        if (servico != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
            if (btConfirmarClicked) {           
                try {
                    servicoDAO.alterar(servico);
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableViewServico();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Servico servico = tableViewServicos.getSelectionModel().getSelectedItem();
        if (servico != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Excluir Servico");
            alert.setContentText("Deseja realmente excluir esse serviço ?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                try {
                    servicoDAO.remover(servico);
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableViewServico();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Serviço na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroServicoDialog(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroServicoController.class.getResource("../view/FXMLAnchorPaneCadastroServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Servico");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o obejto categoria para o controller
        FXMLAnchorPaneCadastroServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
    
}
