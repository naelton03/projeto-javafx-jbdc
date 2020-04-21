package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Apostador;
import model.services.ApostadorService;

public class ApostadorListController implements Initializable, DataChangeListener {

	private ApostadorService service;
	

	@FXML
	private TableView<Apostador> tableViewApostador;

	@FXML
	private TableColumn<Apostador, Integer> tableColumnId;

	@FXML
	private TableColumn<Apostador, String> tableColumnName;

	@FXML
	ComboBox<Apostador> comboBoxApostador;

	@FXML
	private TableColumn<Apostador, Apostador> tableColumnEDIT;

	@FXML
	private TableColumn<Apostador, Apostador> tableColumnREMOVE;

	@FXML
	private Button btNew;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Apostador obj = new Apostador();
		createDialogForm(obj, "/gui/ApostadorForm.fxml", parentStage);
	}

	private ObservableList<Apostador> obsList;

	public void setApostadorService(ApostadorService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewApostador.prefHeightProperty().bind(stage.heightProperty());
		initializeComboBoxApostador();
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Apostador> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewApostador.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	public void loadAssociatedObjects() {
		if (service == null) {
			throw new IllegalStateException("Apostar state is null");
		}
		List<Apostador> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxApostador.setItems(obsList);
	}

	private void createDialogForm(Apostador obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ApostadorFormController controller = loader.getController();
			controller.setApostador(obj);
			controller.setApostadorService(new ApostadorService());
			loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Apostador data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Apostador, Apostador>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Apostador obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ApostadorForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Apostador, Apostador>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Apostador obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Apostador obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void initializeComboBoxApostador() {
		Callback<ListView<Apostador>, ListCell<Apostador>> factory = lv -> new ListCell<Apostador>() {
			@Override
			protected void updateItem(Apostador item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxApostador.setCellFactory(factory);
		comboBoxApostador.setButtonCell(factory.call(null));
	}
}
