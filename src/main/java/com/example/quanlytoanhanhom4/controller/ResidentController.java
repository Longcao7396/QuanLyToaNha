package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Resident;
import com.example.quanlytoanhanhom4.service.ResidentService;
import com.example.quanlytoanhanhom4.util.AlertUtils;
import com.example.quanlytoanhanhom4.util.LoadingIndicator;
import com.example.quanlytoanhanhom4.util.UserSession;
import com.example.quanlytoanhanhom4.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ResidentController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(ResidentController.class);

    @FXML
    private TableView<Resident> residentTable;
    @FXML
    private TableColumn<Resident, String> colFullName;
    @FXML
    private TableColumn<Resident, String> colPhone;
    @FXML
    private TableColumn<Resident, String> colEmail;
    @FXML
    private TableColumn<Resident, String> colStatus;

    @FXML
    private ComboBox<String> filterStatusCombo;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField identityCardField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private ComboBox<String> genderCombo;
    @FXML
    private TextArea addressArea;
    @FXML
    private TextField emergencyContactField;
    @FXML
    private TextField emergencyPhoneField;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private TextArea notesArea;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label statusLabel;

    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        STATUS_OPTIONS.put("ACTIVE", "Đang hoạt động");
        STATUS_OPTIONS.put("INACTIVE", "Ngừng hoạt động");
        STATUS_OPTIONS.put("MOVED_OUT", "Đã chuyển đi");
    }

    private ObservableList<Resident> residents;
    private Resident selectedResident;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();

        // Delay nhỏ để đảm bảo UI đã sẵn sàng trước khi load dữ liệu
        javafx.application.Platform.runLater(() -> {
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(100));
            pause.setOnFinished(e -> {
                loadResidentsWithoutLoading();
            });
            pause.play();
        });

        residentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedResident = newSelection;
                loadResidentToForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }

    private void initializeTable() {
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        residents = FXCollections.observableArrayList();
        residentTable.setItems(residents);
    }

    private void initializeComboBoxes() {
        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "ACTIVE"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);

        genderCombo.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
    }

    private void loadResidentsWithoutLoading() {
        try {
            logger.info("Bắt đầu load dữ liệu cư dân...");

            // Đảm bảo residents list đã được khởi tạo
            if (residents == null) {
                residents = FXCollections.observableArrayList();
            }
            if (residentTable != null && residentTable.getItems() != residents) {
                residentTable.setItems(residents);
            }

            // Đảm bảo filterStatusCombo đã được khởi tạo
            String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
                    ? filterStatusCombo.getValue() : ALL_LABEL;
            logger.debug("Filter status: {}", filterStatus);

            // Load dữ liệu
            List<Resident> residentList;
            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                logger.debug("Loading tất cả cư dân...");
                residentList = ResidentService.getAllResidents();
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                logger.debug("Loading cư dân với status: {}", statusValue);
                residentList = ResidentService.getResidentsByStatus(statusValue);
            }

            logger.info("Đã lấy được {} cư dân từ service", residentList != null ? residentList.size() : 0);

            // Cập nhật UI trên JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                try {
                    residents.clear();
                    if (residentList != null && !residentList.isEmpty()) {
                        residents.addAll(residentList);
                        logger.info("Đã load {} cư dân vào bảng", residentList.size());
                    } else {
                        logger.warn("Không có dữ liệu cư dân nào được trả về từ service!");
                    }

                    // Refresh table để đảm bảo hiển thị
                    if (residentTable != null) {
                        residentTable.refresh();
                        // Force update columns
                        residentTable.getColumns().forEach(col -> col.setVisible(false));
                        residentTable.getColumns().forEach(col -> col.setVisible(true));
                        logger.debug("Đã refresh bảng cư dân");
                    }

                    logger.info("Số lượng cư dân trong ObservableList: {}", residents.size());
                } catch (Exception e) {
                    logger.error("Lỗi khi cập nhật UI", e);
                }
            });

        } catch (Exception e) {
            logger.error("Lỗi khi tải danh sách cư dân", e);
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                AlertUtils.showError("Lỗi", "Không thể tải danh sách cư dân: " + e.getMessage());
            });
        }
    }

    private void loadResidents() {
        Stage stage = (Stage) residentTable.getScene().getWindow();
        if (stage == null) {
            loadResidentsWithoutLoading();
            return;
        }
        try {
            LoadingIndicator.executeWithLoading(stage, () -> {
                residents.clear();
                String filterStatus = filterStatusCombo.getValue();

                if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                    residents.addAll(ResidentService.getAllResidents());
                } else {
                    String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                    residents.addAll(ResidentService.getResidentsByStatus(statusValue));
                }
                return null;
            });
        } catch (Exception e) {
            logger.error("Lỗi khi tải danh sách cư dân", e);
            AlertUtils.showError("Lỗi", "Không thể tải danh sách cư dân");
        }
    }

    private void loadResidentToForm(Resident resident) {
        fullNameField.setText(resident.getFullName());
        phoneField.setText(resident.getPhone());
        emailField.setText(resident.getEmail());
        identityCardField.setText(resident.getIdentityCard());
        dateOfBirthPicker.setValue(resident.getDateOfBirth());
        genderCombo.setValue(resident.getGender());
        addressArea.setText(resident.getAddress());
        emergencyContactField.setText(resident.getEmergencyContact());
        emergencyPhoneField.setText(resident.getEmergencyPhone());
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, resident.getStatus()));
        notesArea.setText(resident.getNotes());
    }

    @FXML
    private void handleFilter() {
        loadResidents();
    }

    @FXML
    private void handleAdd() {
        ValidationUtils.ValidationResult validation = validateInput();
        if (!validation.isValid()) {
            AlertUtils.showError("Lỗi validation", validation.getErrorMessage());
            return;
        }

        Stage stage = (Stage) residentTable.getScene().getWindow();
        try {
            LoadingIndicator.executeWithLoading(stage, () -> {
                Resident resident = new Resident();
                Integer userId = UserSession.getCurrentUserId();
                if (userId == null) {
                    throw new IllegalStateException("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.");
                }
                resident.setUserId(userId);
                resident.setFullName(fullNameField.getText().trim());
                resident.setPhone(phoneField.getText().trim());
                resident.setEmail(emailField.getText().trim());
                resident.setIdentityCard(identityCardField.getText().trim());
                resident.setDateOfBirth(dateOfBirthPicker.getValue());
                resident.setGender(genderCombo.getValue());
                resident.setAddress(addressArea.getText().trim());
                resident.setEmergencyContact(emergencyContactField.getText().trim());
                resident.setEmergencyPhone(emergencyPhoneField.getText().trim());
                resident.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
                resident.setNotes(notesArea.getText().trim());

                if (ResidentService.addResident(resident)) {
                    AlertUtils.showSuccess("Đã thêm cư dân thành công!");
                    clearForm();
                    loadResidents();
                } else {
                    AlertUtils.showError("Lỗi", "Không thể thêm cư dân. Vui lòng thử lại.");
                }
                return null;
            });
        } catch (Exception e) {
            logger.error("Lỗi khi thêm cư dân", e);
            AlertUtils.showError("Lỗi", "Đã xảy ra lỗi khi thêm cư dân");
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedResident == null) {
            AlertUtils.showWarning("Vui lòng chọn cư dân cần cập nhật");
            return;
        }

        ValidationUtils.ValidationResult validation = validateInput();
        if (!validation.isValid()) {
            AlertUtils.showError("Lỗi validation", validation.getErrorMessage());
            return;
        }

        Stage stage = (Stage) residentTable.getScene().getWindow();
        try {
            LoadingIndicator.executeWithLoading(stage, () -> {
                selectedResident.setFullName(fullNameField.getText().trim());
                selectedResident.setPhone(phoneField.getText().trim());
                selectedResident.setEmail(emailField.getText().trim());
                selectedResident.setIdentityCard(identityCardField.getText().trim());
                selectedResident.setDateOfBirth(dateOfBirthPicker.getValue());
                selectedResident.setGender(genderCombo.getValue());
                selectedResident.setAddress(addressArea.getText().trim());
                selectedResident.setEmergencyContact(emergencyContactField.getText().trim());
                selectedResident.setEmergencyPhone(emergencyPhoneField.getText().trim());
                selectedResident.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
                selectedResident.setNotes(notesArea.getText().trim());

                if (ResidentService.updateResident(selectedResident)) {
                    AlertUtils.showSuccess("Đã cập nhật cư dân thành công!");
                    clearForm();
                    loadResidents();
                } else {
                    AlertUtils.showError("Lỗi", "Không thể cập nhật cư dân. Vui lòng thử lại.");
                }
                return null;
            });
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật cư dân", e);
            AlertUtils.showError("Lỗi", "Đã xảy ra lỗi khi cập nhật cư dân");
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedResident == null) {
            AlertUtils.showWarning("Vui lòng chọn cư dân cần xóa");
            return;
        }

        String message = String.format("Bạn có chắc chắn muốn xóa cư dân '%s'?\nHành động này không thể hoàn tác.",
                selectedResident.getFullName());

        if (AlertUtils.showConfirmation("Xác nhận xóa", message)) {
            Stage stage = (Stage) residentTable.getScene().getWindow();
            try {
                LoadingIndicator.executeWithLoading(stage, () -> {
                    if (ResidentService.deleteResident(selectedResident.getId())) {
                        AlertUtils.showSuccess("Đã xóa cư dân thành công!");
                        clearForm();
                        loadResidents();
                    } else {
                        AlertUtils.showError("Lỗi", "Không thể xóa cư dân. Vui lòng thử lại.");
                    }
                    return null;
                });
            } catch (Exception e) {
                logger.error("Lỗi khi xóa cư dân", e);
                AlertUtils.showError("Lỗi", "Đã xảy ra lỗi khi xóa cư dân");
            }
        }
    }

    @FXML
    private void handleBack() {
        ((Stage) residentTable.getScene().getWindow()).close();
    }

    private void clearForm() {
        fullNameField.clear();
        phoneField.clear();
        emailField.clear();
        identityCardField.clear();
        dateOfBirthPicker.setValue(null);
        genderCombo.setValue(null);
        addressArea.clear();
        emergencyContactField.clear();
        emergencyPhoneField.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "ACTIVE"));
        notesArea.clear();
        selectedResident = null;
        residentTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private ValidationUtils.ValidationResult validateInput() {
        ValidationUtils.ValidationResult result = new ValidationUtils.ValidationResult();

        // Validate required fields
        result.merge(ValidationUtils.validateRequired(fullNameField.getText(), "Họ tên"));
        result.merge(ValidationUtils.validateRequired(phoneField.getText(), "Số điện thoại"));

        // Validate formats
        if (!phoneField.getText().trim().isEmpty()) {
            result.merge(ValidationUtils.validatePhone(phoneField.getText(), "Số điện thoại"));
        }

        if (!emailField.getText().trim().isEmpty()) {
            result.merge(ValidationUtils.validateEmail(emailField.getText(), "Email"));
        }

        if (!identityCardField.getText().trim().isEmpty()) {
            result.merge(ValidationUtils.validateIdentityCard(identityCardField.getText(), "CMND/CCCD"));
        }

        if (!emergencyPhoneField.getText().trim().isEmpty()) {
            result.merge(ValidationUtils.validatePhone(emergencyPhoneField.getText(), "Số điện thoại liên hệ khẩn cấp"));
        }

        // Validate length
        result.merge(ValidationUtils.validateLength(fullNameField.getText(), 2, 150, "Họ tên"));

        return result;
    }

    private String toDisplay(LinkedHashMap<String, String> map, String value) {
        return map.getOrDefault(value, value);
    }

    private String toValue(LinkedHashMap<String, String> map, String display) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(display))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(display);
    }
}

