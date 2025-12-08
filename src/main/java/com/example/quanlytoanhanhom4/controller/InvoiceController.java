package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Invoice;
import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.InvoiceService;
import com.example.quanlytoanhanhom4.util.AlertUtils;
import com.example.quanlytoanhanhom4.util.EmptyStateHelper;
import com.example.quanlytoanhanhom4.util.PaginationHelper;
import com.example.quanlytoanhanhom4.util.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class InvoiceController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @FXML
    private TableView<Invoice> invoiceTable;
    @FXML
    private TableColumn<Invoice, String> colInvoiceNumber;
    @FXML
    private TableColumn<Invoice, String> colApartmentId;
    @FXML
    private TableColumn<Invoice, LocalDate> colInvoiceDate;
    @FXML
    private TableColumn<Invoice, LocalDate> colDueDate;
    @FXML
    private TableColumn<Invoice, Double> colTotalAmount;
    @FXML
    private TableColumn<Invoice, Double> colPaidAmount;
    @FXML
    private TableColumn<Invoice, String> colPaymentMethod;
    @FXML
    private TableColumn<Invoice, LocalDate> colPaidDate;
    @FXML
    private TableColumn<Invoice, String> colStatus;

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
    private TextField filterAmountFromField;
    @FXML
    private TextField filterAmountToField;
    @FXML
    private ComboBox<String> filterPaymentMethodCombo;
    @FXML
    private Pagination pagination;
    @FXML
    private Label paginationInfoLabel;
    @FXML
    private ComboBox<Integer> apartmentIdCombo;
    @FXML
    private TextField invoiceNumberField;
    @FXML
    private DatePicker invoiceDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private TextField totalAmountField;
    @FXML
    private TextField paidAmountField;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private ComboBox<String> paymentMethodCombo;
    @FXML
    private DatePicker paymentDatePicker;
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
    private static final LinkedHashMap<String, String> PAYMENT_METHOD_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        STATUS_OPTIONS.put("CHỜ_THANH_TOÁN", "Chờ thanh toán");
        STATUS_OPTIONS.put("THANH_TOÁN_MỘT_PHẦN", "Thanh toán một phần");
        STATUS_OPTIONS.put("ĐÃ_THANH_TOÁN", "Đã thanh toán");
        STATUS_OPTIONS.put("QUÁ_HẠN", "Quá hạn");
        STATUS_OPTIONS.put("ĐÃ_HỦY", "Đã hủy");

        PAYMENT_METHOD_OPTIONS.put("CASH", "Tiền mặt");
        PAYMENT_METHOD_OPTIONS.put("BANK_TRANSFER", "Chuyển khoản");
        PAYMENT_METHOD_OPTIONS.put("CARD", "Thẻ");
        PAYMENT_METHOD_OPTIONS.put("OTHER", "Khác");
    }

    private ObservableList<Invoice> invoices;
    private ObservableList<Invoice> allInvoices; // Lưu tất cả invoices (chưa filter)
    private FilteredList<Invoice> filteredInvoices; // Danh sách đã filter
    private Invoice selectedInvoice;
    private int itemsPerPage = 20; // Mặc định 20 items/trang

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        initializeSearch();
        initializePagination();
        initializeAdvancedFilters();

        // Delay nhỏ để đảm bảo UI đã sẵn sàng trước khi load dữ liệu
        javafx.application.Platform.runLater(() -> {
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(100));
            pause.setOnFinished(e -> {
                loadInvoices();
            });
            pause.play();
        });

        invoiceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedInvoice = newSelection;
                loadInvoiceToForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }

    private void initializeTable() {
        colInvoiceNumber.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        
        // Format Căn hộ ID
        colApartmentId.setCellValueFactory(cell -> {
            Integer apartmentId = cell.getValue().getApartmentId();
            if (apartmentId != null) {
                return new javafx.beans.property.SimpleStringProperty(apartmentId.toString());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        colInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        
        // Format Hạn thanh toán
        colDueDate.setCellValueFactory(cell -> {
            LocalDate date = cell.getValue().getDueDate();
            if (date != null) {
                return new javafx.beans.property.SimpleObjectProperty<>(date);
            }
            return new javafx.beans.property.SimpleObjectProperty<>(null);
        });
        colDueDate.setCellFactory(column -> new javafx.scene.control.TableCell<Invoice, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText("");
                } else {
                    setText(date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });
        
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        
        // Format Ngày thanh toán
        colPaidDate.setCellValueFactory(cell -> {
            LocalDate date = cell.getValue().getPaidDate();
            if (date != null) {
                return new javafx.beans.property.SimpleObjectProperty<>(date);
            }
            return new javafx.beans.property.SimpleObjectProperty<>(null);
        });
        colPaidDate.setCellFactory(column -> new javafx.scene.control.TableCell<Invoice, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText("");
                } else {
                    setText(date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });
        
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        invoices = FXCollections.observableArrayList();
        invoiceTable.setItems(invoices);
        
        // Set empty state với nút thêm mới
        EmptyStateHelper.setInvoiceEmptyState(invoiceTable, this::handleAdd);
    }

    private void initializeComboBoxes() {
        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "CHỜ_THANH_TOÁN"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        // Đã xóa filterStatusCombo khỏi top bar
        // filterStatusCombo.setItems(filterStatuses);
        // filterStatusCombo.setValue(ALL_LABEL);

        ObservableList<String> paymentMethods = FXCollections.observableArrayList(PAYMENT_METHOD_OPTIONS.values());
        paymentMethodCombo.setItems(paymentMethods);

        // Load apartment IDs from service
        try {
            List<com.example.quanlytoanhanhom4.model.Apartment> apartments = ApartmentService.getAllApartments();
            if (apartments != null && !apartments.isEmpty()) {
                List<Integer> apartmentIds = apartments.stream()
                        .map(apartment -> apartment.getId())
                        .collect(java.util.stream.Collectors.toList());
                apartmentIdCombo.setItems(FXCollections.observableArrayList(apartmentIds));
                logger.info("Đã load {} căn hộ vào dropdown", apartmentIds.size());
            } else {
                apartmentIdCombo.setItems(FXCollections.observableArrayList());
                logger.warn("Không có căn hộ nào để load");
            }
        } catch (Exception e) {
            logger.error("Lỗi khi load apartment IDs: {}", e.getMessage(), e);
            apartmentIdCombo.setItems(FXCollections.observableArrayList());
        }

        invoiceDatePicker.setValue(LocalDate.now());
        dueDatePicker.setValue(LocalDate.now().plusDays(30));
        
        // Items per page combo
        if (itemsPerPageCombo != null) {
            itemsPerPageCombo.setItems(FXCollections.observableArrayList(20, 30, 50, 100));
            itemsPerPageCombo.setValue(20);
            itemsPerPageCombo.setOnAction(e -> {
                itemsPerPage = itemsPerPageCombo.getValue();
                updatePagination();
            });
        }
        
        // Filter payment method combo
        if (filterPaymentMethodCombo != null) {
            ObservableList<String> filterPaymentMethods = FXCollections.observableArrayList(PAYMENT_METHOD_OPTIONS.values());
            filterPaymentMethods.add(0, "Tất cả");
            filterPaymentMethodCombo.setItems(filterPaymentMethods);
            filterPaymentMethodCombo.setValue("Tất cả");
            filterPaymentMethodCombo.setOnAction(e -> applyAdvancedFilters());
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
        if (filterPaymentMethodCombo != null) filterPaymentMethodCombo.setValue("Tất cả");
        // Đã xóa filterStatusCombo khỏi top bar
        // if (filterStatusCombo != null) filterStatusCombo.setValue(ALL_LABEL);
        applyFilters();
    }
    
    private void applyFilters() {
        if (allInvoices == null) {
            return;
        }
        
        filteredInvoices = new FilteredList<>(allInvoices, p -> true);
        
        filteredInvoices.setPredicate(invoice -> {
            // Tìm kiếm theo số hóa đơn, căn hộ
            String searchText = searchField != null ? searchField.getText() : "";
            if (searchText != null && !searchText.trim().isEmpty()) {
                String lowerSearchText = searchText.toLowerCase().trim();
                String invoiceNumber = invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber().toLowerCase() : "";
                String apartmentId = invoice.getApartmentId() != null ? invoice.getApartmentId().toString() : "";
                if (!invoiceNumber.contains(lowerSearchText) && !apartmentId.contains(lowerSearchText)) {
                    return false;
                }
            }
            
            // Filter theo status - Đã xóa filterStatusCombo khỏi top bar
            // String filterStatus = filterStatusCombo != null ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status
            if (filterStatus != null && !filterStatus.equals(ALL_LABEL)) {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                if (!statusValue.equals(invoice.getStatus())) {
                    return false;
                }
            }
            
            // Filter theo khoảng tiền
            String amountFrom = filterAmountFromField != null ? filterAmountFromField.getText() : "";
            if (amountFrom != null && !amountFrom.trim().isEmpty()) {
                try {
                    double from = Double.parseDouble(amountFrom.trim());
                    if (invoice.getTotalAmount() == null || invoice.getTotalAmount() < from) {
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
                    if (invoice.getTotalAmount() == null || invoice.getTotalAmount() > to) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            
            // Filter theo phương thức thanh toán
            String filterPaymentMethod = filterPaymentMethodCombo != null ? filterPaymentMethodCombo.getValue() : "Tất cả";
            if (filterPaymentMethod != null && !filterPaymentMethod.equals("Tất cả")) {
                String paymentMethodValue = toValue(PAYMENT_METHOD_OPTIONS, filterPaymentMethod);
                if (!paymentMethodValue.equals(invoice.getPaymentMethod())) {
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
        if (filteredInvoices == null || pagination == null) {
            return;
        }
        
        ObservableList<Invoice> itemsToPaginate = FXCollections.observableArrayList(filteredInvoices);
        PaginationHelper.updatePagination(pagination, invoiceTable, itemsToPaginate, itemsPerPage);
        
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

    private void loadInvoices() {
        try {
            // Đảm bảo invoices list đã được khởi tạo
            if (invoices == null) {
                invoices = FXCollections.observableArrayList();
            }
            if (invoiceTable != null && invoiceTable.getItems() != invoices) {
                invoiceTable.setItems(invoices);
            }

            // Đảm bảo filterStatusCombo đã được khởi tạo - Đã xóa khỏi top bar
            // String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
            //         ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status

            // Load dữ liệu
            List<Invoice> invoiceList;
            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                invoiceList = InvoiceService.getAllInvoices();
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                invoiceList = InvoiceService.getInvoicesByStatus(statusValue);
            }

            System.out.println("Đã lấy được " + (invoiceList != null ? invoiceList.size() : 0) + " hóa đơn từ service");

            // Cập nhật UI trên JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                try {
                    // Lưu tất cả invoices vào allInvoices
                    if (allInvoices == null) {
                        allInvoices = FXCollections.observableArrayList();
                    }
                    allInvoices.clear();
                    if (invoiceList != null && !invoiceList.isEmpty()) {
                        allInvoices.addAll(invoiceList);
                        System.out.println("Đã load " + invoiceList.size() + " hóa đơn vào bảng");
                    } else {
                        System.out.println("Không có dữ liệu hóa đơn nào được trả về từ service!");
                    }

                    // Áp dụng filters và pagination
                    applyFilters();

                    // Update status label
                    if (statusLabel != null) {
                        statusLabel.setText("Đã tải " + allInvoices.size() + " hóa đơn");
                    }

                    System.out.println("Số lượng hóa đơn trong ObservableList: " + allInvoices.size());
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật UI: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("Lỗi khi tải danh sách hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadInvoiceToForm(Invoice invoice) {
        apartmentIdCombo.setValue(invoice.getApartmentId());
        invoiceNumberField.setText(invoice.getInvoiceNumber());
        invoiceDatePicker.setValue(invoice.getInvoiceDate());
        dueDatePicker.setValue(invoice.getDueDate());
        totalAmountField.setText(invoice.getTotalAmount() != null ? invoice.getTotalAmount().toString() : "");
        paidAmountField.setText(invoice.getPaidAmount() != null ? invoice.getPaidAmount().toString() : "");
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, invoice.getStatus()));
        paymentMethodCombo.setValue(toDisplay(PAYMENT_METHOD_OPTIONS, invoice.getPaymentMethod()));
        paymentDatePicker.setValue(invoice.getPaidDate());
        notesArea.setText(invoice.getNotes());
    }

    @FXML
    private void handleFilter() {
        loadInvoices();
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Invoice invoice = new Invoice();
            invoice.setApartmentId(apartmentIdCombo.getValue());
            invoice.setInvoiceNumber(invoiceNumberField.getText().trim());
            invoice.setInvoiceDate(invoiceDatePicker.getValue());
            invoice.setDueDate(dueDatePicker.getValue());
            try {
                invoice.setTotalAmount(Double.parseDouble(totalAmountField.getText().trim()));
                invoice.setPaidAmount(paidAmountField.getText().isEmpty() ? 0.0 : Double.parseDouble(paidAmountField.getText().trim()));
                invoice.setRemainingAmount(invoice.getTotalAmount() - invoice.getPaidAmount());
            } catch (NumberFormatException e) {
                AlertUtils.showWarning("Vui lòng nhập đúng định dạng số!");
                return;
            }
            invoice.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            invoice.setPaymentMethod(toValue(PAYMENT_METHOD_OPTIONS, paymentMethodCombo.getValue()));
            invoice.setPaidDate(paymentDatePicker.getValue());
            invoice.setNotes(notesArea.getText().trim());

            if (InvoiceService.addInvoice(invoice)) {
                AlertUtils.showSuccess("Thêm hóa đơn thành công!");
                clearForm();
                loadInvoices();
            } else {
                AlertUtils.showError("Lỗi khi thêm hóa đơn!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedInvoice != null && validateInput()) {
            selectedInvoice.setApartmentId(apartmentIdCombo.getValue());
            selectedInvoice.setInvoiceNumber(invoiceNumberField.getText().trim());
            selectedInvoice.setInvoiceDate(invoiceDatePicker.getValue());
            selectedInvoice.setDueDate(dueDatePicker.getValue());
            try {
                selectedInvoice.setTotalAmount(Double.parseDouble(totalAmountField.getText().trim()));
                selectedInvoice.setPaidAmount(paidAmountField.getText().isEmpty() ? 0.0 : Double.parseDouble(paidAmountField.getText().trim()));
                selectedInvoice.setRemainingAmount(selectedInvoice.getTotalAmount() - selectedInvoice.getPaidAmount());
            } catch (NumberFormatException e) {
                AlertUtils.showWarning("Vui lòng nhập đúng định dạng số!");
                return;
            }
            selectedInvoice.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedInvoice.setPaymentMethod(toValue(PAYMENT_METHOD_OPTIONS, paymentMethodCombo.getValue()));
            selectedInvoice.setPaidDate(paymentDatePicker.getValue());
            selectedInvoice.setNotes(notesArea.getText().trim());

            if (InvoiceService.updateInvoice(selectedInvoice)) {
                AlertUtils.showSuccess("Cập nhật hóa đơn thành công!");
                clearForm();
                loadInvoices();
            } else {
                AlertUtils.showError("Lỗi khi cập nhật hóa đơn!");
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedInvoice != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa hóa đơn này?");
            alert.setContentText(selectedInvoice.getInvoiceNumber());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (InvoiceService.deleteInvoice(selectedInvoice.getId())) {
                    AlertUtils.showSuccess("Xóa hóa đơn thành công!");
                    clearForm();
                    loadInvoices();
                } else {
                    AlertUtils.showError("Lỗi khi xóa hóa đơn!");
                }
            }
        }
    }

    // @FXML - Đã xóa nút quay lại khỏi top bar
    // private void handleBack() {
    //     ((Stage) invoiceTable.getScene().getWindow()).close();
    // }

    private void clearForm() {
        apartmentIdCombo.setValue(null);
        invoiceNumberField.clear();
        invoiceDatePicker.setValue(LocalDate.now());
        dueDatePicker.setValue(LocalDate.now().plusDays(30));
        totalAmountField.clear();
        paidAmountField.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "CHỜ_THANH_TOÁN"));
        paymentMethodCombo.setValue(null);
        paymentDatePicker.setValue(null);
        notesArea.clear();
        selectedInvoice = null;
        invoiceTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateInput() {
        if (apartmentIdCombo.getValue() == null) {
            AlertUtils.showWarning("Vui lòng chọn căn hộ!");
            return false;
        }
        if (invoiceNumberField.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Vui lòng nhập số hóa đơn!");
            return false;
        }
        if (totalAmountField.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Vui lòng nhập tổng tiền!");
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


