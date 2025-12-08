package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.ApartmentServiceFee;
import com.example.quanlytoanhanhom4.model.ServiceFeeType;
import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.ApartmentServiceFeeService;
import com.example.quanlytoanhanhom4.service.ServiceFeeTypeService;
import com.example.quanlytoanhanhom4.util.AlertUtils;
import com.example.quanlytoanhanhom4.util.EmptyStateHelper;
import com.example.quanlytoanhanhom4.util.PaginationHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller cho quản lý phí dịch vụ & Điện - Nước
 * Module 2 & 3: Quản lý phí & công nợ + Điện - Nước
 */
public class ServiceFeeController implements Initializable {

    @FXML
    private TableView<ApartmentServiceFee> serviceFeeTable;
    @FXML
    private TableColumn<ApartmentServiceFee, Integer> colApartmentId;
    @FXML
    private TableColumn<ApartmentServiceFee, String> colFeeType;
    @FXML
    private TableColumn<ApartmentServiceFee, String> colPeriod;
    @FXML
    private TableColumn<ApartmentServiceFee, Double> colPreviousReading;
    @FXML
    private TableColumn<ApartmentServiceFee, Double> colCurrentReading;
    @FXML
    private TableColumn<ApartmentServiceFee, Double> colConsumption;
    @FXML
    private TableColumn<ApartmentServiceFee, Double> colTotalAmount;
    @FXML
    private TableColumn<ApartmentServiceFee, LocalDate> colDueDate;
    @FXML
    private TableColumn<ApartmentServiceFee, String> colStatus;

