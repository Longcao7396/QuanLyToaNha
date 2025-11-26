package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.CustomerRequest;
import com.example.quanlytoanhanhom4.service.CustomerRequestService;
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

public class CustomerController implements Initializable {
    
    @FXML private TableView<CustomerRequest> requestTable;
    @FXML private TableColumn<CustomerRequest, String> colRequestType;
    @FXML private TableColumn<CustomerRequest, String> colTitle;
    @FXML private TableColumn<CustomerRequest, String> colContent;
    @FXML private TableColumn<CustomerRequest, String> colStatus;
    @FXML private TableColumn<CustomerRequest, String> colPriority;
    
    @FXML private ComboBox<String> filterStatusCombo;
    @FXML private TextField residentIdField;
    @FXML private ComboBox<String> requestTypeCombo;
    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private ComboBox<String> statusCombo;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private TextArea resolutionArea;
    @FXML private Spinner<Integer> ratingSpinner;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button resolveButton;
    @FXML private Button deleteButton;
    @FXML private Label statusLabel;

    private static final LinkedHashMap<String, String> REQUEST_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> PRIORITY_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        REQUEST_TYPE_OPTIONS.put("COMPLAINT", "Khiếu nại");
        REQUEST_TYPE_OPTIONS.put("REQUEST", "Yêu cầu");
        REQUEST_TYPE_OPTIONS.put("FEEDBACK", "Góp ý");
        REQUEST_TYPE_OPTIONS.put("EMERGENCY", "Khẩn cấp");

        STATUS_OPTIONS.put("PENDING", "Chờ xử lý");
        STATUS_OPTIONS.put("IN_PROGRESS", "Đang xử lý");
        STATUS_OPTIONS.put("RESOLVED", "Đã giải quyết");
        STATUS_OPTIONS.put("CLOSED", "Đã đóng");

        PRIORITY_OPTIONS.put("LOW", "Thấp");
        PRIORITY_OPTIONS.put("MEDIUM", "Trung bình");
        PRIORITY_OPTIONS.put("HIGH", "Cao");
        PRIORITY_OPTIONS.put("URGENT", "Khẩn cấp");
    }

    private ObservableList<CustomerRequest> requests;
    private CustomerRequest selectedRequest;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        initializeSpinner();
        loadRequests();
        
        requestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRequest = newSelection;
                loadRequestToForm(newSelection);
                updateButton.setDisable(false);
                resolveButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }
    
    private void initializeTable() {
        colRequestType.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        
        requests = FXCollections.observableArrayList();
        requestTable.setItems(requests);
    }
    
    private void initializeComboBoxes() {
        ObservableList<String> requestTypes = FXCollections.observableArrayList(REQUEST_TYPE_OPTIONS.values());
        requestTypeCombo.setItems(requestTypes);

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
    
    private void initializeSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        ratingSpinner.setValueFactory(valueFactory);
    }
    
    private void loadRequests() {
        try {
            requests.clear();
            String filterStatus = filterStatusCombo.getValue();

            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                requests.addAll(CustomerRequestService.getAllRequests());
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                requests.addAll(CustomerRequestService.getRequestsByStatus(statusValue));
            }

            // Refresh table to ensure data is displayed
            requestTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu yêu cầu khách hàng: " + e.getMessage());
        }
    }
    
    private void loadRequestToForm(CustomerRequest request) {
        residentIdField.setText(String.valueOf(request.getResidentId()));
        requestTypeCombo.setValue(toDisplay(REQUEST_TYPE_OPTIONS, request.getRequestType()));
        titleField.setText(request.getTitle());
        contentArea.setText(request.getContent());
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, request.getStatus()));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, request.getPriority()));
        resolutionArea.setText(request.getResolution());
        if (request.getSatisfactionRating() != null) {
            ratingSpinner.getValueFactory().setValue(request.getSatisfactionRating());
        }
    }
    
    @FXML
    private void handleFilter() {
        loadRequests();
    }
    
    @FXML
    private void handleAdd() {
        if (validateInput()) {
            CustomerRequest request = new CustomerRequest();
            try {
                request.setResidentId(Integer.parseInt(residentIdField.getText().trim()));
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "ID cư dân không hợp lệ!", Alert.AlertType.ERROR);
                return;
            }
            request.setRequestType(toValue(REQUEST_TYPE_OPTIONS, requestTypeCombo.getValue()));
            request.setTitle(titleField.getText().trim());
            request.setContent(contentArea.getText().trim());
            request.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            request.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            
            if (CustomerRequestService.addRequest(request)) {
                statusLabel.setText("✅ Thêm yêu cầu thành công!");
                clearForm();
                loadRequests();
            } else {
                showAlert("Lỗi", "Không thể thêm yêu cầu!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleUpdate() {
        if (selectedRequest == null) return;
        
        if (validateInput()) {
            selectedRequest.setRequestType(toValue(REQUEST_TYPE_OPTIONS, requestTypeCombo.getValue()));
            selectedRequest.setTitle(titleField.getText().trim());
            selectedRequest.setContent(contentArea.getText().trim());
            selectedRequest.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedRequest.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            selectedRequest.setResolution(resolutionArea.getText().trim());
            
            if (CustomerRequestService.updateRequest(selectedRequest)) {
                statusLabel.setText("✅ Cập nhật yêu cầu thành công!");
                clearForm();
                loadRequests();
                resetSelection();
            } else {
                showAlert("Lỗi", "Không thể cập nhật yêu cầu!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleResolve() {
        if (selectedRequest == null) return;
        
        if (CustomerRequestService.resolveRequest(selectedRequest.getId(), 
                resolutionArea.getText().trim(), ratingSpinner.getValue())) {
            statusLabel.setText("✅ Đã giải quyết yêu cầu!");
            clearForm();
            loadRequests();
            resetSelection();
        }
    }
    
    @FXML
    private void handleDelete() {
        if (selectedRequest == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText("Xóa yêu cầu");
        confirm.setContentText("Bạn có chắc chắn muốn xóa yêu cầu này?");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (CustomerRequestService.deleteRequest(selectedRequest.getId())) {
                statusLabel.setText("✅ Xóa yêu cầu thành công!");
                clearForm();
                loadRequests();
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
        Stage stage = (Stage) requestTable.getScene().getWindow();
        stage.close();
    }
    
    private void clearForm() {
        residentIdField.clear();
        requestTypeCombo.setValue(null);
        titleField.clear();
        contentArea.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "PENDING"));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));
        resolutionArea.clear();
        ratingSpinner.getValueFactory().setValue(3);
        statusLabel.setText("");
    }
    
    private void resetSelection() {
        selectedRequest = null;
        requestTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        resolveButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setDisable(false);
    }
    
    private boolean validateInput() {
        if (residentIdField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập ID cư dân!", Alert.AlertType.ERROR);
            return false;
        }
        if (requestTypeCombo.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn loại yêu cầu!", Alert.AlertType.ERROR);
            return false;
        }
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tiêu đề!", Alert.AlertType.ERROR);
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






