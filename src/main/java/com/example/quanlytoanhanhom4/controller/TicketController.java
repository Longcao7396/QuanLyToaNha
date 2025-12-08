package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Ticket;
import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.ResidentService;
import com.example.quanlytoanhanhom4.service.TicketService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller cho quản lý Ticket/Yêu cầu & sự cố
 * Module 5: Quản lý yêu cầu & sự cố (Ticket System)
 */
public class TicketController implements Initializable {

    @FXML
    private TableView<Ticket> ticketTable;
    @FXML
    private TableColumn<Ticket, String> colTicketNumber;
    @FXML
    private TableColumn<Ticket, String> colApartmentId;
    @FXML
    private TableColumn<Ticket, String> colResidentId;
    @FXML
    private TableColumn<Ticket, String> colTitle;
    @FXML
    private TableColumn<Ticket, String> colType;
    @FXML
    private TableColumn<Ticket, String> colCategory;
    @FXML
    private TableColumn<Ticket, String> colPriority;
    @FXML
    private TableColumn<Ticket, String> colStatus;
    @FXML
    private TableColumn<Ticket, String> colCreatedDate;

    @FXML
    private TextField ticketNumberField;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<String> ticketTypeCombo;
    @FXML
    private ComboBox<String> categoryCombo;
    @FXML
    private ComboBox<String> priorityCombo;
    @FXML
    private ComboBox<String> statusCombo;
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
    private ComboBox<String> filterTypeCombo;
    @FXML
    private ComboBox<String> filterPriorityCombo;
    @FXML
    private ComboBox<String> filterCategoryCombo;
    @FXML
    private Pagination pagination;
    @FXML
    private Label paginationInfoLabel;
    @FXML
    private ComboBox<Integer> apartmentIdCombo;
    @FXML
    private ComboBox<String> apartmentTypeCombo;
    @FXML
    private ComboBox<Integer> residentIdCombo;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label statusLabel;

    private static final LinkedHashMap<String, String> TICKET_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> CATEGORY_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> PRIORITY_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        TICKET_TYPE_OPTIONS.put("SỬA_CHỮA", "Sửa chữa");
        TICKET_TYPE_OPTIONS.put("KHIẾU_NẠI", "Khiếu nại");
        TICKET_TYPE_OPTIONS.put("YÊU_CẦU", "Yêu cầu");
        TICKET_TYPE_OPTIONS.put("SỰ_CỐ", "Sự cố");
        TICKET_TYPE_OPTIONS.put("KHÁC", "Khác");

        CATEGORY_OPTIONS.put("ĐIỆN", "Điện");
        CATEGORY_OPTIONS.put("NƯỚC", "Nước");
        CATEGORY_OPTIONS.put("THANG_MÁY", "Thang máy");
        CATEGORY_OPTIONS.put("AN_NINH", "An ninh");
        CATEGORY_OPTIONS.put("VỆ_SINH", "Vệ sinh");
        CATEGORY_OPTIONS.put("KHÁC", "Khác");

        PRIORITY_OPTIONS.put("LOW", "Thấp");
        PRIORITY_OPTIONS.put("MEDIUM", "Trung bình");
        PRIORITY_OPTIONS.put("HIGH", "Cao");
        PRIORITY_OPTIONS.put("URGENT", "Khẩn cấp");