    @FXML
    private ComboBox<Integer> apartmentIdCombo;
    @FXML
    private ComboBox<String> apartmentTypeCombo;
    @FXML
    private ComboBox<String> feeTypeCombo;
    @FXML
    private DatePicker periodDatePicker;
    @FXML
    private TextField previousReadingField;
    @FXML
    private TextField currentReadingField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private ComboBox<String> statusCombo;
    // @FXML - Đã xóa khỏi FXML (top bar đã bị xóa)
    // private ComboBox<String> filterTypeCombo;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Integer> itemsPerPageCombo;
    @FXML
    private Button advancedFilterButton;
    @FXML
    private VBox advancedFilterPane;
    @FXML
    private TextField filterAmountFromField;
    @FXML
    private TextField filterAmountToField;
    @FXML
    private Pagination pagination;
    @FXML
    private Label paginationInfoLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button calculateButton;
    @FXML
    private Label statusLabel;

    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        STATUS_OPTIONS.put("CHƯA_THANH_TOÁN", "Chưa thanh toán");
        STATUS_OPTIONS.put("CHỜ_THANH_TOÁN", "Chờ thanh toán");
        STATUS_OPTIONS.put("ĐÃ_THANH_TOÁN", "Đã thanh toán");
        STATUS_OPTIONS.put("QUÁ_HẠN", "Quá hạn");
        STATUS_OPTIONS.put("ĐÃ_HỦY", "Đã hủy");
        // Hỗ trợ cả tiếng Anh (để tương thích với dữ liệu cũ)
        STATUS_OPTIONS.put("PENDING", "Chờ thanh toán");
        STATUS_OPTIONS.put("PAID", "Đã thanh toán");
        STATUS_OPTIONS.put("OVERDUE", "Quá hạn");
        STATUS_OPTIONS.put("CANCELLED", "Đã hủy");
    }

    private ObservableList<ApartmentServiceFee> serviceFees;
    private ObservableList<ApartmentServiceFee> allServiceFees; // Lưu tất cả service fees (chưa filter)
    private FilteredList<ApartmentServiceFee> filteredServiceFees; // Danh sách đã filter
    private ApartmentServiceFee selectedServiceFee;
    private int itemsPerPage = 20; // Mặc định 20 items/trang

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        initializeDatePicker();
        initializeSearch();
        initializePagination();
        initializeAdvancedFilters();
        
        // Đảm bảo load dữ liệu sau khi UI đã sẵn sàng
        javafx.application.Platform.runLater(() -> {
            loadServiceFees();
            // Reload comboboxes để đảm bảo có dữ liệu
            if (apartmentIdCombo != null && (apartmentIdCombo.getItems() == null || apartmentIdCombo.getItems().isEmpty())) {
                loadApartmentIds();
            }
            if (feeTypeCombo != null && (feeTypeCombo.getItems() == null || feeTypeCombo.getItems().isEmpty())) {
                // Reload fee types
                ObservableList<String> feeTypeNames = FXCollections.observableArrayList();
                try {
                    List<ServiceFeeType> feeTypes = ServiceFeeTypeService.getActiveFeeTypes();
                    if (feeTypes != null && !feeTypes.isEmpty()) {
                        for (ServiceFeeType feeType : feeTypes) {
                            if (feeType.getFeeName() != null) {
                                feeTypeNames.add(feeType.getFeeName());
                            }
                        }
                    } else {
                        // Thêm loại phí mặc định
                        feeTypeNames.add("Điện");
                        feeTypeNames.add("Nước");
                        feeTypeNames.add("Phí quản lý");
                    }
                    feeTypeCombo.setItems(feeTypeNames);
                    // Đảm bảo có giá trị mặc định nếu dropdown trống
                    if (feeTypeCombo.getValue() == null && !feeTypeNames.isEmpty()) {
                        feeTypeCombo.setValue(feeTypeNames.get(0));
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi reload fee types: " + e.getMessage());
                    feeTypeNames.add("Điện");
                    feeTypeNames.add("Nước");
                    feeTypeNames.add("Phí quản lý");
                    feeTypeCombo.setItems(feeTypeNames);
                    if (feeTypeCombo.getValue() == null && !feeTypeNames.isEmpty()) {
                        feeTypeCombo.setValue(feeTypeNames.get(0));
                    }
                }
            }
        });
    }

    private void initializeTable() {
        colApartmentId.setCellValueFactory(new PropertyValueFactory<>("apartmentId"));
        colApartmentId.setText("ID Căn hộ");
        colFeeType.setCellValueFactory(cell -> {
            ApartmentServiceFee fee = cell.getValue();
            if (fee != null && fee.getFeeTypeId() != null) {
                ServiceFeeType feeType = ServiceFeeTypeService.getFeeTypeById(fee.getFeeTypeId());
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> feeType != null ? feeType.getFeeName() : ""
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });
        colFeeType.setText("Loại phí");
        colPeriod.setCellValueFactory(cell -> {
            ApartmentServiceFee fee = cell.getValue();
            if (fee != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> fee.getPeriodMonth() + "/" + fee.getPeriodYear()
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });
        colPeriod.setText("Kỳ");
        colPreviousReading.setCellValueFactory(new PropertyValueFactory<>("previousReading"));
        colPreviousReading.setText("Chỉ số cũ");
        colCurrentReading.setCellValueFactory(new PropertyValueFactory<>("currentReading"));
        colCurrentReading.setText("Chỉ số mới");
        colConsumption.setCellValueFactory(new PropertyValueFactory<>("consumption"));
        colConsumption.setText("Tiêu thụ");
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colTotalAmount.setText("Tổng tiền");
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colDueDate.setCellFactory(column -> new javafx.scene.control.TableCell<ApartmentServiceFee, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    // Highlight nếu đến hạn hoặc quá hạn
                    LocalDate today = LocalDate.now();
                    if (item.isBefore(today)) {
                        setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;"); // Màu đỏ cho quá hạn
                    } else if (item.isEqual(today)) {
                        setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold;"); // Màu vàng cho hôm nay
                    } else {
                        setStyle("-fx-text-fill: #059669;"); // Màu xanh cho còn thời gian
                    }
                }
            }
        });
        colDueDate.setText("Ngày phải đóng");
        colStatus.setCellValueFactory(cell -> {
            ApartmentServiceFee fee = cell.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> toStatusDisplay(fee != null ? fee.getStatus() : null)
            );
        });
        colStatus.setText("Trạng thái");

        serviceFees = FXCollections.observableArrayList();
        serviceFeeTable.setItems(serviceFees);
        
        // Set empty state với nút thêm mới
        EmptyStateHelper.setServiceFeeEmptyState(serviceFeeTable, this::handleAdd);

        serviceFeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedServiceFee = newSelection;
                loadServiceFeeToForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }

    private void initializeComboBoxes() {
        // Load apartment types
        if (apartmentTypeCombo != null) {
            ObservableList<String> apartmentTypes = FXCollections.observableArrayList(
                    "Tất cả", "STUDIO", "1PN", "2PN", "3PN", "4PN", "PENTHOUSE", "DUPLEX"
            );
            apartmentTypeCombo.setItems(apartmentTypes);
            apartmentTypeCombo.setValue("Tất cả");
            apartmentTypeCombo.setOnAction(e -> filterApartmentsByType());
        }
        
        // Load apartment IDs
        loadApartmentIds();
        
        // Load fee types
        if (feeTypeCombo != null) {
            ObservableList<String> feeTypeNames = FXCollections.observableArrayList();
            try {
                List<ServiceFeeType> feeTypes = ServiceFeeTypeService.getActiveFeeTypes();
                if (feeTypes != null && !feeTypes.isEmpty()) {
                    for (ServiceFeeType feeType : feeTypes) {
                        if (feeType.getFeeName() != null) {
                            feeTypeNames.add(feeType.getFeeName());
                        }
                    }
                    feeTypeCombo.setItems(feeTypeNames);
                    System.out.println("Đã load " + feeTypeNames.size() + " loại phí vào dropdown");
                } else {
                    feeTypeCombo.setItems(FXCollections.observableArrayList());
                    System.out.println("Không có loại phí nào để load - có thể cần thêm dữ liệu vào database");
                    // Thêm một số loại phí mặc định nếu database trống
                    feeTypeNames.add("Điện");
                    feeTypeNames.add("Nước");
                    feeTypeNames.add("Phí quản lý");
                    feeTypeCombo.setItems(feeTypeNames);
                    System.out.println("Đã thêm " + feeTypeNames.size() + " loại phí mặc định");
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi load fee types: " + e.getMessage());
                e.printStackTrace();
                // Fallback: thêm loại phí mặc định
                feeTypeNames.add("Điện");
                feeTypeNames.add("Nước");
                feeTypeNames.add("Phí quản lý");
                feeTypeCombo.setItems(feeTypeNames);
            }
        } else {
            System.err.println("feeTypeCombo is null!");
        }

        // Status combo - chỉ hiển thị các option tiếng Việt
        ObservableList<String> statuses = FXCollections.observableArrayList(
            "Chưa thanh toán",
            "Chờ thanh toán",
            "Đã thanh toán",
            "Quá hạn",
            "Đã hủy"
        );
        statusCombo.setItems(statuses);
        statusCombo.setValue("Chưa thanh toán");

        // Filter combo - reload fee types for filter
        ObservableList<String> filterTypes = FXCollections.observableArrayList();
        try {
            List<ServiceFeeType> feeTypes = ServiceFeeTypeService.getActiveFeeTypes();
            if (feeTypes != null && !feeTypes.isEmpty()) {
                for (ServiceFeeType feeType : feeTypes) {
                    filterTypes.add(feeType.getFeeName());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load fee types cho filter: " + e.getMessage());
        }
        filterTypes.add(0, ALL_LABEL);
        // Đã xóa filterTypeCombo khỏi top bar
        // filterTypeCombo.setItems(filterTypes);
        // filterTypeCombo.setValue(ALL_LABEL);
        
        // Items per page combo
        if (itemsPerPageCombo != null) {
            itemsPerPageCombo.setItems(FXCollections.observableArrayList(20, 30, 50, 100));
            itemsPerPageCombo.setValue(20);
            itemsPerPageCombo.setOnAction(e -> {
                itemsPerPage = itemsPerPageCombo.getValue();
                updatePagination();
            });
        }
    }
    
    private void initializeSearch() {
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
        if (filterAmountFromField != null) {
            filterAmountFromField.textProperty().addListener((observable, oldValue, newValue) -> {
                applyAdvancedFilters();
            });
        }
        
        if (filterAmountToField != null) {
            filterAmountToField.textProperty().addListener((observable, oldValue, newValue) -> {
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
        if (filterAmountFromField != null) filterAmountFromField.clear();
        if (filterAmountToField != null) filterAmountToField.clear();
        // Đã xóa filterTypeCombo khỏi top bar
        // if (filterTypeCombo != null) filterTypeCombo.setValue(ALL_LABEL);
        applyFilters();
    }
    
    private void applyFilters() {
        if (allServiceFees == null) {
            return;
        }
        
        filteredServiceFees = new FilteredList<>(allServiceFees, p -> true);
        
        filteredServiceFees.setPredicate(fee -> {
            // Tìm kiếm theo ID căn hộ, loại phí
            String searchText = searchField != null ? searchField.getText() : "";
            if (searchText != null && !searchText.trim().isEmpty()) {
                String lowerSearchText = searchText.toLowerCase().trim();
                String apartmentId = fee.getApartmentId() != null ? fee.getApartmentId().toString() : "";
                ServiceFeeType feeType = ServiceFeeTypeService.getFeeTypeById(fee.getFeeTypeId());
                String feeTypeName = feeType != null ? feeType.getFeeName().toLowerCase() : "";
                if (!apartmentId.contains(lowerSearchText) && !feeTypeName.contains(lowerSearchText)) {
                    return false;
                }
            }
            
            // Filter theo type - Đã xóa filterTypeCombo khỏi top bar
            // String filterType = filterTypeCombo != null ? filterTypeCombo.getValue() : ALL_LABEL;
            String filterType = ALL_LABEL; // Tạm thời bỏ qua filter theo type
            if (filterType != null && !filterType.equals(ALL_LABEL)) {
                ServiceFeeType feeType = ServiceFeeTypeService.getFeeTypeById(fee.getFeeTypeId());
                if (feeType == null || !filterType.equals(feeType.getFeeName())) {
                    return false;
                }
            }
            
            // Filter theo khoảng tiền
            String amountFrom = filterAmountFromField != null ? filterAmountFromField.getText() : "";
            if (amountFrom != null && !amountFrom.trim().isEmpty()) {
                try {
                    double from = Double.parseDouble(amountFrom.trim());
                    if (fee.getTotalAmount() == null || fee.getTotalAmount() < from) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            
            String amountTo = filterAmountToField != null ? filterAmountToField.getText() : "";
            if (amountTo != null && !amountTo.trim().isEmpty()) {
                try {
                    double to = Double.parseDouble(amountTo.trim());
                    if (fee.getTotalAmount() == null || fee.getTotalAmount() > to) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            
            return true;
        });
        
        updatePagination();
    }
    
    private void applyAdvancedFilters() {
        applyFilters();
    }
    
    private void updatePagination() {
        if (filteredServiceFees == null || pagination == null) {
            return;
        }
        
        ObservableList<ApartmentServiceFee> itemsToPaginate = FXCollections.observableArrayList(filteredServiceFees);
        PaginationHelper.updatePagination(pagination, serviceFeeTable, itemsToPaginate, itemsPerPage);
        
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

    private void initializeDatePicker() {
        // Set giá trị mặc định là ngày đầu tiên của tháng hiện tại
        LocalDate now = LocalDate.now();
        periodDatePicker.setValue(LocalDate.of(now.getYear(), now.getMonth(), 1));
    }

    private void loadServiceFees() {
        try {
            List<ApartmentServiceFee> feeList = ApartmentServiceFeeService.getAllServiceFees();
            
            // Lưu tất cả service fees vào allServiceFees
            if (allServiceFees == null) {
                allServiceFees = FXCollections.observableArrayList();
            }
            allServiceFees.clear();
            if (feeList != null) {
                allServiceFees.addAll(feeList);
            }
            
            // Áp dụng filters và pagination
            applyFilters();
            
            statusLabel.setText("Đã tải " + allServiceFees.size() + " phí dịch vụ");
        } catch (Exception e) {
            AlertUtils.showError("Lỗi khi tải danh sách phí dịch vụ", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadServiceFeeToForm(ApartmentServiceFee fee) {
        apartmentIdCombo.setValue(fee.getApartmentId());
        
        // Load apartment type based on selected apartment
        if (fee.getApartmentId() != null && apartmentTypeCombo != null) {
            try {
                com.example.quanlytoanhanhom4.model.Apartment apartment = 
                    ApartmentService.getApartmentById(fee.getApartmentId());
                if (apartment != null && apartment.getApartmentType() != null) {
                    apartmentTypeCombo.setValue(apartment.getApartmentType());
                    filterApartmentsByType(); // Filter to show only same type
                } else {
                    apartmentTypeCombo.setValue("Tất cả");
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi load apartment type: " + e.getMessage());
                apartmentTypeCombo.setValue("Tất cả");
            }
        }
        
        ServiceFeeType feeType = ServiceFeeTypeService.getFeeTypeById(fee.getFeeTypeId());
        if (feeType != null) {
            feeTypeCombo.setValue(feeType.getFeeName());
        }
        
        // Set DatePicker từ tháng/năm của phí dịch vụ
        if (fee.getPeriodMonth() != null && fee.getPeriodYear() != null) {
            LocalDate periodDate = LocalDate.of(fee.getPeriodYear(), fee.getPeriodMonth(), 1);
            periodDatePicker.setValue(periodDate);
        }
        previousReadingField.setText(fee.getPreviousReading() != null ? fee.getPreviousReading().toString() : "");
        currentReadingField.setText(fee.getCurrentReading() != null ? fee.getCurrentReading().toString() : "");
        unitPriceField.setText(fee.getUnitPrice() != null ? fee.getUnitPrice().toString() : "");
        totalAmountLabel.setText(fee.getTotalAmount() != null ? String.format("%.0f VNĐ", fee.getTotalAmount()) : "0 VNĐ");
        dueDatePicker.setValue(fee.getDueDate());
        statusCombo.setValue(toStatusDisplay(fee.getStatus()));
    }

    @FXML
    private void handleCalculate() {
        try {
            double previous = previousReadingField.getText().isEmpty() ? 0 : Double.parseDouble(previousReadingField.getText().trim());
            double current = currentReadingField.getText().isEmpty() ? 0 : Double.parseDouble(currentReadingField.getText().trim());
            double unitPrice = unitPriceField.getText().isEmpty() ? 0 : Double.parseDouble(unitPriceField.getText().trim());

            double consumption = current - previous;
            double totalAmount = consumption * unitPrice;

            totalAmountLabel.setText(String.format("%.0f VNĐ", totalAmount));
            statusLabel.setText(String.format("Tiêu thụ: %.2f | Tổng tiền: %.0f VNĐ", consumption, totalAmount));
        } catch (NumberFormatException e) {
            statusLabel.setText("Vui lòng nhập đúng định dạng số!");
        }
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            ApartmentServiceFee fee = new ApartmentServiceFee();
            fee.setApartmentId(apartmentIdCombo.getValue());
            
            String selectedFeeTypeName = feeTypeCombo.getValue();
            ServiceFeeType feeType = ServiceFeeTypeService.getActiveFeeTypes().stream()
                    .filter(ft -> ft.getFeeName().equals(selectedFeeTypeName))
                    .findFirst()
                    .orElse(null);
            if (feeType != null) {
                fee.setFeeTypeId(feeType.getId());
            }
            
            // Lấy tháng/năm từ DatePicker
            LocalDate periodDate = periodDatePicker.getValue();
            if (periodDate == null) {
                AlertUtils.showWarning("Vui lòng chọn kỳ tính phí!");
                return;
            }
            fee.setPeriodMonth(periodDate.getMonthValue());
            fee.setPeriodYear(periodDate.getYear());
            
            try {
                fee.setPreviousReading(previousReadingField.getText().isEmpty() ? null : Double.parseDouble(previousReadingField.getText().trim()));
                fee.setCurrentReading(currentReadingField.getText().isEmpty() ? null : Double.parseDouble(currentReadingField.getText().trim()));
                fee.setUnitPrice(unitPriceField.getText().isEmpty() ? null : Double.parseDouble(unitPriceField.getText().trim()));
                
                fee.calculateConsumption();
                fee.calculateTotalAmount();
            } catch (NumberFormatException e) {
                AlertUtils.showWarning("Vui lòng nhập đúng định dạng số!");
                return;
            }
            
            fee.setDueDate(dueDatePicker.getValue());
            String statusDisplay = statusCombo.getValue();
            String statusValue = toStatusValue(statusDisplay);
            fee.setStatus(statusValue);

            if (ApartmentServiceFeeService.addServiceFee(fee)) {
                AlertUtils.showSuccess("Thêm phí dịch vụ thành công!");
                clearForm();
                loadServiceFees();
            } else {
                AlertUtils.showError("Lỗi khi thêm phí dịch vụ!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedServiceFee != null && validateInput()) {
            selectedServiceFee.setApartmentId(apartmentIdCombo.getValue());
            
            String selectedFeeTypeName = feeTypeCombo.getValue();
            ServiceFeeType feeType = ServiceFeeTypeService.getActiveFeeTypes().stream()
                    .filter(ft -> ft.getFeeName().equals(selectedFeeTypeName))
                    .findFirst()
                    .orElse(null);
            if (feeType != null) {
                selectedServiceFee.setFeeTypeId(feeType.getId());
            }
            
            // Lấy tháng/năm từ DatePicker
            LocalDate periodDate = periodDatePicker.getValue();
            if (periodDate == null) {
                AlertUtils.showWarning("Vui lòng chọn kỳ tính phí!");
                return;
            }
            selectedServiceFee.setPeriodMonth(periodDate.getMonthValue());
            selectedServiceFee.setPeriodYear(periodDate.getYear());
            
            try {
                selectedServiceFee.setPreviousReading(previousReadingField.getText().isEmpty() ? null : Double.parseDouble(previousReadingField.getText().trim()));
                selectedServiceFee.setCurrentReading(currentReadingField.getText().isEmpty() ? null : Double.parseDouble(currentReadingField.getText().trim()));
                selectedServiceFee.setUnitPrice(unitPriceField.getText().isEmpty() ? null : Double.parseDouble(unitPriceField.getText().trim()));
                
                selectedServiceFee.calculateConsumption();
                selectedServiceFee.calculateTotalAmount();
            } catch (NumberFormatException e) {
                AlertUtils.showWarning("Vui lòng nhập đúng định dạng số!");
                return;
            }
            
            selectedServiceFee.setDueDate(dueDatePicker.getValue());
            String statusDisplay = statusCombo.getValue();
            String statusValue = toStatusValue(statusDisplay);
            selectedServiceFee.setStatus(statusValue);

            if (ApartmentServiceFeeService.updateServiceFee(selectedServiceFee)) {
                AlertUtils.showSuccess("Cập nhật phí dịch vụ thành công!");
                clearForm();
                loadServiceFees();
            } else {
                AlertUtils.showError("Lỗi khi cập nhật phí dịch vụ!");
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedServiceFee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa phí dịch vụ này?");
            
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Note: Cần thêm method delete trong ApartmentServiceFeeService
                if (ApartmentServiceFeeService.deleteServiceFee(selectedServiceFee.getId())) {
                    AlertUtils.showSuccess("Xóa phí dịch vụ thành công!");
                    clearForm();
                    loadServiceFees();
                } else {
                    AlertUtils.showError("Lỗi khi xóa phí dịch vụ!");
                }
            }
        }
    }

    @FXML
    private void handleFilter() {
        loadServiceFees();
    }

    // @FXML - Đã xóa nút quay lại khỏi top bar
    // private void handleBack() {
    //     ((Stage) serviceFeeTable.getScene().getWindow()).close();
    // }

    private void loadApartmentIds() {
        if (apartmentIdCombo == null) {
            System.err.println("apartmentIdCombo is null!");
            return;
        }
        
        try {
            List<com.example.quanlytoanhanhom4.model.Apartment> apartments = ApartmentService.getAllApartments();
            if (apartments != null && !apartments.isEmpty()) {
                List<Integer> apartmentIds = apartments.stream()
                        .filter(apt -> apt != null)
                        .map(apartment -> apartment.getId())
                        .filter(id -> id != null)
                        .collect(java.util.stream.Collectors.toList());
                
                javafx.application.Platform.runLater(() -> {
                    apartmentIdCombo.setItems(FXCollections.observableArrayList(apartmentIds));
                    System.out.println("Đã load " + apartmentIds.size() + " căn hộ vào dropdown");
                });
            } else {
                javafx.application.Platform.runLater(() -> {
                    apartmentIdCombo.setItems(FXCollections.observableArrayList());
                    System.out.println("Không có căn hộ nào để load");
                });
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load apartment IDs: " + e.getMessage());
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                if (apartmentIdCombo != null) {
                    apartmentIdCombo.setItems(FXCollections.observableArrayList());
                }
            });
        }
    }
    
    private void filterApartmentsByType() {
        if (apartmentIdCombo == null) {
            System.err.println("apartmentIdCombo is null!");
            return;
        }
        
        if (apartmentTypeCombo == null || apartmentTypeCombo.getValue() == null) {
            loadApartmentIds();
            return;
        }
        
        String selectedType = apartmentTypeCombo.getValue();
        if ("Tất cả".equals(selectedType)) {
            loadApartmentIds();
            return;
        }
        
        try {
            List<com.example.quanlytoanhanhom4.model.Apartment> allApartments = ApartmentService.getAllApartments();
            if (allApartments != null && !allApartments.isEmpty()) {
                List<Integer> filteredIds = allApartments.stream()
                        .filter(apt -> apt != null && selectedType.equals(apt.getApartmentType()))
                        .map(apartment -> apartment.getId())
                        .collect(java.util.stream.Collectors.toList());
                
                javafx.application.Platform.runLater(() -> {
                    apartmentIdCombo.setItems(FXCollections.observableArrayList(filteredIds));
                    System.out.println("Đã filter " + filteredIds.size() + " căn hộ loại " + selectedType);
                });
            } else {
                javafx.application.Platform.runLater(() -> {
                    apartmentIdCombo.setItems(FXCollections.observableArrayList());
                });
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi filter apartments: " + e.getMessage());
            e.printStackTrace();
            loadApartmentIds(); // Fallback to load all
        }
    }

    private void clearForm() {
        apartmentIdCombo.setValue(null);
        if (apartmentTypeCombo != null) {
            apartmentTypeCombo.setValue("Tất cả");
        }
        feeTypeCombo.setValue(null);
        // Reset DatePicker về ngày đầu tiên của tháng hiện tại
        LocalDate now = LocalDate.now();
        periodDatePicker.setValue(LocalDate.of(now.getYear(), now.getMonth(), 1));
        previousReadingField.clear();
        currentReadingField.clear();
        unitPriceField.clear();
        totalAmountLabel.setText("0 VNĐ");
        // Set ngày phải đóng mặc định là 25 tháng hiện tại
        LocalDate defaultDueDate = LocalDate.now().withDayOfMonth(25);
        if (defaultDueDate.isBefore(LocalDate.now())) {
            defaultDueDate = defaultDueDate.plusMonths(1);
        }
        dueDatePicker.setValue(defaultDueDate);
        statusCombo.setValue("Chưa thanh toán");
        selectedServiceFee = null;
        serviceFeeTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateInput() {
        if (apartmentIdCombo.getValue() == null) {
            AlertUtils.showWarning("Vui lòng chọn căn hộ!");
            return false;
        }
        if (feeTypeCombo.getValue() == null || feeTypeCombo.getValue().trim().isEmpty()) {
            AlertUtils.showWarning("Vui lòng chọn loại phí!");
            return false;
        }
        if (periodDatePicker.getValue() == null) {
            AlertUtils.showWarning("Vui lòng chọn kỳ tính phí!");
            return false;
        }
        if (statusCombo.getValue() == null || statusCombo.getValue().trim().isEmpty()) {
            AlertUtils.showWarning("Vui lòng chọn trạng thái!");
            return false;
        }
        if (dueDatePicker.getValue() == null) {
            AlertUtils.showWarning("Vui lòng chọn ngày phải đóng!");
            return false;
        }
        if (totalAmountLabel.getText() == null || totalAmountLabel.getText().equals("0 VNĐ")) {
            // Cho phép 0 nhưng cảnh báo
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText("Tổng tiền là 0 VNĐ");
            alert.setContentText("Bạn có muốn tiếp tục với tổng tiền là 0 VNĐ không?");
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.CANCEL) {
                return false;
            }
        }
        return true;
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
    
    /**
     * Chuyển đổi từ hiển thị tiếng Việt sang giá trị trong database
     */
    private String toStatusValue(String display) {
        if (display == null) {
            return "CHƯA_THANH_TOÁN";
        }
        switch (display) {
            case "Chưa thanh toán":
                return "CHƯA_THANH_TOÁN";
            case "Chờ thanh toán":
                return "CHỜ_THANH_TOÁN";
            case "Đã thanh toán":
                return "ĐÃ_THANH_TOÁN";
            case "Quá hạn":
                return "QUÁ_HẠN";
            case "Đã hủy":
                return "ĐÃ_HỦY";
            default:
                // Nếu là giá trị cũ (PENDING, PAID, etc), giữ nguyên
                return display;
        }
    }
    
    /**
     * Chuyển đổi từ giá trị trong database sang hiển thị tiếng Việt
     */
    private String toStatusDisplay(String value) {
        if (value == null) {
            return "Chưa thanh toán";
        }
        switch (value) {
            case "CHƯA_THANH_TOÁN":
                return "Chưa thanh toán";
            case "CHỜ_THANH_TOÁN":
            case "PENDING":
                return "Chờ thanh toán";
            case "ĐÃ_THANH_TOÁN":
            case "PAID":
                return "Đã thanh toán";
            case "QUÁ_HẠN":
            case "OVERDUE":
                return "Quá hạn";
            case "ĐÃ_HỦY":
            case "CANCELLED":
                return "Đã hủy";
            default:
                return toDisplay(STATUS_OPTIONS, value);
        }
    }
}

