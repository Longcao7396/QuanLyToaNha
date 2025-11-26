package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.BMSSystem;
import com.example.quanlytoanhanhom4.service.BMSService;
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

public class BMSController implements Initializable {
    
    @FXML private TableView<BMSSystem> systemTable;
    @FXML private TableColumn<BMSSystem, String> colSystemType;
    @FXML private TableColumn<BMSSystem, String> colSystemName;
    @FXML private TableColumn<BMSSystem, String> colLocation;
    @FXML private TableColumn<BMSSystem, String> colStatus;
    @FXML private TableColumn<BMSSystem, Double> colValue;
    @FXML private TableColumn<BMSSystem, String> colUnit;
    
    @FXML private ComboBox<String> filterTypeCombo;
    @FXML private TextField systemNameField;
    @FXML private TextField locationField;
    @FXML private ComboBox<String> systemTypeCombo;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextField valueField;
    @FXML private TextField unitField;
    @FXML private TextArea descriptionArea;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Label statusLabel;
    @FXML
    private Label titleLabel;

    private static final LinkedHashMap<String, String> SYSTEM_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        SYSTEM_TYPE_OPTIONS.put("ĐIỆN", "Điện");
        SYSTEM_TYPE_OPTIONS.put("NƯỚC", "Nước");
        SYSTEM_TYPE_OPTIONS.put("HVAC", "HVAC (Điều hòa)");
        SYSTEM_TYPE_OPTIONS.put("PCCC", "Phòng cháy chữa cháy");
        SYSTEM_TYPE_OPTIONS.put("AN_NINH", "An ninh");
        SYSTEM_TYPE_OPTIONS.put("CHIEU_SANG", "Chiếu sáng");
        SYSTEM_TYPE_OPTIONS.put("THANG_MÁY", "Thang máy");