        STATUS_OPTIONS.put("OPEN", "Mở");
        STATUS_OPTIONS.put("ASSIGNED", "Đã phân công");
        STATUS_OPTIONS.put("IN_PROGRESS", "Đang xử lý");
        STATUS_OPTIONS.put("RESOLVED", "Đã giải quyết");
        STATUS_OPTIONS.put("CLOSED", "Đã đóng");
        STATUS_OPTIONS.put("CANCELLED", "Đã hủy");
    }

    private ObservableList<Ticket> tickets;
    private ObservableList<Ticket> allTickets; // Lưu tất cả tickets (chưa filter)
    private FilteredList<Ticket> filteredTickets; // Danh sách đã filter
    private Ticket selectedTicket;
    private int itemsPerPage = 20; // Mặc định 20 items/trang

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        initializeSearch();
        initializePagination();
        initializeAdvancedFilters();
        
        // Đảm bảo load dữ liệu sau khi UI đã sẵn sàng
        javafx.application.Platform.runLater(() -> {
            loadTickets();
            // Reload comboboxes để đảm bảo có dữ liệu
            if (apartmentIdCombo != null && (apartmentIdCombo.getItems() == null || apartmentIdCombo.getItems().isEmpty())) {
                loadApartmentIds();
            }
            if (residentIdCombo != null && (residentIdCombo.getItems() == null || residentIdCombo.getItems().isEmpty())) {
                loadResidentIds();
            }
        });
    }

    private void initializeTable() {
        colTicketNumber.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
        colTicketNumber.setText("Số ticket");
        
        // Format Căn hộ ID
        colApartmentId.setCellValueFactory(cell -> {
            Integer apartmentId = cell.getValue().getApartmentId();
            if (apartmentId != null) {
                return new javafx.beans.property.SimpleStringProperty(apartmentId.toString());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        colApartmentId.setText("Căn hộ");
        
        // Format Cư dân ID
        colResidentId.setCellValueFactory(cell -> {
            Integer residentId = cell.getValue().getResidentId();
            if (residentId != null) {
                return new javafx.beans.property.SimpleStringProperty(residentId.toString());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        colResidentId.setText("Cư dân");
        
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setText("Tiêu đề");
        colType.setCellValueFactory(cell -> {
            Ticket ticket = cell.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> toDisplay(TICKET_TYPE_OPTIONS, ticket != null ? ticket.getTicketType() : null)
            );
        });
        colType.setText("Loại");
        colCategory.setCellValueFactory(cell -> {
            Ticket ticket = cell.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> toDisplay(CATEGORY_OPTIONS, ticket != null ? ticket.getCategory() : null)
            );
        });
        colCategory.setText("Danh mục");
        colPriority.setCellValueFactory(cell -> {
            Ticket ticket = cell.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> toDisplay(PRIORITY_OPTIONS, ticket != null ? ticket.getPriority() : null)
            );
        });
        colPriority.setText("Ưu tiên");
        
        colStatus.setCellValueFactory(cell -> {
            Ticket ticket = cell.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> toDisplay(STATUS_OPTIONS, ticket != null ? ticket.getStatus() : null)
            );
        });
        colStatus.setText("Trạng thái");
        colCreatedDate.setCellValueFactory(cell -> {
            Ticket ticket = cell.getValue();
            if (ticket != null && ticket.getCreatedDate() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> ticket.getCreatedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });
        colCreatedDate.setText("Ngày tạo");

        tickets = FXCollections.observableArrayList();
        ticketTable.setItems(tickets);
        
        // Set empty state với nút thêm mới
        EmptyStateHelper.setTicketEmptyState(ticketTable, this::handleAdd);

        ticketTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTicket = newSelection;
                loadTicketToForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }

    private void initializeComboBoxes() {
        ObservableList<String> ticketTypes = FXCollections.observableArrayList(TICKET_TYPE_OPTIONS.values());
        ticketTypeCombo.setItems(ticketTypes);

        ObservableList<String> categories = FXCollections.observableArrayList(CATEGORY_OPTIONS.values());
        categoryCombo.setItems(categories);

        ObservableList<String> priorities = FXCollections.observableArrayList(PRIORITY_OPTIONS.values());
        priorityCombo.setItems(priorities);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "OPEN"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        // Đã xóa filterStatusCombo khỏi top bar
        // filterStatusCombo.setItems(filterStatuses);
        // filterStatusCombo.setValue(ALL_LABEL);
        
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
        
        // Load resident IDs
        loadResidentIds();
        
        // Items per page combo
        if (itemsPerPageCombo != null) {
            itemsPerPageCombo.setItems(FXCollections.observableArrayList(20, 30, 50, 100));
            itemsPerPageCombo.setValue(20);
            itemsPerPageCombo.setOnAction(e -> {
                itemsPerPage = itemsPerPageCombo.getValue();
                updatePagination();
            });
        }
        
        // Filter type combo
        if (filterTypeCombo != null) {
            ObservableList<String> types = FXCollections.observableArrayList(TICKET_TYPE_OPTIONS.values());
            types.add(0, "Tất cả");
            filterTypeCombo.setItems(types);
            filterTypeCombo.setValue("Tất cả");
            filterTypeCombo.setOnAction(e -> applyAdvancedFilters());
        }
        
        // Filter priority combo
        if (filterPriorityCombo != null) {
            ObservableList<String> filterPriorities = FXCollections.observableArrayList(PRIORITY_OPTIONS.values());
            filterPriorities.add(0, "Tất cả");
            filterPriorityCombo.setItems(filterPriorities);
            filterPriorityCombo.setValue("Tất cả");
            filterPriorityCombo.setOnAction(e -> applyAdvancedFilters());
        }
        
        // Filter category combo
        if (filterCategoryCombo != null) {
            ObservableList<String> filterCategories = FXCollections.observableArrayList(CATEGORY_OPTIONS.values());
            filterCategories.add(0, "Tất cả");
            filterCategoryCombo.setItems(filterCategories);
            filterCategoryCombo.setValue("Tất cả");
            filterCategoryCombo.setOnAction(e -> applyAdvancedFilters());
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
        // Advanced filters are initialized in initializeComboBoxes
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
        if (filterTypeCombo != null) filterTypeCombo.setValue("Tất cả");
        if (filterPriorityCombo != null) filterPriorityCombo.setValue("Tất cả");
        if (filterCategoryCombo != null) filterCategoryCombo.setValue("Tất cả");
        // Đã xóa filterStatusCombo khỏi top bar
        // if (filterStatusCombo != null) filterStatusCombo.setValue(ALL_LABEL);
        applyFilters();
    }
    
    private void applyFilters() {
        if (allTickets == null) {
            return;
        }
        
        filteredTickets = new FilteredList<>(allTickets, p -> true);
        
        filteredTickets.setPredicate(ticket -> {
            // Tìm kiếm theo số ticket, tiêu đề
            String searchText = searchField != null ? searchField.getText() : "";
            if (searchText != null && !searchText.trim().isEmpty()) {
                String lowerSearchText = searchText.toLowerCase().trim();
                String ticketNumber = ticket.getTicketNumber() != null ? ticket.getTicketNumber().toLowerCase() : "";
                String title = ticket.getTitle() != null ? ticket.getTitle().toLowerCase() : "";
                if (!ticketNumber.contains(lowerSearchText) && !title.contains(lowerSearchText)) {
                    return false;
                }
            }
            
            // Filter theo status - Đã xóa filterStatusCombo khỏi top bar
            // String filterStatus = filterStatusCombo != null ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status
            if (filterStatus != null && !filterStatus.equals(ALL_LABEL)) {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                if (!statusValue.equals(ticket.getStatus())) {
                    return false;
                }
            }
            
            // Filter theo type
            String filterType = filterTypeCombo != null ? filterTypeCombo.getValue() : "Tất cả";
            if (filterType != null && !filterType.equals("Tất cả")) {
                String typeValue = toValue(TICKET_TYPE_OPTIONS, filterType);
                if (!typeValue.equals(ticket.getTicketType())) {
                    return false;
                }
            }
            
            // Filter theo priority
            String filterPriority = filterPriorityCombo != null ? filterPriorityCombo.getValue() : "Tất cả";
            if (filterPriority != null && !filterPriority.equals("Tất cả")) {
                String priorityValue = toValue(PRIORITY_OPTIONS, filterPriority);
                if (!priorityValue.equals(ticket.getPriority())) {
                    return false;
                }
            }
            
            // Filter theo category
            String filterCategory = filterCategoryCombo != null ? filterCategoryCombo.getValue() : "Tất cả";
            if (filterCategory != null && !filterCategory.equals("Tất cả")) {
                String categoryValue = toValue(CATEGORY_OPTIONS, filterCategory);
                if (!categoryValue.equals(ticket.getCategory())) {
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
        if (filteredTickets == null || pagination == null) {
            return;
        }
        
        ObservableList<Ticket> itemsToPaginate = FXCollections.observableArrayList(filteredTickets);
        PaginationHelper.updatePagination(pagination, ticketTable, itemsToPaginate, itemsPerPage);
        
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

    private void loadTickets() {
        try {
            // Đã xóa filterStatusCombo khỏi top bar
            // String filterStatus = filterStatusCombo.getValue();
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status
            java.util.List<Ticket> ticketList;
            
            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                ticketList = TicketService.getAllTickets();
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                ticketList = TicketService.getTicketsByStatus(statusValue);
            }

            // Lưu tất cả tickets vào allTickets
            if (allTickets == null) {
                allTickets = FXCollections.observableArrayList();
            }
            allTickets.clear();
            if (ticketList != null) {
                allTickets.addAll(ticketList);
            }
            
            // Áp dụng filters và pagination
            applyFilters();
            
            statusLabel.setText("Đã tải " + allTickets.size() + " ticket");
        } catch (Exception e) {
            AlertUtils.showError("Lỗi khi tải danh sách ticket", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTicketToForm(Ticket ticket) {
        ticketNumberField.setText(ticket.getTicketNumber());
        titleField.setText(ticket.getTitle());
        descriptionArea.setText(ticket.getDescription());
        ticketTypeCombo.setValue(toDisplay(TICKET_TYPE_OPTIONS, ticket.getTicketType()));
        categoryCombo.setValue(toDisplay(CATEGORY_OPTIONS, ticket.getCategory()));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, ticket.getPriority()));
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, ticket.getStatus()));
        apartmentIdCombo.setValue(ticket.getApartmentId());
        
        // Load apartment type based on selected apartment
        if (ticket.getApartmentId() != null && apartmentTypeCombo != null) {
            try {
                com.example.quanlytoanhanhom4.model.Apartment apartment = 
                    ApartmentService.getApartmentById(ticket.getApartmentId());
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
        
        residentIdCombo.setValue(ticket.getResidentId());
    }

    @FXML
    private void handleFilter() {
        loadTickets();
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Ticket ticket = new Ticket();
            ticket.setTicketNumber(generateTicketNumber());
            ticket.setTitle(titleField.getText().trim());
            ticket.setDescription(descriptionArea.getText().trim());
            ticket.setTicketType(toValue(TICKET_TYPE_OPTIONS, ticketTypeCombo.getValue()));
            ticket.setCategory(toValue(CATEGORY_OPTIONS, categoryCombo.getValue()));
            ticket.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            ticket.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            ticket.setCreatedDate(LocalDateTime.now());

            if (apartmentIdCombo.getValue() != null) {
                ticket.setApartmentId(apartmentIdCombo.getValue());
            }
            if (residentIdCombo.getValue() != null) {
                ticket.setResidentId(residentIdCombo.getValue());
            }

            if (TicketService.addTicket(ticket)) {
                AlertUtils.showSuccess("Thêm ticket thành công!");
                clearForm();
                loadTickets();
            } else {
                AlertUtils.showError("Lỗi khi thêm ticket!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedTicket != null && validateInput()) {
            selectedTicket.setTitle(titleField.getText().trim());
            selectedTicket.setDescription(descriptionArea.getText().trim());
            selectedTicket.setTicketType(toValue(TICKET_TYPE_OPTIONS, ticketTypeCombo.getValue()));
            selectedTicket.setCategory(toValue(CATEGORY_OPTIONS, categoryCombo.getValue()));
            selectedTicket.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            selectedTicket.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));

            if (TicketService.updateTicket(selectedTicket)) {
                AlertUtils.showSuccess("Cập nhật ticket thành công!");
                clearForm();
                loadTickets();
            } else {
                AlertUtils.showError("Lỗi khi cập nhật ticket!");
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedTicket != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa ticket này?");
            alert.setContentText(selectedTicket.getTicketNumber());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Note: Cần thêm method delete trong TicketService
                AlertUtils.showWarning("Chức năng xóa chưa được triển khai");
            }
        }
    }

    // @FXML - Đã xóa nút quay lại khỏi top bar
    // private void handleBack() {
    //     ((Stage) ticketTable.getScene().getWindow()).close();
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
    
    private void loadResidentIds() {
        if (residentIdCombo == null) {
            System.err.println("residentIdCombo is null!");
            return;
        }
        
        try {
            List<com.example.quanlytoanhanhom4.model.Resident> residents = ResidentService.getAllResidents();
            if (residents != null && !residents.isEmpty()) {
                List<Integer> residentIds = residents.stream()
                        .filter(res -> res != null)
                        .map(resident -> resident.getId())
                        .filter(id -> id != null)
                        .collect(java.util.stream.Collectors.toList());
                
                javafx.application.Platform.runLater(() -> {
                    residentIdCombo.setItems(FXCollections.observableArrayList(residentIds));
                    System.out.println("Đã load " + residentIds.size() + " cư dân vào dropdown");
                });
            } else {
                javafx.application.Platform.runLater(() -> {
                    residentIdCombo.setItems(FXCollections.observableArrayList());
                    System.out.println("Không có cư dân nào để load");
                });
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load resident IDs: " + e.getMessage());
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                if (residentIdCombo != null) {
                    residentIdCombo.setItems(FXCollections.observableArrayList());
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
                        .filter(id -> id != null)
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
        ticketNumberField.clear();
        titleField.clear();
        descriptionArea.clear();
        ticketTypeCombo.setValue(null);
        categoryCombo.setValue(null);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "OPEN"));
        apartmentIdCombo.setValue(null);
        if (apartmentTypeCombo != null) {
            apartmentTypeCombo.setValue("Tất cả");
        }
        residentIdCombo.setValue(null);
        selectedTicket = null;
        ticketTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Vui lòng nhập tiêu đề!");
            return false;
        }
        if (descriptionArea.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Vui lòng nhập mô tả!");
            return false;
        }
        return true;
    }

    private String generateTicketNumber() {
        return "TKT-" + System.currentTimeMillis();
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






