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
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroServicoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<ECategoriaS> cbCategoria;

    @FXML
    private Label lbGrande;

    @FXML
    private Label lbMedio;

    @FXML
    private Label lbMoto;

    @FXML
    private Label lbPadrao;

    @FXML
    private Label lbPequeno;
    
    @FXML
    private TextField tfDescricao;

  @FXML
    private Spinner<Double> spnGrande;

    @FXML
    private Spinner<Double> spnMedio;

    @FXML
    private Spinner<Double> spnMoto;

    @FXML
    private Spinner<Double> spnPadrao;

    @FXML
    private Spinner<Double> spnPequeno;
     
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Servico servico;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         servicoDAO.setConnection(connection);
         SpinnerValueFactory<Double> spnDoubleFPequeno = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 99999.99, 0.0);
         SpinnerValueFactory<Double> spnDoubleFMedio = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 99999.99, 0.0);
         SpinnerValueFactory<Double> spnDoubleFGrande = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 99999.99, 0.0);
         SpinnerValueFactory<Double> spnDoubleFMoto = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 99999.99, 0.0);
         SpinnerValueFactory<Double> spnDoubleFPadrao = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 99999.99, 0.0);
         
         spnPequeno.setValueFactory(spnDoubleFPequeno);
         spnMedio.setValueFactory(spnDoubleFMedio);
         spnGrande.setValueFactory(spnDoubleFGrande);
         spnMoto.setValueFactory(spnDoubleFMoto);
         spnPadrao.setValueFactory(spnDoubleFPadrao);
         carregarComboBoxCategorias();
         
         cbCategoria.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> 
         {
             if("PEQUENO".equals(newValue.name()))
             {
                lbPequeno.setDisable(false);
                spnPequeno.setDisable(false);
               
                lbMedio.setDisable(true);
                spnMedio.setDisable(true);
                spnMedio.getValueFactory().setValue(0.0);
                lbGrande.setDisable(true);
                spnGrande.setDisable(true);
                spnGrande.getValueFactory().setValue(0.0);
                lbMoto.setDisable(true);
                spnMoto.setDisable(true);
                spnMoto.getValueFactory().setValue(0.0);
                lbPadrao.setDisable(true);
                spnPadrao.setDisable(true);
                spnPadrao.getValueFactory().setValue(0.0);
             }
             else if("MEDIO".equals(newValue.name()))
             {
                lbPequeno.setDisable(true);
                spnPequeno.setDisable(true);
                spnPequeno.getValueFactory().setValue(0.0);
                
                lbMedio.setDisable(false);     
                spnMedio.setDisable(false);
                
                lbGrande.setDisable(true);
                spnGrande.setDisable(true);
                spnGrande.getValueFactory().setValue(0.0);
                lbMoto.setDisable(true);
                spnMoto.setDisable(true);
                spnMoto.getValueFactory().setValue(0.0);
                lbPadrao.setDisable(true);
                spnPadrao.setDisable(true);
                spnPadrao.getValueFactory().setValue(0.0);
             }
             else if("GRANDE".equals(newValue.name()))
             {
                lbPequeno.setDisable(true);
                spnPequeno.setDisable(true);
                spnPequeno.getValueFactory().setValue(0.0);
                lbMedio.setDisable(true);
                spnMedio.setDisable(true);
                spnMedio.getValueFactory().setValue(0.0);
                
                lbGrande.setDisable(false);
                spnGrande.setDisable(false);
                
                lbMoto.setDisable(true);
                spnMoto.setDisable(true);
                spnMoto.getValueFactory().setValue(0.0);
                lbPadrao.setDisable(true);
                spnPadrao.setDisable(true);
                spnPadrao.getValueFactory().setValue(0.0);
             }
             else if("MOTO".equals(newValue.name()))
             {
                lbPequeno.setDisable(true);
                spnPequeno.setDisable(true);
                spnPequeno.getValueFactory().setValue(0.0);
                lbMedio.setDisable(true);
                spnMedio.setDisable(true);
                spnMedio.getValueFactory().setValue(0.0);
                lbGrande.setDisable(true);
                spnGrande.setDisable(true);
                spnGrande.getValueFactory().setValue(0.0);
                
                lbMoto.setDisable(false);
                spnMoto.setDisable(false);
                
                lbPadrao.setDisable(true);
                spnPadrao.setDisable(true);
                spnPadrao.getValueFactory().setValue(0.0);
             }
             else if("PADRAO".equals(newValue.name()))
             {
                lbPequeno.setDisable(true);
                spnPequeno.setDisable(true);
                spnPequeno.getValueFactory().setValue(0.0);
                lbMedio.setDisable(true);
                spnMedio.setDisable(true);
                spnMedio.getValueFactory().setValue(0.0);
                lbGrande.setDisable(true);
                spnGrande.setDisable(true);
                spnGrande.getValueFactory().setValue(0.0);
                lbMoto.setDisable(true);
                spnMoto.setDisable(true);
                spnMoto.getValueFactory().setValue(0.0);
                
                lbPadrao.setDisable(false);
                spnPadrao.setDisable(false);
             }
             else if("TODAS".equals(newValue.name()))
             {
                lbPequeno.setDisable(false);
                spnPequeno.setDisable(false);
                lbMedio.setDisable(false);
                spnMedio.setDisable(false);
                lbGrande.setDisable(false);
                spnGrande.setDisable(false);
                lbMoto.setDisable(false);
                spnMoto.setDisable(false);
                lbPadrao.setDisable(false);
                spnPadrao.setDisable(false);
             }
         });   
         
    }       

    public void carregarComboBoxCategorias() {
        cbCategoria.setItems(FXCollections.observableArrayList(ECategoriaS.values()));
    }
    
    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        this.tfDescricao.setText(servico.getDescricao());
        
        cbCategoria.getSelectionModel().select(servico.getCategoria());
        
        
        if(servico.getCategoria().getDescricao() == "PEQUENO")
        {
            spnPequeno.getValueFactory().setValue(servico.getValor());
        }
        else if(servico.getCategoria().getDescricao() == "MEDIO")
        {
            spnMedio.getValueFactory().setValue(servico.getValor());
        }
        else if(servico.getCategoria().getDescricao() == "GRANDE")
        {
            spnGrande.getValueFactory().setValue(servico.getValor());
        }
        else if(servico.getCategoria().getDescricao() == "MOTO")
        {
            spnMoto.getValueFactory().setValue(servico.getValor());
        }
        else if(servico.getCategoria().getDescricao() == "PADRAO")
        {
            spnPadrao.getValueFactory().setValue(servico.getValor());
        }
    }
    

    @FXML
    public void handleBtConfirmar() throws DAOException {
        if (validarEntradaDeDados()) {
            servico.setDescricao(tfDescricao.getText());
            
            servico.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
           
            if(servico.getCategoria().getDescricao() == "TODAS")
            {
                if(validarTodosSpinners())
                {
                    ServicoDAO servicoDAO = new ServicoDAO();
                    servicoDAO.setConnection(connection);
                    
                    Servico servicoPequeno = new Servico();
                    Servico servicoMedio = new Servico();
                    Servico servicoGrande = new Servico();
                    Servico servicoMoto = new Servico();
                    Servico servicoPadrao = new Servico();

                    servicoPequeno.setDescricao(tfDescricao.getText());
                    servicoMedio.setDescricao(tfDescricao.getText());
                    servicoGrande.setDescricao(tfDescricao.getText());
                    servicoMoto.setDescricao(tfDescricao.getText());
                    servicoPadrao.setDescricao(tfDescricao.getText());

                    servicoPequeno.setCategoria(ECategoriaS.PEQUENO);
                    servicoMedio.setCategoria(ECategoriaS.MEDIO);
                    servicoGrande.setCategoria(ECategoriaS.GRANDE);
                    servicoMoto.setCategoria(ECategoriaS.MOTO);
                    servicoPadrao.setCategoria(ECategoriaS.PADRAO);
                    
                                    
                    servicoPequeno.setValor(spnPequeno.getValue());
                    servicoMedio.setValor(spnMedio.getValue());
                    servicoGrande.setValor(spnGrande.getValue());
                    servicoMoto.setValor(spnMoto.getValue());
                    servicoPadrao.setValor(spnPadrao.getValue());
                    
                    //TODO verificar com o professor se o tratamento de exceções deve ser feito aqui
                    servicoDAO.inserir(servicoPequeno);
                    servicoDAO.inserir(servicoMedio);
                    servicoDAO.inserir(servicoGrande);
                    servicoDAO.inserir(servicoMoto);
                    servicoDAO.inserir(servicoPadrao);
                    
                }        
            }
            else if(servico.getCategoria().getDescricao() == "PEQUENO")
            {
                if(validarSpinner(spnPequeno))
                {
                   servico.setValor(spnPequeno.getValue());
                }
            }
            else if(servico.getCategoria().getDescricao() == "MEDIO")
            {
                if(validarSpinner(spnMedio))
                {
                   servico.setValor(spnMedio.getValue());
                }
            }
            else if(servico.getCategoria().getDescricao() == "GRANDE")
            {
                if(validarSpinner(spnGrande))
                {
                   servico.setValor(spnGrande.getValue());
                }
            }
            else if(servico.getCategoria().getDescricao() == "MOTO")
            {
                if(validarSpinner(spnMoto))
                {
                   servico.setValor(spnMoto.getValue());
                }
            }
            else if(servico.getCategoria().getDescricao() == "PADRAO")
            {
                if(validarSpinner(spnPadrao))
                {
                   servico.setValor(spnPadrao.getValue());
                }
            }
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }
    
    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida!\n";
        }
        
        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
    private boolean validarSpinner(Spinner<Double> spn) 
    {
        String errorMessage = "";
        
        if(spn.getValue() == 0.0 || spn.getValue() == null)
        {
            errorMessage += "Valor do serviço inválido! O preço deve ser maior que zero!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
    private boolean validarTodosSpinners() 
    {
        String errorMessage = "";
        
        if(spnPequeno.getValue() == 0.0 || spnPequeno.getValue() == null)
        {
            errorMessage += "Valor do serviço pequeno inválido! O preço deve ser maior que zero!\n";
        }
        
        if(spnMedio.getValue() == 0.0 || spnMedio.getValue() == null)
        {
            errorMessage += "Valor do serviço médio inválido! O preço deve ser maior que zero!\n";
        }
        
        if(spnGrande.getValue() == 0.0 || spnGrande.getValue() == null)
        {
            errorMessage += "Valor do serviço grande inválido! O preço deve ser maior que zero!\n";
        }
        
        if(spnMoto.getValue() == 0.0 || spnMoto.getValue() == null)
        {
            errorMessage += "Valor do serviço moto inválido! O preço deve ser maior que zero!\n";
        }
        
        if(spnPadrao.getValue() == 0.0 || spnPadrao.getValue() == null)
        {
            errorMessage += "Valor do serviço padrão inválido! O preço deve ser maior que zero!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }  
}
