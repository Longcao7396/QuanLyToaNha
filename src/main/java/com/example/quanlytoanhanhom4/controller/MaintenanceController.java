package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Maintenance;
import com.example.quanlytoanhanhom4.service.MaintenanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MaintenanceController implements Initializable {
    
    @FXML private TableView<Maintenance> maintenanceTable;
    @FXML private TableColumn<Maintenance, String> colSystemType;
    @FXML private TableColumn<Maintenance, String> colMaintenanceType;
    @FXML private TableColumn<Maintenance, String> colDescription;
    @FXML private TableColumn<Maintenance, LocalDate> colScheduledDate;
    @FXML private TableColumn<Maintenance, String> colStatus;
    @FXML private TableColumn<Maintenance, String> colPriority;
    
    @FXML private ComboBox<String> filterStatusCombo;
    @FXML private ComboBox<String> systemTypeCombo;
    @FXML private ComboBox<String> maintenanceTypeCombo;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker scheduledDatePicker;
    @FXML private ComboBox<String> statusCombo;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private TextArea notesArea;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button completeButton;
    @FXML private Button deleteButton;
    @FXML private Label statusLabel;

    private static final LinkedHashMap<String, String> SYSTEM_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> MAINTENANCE_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> PRIORITY_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        SYSTEM_TYPE_OPTIONS.put("ĐIỆN", "Điện");
        SYSTEM_TYPE_OPTIONS.put("NƯỚC", "Nước");
        SYSTEM_TYPE_OPTIONS.put("HVAC", "HVAC (Điều hòa)");
        SYSTEM_TYPE_OPTIONS.put("PCCC", "Phòng cháy chữa cháy");
        SYSTEM_TYPE_OPTIONS.put("AN_NINH", "An ninh");
        SYSTEM_TYPE_OPTIONS.put("CHIEU_SANG", "Chiếu sáng");

        MAINTENANCE_TYPE_OPTIONS.put("PREVENTIVE", "Bảo trì định kỳ");
        MAINTENANCE_TYPE_OPTIONS.put("CORRECTIVE", "Bảo trì sửa chữa");
        MAINTENANCE_TYPE_OPTIONS.put("EMERGENCY", "Bảo trì khẩn cấp");

        STATUS_OPTIONS.put("PENDING", "Chờ xử lý");
        STATUS_OPTIONS.put("IN_PROGRESS", "Đang thực hiện");
        STATUS_OPTIONS.put("COMPLETED", "Hoàn thành");
        STATUS_OPTIONS.put("CANCELLED", "Đã hủy");

        PRIORITY_OPTIONS.put("LOW", "Thấp");
        PRIORITY_OPTIONS.put("MEDIUM", "Trung bình");
        PRIORITY_OPTIONS.put("HIGH", "Cao");
        PRIORITY_OPTIONS.put("URGENT", "Khẩn cấp");
    }

    private ObservableList<Maintenance> maintenances;
    private Maintenance selectedMaintenance;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        loadMaintenances();
        
        maintenanceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMaintenance = newSelection;
                loadMaintenanceToForm(newSelection);
                updateButton.setDisable(false);
                completeButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }
    
    private void initializeTable() {
        colSystemType.setCellValueFactory(new PropertyValueFactory<>("systemType"));
        colMaintenanceType.setCellValueFactory(new PropertyValueFactory<>("maintenanceType"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colScheduledDate.setCellValueFactory(new PropertyValueFactory<>("scheduledDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        
        maintenances = FXCollections.observableArrayList();
        maintenanceTable.setItems(maintenances);
    }
    
    private void initializeComboBoxes() {
        ObservableList<String> systemTypes = FXCollections.observableArrayList(SYSTEM_TYPE_OPTIONS.values());
        systemTypeCombo.setItems(systemTypes);

        ObservableList<String> maintenanceTypes = FXCollections.observableArrayList(MAINTENANCE_TYPE_OPTIONS.values());
        maintenanceTypeCombo.setItems(maintenanceTypes);

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "PENDING"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);

        ObservableList<String> priorities = FXCollections.observableArrayList(PRIORITY_OPTIONS.values());
        priorityCombo.setItems(priorities);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));
    }
    
    private void loadMaintenances() {
        try {
            maintenances.clear();
            String filterStatus = filterStatusCombo.getValue();

            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                maintenances.addAll(MaintenanceService.getAllMaintenances());
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                maintenances.addAll(MaintenanceService.getMaintenancesByStatus(statusValue));
            }

            // Refresh table to ensure data is displayed
            maintenanceTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu bảo trì: " + e.getMessage());
        }
    }
    
    private void loadMaintenanceToForm(Maintenance maintenance) {
        systemTypeCombo.setValue(toDisplay(SYSTEM_TYPE_OPTIONS, maintenance.getSystemType()));
        maintenanceTypeCombo.setValue(toDisplay(MAINTENANCE_TYPE_OPTIONS, maintenance.getMaintenanceType()));
        descriptionArea.setText(maintenance.getDescription());
        scheduledDatePicker.setValue(maintenance.getScheduledDate());
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, maintenance.getStatus()));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, maintenance.getPriority()));
        notesArea.setText(maintenance.getNotes());
    }
    
    @FXML
    private void handleFilter() {
        loadMaintenances();
    }
    
    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Maintenance maintenance = new Maintenance();
            maintenance.setSystemType(toValue(SYSTEM_TYPE_OPTIONS, systemTypeCombo.getValue()));
            maintenance.setMaintenanceType(toValue(MAINTENANCE_TYPE_OPTIONS, maintenanceTypeCombo.getValue()));
            maintenance.setDescription(descriptionArea.getText().trim());
            maintenance.setScheduledDate(scheduledDatePicker.getValue());
            maintenance.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            maintenance.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            maintenance.setNotes(notesArea.getText().trim());
            
            if (MaintenanceService.addMaintenance(maintenance)) {
                statusLabel.setText("✅ Thêm bảo trì thành công!");
                clearForm();
                loadMaintenances();
            } else {
                showAlert("Lỗi", "Không thể thêm bảo trì!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleUpdate() {
        if (selectedMaintenance == null) return;
        
        if (validateInput()) {
            selectedMaintenance.setSystemType(toValue(SYSTEM_TYPE_OPTIONS, systemTypeCombo.getValue()));
            selectedMaintenance.setMaintenanceType(toValue(MAINTENANCE_TYPE_OPTIONS, maintenanceTypeCombo.getValue()));
            selectedMaintenance.setDescription(descriptionArea.getText().trim());
            selectedMaintenance.setScheduledDate(scheduledDatePicker.getValue());
            selectedMaintenance.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedMaintenance.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            selectedMaintenance.setNotes(notesArea.getText().trim());
            
            if (MaintenanceService.updateMaintenance(selectedMaintenance)) {
                statusLabel.setText("✅ Cập nhật bảo trì thành công!");
                clearForm();
                loadMaintenances();
                resetSelection();
            } else {
                showAlert("Lỗi", "Không thể cập nhật bảo trì!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleComplete() {
        if (selectedMaintenance == null) return;
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Hoàn thành bảo trì");
        dialog.setHeaderText("Nhập ghi chú hoàn thành");
        dialog.setContentText("Ghi chú:");
        
        dialog.showAndWait().ifPresent(notes -> {
            if (MaintenanceService.completeMaintenance(selectedMaintenance.getId(), LocalDate.now(), notes)) {
                statusLabel.setText("✅ Hoàn thành bảo trì!");
                clearForm();
                loadMaintenances();
                resetSelection();
            }
        });
    }
    
    @FXML
    private void handleDelete() {
        if (selectedMaintenance == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText("Xóa bảo trì");
        confirm.setContentText("Bạn có chắc chắn muốn xóa bảo trì này?");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (MaintenanceService.deleteMaintenance(selectedMaintenance.getId())) {
                statusLabel.setText("✅ Xóa bảo trì thành công!");
                clearForm();
                loadMaintenances();
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
        Stage stage = (Stage) maintenanceTable.getScene().getWindow();
        stage.close();
    }
    
    private void clearForm() {
        systemTypeCombo.setValue(null);
        maintenanceTypeCombo.setValue(null);
        descriptionArea.clear();
        scheduledDatePicker.setValue(null);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "PENDING"));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));
        notesArea.clear();
        statusLabel.setText("");
    }
    
    private void resetSelection() {
        selectedMaintenance = null;
        maintenanceTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        completeButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setDisable(false);
    }
    
    private boolean validateInput() {
        if (systemTypeCombo.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn loại hệ thống!", Alert.AlertType.ERROR);
            return false;
        }
        if (maintenanceTypeCombo.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn loại bảo trì!", Alert.AlertType.ERROR);
            return false;
        }
        if (scheduledDatePicker.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn ngày lên lịch!", Alert.AlertType.ERROR);
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






