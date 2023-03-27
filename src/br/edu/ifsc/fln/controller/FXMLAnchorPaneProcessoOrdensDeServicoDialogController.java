/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.ItemOS_DAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneProcessoOrdensDeServicoDialogController implements Initializable {
    @FXML
    private Button buttonPesquisar;
    
    @FXML
    private Button buttonAdicionar;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private ChoiceBox choiceBoxStatus;

    @FXML
    private ComboBox<Servico> comboBoxServico;

    @FXML
    private ComboBox<Veiculo> comboBoxVeiculos;

    @FXML
    private MenuItem contextMenuItemAtualizarObs;

    @FXML
    private MenuItem contextMenuItemRemoverItem;

    @FXML
    private ContextMenu contextMenuTableView;

    @FXML
    private DatePicker datePickerData;

    @FXML
    private TableColumn<ItemOS, String> tableColumnObservacoes;

    @FXML
    private TableColumn<ItemOS, Servico> tableColumnServico;

    @FXML
    private TableColumn<ItemOS, BigDecimal> tableColumnValor;

    @FXML
    private TableView<ItemOS> tableViewItensDeVenda;

       @FXML
    private TextField textFieldCategoria;

    @FXML
    private TextField textFieldCliente;

    @FXML
    private TextField textFieldDesconto;

    @FXML
    private TextField textFieldMarca;

    @FXML
    private TextField textFieldModelo;

    @FXML
    private TextField textFieldObservacoesServico;

    @FXML
    private TextField textFieldValor;

    @FXML
    private TextField textFieldValorServico;

    private List<Veiculo> listaVeiculos;
    private List<Servico> listaServicos;
    private ObservableList<Veiculo> observableListVeiculos;
    private ObservableList<Servico> observableListServicos;
    private ObservableList<ItemOS> observableListItensOS;

    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);
        carregarComboBoxVeiculos();
        carregarChoiceBoxSituacao();
        setFocusLostHandle();
        tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("servico"));
        tableColumnObservacoes.setCellValueFactory(new PropertyValueFactory<>("observacoes"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorServico"));
    }

    private void carregarComboBoxVeiculos() {
        try {
            listaVeiculos = veiculoDAO.listar();
        } catch (DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        comboBoxVeiculos.setItems(observableListVeiculos);
    }

    private void carregarComboBoxServicos(Veiculo veiculo) {
        try {
            listaServicos = servicoDAO.listarPorCategoria(veiculo);
        } catch (DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListServicos = FXCollections.observableArrayList(listaServicos);
        comboBoxServico.setItems(observableListServicos);
    }
    
    
    public void carregarChoiceBoxSituacao() {
        choiceBoxStatus.setItems( FXCollections.observableArrayList(EStatus.values()));
        choiceBoxStatus.getSelectionModel().select(0);
    }

    private void setFocusLostHandle() {
        textFieldDesconto.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (textFieldDesconto.getText() != null && !textFieldDesconto.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    ordemServico.setDesconto(Double.parseDouble(textFieldDesconto.getText()));
                    textFieldValor.setText(ordemServico.getTotal().toString());
                    
                }
            }
        });
    }
    
    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the venda
     */
    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    /**
     * @param venda the venda to set
     */
    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
        if (ordemServico.getNumero()!= 0) { 
            comboBoxVeiculos.getSelectionModel().select(this.ordemServico.getVeiculo()); 
            textFieldCliente.setText(ordemServico.getVeiculo().getCliente().getNome());
            textFieldModelo.setText(ordemServico.getVeiculo().getModelo().getDescricao());
            textFieldMarca.setText(ordemServico.getVeiculo().getModelo().getMarca().getNome());
            textFieldCategoria.setText(ordemServico.getVeiculo().getModelo().getCategoria().name());
            carregarComboBoxServicos(ordemServico.getVeiculo());
            comboBoxServico.setDisable(false);
            comboBoxServico.setPromptText("Selecione o serviço ...");
            textFieldValorServico.setDisable(false);
            
            comboBoxServico.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> 
            {
                textFieldValorServico.setText(String.valueOf(newValue.getValor()));  
            });
            
            textFieldObservacoesServico.setDisable(false);
            buttonAdicionar.setDisable(false);
            comboBoxVeiculos.setDisable(true);
            buttonPesquisar.setDisable(true);
            datePickerData.setValue(this.ordemServico.getAgenda());
            observableListItensOS = FXCollections.observableArrayList(
                    this.ordemServico.getItensOS());
            tableViewItensDeVenda.setItems(observableListItensOS);
            textFieldValor.setText(String.format("%.2f", this.ordemServico.getTotal()));
            textFieldDesconto.setText(String.format("%.2f", this.ordemServico.getDesconto()));
            choiceBoxStatus.getSelectionModel().select(this.ordemServico.getStatus());

        }
    }

    @FXML
    public void handleButtonAdicionar() {
        Servico servico;
        ItemOS itemOS = new ItemOS();
        if (validarServiçoSelecionado()) {
            //o comboBox possui dados sintetizados de Serviço para evitar carga desnecessária de informação
            servico = comboBoxServico.getSelectionModel().getSelectedItem();
            try {
                //a instrução a seguir busca detalhes do serviço selecionado
                servico = servicoDAO.buscar(servico);
            } catch (DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
                
            itemOS.setServico(servico); 
            itemOS.setObservacoes(textFieldObservacoesServico.getText());
            itemOS.setValorServico(BigDecimal.valueOf(Double.parseDouble(textFieldValorServico.getText())));
            itemOS.setOrdemServico(ordemServico);
            ordemServico.getItensOS().add(itemOS);
            observableListItensOS = FXCollections.observableArrayList(ordemServico.getItensOS());
            tableViewItensDeVenda.setItems(observableListItensOS);
            textFieldValor.setText(String.format("%.2f", ordemServico.getTotal()));   
        }
    }

    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            ordemServico.setVeiculo(comboBoxVeiculos.getSelectionModel().getSelectedItem());
            ordemServico.setAgenda(datePickerData.getValue());
            ordemServico.setStatus((EStatus)choiceBoxStatus.getSelectionModel().getSelectedItem());
            ordemServico.setDesconto(Double.parseDouble(textFieldDesconto.getText()));
            ordemServico.setItensOS(observableListItensOS);
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleButtonCancelar() {
        dialogStage.close();
    }
    
    @FXML
    void handleButtonPesquisar(ActionEvent event) {   
        if (validarVeiculoSelecionado()) 
        {
            ordemServico.setVeiculo(comboBoxVeiculos.getSelectionModel().getSelectedItem());
            textFieldCliente.setText(ordemServico.getVeiculo().getCliente().getNome());
            textFieldModelo.setText(ordemServico.getVeiculo().getModelo().getDescricao());
            textFieldMarca.setText(ordemServico.getVeiculo().getModelo().getMarca().getNome());
            textFieldCategoria.setText(ordemServico.getVeiculo().getModelo().getCategoria().name());
            carregarComboBoxServicos(ordemServico.getVeiculo());
            comboBoxServico.setDisable(false);
            comboBoxServico.setPromptText("Selecione o serviço ...");
            textFieldValorServico.setDisable(false);
            
            comboBoxServico.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> 
            {
                textFieldValorServico.setText(String.valueOf(newValue.getValor()));  
            });
            
            textFieldObservacoesServico.setDisable(false);
            buttonAdicionar.setDisable(false);
        }
    }

    
    @FXML
    void handleTableViewMouseClicked(MouseEvent event) {
        ItemOS itemOS
                = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
        if (itemOS == null) {
            contextMenuItemAtualizarObs.setDisable(true);
            contextMenuItemRemoverItem.setDisable(true);
        } else {
            contextMenuItemAtualizarObs.setDisable(false);
            contextMenuItemRemoverItem.setDisable(false);
        }

    }    

    @FXML
    private void handleContextMenuItemAtualizarObs() {
        ItemOS itemOS = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
        int index = tableViewItensDeVenda.getSelectionModel().getSelectedIndex(); 
        
        String observaçõesAtualizadas = inputDialog(itemOS.getObservacoes());
        if(observaçõesAtualizadas != "Cancelado")
        {
            itemOS.setObservacoes(observaçõesAtualizadas);
        }
        
        ordemServico.getItensOS().set(index, itemOS);
        tableViewItensDeVenda.refresh();
        textFieldValor.setText(String.format("%.2f", ordemServico.getTotal()));
    }
    
    private String inputDialog(String value) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Entrada de dados.");
        dialog.setHeaderText("Atualização do campo observações do serviço.");
        dialog.setContentText("Observações: ");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()) {
           return result.get();
        } else {
           return "Cancelado";
        }
    }


    @FXML
    private void handleContextMenuItemRemoverItem() throws DAOException {
       
//        ItemOS itemOs = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
//        ItemOS_DAO itemOsDAO = new ItemOS_DAO();
//        itemOsDAO.setConnection(connection);
//        itemOsDAO.remover(itemOs);
        
        int index = tableViewItensDeVenda.getSelectionModel().getSelectedIndex(); 
        ordemServico.getItensOS().remove(index);  
        
        observableListItensOS= FXCollections.observableArrayList(ordemServico.getItensOS());
        tableViewItensDeVenda.setItems(observableListItensOS);

        textFieldValor.setText(String.format("%.2f", ordemServico.getTotal()));
    }
    
    private boolean validarVeiculoSelecionado() 
    {
        String errorMessage = "";

        if (comboBoxVeiculos.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Veículo inválido! Selecione um veiculo.";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
    private boolean validarServiçoSelecionado() 
    {
        String errorMessage = "";

        if (comboBoxServico.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Serviço inválido! Selecione um serviço.";
        }      
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (comboBoxVeiculos.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Veículo inválido!\n";
        }

        if (datePickerData.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }

        if (observableListItensOS == null) {
            errorMessage += "Itens de Ordem de Serviço inválidos!\n";
        }
        
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            textFieldDesconto.setText(df.parse(textFieldDesconto.getText()).toString());
        } catch (ParseException ex) {
            errorMessage += "A taxa de desconto está incorreta! Use \",\" como ponto decimal.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
