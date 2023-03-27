/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cor;
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
public class FXMLAnchorPaneCadastroCorController implements Initializable {

    
  @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbCorId;

    @FXML
    private Label lbCorNome;

    @FXML
    private TableColumn<Cor, String> tableColumnCorNome;

    @FXML
    private TableView<Cor> tableViewCores;
    
    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CorDAO corDAO = new CorDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        corDAO.setConnection(connection);
        carregarTableViewCor();
        
        tableViewCores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCores(newValue)); //lambda ->
    }     
    
    public void carregarTableViewCor() {
        tableColumnCorNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
      try {
          listaCores = corDAO.listar();
      } catch (DAOException ex) {
            AlertDialog.exceptionMessage(ex);
      }
        
        observableListCores = FXCollections.observableArrayList(listaCores);
        tableViewCores.setItems(observableListCores);
    }
    
    public void selecionarItemTableViewCores(Cor cor) {
        if (cor != null) {
            lbCorId.setText(String.valueOf(cor.getId())); 
            lbCorNome.setText(cor.getNome());
        } else {
            lbCorId.setText(""); 
            lbCorNome.setText("");
        }
        
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Cor cor = new Cor();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
        if (btConfirmarClicked) {
            try {
                corDAO.inserir(cor);
            } catch (DAOException ex) {
                  AlertDialog.exceptionMessage(ex);
            }
            carregarTableViewCor();
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Cor cor = tableViewCores.getSelectionModel().getSelectedItem();
        if (cor != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
            if (btConfirmarClicked) {
                try {
                    corDAO.alterar(cor);
                } catch (DAOException ex) {
                     AlertDialog.exceptionMessage(ex);
                }
                carregarTableViewCor();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Cor cor = tableViewCores.getSelectionModel().getSelectedItem();
        if (cor != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Excluir Cor");
            alert.setContentText("Deseja realmente excluir essa cor ?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                try {
                    corDAO.remover(cor);
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableViewCor();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroCorDialog(Cor cor) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroCorController.class.getResource("../view/FXMLAnchorPaneCadastroCorDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cor");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o obejto categoria para o controller
        FXMLAnchorPaneCadastroCorDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCor(cor);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
    
}
