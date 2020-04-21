package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Apostas;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.ApostasService;

public class EditarSaldosFormController implements Initializable {

	private Apostas entity;

	private ApostasService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtsaldoADever;
	
	@FXML
	private TextField txtSaldoAReceber;
	
	@FXML
	private TextField txtSaldo;
	
	@FXML
	private ComboBox<Department> comboBoxApostadores;

	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorSaldoADever;
	
	@FXML
	private Label labelErrorSaldoAReceber;
	

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;


	

	public void setApostas(Apostas entity) {
		this.entity = entity;
	}

	public void setServices(ApostasService service) {
		this.service = service;
		
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Apostas getFormData() {
		 
		
		Apostas obj = new Apostas();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtsaldoADever.getText() == null || txtsaldoADever.getText().trim().equals("")) {
			exception.addError("saldoADever", "Field can't be empty");
		}
		obj.setSaldoADever(Utils.tryParseToDouble(txtsaldoADever.getText()));
		
		if (txtSaldoAReceber.getText() == null || txtSaldoAReceber.getText().trim().equals("")) {
			exception.addError("saldoAReceber", "Field can't be empty");
		}
		obj.setSaldoAReceber(Utils.tryParseToDouble(txtSaldoAReceber.getText()));
		
		double textoSaldo = (Utils.tryParseToDouble(txtSaldoAReceber.getText())) - (Utils.tryParseToDouble(txtsaldoADever.getText()));
		
		obj.setSaldo(textoSaldo);

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtsaldoADever, 100);
		Constraints.setTextFieldMaxLength(txtSaldoAReceber, 100);

	}

	public void updateFormData() {
		
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		
		txtsaldoADever.setText(String.format("%.2f", entity.getSaldoADever()));
		txtSaldoAReceber.setText(String.format("%.2f", entity.getSaldoAReceber()));
		double textoSaldo = (Utils.tryParseToDouble(txtSaldoAReceber.getText())) - (Utils.tryParseToDouble(txtsaldoADever.getText()));
		txtSaldo.setText(String.format("%.2f", textoSaldo));

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorSaldoADever.setText((fields.contains("saldoADever") ? errors.get("saldoADever") : ""));
		labelErrorSaldoAReceber.setText((fields.contains("saldoAReceber") ? errors.get("saldoAReceber") : ""));
		

	}

}