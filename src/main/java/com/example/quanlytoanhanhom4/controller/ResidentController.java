package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Resident;
import com.example.quanlytoanhanhom4.service.AutoFillService;
import com.example.quanlytoanhanhom4.service.ResidentService;
import com.example.quanlytoanhanhom4.util.AlertUtils;
import com.example.quanlytoanhanhom4.util.AutoCompleteHelper;
import com.example.quanlytoanhanhom4.util.EmptyStateHelper;
import com.example.quanlytoanhanhom4.util.FormHelper;
import com.example.quanlytoanhanhom4.util.LoadingIndicator;
import com.example.quanlytoanhanhom4.util.PaginationHelper;
import com.example.quanlytoanhanhom4.util.SearchHelper;
import com.example.quanlytoanhanhom4.util.UserSession;
import com.example.quanlytoanhanhom4.util.ValidationUtils;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
    private TableColumn<Resident, String> colIdentityCard;
    @FXML
    private TableColumn<Resident, String> colDateOfBirth;
    @FXML
    private TableColumn<Resident, String> colGender;
    @FXML
    private TableColumn<Resident, String> colPhone;
    @FXML
    private TableColumn<Resident, String> colEmail;
    @FXML
    private TableColumn<Resident, String> colAddress;
    @FXML
    private TableColumn<Resident, String> colStatus;

    // @FXML - Đã xóa khỏi FXML (top bar đã bị xóa)
    // private ComboBox<String> filterStatusCombo;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Integer> itemsPerPageCombo;
    @FXML
    private Button advancedFilterButton;
    @FXML
    private VBox advancedFilterPane;
    @FXML
    private TextField filterEmailField;
    @FXML
    private TextField filterIdentityCardField;
    @FXML
    private ComboBox<String> filterResidentTypeCombo;
    @FXML
    private ComboBox<String> filterGenderCombo;
    @FXML
    private Pagination pagination;
    @FXML
    private Label paginationInfoLabel;
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
    @FXML
    private TabPane formTabPane;
    @FXML
    private Button clearButton;

    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        STATUS_OPTIONS.put("ĐANG_Ở", "Đang ở");
        STATUS_OPTIONS.put("ĐÃ_CHUYỂN_ĐI", "Đã chuyển đi");
    }

    private ObservableList<Resident> residents;
    private ObservableList<Resident> allResidents; // Lưu tất cả residents (chưa filter)
    private FilteredList<Resident> filteredResidents; // Danh sách đã filter
    private Resident selectedResident;
    private int itemsPerPage = 20; // Mặc định 20 items/trang

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        initializeSearch();
        initializePagination();
        initializeAdvancedFilters();
        initializeFormValidation();
        initializeKeyboardShortcuts();
        initializeAutoFill();

        // Delay nhỏ để đảm bảo UI đã sẵn sàng trước khi load dữ liệu
        javafx.application.Platform.runLater(() -> {
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(100));
            pause.setOnFinished(e -> {
                loadResidentsWithoutLoading();
            });
            pause.play();
        });

        // Table selection is now handled by handleTableRowClick() in FXML
        // Keep this listener as backup
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
    
    private void initializeFormValidation() {
        // Add real-time validation
        FormHelper.addRealtimeValidation(fullNameField, 
            value -> value.length() >= 2 && value.length() <= 150,
            "Họ tên phải có từ 2-150 ký tự");
        
        FormHelper.addRealtimeValidation(phoneField,
            value -> value.isEmpty() || ValidationUtils.isValidPhone(value),
            "Số điện thoại không hợp lệ (10-11 chữ số)");
        
        FormHelper.addRealtimeValidation(emailField,
            value -> value.isEmpty() || ValidationUtils.isValidEmail(value),
            "Email không hợp lệ");
        
        FormHelper.addRealtimeValidation(identityCardField,
            value -> value.isEmpty() || ValidationUtils.isValidIdentityCard(value),
            "CMND/CCCD không hợp lệ (9-12 chữ số)");
        
        FormHelper.addRealtimeValidation(emergencyPhoneField,
            value -> value.isEmpty() || ValidationUtils.isValidPhone(value),
            "Số điện thoại không hợp lệ (10-11 chữ số)");
        
        // Add tooltips
        FormHelper.addTooltip(fullNameField, "Nhập họ tên đầy đủ của cư dân (2-150 ký tự)");
        FormHelper.addTooltip(phoneField, "Nhập số điện thoại 10-11 chữ số (ví dụ: 0912345678)");
        FormHelper.addTooltip(emailField, "Nhập địa chỉ email hợp lệ (ví dụ: user@example.com)");
        FormHelper.addTooltip(identityCardField, "Nhập số CMND/CCCD (9-12 chữ số)");
        FormHelper.addTooltip(emergencyContactField, "Tên người liên hệ khẩn cấp");
        FormHelper.addTooltip(emergencyPhoneField, "Số điện thoại người liên hệ khẩn cấp");
    }
    
    private void initializeKeyboardShortcuts() {
        javafx.scene.Scene scene = residentTable.getScene();
        if (scene != null) {
            FormHelper.addKeyboardShortcuts(scene,
                () -> {
                    if (selectedResident != null) {
                        handleUpdate();
                    } else {
                        handleAdd();
                    }
                },
                () -> {
                    clearForm();
                }
            );
        } else {
            // Wait for scene to be available
            residentTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    FormHelper.addKeyboardShortcuts(newScene,
                        () -> {
                            if (selectedResident != null) {
                                handleUpdate();
                            } else {
                                handleAdd();
                            }
                        },
                        () -> {
                            clearForm();
                        }
                    );
                }
            });
        }
    }
    
    /**
     * Khởi tạo tự động điền form
     */
    private void initializeAutoFill() {
        // Tự động format và tìm cư dân khi nhập CMND/CCCD
        if (identityCardField != null) {
            identityCardField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null && !newValue.equals(oldValue) && selectedResident == null) {
                    // Format CMND/CCCD (chỉ giữ số)
                    String formatted = AutoFillService.autoFormatIdentityCard(newValue);
                    if (!formatted.equals(newValue)) {
                        identityCardField.setText(formatted);
                        return;
                    }
                    
                    // Tìm cư dân đã có nếu CMND/CCCD hợp lệ (9-12 số)
                    if (formatted.length() >= 9 && formatted.length() <= 12) {
                        javafx.concurrent.Task<Resident> searchTask = new javafx.concurrent.Task<Resident>() {
                            @Override
                            protected Resident call() throws Exception {
                                return AutoFillService.autoFillResidentByIdentityCard(formatted);
                            }
                        };
                        
                        searchTask.setOnSucceeded(e -> {
                            Resident foundResident = searchTask.getValue();
                            if (foundResident != null && selectedResident == null) {
                                // Hỏi người dùng có muốn điền thông tin không
                                boolean confirm = AlertUtils.showConfirmation("Tìm thấy cư dân",
                                    String.format("Tìm thấy cư dân: %s\nBạn có muốn tự động điền thông tin?", 
                                        foundResident.getFullName()));
                                if (confirm) {
                                    loadResidentToForm(foundResident);
                                }
                            }
                        });
                        
                        new Thread(searchTask).start();
                    }
                }
            });
        }
        
        // Tự động format số điện thoại
        if (phoneField != null) {
            phoneField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null && !newValue.equals(oldValue) && selectedResident == null) {
                    String formatted = AutoFillService.autoFormatPhone(newValue);
                    if (!formatted.equals(newValue) && formatted.length() > 0) {
                        // Chỉ format khi người dùng nhập xong (không format trong lúc đang gõ)
                        javafx.application.Platform.runLater(() -> {
                            if (phoneField.getText().equals(newValue)) {
                                phoneField.setText(formatted);
                            }
                        });
                    }
                }
            });
        }
        
        // Tự động format số điện thoại khẩn cấp
        if (emergencyPhoneField != null) {
            emergencyPhoneField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null && !newValue.equals(oldValue)) {
                    String formatted = AutoFillService.autoFormatPhone(newValue);
                    if (!formatted.equals(newValue) && formatted.length() > 0) {
                        javafx.application.Platform.runLater(() -> {
                            if (emergencyPhoneField.getText().equals(newValue)) {
                                emergencyPhoneField.setText(formatted);
                            }
                        });
                    }
                }
            });
        }
        
        // Mặc định trạng thái "Đang ở" khi thêm mới
        if (statusCombo != null) {
            statusCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                if (selectedResident == null && newValue == null) {
                    statusCombo.setValue("Đang ở");
                }
            });
        }
    }

    private void initializeTable() {
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colIdentityCard.setCellValueFactory(new PropertyValueFactory<>("identityCard"));
        
        // Format ngày sinh
        colDateOfBirth.setCellValueFactory(cell -> {
            java.time.LocalDate date = cell.getValue().getDateOfBirth();
            if (date != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        // Format giới tính
        colGender.setCellValueFactory(cell -> {
            String gender = cell.getValue().getGender();
            if (gender != null) {
                String display = gender;
                if ("MALE".equalsIgnoreCase(gender) || "NAM".equalsIgnoreCase(gender)) {
                    display = "Nam";
                } else if ("FEMALE".equalsIgnoreCase(gender) || "NỮ".equalsIgnoreCase(gender)) {
                    display = "Nữ";
                } else if ("OTHER".equalsIgnoreCase(gender) || "KHÁC".equalsIgnoreCase(gender)) {
                    display = "Khác";
                }
                return new javafx.beans.property.SimpleStringProperty(display);
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        residents = FXCollections.observableArrayList();
        residentTable.setItems(residents);
        
        // Set empty state với nút thêm mới
        EmptyStateHelper.setResidentEmptyState(residentTable, this::handleAdd);
    }

    private void initializeComboBoxes() {
        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "ĐANG_Ở"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        // Đã xóa filterStatusCombo khỏi top bar
        // filterStatusCombo.setItems(filterStatuses);
        // filterStatusCombo.setValue(ALL_LABEL);

        genderCombo.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        
        // Items per page combo - now handled by handleItemsPerPageChange() in FXML
        itemsPerPageCombo.setItems(FXCollections.observableArrayList(20, 30, 50, 100));
        itemsPerPageCombo.setValue(20);
        
        // Filter resident type combo
        filterResidentTypeCombo.setItems(FXCollections.observableArrayList("Tất cả", "CHỦ_HỘ", "NGƯỜI_THUÊ", "NGƯỜI_THÂN", "TRẺ_EM", "NGƯỜI_GIÚP_VIỆC"));
        filterResidentTypeCombo.setValue("Tất cả");
        filterResidentTypeCombo.setOnAction(e -> applyAdvancedFilters());
        
        // Filter gender combo
        filterGenderCombo.setItems(FXCollections.observableArrayList("Tất cả", "Nam", "Nữ", "Khác"));
        filterGenderCombo.setValue("Tất cả");
        filterGenderCombo.setOnAction(e -> applyAdvancedFilters());
    }
    
    private void initializeSearch() {
        // Search is now handled by handleSearch() in FXML
        // Keep this as backup for programmatic changes
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                applyFilters();
            });
        }
    }
    
    private void initializePagination() {
        if (pagination != null) {
            pagination.setPageCount(1);
            pagination.setMaxPageIndicatorCount(10);
        }
    }
    
    private void initializeAdvancedFilters() {
        // Advanced filters are now handled by handleAdvancedFilterChange() in FXML
        // Keep these listeners as backup
        if (filterEmailField != null) {
            filterEmailField.textProperty().addListener((observable, oldValue, newValue) -> {
                applyAdvancedFilters();
            });
        }
        
        if (filterIdentityCardField != null) {
            filterIdentityCardField.textProperty().addListener((observable, oldValue, newValue) -> {
                applyAdvancedFilters();
            });
        }
    }
    
    @FXML
    private void handleToggleAdvancedFilter() {
        if (advancedFilterPane != null) {
            boolean isVisible = advancedFilterPane.isVisible();
            advancedFilterPane.setVisible(!isVisible);
            advancedFilterPane.setManaged(!isVisible);
            advancedFilterButton.setText(isVisible ? "🔽 Bộ lọc nâng cao" : "🔼 Thu gọn bộ lọc");
        }
    }
    
    @FXML
    private void handleClearFilters() {
        if (searchField != null) searchField.clear();
        if (filterEmailField != null) filterEmailField.clear();
        if (filterIdentityCardField != null) filterIdentityCardField.clear();
        if (filterResidentTypeCombo != null) filterResidentTypeCombo.setValue("Tất cả");
        if (filterGenderCombo != null) filterGenderCombo.setValue("Tất cả");
        // Đã xóa filterStatusCombo khỏi top bar
        // if (filterStatusCombo != null) filterStatusCombo.setValue(ALL_LABEL);
        applyFilters();
    }
    
    private void applyFilters() {
        if (allResidents == null) {
            return;
        }
        
        // Tạo filtered list mới với tất cả filters
        filteredResidents = new FilteredList<>(allResidents, p -> true);
        
        // Áp dụng tất cả filters cùng lúc
        filteredResidents.setPredicate(resident -> {
            // Tìm kiếm theo tên và số điện thoại
            String searchText = searchField != null ? searchField.getText() : "";
            if (searchText != null && !searchText.trim().isEmpty()) {
                String lowerSearchText = searchText.toLowerCase().trim();
                String name = resident.getFullName() != null ? resident.getFullName().toLowerCase() : "";
                String phone = resident.getPhone() != null ? resident.getPhone() : "";
                if (!name.contains(lowerSearchText) && !phone.contains(lowerSearchText)) {
                    return false;
                }
            }
            
            // Filter theo status - Đã xóa filterStatusCombo khỏi top bar
            // String filterStatus = filterStatusCombo != null ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status
            if (filterStatus != null && !filterStatus.equals(ALL_LABEL)) {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                if (!statusValue.equals(resident.getStatus())) {
                    return false;
                }
            }
            
            // Filter theo email
            String filterEmail = filterEmailField != null ? filterEmailField.getText() : "";
            if (filterEmail != null && !filterEmail.trim().isEmpty()) {
                String email = resident.getEmail() != null ? resident.getEmail().toLowerCase() : "";
                if (!email.contains(filterEmail.toLowerCase().trim())) {
                    return false;
                }
            }
            
            // Filter theo CMND/CCCD
            String filterIdentityCard = filterIdentityCardField != null ? filterIdentityCardField.getText() : "";
            if (filterIdentityCard != null && !filterIdentityCard.trim().isEmpty()) {
                String identityCard = resident.getIdentityCard() != null ? resident.getIdentityCard() : "";
                if (!identityCard.contains(filterIdentityCard)) {
                    return false;
                }
            }
            
            // Filter theo loại cư dân
            String filterResidentType = filterResidentTypeCombo != null ? filterResidentTypeCombo.getValue() : "Tất cả";
            if (filterResidentType != null && !filterResidentType.equals("Tất cả")) {
                if (!filterResidentType.equals(resident.getResidentType())) {
                    return false;
                }
            }
            
            // Filter theo giới tính
            String filterGender = filterGenderCombo != null ? filterGenderCombo.getValue() : "Tất cả";
            if (filterGender != null && !filterGender.equals("Tất cả")) {
                if (!filterGender.equals(resident.getGender())) {
                    return false;
                }
            }
            
            return true;
        });
        
        // Cập nhật pagination
        updatePagination();
    }
    
    private void applyAdvancedFilters() {
        applyFilters();
    }
    
    private void updatePagination() {
        if (filteredResidents == null || pagination == null) {
            return;
        }
        
        ObservableList<Resident> itemsToPaginate = FXCollections.observableArrayList(filteredResidents);
        
        PaginationHelper.updatePagination(pagination, residentTable, itemsToPaginate, itemsPerPage);
        
        // Cập nhật thông tin pagination
        if (paginationInfoLabel != null) {
            int totalItems = itemsToPaginate.size();
            int currentPage = pagination.getCurrentPageIndex();
            int fromIndex = currentPage * itemsPerPage + 1;
            int toIndex = Math.min((currentPage + 1) * itemsPerPage, totalItems);
            
            if (totalItems == 0) {
                paginationInfoLabel.setText("Không có dữ liệu");
            } else {
                paginationInfoLabel.setText(String.format("Hiển thị %d-%d / %d bản ghi", fromIndex, toIndex, totalItems));
            }
        }
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

            // Đảm bảo filterStatusCombo đã được khởi tạo - Đã xóa khỏi top bar
            // String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
            //         ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status
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
                    // Lưu tất cả residents vào allResidents
                    if (allResidents == null) {
                        allResidents = FXCollections.observableArrayList();
                    }
                    allResidents.clear();
                    if (residentList != null && !residentList.isEmpty()) {
                        allResidents.addAll(residentList);
                        logger.info("Đã load {} cư dân vào bảng", residentList.size());
                    } else {
                        logger.warn("Không có dữ liệu cư dân nào được trả về từ service!");
                    }

                    // Áp dụng filters và pagination
                    applyFilters();

                    logger.info("Số lượng cư dân trong ObservableList: {}", allResidents.size());
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
                // Đã xóa filterStatusCombo khỏi top bar
                // String filterStatus = filterStatusCombo.getValue();
                String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status

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
    private void handleFilterStatusChange() {
        applyFilters();
    }
    
    @FXML
    private void handleSearch() {
        applyFilters();
    }
    
    @FXML
    private void handleItemsPerPageChange() {
        if (itemsPerPageCombo != null && itemsPerPageCombo.getValue() != null) {
            itemsPerPage = itemsPerPageCombo.getValue();
            updatePagination();
        }
    }
    
    @FXML
    private void handleAdvancedFilterChange() {
        applyAdvancedFilters();
    }
    
    @FXML
    private void handleTableRowClick() {
        Resident selected = residentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedResident = selected;
            loadResidentToForm(selected);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            addButton.setDisable(true);
        }
    }
    
    @FXML
    private void handlePageChange() {
        if (pagination != null && filteredResidents != null) {
            updatePagination();
        }
    }
    
    @FXML
    private void handleFieldValidation() {
        // Real-time validation is already handled in initializeFormValidation()
    }
    
    @FXML
    private void handleClear() {
        clearForm();
    }
    
    @FXML
    private void handleFilter() {
        loadResidentsWithoutLoading();
    }

    @FXML
    private void handleAdd() {
        ValidationUtils.ValidationResult validation = validateInput();
        if (!validation.isValid()) {
            AlertUtils.showWarning("Lỗi validation", validation.getErrorMessage());
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
                    loadResidentsWithoutLoading();
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
            AlertUtils.showWarning("Lỗi validation", validation.getErrorMessage());
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
                    loadResidentsWithoutLoading();
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
                        loadResidentsWithoutLoading();
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

    // @FXML - Đã xóa nút quay lại khỏi top bar
    // private void handleBack() {
    //     ((Stage) residentTable.getScene().getWindow()).close();
    // }

    private void clearForm() {
        selectedResident = null; // Set trước để tránh trigger auto-fill
        
        fullNameField.clear();
        phoneField.clear();
        emailField.clear();
        identityCardField.clear();
        dateOfBirthPicker.setValue(null);
        genderCombo.setValue(null);
        addressArea.clear();
        emergencyContactField.clear();
        emergencyPhoneField.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "ĐANG_Ở")); // Mặc định "Đang ở"
        notesArea.clear();
        residentTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        
        // Clear validation states
        FormHelper.clearValidation(fullNameField);
        FormHelper.clearValidation(phoneField);
        FormHelper.clearValidation(emailField);
        FormHelper.clearValidation(identityCardField);
        FormHelper.clearValidation(emergencyPhoneField);
    }

    private ValidationUtils.ValidationResult validateInput() {
        ValidationUtils.ValidationResult result = new ValidationUtils.ValidationResult();
        boolean isValid = true;
        
        // Validate required fields with visual feedback
        if (!FormHelper.validateRequired(fullNameField, "Họ tên")) {
            isValid = false;
        }
        if (!FormHelper.validateRequired(phoneField, "Số điện thoại")) {
            isValid = false;
        }
        
        // Validate formats with visual feedback
        if (!FormHelper.validatePhone(phoneField)) {
            isValid = false;
        }
        if (!FormHelper.validateEmail(emailField)) {
            isValid = false;
        }
        if (!FormHelper.validateTextField(identityCardField,
            value -> value.isEmpty() || ValidationUtils.isValidIdentityCard(value),
            "CMND/CCCD không hợp lệ (9-12 chữ số)")) {
            isValid = false;
        }
        if (!FormHelper.validatePhone(emergencyPhoneField)) {
            isValid = false;
        }
        
        // Validate length
        if (!FormHelper.validateTextField(fullNameField,
            value -> value.length() >= 2 && value.length() <= 150,
            "Họ tên phải có từ 2-150 ký tự")) {
            isValid = false;
        }
        
        // If all valid, clear any error states
        if (isValid) {
            FormHelper.clearValidation(fullNameField);
            FormHelper.clearValidation(phoneField);
            FormHelper.clearValidation(emailField);
            FormHelper.clearValidation(identityCardField);
            FormHelper.clearValidation(emergencyPhoneField);
        }
        
        // Also populate ValidationResult for backward compatibility
        result.merge(ValidationUtils.validateRequired(fullNameField.getText(), "Họ tên"));
        result.merge(ValidationUtils.validateRequired(phoneField.getText(), "Số điện thoại"));
        if (phoneField.getText() != null && !phoneField.getText().trim().isEmpty()) {
            result.merge(ValidationUtils.validatePhone(phoneField.getText(), "Số điện thoại"));
        }
        if (emailField.getText() != null && !emailField.getText().trim().isEmpty()) {
            result.merge(ValidationUtils.validateEmail(emailField.getText(), "Email"));
        }
        if (identityCardField.getText() != null && !identityCardField.getText().trim().isEmpty()) {
            result.merge(ValidationUtils.validateIdentityCard(identityCardField.getText(), "CMND/CCCD"));
        }
        if (emergencyPhoneField.getText() != null && !emergencyPhoneField.getText().trim().isEmpty()) {
            result.merge(ValidationUtils.validatePhone(emergencyPhoneField.getText(), "Số điện thoại liên hệ khẩn cấp"));
        }
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

