package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Invoice;
import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.InvoiceService;
import com.example.quanlytoanhanhom4.util.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private TableColumn<Invoice, LocalDate> colInvoiceDate;
    @FXML
    private TableColumn<Invoice, Double> colTotalAmount;
    @FXML
    private TableColumn<Invoice, Double> colPaidAmount;
    @FXML
    private TableColumn<Invoice, String> colStatus;

    @FXML
    private ComboBox<String> filterStatusCombo;
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
        STATUS_OPTIONS.put("PENDING", "Chờ thanh toán");
        STATUS_OPTIONS.put("PARTIAL", "Thanh toán một phần");
        STATUS_OPTIONS.put("PAID", "Đã thanh toán");
        STATUS_OPTIONS.put("OVERDUE", "Quá hạn");
        STATUS_OPTIONS.put("CANCELLED", "Đã hủy");

        PAYMENT_METHOD_OPTIONS.put("CASH", "Tiền mặt");
        PAYMENT_METHOD_OPTIONS.put("BANK_TRANSFER", "Chuyển khoản");
        PAYMENT_METHOD_OPTIONS.put("CARD", "Thẻ");
        PAYMENT_METHOD_OPTIONS.put("OTHER", "Khác");
    }

    private ObservableList<Invoice> invoices;
    private Invoice selectedInvoice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();

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
        colInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        invoices = FXCollections.observableArrayList();
        invoiceTable.setItems(invoices);
    }

    private void initializeComboBoxes() {
        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "PENDING"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);

        ObservableList<String> paymentMethods = FXCollections.observableArrayList(PAYMENT_METHOD_OPTIONS.values());
        paymentMethodCombo.setItems(paymentMethods);

        // Load apartment IDs from service
        try {
            List<Integer> apartmentIds = ApartmentService.getAllApartments().stream()
                    .map(com.example.quanlytoanhanhom4.model.Apartment::getId)
                    .collect(java.util.stream.Collectors.toList());
            apartmentIdCombo.setItems(FXCollections.observableArrayList(apartmentIds));
        } catch (Exception e) {
            logger.error("Lỗi khi load apartment IDs: {}", e.getMessage());
            apartmentIdCombo.setItems(FXCollections.observableArrayList());
        }

        invoiceDatePicker.setValue(LocalDate.now());
        dueDatePicker.setValue(LocalDate.now().plusDays(30));
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

            // Đảm bảo filterStatusCombo đã được khởi tạo
            String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
                    ? filterStatusCombo.getValue() : ALL_LABEL;

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
                    invoices.clear();
                    if (invoiceList != null && !invoiceList.isEmpty()) {
                        invoices.addAll(invoiceList);
                        System.out.println("Đã load " + invoiceList.size() + " hóa đơn vào bảng");
                    } else {
                        System.out.println("Không có dữ liệu hóa đơn nào được trả về từ service!");
                    }

                    // Refresh table để đảm bảo hiển thị
                    if (invoiceTable != null) {
                        invoiceTable.refresh();
                        // Force update columns
                        invoiceTable.getColumns().forEach(col -> col.setVisible(false));
                        invoiceTable.getColumns().forEach(col -> col.setVisible(true));
                        System.out.println("Đã refresh bảng hóa đơn, số lượng hiển thị: " + invoiceTable.getItems().size());
                    }
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
        paymentDatePicker.setValue(invoice.getPaymentDate());
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
                statusLabel.setText("Vui lòng nhập đúng định dạng số!");
                return;
            }
            invoice.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            invoice.setPaymentMethod(toValue(PAYMENT_METHOD_OPTIONS, paymentMethodCombo.getValue()));
            invoice.setPaymentDate(paymentDatePicker.getValue());
            invoice.setNotes(notesArea.getText().trim());
            invoice.setCreatedBy(UserSession.getCurrentUserId() != null ? UserSession.getCurrentUserId() : 1);

            if (InvoiceService.addInvoice(invoice)) {
                statusLabel.setText("Thêm hóa đơn thành công!");
                clearForm();
                loadInvoices();
            } else {
                statusLabel.setText("Lỗi khi thêm hóa đơn!");
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
                statusLabel.setText("Vui lòng nhập đúng định dạng số!");
                return;
            }
            selectedInvoice.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedInvoice.setPaymentMethod(toValue(PAYMENT_METHOD_OPTIONS, paymentMethodCombo.getValue()));
            selectedInvoice.setPaymentDate(paymentDatePicker.getValue());
            selectedInvoice.setNotes(notesArea.getText().trim());

            if (InvoiceService.updateInvoice(selectedInvoice)) {
                statusLabel.setText("Cập nhật hóa đơn thành công!");
                clearForm();
                loadInvoices();
            } else {
                statusLabel.setText("Lỗi khi cập nhật hóa đơn!");
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
                    statusLabel.setText("Xóa hóa đơn thành công!");
                    clearForm();
                    loadInvoices();
                } else {
                    statusLabel.setText("Lỗi khi xóa hóa đơn!");
                }
            }
        }
    }

    @FXML
    private void handleBack() {
        ((Stage) invoiceTable.getScene().getWindow()).close();
    }

    private void clearForm() {
        apartmentIdCombo.setValue(null);
        invoiceNumberField.clear();
        invoiceDatePicker.setValue(LocalDate.now());
        dueDatePicker.setValue(LocalDate.now().plusDays(30));
        totalAmountField.clear();
        paidAmountField.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "PENDING"));
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
            statusLabel.setText("Vui lòng chọn căn hộ!");
            return false;
        }
        if (invoiceNumberField.getText().trim().isEmpty()) {
            statusLabel.setText("Vui lòng nhập số hóa đơn!");
            return false;
        }
        if (totalAmountField.getText().trim().isEmpty()) {
            statusLabel.setText("Vui lòng nhập tổng tiền!");
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


