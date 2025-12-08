package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Apartment;
import com.example.quanlytoanhanhom4.service.ApartmentService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ApartmentController implements Initializable {

    @FXML
    private TableView<Apartment> apartmentTable;
    @FXML
    private TableColumn<Apartment, String> colApartmentNo;
    @FXML
    private TableColumn<Apartment, Integer> colFloorNumber;
    @FXML
    private TableColumn<Apartment, String> colBuildingBlock;
    @FXML
    private TableColumn<Apartment, Integer> colRooms;
    @FXML
    private TableColumn<Apartment, Double> colArea;
    @FXML
    private TableColumn<Apartment, String> colStatus;

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
    private TextField filterBlockField;
    @FXML
    private TextField filterFloorField;
    @FXML
    private ComboBox<String> filterApartmentTypeCombo;
    @FXML
    private Pagination pagination;
    @FXML
    private Label paginationInfoLabel;
    @FXML
    private TextField apartmentNoField;
    @FXML
    private TextField floorNumberField;
    @FXML
    private TextField buildingBlockField;
    @FXML
    private TextField numberOfRoomsField;
    @FXML
    private TextField areaField;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button clearButton;

    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        STATUS_OPTIONS.put("ĐỂ_TRỐNG", "Để trống");
        STATUS_OPTIONS.put("ĐANG_Ở", "Đang ở");
        STATUS_OPTIONS.put("CHO_THUÊ", "Cho thuê");
        STATUS_OPTIONS.put("SỬA_CHỮA", "Sửa chữa");
    }

    private ObservableList<Apartment> apartments;
    private ObservableList<Apartment> allApartments; // Lưu tất cả apartments (chưa filter)
    private FilteredList<Apartment> filteredApartments; // Danh sách đã filter
    private Apartment selectedApartment;
    private int itemsPerPage = 20; // Mặc định 20 items/trang

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("========================================");
        System.out.println("KHỞI TẠO APARTMENT CONTROLLER");
        System.out.println("========================================");

        initializeTable();
        initializeComboBoxes();
        initializeSearch();
        initializePagination();
        initializeAdvancedFilters();

        System.out.println("✓ Đã khởi tạo table, comboboxes, spinners, search, pagination");

        // Load dữ liệu ngay lập tức, không cần delay
        System.out.println("✓ Bắt đầu load dữ liệu...");
        loadApartments();

        apartmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedApartment = newSelection;
                loadApartmentToForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }

    private void initializeTable() {
        colApartmentNo.setCellValueFactory(new PropertyValueFactory<>("apartmentNo"));
        colFloorNumber.setCellValueFactory(new PropertyValueFactory<>("floorNumber"));
        colBuildingBlock.setCellValueFactory(new PropertyValueFactory<>("buildingBlock"));
        colRooms.setCellValueFactory(new PropertyValueFactory<>("numberOfRooms"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colStatus.setCellValueFactory(cell -> {
            Apartment apartment = cell.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> toDisplay(STATUS_OPTIONS, apartment != null ? apartment.getStatus() : null)
            );
        });

        apartments = FXCollections.observableArrayList();
        apartmentTable.setItems(apartments);
        
        // Set empty state với nút thêm mới
        EmptyStateHelper.setApartmentEmptyState(apartmentTable, this::handleAdd);
    }

    private void initializeComboBoxes() {
        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "ĐỂ_TRỐNG"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        // Đã xóa filterStatusCombo khỏi top bar
        // filterStatusCombo.setItems(filterStatuses);
        // filterStatusCombo.setValue(ALL_LABEL);
        
        // Items per page combo
        if (itemsPerPageCombo != null) {
            itemsPerPageCombo.setItems(FXCollections.observableArrayList(20, 30, 50, 100));
            itemsPerPageCombo.setValue(20);
            itemsPerPageCombo.setOnAction(e -> {
                itemsPerPage = itemsPerPageCombo.getValue();
                updatePagination();
            });
        }
        
        // Filter apartment type combo
        if (filterApartmentTypeCombo != null) {
            filterApartmentTypeCombo.setItems(FXCollections.observableArrayList("Tất cả", "STUDIO", "1PN", "2PN", "3PN", "4PN", "PENTHOUSE", "DUPLEX"));
            filterApartmentTypeCombo.setValue("Tất cả");
            filterApartmentTypeCombo.setOnAction(e -> applyAdvancedFilters());
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
        if (filterBlockField != null) {
            filterBlockField.textProperty().addListener((observable, oldValue, newValue) -> {
                applyAdvancedFilters();
            });
        }
        
        if (filterFloorField != null) {
            filterFloorField.textProperty().addListener((observable, oldValue, newValue) -> {
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
        if (filterBlockField != null) filterBlockField.clear();
        if (filterFloorField != null) filterFloorField.clear();
        if (filterApartmentTypeCombo != null) filterApartmentTypeCombo.setValue("Tất cả");
        // Đã xóa filterStatusCombo khỏi top bar
        // if (filterStatusCombo != null) filterStatusCombo.setValue(ALL_LABEL);
        applyFilters();
    }
    
    private void applyFilters() {
        if (allApartments == null) {
            return;
        }
        
        filteredApartments = new FilteredList<>(allApartments, p -> true);
        
        filteredApartments.setPredicate(apartment -> {
            // Tìm kiếm theo số căn hộ, block, tầng
            String searchText = searchField != null ? searchField.getText() : "";
            if (searchText != null && !searchText.trim().isEmpty()) {
                String lowerSearchText = searchText.toLowerCase().trim();
                String apartmentNo = apartment.getApartmentNo() != null ? apartment.getApartmentNo().toLowerCase() : "";
                String block = apartment.getBuildingBlock() != null ? apartment.getBuildingBlock().toLowerCase() : "";
                String floor = apartment.getFloorNumber() != null ? apartment.getFloorNumber().toString() : "";
                if (!apartmentNo.contains(lowerSearchText) && 
                    !block.contains(lowerSearchText) && 
                    !floor.contains(lowerSearchText)) {
                    return false;
                }
            }
            
            // Filter theo status - Đã xóa filterStatusCombo khỏi top bar
            // String filterStatus = filterStatusCombo != null ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status
            if (filterStatus != null && !filterStatus.equals(ALL_LABEL)) {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                if (!statusValue.equals(apartment.getStatus())) {
                    return false;
                }
            }
            
            // Filter theo block
            String filterBlock = filterBlockField != null ? filterBlockField.getText() : "";
            if (filterBlock != null && !filterBlock.trim().isEmpty()) {
                String block = apartment.getBuildingBlock() != null ? apartment.getBuildingBlock().toLowerCase() : "";
                if (!block.contains(filterBlock.toLowerCase().trim())) {
                    return false;
                }
            }
            
            // Filter theo tầng
            String filterFloor = filterFloorField != null ? filterFloorField.getText() : "";
            if (filterFloor != null && !filterFloor.trim().isEmpty()) {
                try {
                    int floor = Integer.parseInt(filterFloor.trim());
                    if (apartment.getFloorNumber() == null || !apartment.getFloorNumber().equals(floor)) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid floor number
                }
            }
            
            // Filter theo loại căn hộ
            String filterType = filterApartmentTypeCombo != null ? filterApartmentTypeCombo.getValue() : "Tất cả";
            if (filterType != null && !filterType.equals("Tất cả")) {
                if (!filterType.equals(apartment.getApartmentType())) {
                    return false;
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
        if (filteredApartments == null || pagination == null) {
            return;
        }
        
        ObservableList<Apartment> itemsToPaginate = FXCollections.observableArrayList(filteredApartments);
        PaginationHelper.updatePagination(pagination, apartmentTable, itemsToPaginate, itemsPerPage);
        
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


    private void loadApartments() {
        try {
            System.out.println("Bắt đầu load dữ liệu căn hộ...");

            // Đảm bảo apartments list đã được khởi tạo
            if (apartments == null) {
                apartments = FXCollections.observableArrayList();
            }
            if (apartmentTable != null && apartmentTable.getItems() != apartments) {
                apartmentTable.setItems(apartments);
            }

            // Đã xóa filterStatusCombo khỏi top bar
            // String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
            //         ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status
            System.out.println("Filter status: " + filterStatus);

            // Load dữ liệu
            List<Apartment> apartmentList;
            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                System.out.println("Loading tất cả căn hộ...");
                apartmentList = ApartmentService.getAllApartments();
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                System.out.println("Loading căn hộ với status: " + statusValue);
                apartmentList = ApartmentService.getApartmentsByStatus(statusValue);
            }

            System.out.println("Đã lấy được " + (apartmentList != null ? apartmentList.size() : 0) + " căn hộ từ service");

            // Cập nhật UI trên JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                try {
                    // Lưu tất cả apartments vào allApartments
                    if (allApartments == null) {
                        allApartments = FXCollections.observableArrayList();
                    }
                    allApartments.clear();
                    if (apartmentList != null && !apartmentList.isEmpty()) {
                        allApartments.addAll(apartmentList);
                        System.out.println("Đã load " + apartmentList.size() + " căn hộ vào bảng");
                    } else {
                        System.out.println("CẢNH BÁO: Không có dữ liệu căn hộ nào được trả về từ service!");
                    }

                    // Áp dụng filters và pagination
                    applyFilters();

                    // Update status label
                    if (statusLabel != null) {
                        statusLabel.setText("Đã tải " + allApartments.size() + " căn hộ");
                    }

                    System.out.println("Số lượng căn hộ trong ObservableList: " + allApartments.size());
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật UI: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu căn hộ: " + e.getMessage());
            javafx.application.Platform.runLater(() -> {
                AlertUtils.showError("Lỗi khi tải dữ liệu", e.getMessage());
            });
        }
    }

    private void loadApartmentToForm(Apartment apartment) {
        apartmentNoField.setText(apartment.getApartmentNo());
        floorNumberField.setText(apartment.getFloorNumber() != null ? apartment.getFloorNumber().toString() : "");
        buildingBlockField.setText(apartment.getBuildingBlock());
        numberOfRoomsField.setText(apartment.getNumberOfRooms() != null ? apartment.getNumberOfRooms().toString() : "");
        areaField.setText(apartment.getArea() != null ? apartment.getArea().toString() : "");
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, apartment.getStatus()));
    }

    @FXML
    private void handleFilterStatusChange() {
        loadApartments();
    }
    
    @FXML
    private void handleSearch() {
        applyFilters();
    }
    
    @FXML
    private void handleItemsPerPageChange() {
        if (itemsPerPageCombo.getValue() != null) {
            itemsPerPage = itemsPerPageCombo.getValue();
            updatePagination();
        }
    }
    
    @FXML
    private void handleAdvancedFilterChange() {
        applyFilters();
    }
    
    @FXML
    private void handleTableRowClick() {
        Apartment selected = apartmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedApartment = selected;
            loadApartmentToForm(selected);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            addButton.setDisable(true);
        }
    }
    
    @FXML
    private void handlePageChange() {
        if (pagination != null && filteredApartments != null) {
            updatePagination();
        }
    }
    
    @FXML
    private void handleFieldValidation() {
        // Real-time validation can be added here if needed
    }
    
    @FXML
    private void handleClear() {
        clearForm();
    }
    
    @FXML
    private void handleFilter() {
        loadApartments();
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Apartment apartment = new Apartment();
            apartment.setApartmentNo(apartmentNoField.getText().trim());
            try {
                apartment.setFloorNumber(Integer.parseInt(floorNumberField.getText().trim()));
            } catch (NumberFormatException e) {
                apartment.setFloorNumber(null);
            }
            apartment.setBuildingBlock(buildingBlockField.getText().trim());
            try {
                apartment.setNumberOfRooms(Integer.parseInt(numberOfRoomsField.getText().trim()));
            } catch (NumberFormatException e) {
                apartment.setNumberOfRooms(null);
            }
            try {
                apartment.setArea(Double.parseDouble(areaField.getText().trim()));
            } catch (NumberFormatException e) {
                apartment.setArea(null);
            }
            apartment.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));

            if (ApartmentService.addApartment(apartment)) {
                AlertUtils.showSuccess("Thêm căn hộ thành công!");
                clearForm();
                loadApartments(); // Sẽ tự động apply filters và pagination
            } else {
                AlertUtils.showError("Lỗi khi thêm căn hộ!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedApartment != null && validateInput()) {
            selectedApartment.setApartmentNo(apartmentNoField.getText().trim());
            try {
                selectedApartment.setFloorNumber(Integer.parseInt(floorNumberField.getText().trim()));
            } catch (NumberFormatException e) {
                selectedApartment.setFloorNumber(null);
            }
            selectedApartment.setBuildingBlock(buildingBlockField.getText().trim());
            try {
                selectedApartment.setNumberOfRooms(Integer.parseInt(numberOfRoomsField.getText().trim()));
            } catch (NumberFormatException e) {
                selectedApartment.setNumberOfRooms(null);
            }
            try {
                selectedApartment.setArea(Double.parseDouble(areaField.getText().trim()));
            } catch (NumberFormatException e) {
                selectedApartment.setArea(null);
            }
            selectedApartment.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));

            if (ApartmentService.updateApartment(selectedApartment)) {
                AlertUtils.showSuccess("Cập nhật căn hộ thành công!");
                clearForm();
                loadApartments();
            } else {
                AlertUtils.showError("Lỗi khi cập nhật căn hộ!");
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedApartment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa căn hộ này?");
            alert.setContentText(selectedApartment.getApartmentNo());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (ApartmentService.deleteApartment(selectedApartment.getId())) {
                    AlertUtils.showSuccess("Xóa căn hộ thành công!");
                    clearForm();
                    loadApartments();
                } else {
                    AlertUtils.showError("Lỗi khi xóa căn hộ!");
                }
            }
        }
    }

    // @FXML - Đã xóa nút quay lại khỏi top bar
    // private void handleBack() {
    //     ((Stage) apartmentTable.getScene().getWindow()).close();
    // }

    private void clearForm() {
        apartmentNoField.clear();
        floorNumberField.clear();
        buildingBlockField.clear();
        numberOfRoomsField.clear();
        areaField.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "ĐỂ_TRỐNG"));
        selectedApartment = null;
        apartmentTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateInput() {
        if (apartmentNoField.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Vui lòng nhập số căn hộ!");
            return false;
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
}


