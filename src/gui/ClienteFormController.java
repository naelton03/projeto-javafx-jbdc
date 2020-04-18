package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Cliente;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.ClienteService;
import model.services.DepartmentService;

public class ClienteFormController implements Initializable {

	private Cliente entity;

	private ClienteService clienteService;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtTelephone;

	@FXML
	private DatePicker dpDataDoCadastro;

	@FXML
	private TextField txtSaldoTotal;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorTelephone;

	@FXML
	private Label labelErrorDataDoCadastro;

	@FXML
	private Label labelErrorSaldoTotal;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Department> obsList;

	public void setCliente(Cliente entity) {
		this.entity = entity;
	}

	public void setServices(ClienteService clienteService, DepartmentService departmentService) {
		this.clienteService = clienteService;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (clienteService == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			clienteService.saveOrUpdate(entity);
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

	private Cliente getFormData() {
		Cliente obj = new Cliente();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());

		if (txtTelephone.getText() == null || txtTelephone.getText().trim().equals("")) {
			exception.addError("telefone", "Field can't be empty");
		}
		obj.setTelephone(txtTelephone.getText());
		
		if (dpDataDoCadastro.getValue() == null) {
			exception.addError("dataDoCadastro", "Field can't be empty");
		}
		else {
			Instant instant = Instant.from(dpDataDoCadastro.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataDoCadastro(Date.from(instant));
		}
		
		if (txtSaldoTotal.getText() == null || txtSaldoTotal.getText().trim().equals("")) {
			exception.addError("saldoTotal", "Field can't be empty");
		}
		obj.setSaldoTotal(Utils.tryParseToDouble(txtSaldoTotal.getText()));
		
		obj.setDepartment(comboBoxDepartment.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

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
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtSaldoTotal);
		Constraints.setTextFieldMaxLength(txtTelephone, 60);
		Utils.formatDatePicker(dpDataDoCadastro, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtTelephone.setText(entity.getTelephone());
		Locale.setDefault(Locale.US);
		txtSaldoTotal.setText(String.format("%.2f", entity.getSaldoTotal()));
		if (entity.getDataDoCadastro() != null) {
			dpDataDoCadastro.setValue(LocalDate.ofInstant(entity.getDataDoCadastro().toInstant(), ZoneId.systemDefault()));
		}
		if (entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorTelephone.setText((fields.contains("telefone") ? errors.get("telefone") : ""));
		labelErrorDataDoCadastro.setText((fields.contains("dataDoCadastro") ? errors.get("dataDoCadastro") : ""));
		labelErrorSaldoTotal.setText((fields.contains("saldoTotal") ? errors.get("saldoTotal") : ""));
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}