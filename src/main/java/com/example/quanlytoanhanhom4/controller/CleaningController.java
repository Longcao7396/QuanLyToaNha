package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Cleaning;
import com.example.quanlytoanhanhom4.service.CleaningService;
import com.example.quanlytoanhanhom4.util.UserSession;
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

public class CleaningController implements Initializable {
    
    @FXML private TableView<Cleaning> cleaningTable;
    @FXML private TableColumn<Cleaning, String> colArea;
    @FXML private TableColumn<Cleaning, String> colCleaningType;
    @FXML private TableColumn<Cleaning, LocalDate> colScheduledDate;
    @FXML private TableColumn<Cleaning, String> colStatus;
    @FXML private TableColumn<Cleaning, Integer> colRating;
    
    @FXML private ComboBox<String> filterStatusCombo;
    @FXML private TextField areaField;
    @FXML private ComboBox<String> cleaningTypeCombo;
    @FXML private DatePicker scheduledDatePicker;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextArea notesArea;
    @FXML private Spinner<Integer> ratingSpinner;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button completeButton;
    @FXML private Button deleteButton;
    @FXML private Label statusLabel;

    private static final LinkedHashMap<String, String> CLEANING_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        CLEANING_TYPE_OPTIONS.put("HÀNG_NGÀY", "Hàng ngày");
        CLEANING_TYPE_OPTIONS.put("HÀNG_TUẦN", "Hàng tuần");
        CLEANING_TYPE_OPTIONS.put("TỔNG_VỆ_SINH", "Tổng vệ sinh");
        CLEANING_TYPE_OPTIONS.put("ĐẶC_BIỆT", "Đặc biệt");

        STATUS_OPTIONS.put("CHỜ_XỬ_LÝ", "Chờ xử lý");
        STATUS_OPTIONS.put("ĐANG_XỬ_LÝ", "Đang thực hiện");
        STATUS_OPTIONS.put("HOÀN_THÀNH", "Hoàn thành");
        STATUS_OPTIONS.put("ĐÃ_HỦY", "Đã hủy");
    }

    private ObservableList<Cleaning> cleanings;
    private Cleaning selectedCleaning;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        initializeSpinner();
        loadCleanings();
        
        cleaningTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCleaning = newSelection;
                loadCleaningToForm(newSelection);
                updateButton.setDisable(false);
                completeButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }
    
    private void initializeTable() {
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colCleaningType.setCellValueFactory(new PropertyValueFactory<>("cleaningType"));
        colScheduledDate.setCellValueFactory(new PropertyValueFactory<>("scheduledDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("qualityRating"));
        
        cleanings = FXCollections.observableArrayList();
        cleaningTable.setItems(cleanings);
    }
    
    private void initializeComboBoxes() {
        ObservableList<String> cleaningTypes = FXCollections.observableArrayList(CLEANING_TYPE_OPTIONS.values());
        cleaningTypeCombo.setItems(cleaningTypes);

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "CHỜ_XỬ_LÝ"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);
    }
    
    private void initializeSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        ratingSpinner.setValueFactory(valueFactory);
    }
    
    private void loadCleanings() {
        try {
            cleanings.clear();
            String filterStatus = filterStatusCombo.getValue();

            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                cleanings.addAll(CleaningService.getAllCleanings());
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                cleanings.addAll(CleaningService.getCleaningsByStatus(statusValue));
            }

            // Refresh table to ensure data is displayed
            cleaningTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu vệ sinh: " + e.getMessage());
        }
    }
    
    private void loadCleaningToForm(Cleaning cleaning) {
        areaField.setText(cleaning.getArea());
        cleaningTypeCombo.setValue(toDisplay(CLEANING_TYPE_OPTIONS, cleaning.getCleaningType()));
        scheduledDatePicker.setValue(cleaning.getScheduledDate());
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, cleaning.getStatus()));
        notesArea.setText(cleaning.getNotes());
        if (cleaning.getQualityRating() != null) {
            ratingSpinner.getValueFactory().setValue(cleaning.getQualityRating());
        }
    }
    
    @FXML
    private void handleFilter() {
        loadCleanings();
    }
    
    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Cleaning cleaning = new Cleaning();
            cleaning.setArea(areaField.getText().trim());
            cleaning.setCleaningType(toValue(CLEANING_TYPE_OPTIONS, cleaningTypeCombo.getValue()));
            cleaning.setScheduledDate(scheduledDatePicker.getValue());
            cleaning.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            cleaning.setNotes(notesArea.getText().trim());
            cleaning.setCreatedBy(UserSession.getCurrentUserId() != null ? UserSession.getCurrentUserId() : 1);
            
            if (CleaningService.addCleaning(cleaning)) {
                statusLabel.setText("✅ Thêm công việc vệ sinh thành công!");
                clearForm();
                loadCleanings();
            } else {
                showAlert("Lỗi", "Không thể thêm công việc vệ sinh!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleUpdate() {
        if (selectedCleaning == null) return;
        
        if (validateInput()) {
            selectedCleaning.setArea(areaField.getText().trim());
            selectedCleaning.setCleaningType(toValue(CLEANING_TYPE_OPTIONS, cleaningTypeCombo.getValue()));
            selectedCleaning.setScheduledDate(scheduledDatePicker.getValue());
            selectedCleaning.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedCleaning.setNotes(notesArea.getText().trim());
            
            if (CleaningService.updateCleaning(selectedCleaning)) {
                statusLabel.setText("✅ Cập nhật công việc vệ sinh thành công!");
                clearForm();
                loadCleanings();
                resetSelection();
            } else {
                showAlert("Lỗi", "Không thể cập nhật công việc vệ sinh!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleComplete() {
        if (selectedCleaning == null) return;
        
        if (CleaningService.completeCleaning(selectedCleaning.getId(), LocalDate.now(), 
                ratingSpinner.getValue(), notesArea.getText().trim())) {
            statusLabel.setText("✅ Hoàn thành công việc vệ sinh!");
            clearForm();
            loadCleanings();
            resetSelection();
        }
    }
    
    @FXML
    private void handleDelete() {
        if (selectedCleaning == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText("Xóa công việc vệ sinh");
        confirm.setContentText("Bạn có chắc chắn muốn xóa công việc vệ sinh này?");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (CleaningService.deleteCleaning(selectedCleaning.getId())) {
                statusLabel.setText("✅ Xóa công việc vệ sinh thành công!");
                clearForm();
                loadCleanings();
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
        Stage stage = (Stage) cleaningTable.getScene().getWindow();
        stage.close();
    }
    
    private void clearForm() {
        areaField.clear();
        cleaningTypeCombo.setValue(null);
        scheduledDatePicker.setValue(null);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "CHỜ_XỬ_LÝ"));
        notesArea.clear();
        ratingSpinner.getValueFactory().setValue(3);
        statusLabel.setText("");
    }
    
    private void resetSelection() {
        selectedCleaning = null;
        cleaningTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        completeButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setDisable(false);
    }
    
    private boolean validateInput() {
        if (areaField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập khu vực!", Alert.AlertType.ERROR);
            return false;
        }
        if (cleaningTypeCombo.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn loại vệ sinh!", Alert.AlertType.ERROR);
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






