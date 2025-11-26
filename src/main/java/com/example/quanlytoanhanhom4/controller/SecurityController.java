package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Security;
import com.example.quanlytoanhanhom4.service.SecurityService;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SecurityController implements Initializable {
    
    @FXML private TableView<Security> securityTable;
    @FXML private TableColumn<Security, String> colIncidentType;
    @FXML private TableColumn<Security, String> colLocation;
    @FXML private TableColumn<Security, String> colDescription;
    @FXML private TableColumn<Security, String> colStatus;
    @FXML private TableColumn<Security, String> colPriority;
    
    @FXML private ComboBox<String> filterStatusCombo;
    @FXML private ComboBox<String> incidentTypeCombo;
    @FXML private TextField locationField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> statusCombo;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private TextArea resolutionArea;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button resolveButton;
    @FXML private Button deleteButton;
    @FXML private Label statusLabel;

    private static final LinkedHashMap<String, String> INCIDENT_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> PRIORITY_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        INCIDENT_TYPE_OPTIONS.put("CAMERA", "Camera");
        INCIDENT_TYPE_OPTIONS.put("ACCESS_CONTROL", "Kiểm soát ra vào");
        INCIDENT_TYPE_OPTIONS.put("EMERGENCY", "Khẩn cấp");
        INCIDENT_TYPE_OPTIONS.put("THEFT", "Trộm cắp");
        INCIDENT_TYPE_OPTIONS.put("OTHER", "Khác");

        STATUS_OPTIONS.put("OPEN", "Mới ghi nhận");
        STATUS_OPTIONS.put("IN_PROGRESS", "Đang xử lý");
        STATUS_OPTIONS.put("RESOLVED", "Đã giải quyết");
        STATUS_OPTIONS.put("CLOSED", "Đã đóng");

        PRIORITY_OPTIONS.put("LOW", "Thấp");
        PRIORITY_OPTIONS.put("MEDIUM", "Trung bình");
        PRIORITY_OPTIONS.put("HIGH", "Cao");
        PRIORITY_OPTIONS.put("URGENT", "Khẩn cấp");
    }

    private ObservableList<Security> incidents;
    private Security selectedIncident;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        loadIncidents();
        
        securityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedIncident = newSelection;
                loadIncidentToForm(newSelection);
                updateButton.setDisable(false);
                resolveButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }
    
    private void initializeTable() {
        colIncidentType.setCellValueFactory(new PropertyValueFactory<>("incidentType"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        
        incidents = FXCollections.observableArrayList();
        securityTable.setItems(incidents);
    }
    
    private void initializeComboBoxes() {
        ObservableList<String> incidentTypes = FXCollections.observableArrayList(INCIDENT_TYPE_OPTIONS.values());
        incidentTypeCombo.setItems(incidentTypes);

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "OPEN"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);

        ObservableList<String> priorities = FXCollections.observableArrayList(PRIORITY_OPTIONS.values());
        priorityCombo.setItems(priorities);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));
    }
    
    private void loadIncidents() {
        try {
            incidents.clear();
            String filterStatus = filterStatusCombo.getValue();

            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                incidents.addAll(SecurityService.getAllIncidents());
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                incidents.addAll(SecurityService.getIncidentsByStatus(statusValue));
            }

            // Refresh table to ensure data is displayed
            securityTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu an ninh: " + e.getMessage());
        }
    }
    
    private void loadIncidentToForm(Security incident) {
        incidentTypeCombo.setValue(toDisplay(INCIDENT_TYPE_OPTIONS, incident.getIncidentType()));
        locationField.setText(incident.getLocation());
        descriptionArea.setText(incident.getDescription());
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, incident.getStatus()));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, incident.getPriority()));
        resolutionArea.setText(incident.getResolution());
    }
    
    @FXML
    private void handleFilter() {
        loadIncidents();
    }
    
    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Security incident = new Security();
            incident.setIncidentType(toValue(INCIDENT_TYPE_OPTIONS, incidentTypeCombo.getValue()));
            incident.setLocation(locationField.getText().trim());
            incident.setDescription(descriptionArea.getText().trim());
            incident.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            incident.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            incident.setReportedBy(UserSession.getCurrentUserId() != null ? UserSession.getCurrentUserId() : 1);
            
            if (SecurityService.addIncident(incident)) {
                statusLabel.setText("✅ Thêm sự cố thành công!");
                clearForm();
                loadIncidents();
            } else {
                showAlert("Lỗi", "Không thể thêm sự cố!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleUpdate() {
        if (selectedIncident == null) return;
        
        if (validateInput()) {
            selectedIncident.setIncidentType(toValue(INCIDENT_TYPE_OPTIONS, incidentTypeCombo.getValue()));
            selectedIncident.setLocation(locationField.getText().trim());
            selectedIncident.setDescription(descriptionArea.getText().trim());
            selectedIncident.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedIncident.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            selectedIncident.setResolution(resolutionArea.getText().trim());
            
            if (SecurityService.updateIncident(selectedIncident)) {
                statusLabel.setText("✅ Cập nhật sự cố thành công!");
                clearForm();
                loadIncidents();
                resetSelection();
            } else {
                showAlert("Lỗi", "Không thể cập nhật sự cố!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleResolve() {
        if (selectedIncident == null) return;
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Giải quyết sự cố");
        dialog.setHeaderText("Nhập giải pháp");
        dialog.setContentText("Giải pháp:");
        
        dialog.showAndWait().ifPresent(resolution -> {
            if (SecurityService.resolveIncident(selectedIncident.getId(), resolution)) {
                statusLabel.setText("✅ Đã giải quyết sự cố!");
                clearForm();
                loadIncidents();
                resetSelection();
            }
        });
    }
    
    @FXML
    private void handleDelete() {
        if (selectedIncident == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText("Xóa sự cố");
        confirm.setContentText("Bạn có chắc chắn muốn xóa sự cố này?");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (SecurityService.deleteIncident(selectedIncident.getId())) {
                statusLabel.setText("✅ Xóa sự cố thành công!");
                clearForm();
                loadIncidents();
                resetSelection();
            }
        }
    }
    
    @FXML
    private void handleClear() {
        clearForm();
        resetSelection();
    }
    
    @FXML
    private void handleBack() {
        Stage stage = (Stage) securityTable.getScene().getWindow();
        stage.close();
    }
    
    private void clearForm() {
        incidentTypeCombo.setValue(null);
        locationField.clear();
        descriptionArea.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "OPEN"));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));
        resolutionArea.clear();
        statusLabel.setText("");
    }
    
    private void resetSelection() {
        selectedIncident = null;
        securityTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        resolveButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setDisable(false);
    }
    
    private boolean validateInput() {
        if (incidentTypeCombo.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn loại sự cố!", Alert.AlertType.ERROR);
            return false;
        }
        if (locationField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập vị trí!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String toDisplay(Map<String, String> options, String value) {
        if (value == null) {
            return null;
        }
        return options.getOrDefault(value, value);
    }

    private String toValue(Map<String, String> options, String display) {
        if (display == null) {
            return null;
        }
        return options.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(display))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(display);
    }
}