        STATUS_OPTIONS.put("BÌNH_THƯỜNG", "Bình thường");
        STATUS_OPTIONS.put("CẢNH_BÁO", "Cảnh báo");
        STATUS_OPTIONS.put("LỖI", "Lỗi");
        STATUS_OPTIONS.put("ĐANG_BẢO_TRÌ", "Đang bảo trì");
    }

    private ObservableList<BMSSystem> systems;
    private BMSSystem selectedSystem;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        loadSystems();
        
        systemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSystem = newSelection;
                loadSystemToForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }
    
    private void initializeTable() {
        colSystemType.setCellValueFactory(new PropertyValueFactory<>("systemType"));
        colSystemName.setCellValueFactory(new PropertyValueFactory<>("systemName"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("currentValue"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        
        systems = FXCollections.observableArrayList();
        systemTable.setItems(systems);
    }
    
    private void initializeComboBoxes() {
        ObservableList<String> systemTypes = FXCollections.observableArrayList(SYSTEM_TYPE_OPTIONS.values());
        systemTypeCombo.setItems(systemTypes);
        ObservableList<String> filterTypes = FXCollections.observableArrayList(systemTypes);
        filterTypes.add(0, ALL_LABEL);
        filterTypeCombo.setItems(filterTypes);
        filterTypeCombo.setValue(ALL_LABEL);

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "BÌNH_THƯỜNG"));
    }
    
    private void loadSystems() {
        try {
            systems.clear();
            String filterType = filterTypeCombo.getValue();

            if (filterType == null || filterType.equals(ALL_LABEL)) {
                systems.addAll(BMSService.getAllSystems());
            } else {
                String systemTypeValue = toValue(SYSTEM_TYPE_OPTIONS, filterType);
                systems.addAll(BMSService.getSystemsByType(systemTypeValue));
            }

            // Refresh table to ensure data is displayed
            systemTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu BMS: " + e.getMessage());
        }
    }
    
    private void loadSystemToForm(BMSSystem system) {
        systemTypeCombo.setValue(toDisplay(SYSTEM_TYPE_OPTIONS, system.getSystemType()));
        systemNameField.setText(system.getSystemName());
        locationField.setText(system.getLocation());
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, system.getStatus()));
        valueField.setText(system.getCurrentValue() != null ? system.getCurrentValue().toString() : "");
        unitField.setText(system.getUnit());
        descriptionArea.setText(system.getDescription());
    }
    
    @FXML
    private void handleFilter() {
        loadSystems();
    }
    
    @FXML
    private void handleAdd() {
        if (validateInput()) {
            BMSSystem system = new BMSSystem();
            system.setSystemType(toValue(SYSTEM_TYPE_OPTIONS, systemTypeCombo.getValue()));
            system.setSystemName(systemNameField.getText().trim());
            system.setLocation(locationField.getText().trim());
            system.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            system.setUnit(unitField.getText().trim());
            system.setDescription(descriptionArea.getText().trim());
            
            try {
                system.setCurrentValue(valueField.getText().trim().isEmpty() ? null : Double.parseDouble(valueField.getText().trim()));
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Giá trị không hợp lệ!", Alert.AlertType.ERROR);
                return;
            }
            
            if (BMSService.addSystem(system)) {
                statusLabel.setText("✅ Thêm hệ thống thành công!");
                clearForm();
                loadSystems();
            } else {
                showAlert("Lỗi", "Không thể thêm hệ thống!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleUpdate() {
        if (selectedSystem == null) {
            showAlert("Cảnh báo", "Vui lòng chọn hệ thống cần cập nhật!", Alert.AlertType.WARNING);
            return;
        }
        
        if (validateInput()) {
            selectedSystem.setSystemType(toValue(SYSTEM_TYPE_OPTIONS, systemTypeCombo.getValue()));
            selectedSystem.setSystemName(systemNameField.getText().trim());
            selectedSystem.setLocation(locationField.getText().trim());
            selectedSystem.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedSystem.setUnit(unitField.getText().trim());
            selectedSystem.setDescription(descriptionArea.getText().trim());
            
            try {
                selectedSystem.setCurrentValue(valueField.getText().trim().isEmpty() ? null : Double.parseDouble(valueField.getText().trim()));
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Giá trị không hợp lệ!", Alert.AlertType.ERROR);
                return;
            }
            
            if (BMSService.updateSystem(selectedSystem)) {
                statusLabel.setText("✅ Cập nhật hệ thống thành công!");
                clearForm();
                loadSystems();
                selectedSystem = null;
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                addButton.setDisable(false);
            } else {
                showAlert("Lỗi", "Không thể cập nhật hệ thống!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleDelete() {
        if (selectedSystem == null) {
            showAlert("Cảnh báo", "Vui lòng chọn hệ thống cần xóa!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText("Xóa hệ thống");
        confirm.setContentText("Bạn có chắc chắn muốn xóa hệ thống: " + selectedSystem.getSystemName() + "?");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (BMSService.deleteSystem(selectedSystem.getId())) {
                statusLabel.setText("✅ Xóa hệ thống thành công!");
                clearForm();
                loadSystems();
                selectedSystem = null;
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                addButton.setDisable(false);
            } else {
                showAlert("Lỗi", "Không thể xóa hệ thống!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleClear() {
        clearForm();
        selectedSystem = null;
        systemTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setDisable(false);
    }
    
    @FXML
    private void handleBack() {
        Stage stage = (Stage) systemTable.getScene().getWindow();
        stage.close();
    }
    
    private void clearForm() {
        systemTypeCombo.setValue(null);
        systemNameField.clear();
        locationField.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "BÌNH_THƯỜNG"));
        valueField.clear();
        unitField.clear();
        descriptionArea.clear();
        statusLabel.setText("");
    }
    
    private boolean validateInput() {
        if (systemTypeCombo.getValue() == null || systemTypeCombo.getValue().isEmpty()) {
            showAlert("Lỗi", "Vui lòng chọn loại hệ thống!", Alert.AlertType.ERROR);
            return false;
        }
        if (systemNameField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên hệ thống!", Alert.AlertType.ERROR);
            return false;
        }
        if (statusCombo.getValue() == null || statusCombo.getValue().isEmpty()) {
            showAlert("Lỗi", "Vui lòng chọn trạng thái!", Alert.AlertType.ERROR);
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

    public void setTitle(String title) {
        if (titleLabel != null) {
            titleLabel.setText(title);
        }
    }
}






