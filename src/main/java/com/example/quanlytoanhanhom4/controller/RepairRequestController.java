package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.RepairRequest;
import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.ResidentService;
import com.example.quanlytoanhanhom4.service.RepairRequestService;
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

public class RepairRequestController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(RepairRequestController.class);

    @FXML
    private TableView<RepairRequest> repairTable;
    @FXML
    private TableColumn<RepairRequest, String> colTitle;
    @FXML
    private TableColumn<RepairRequest, String> colRepairType;
    @FXML
    private TableColumn<RepairRequest, String> colPriority;
    @FXML
    private TableColumn<RepairRequest, String> colStatus;

    @FXML
    private ComboBox<String> filterStatusCombo;
    @FXML
    private ComboBox<Integer> apartmentIdCombo;
    @FXML
    private ComboBox<Integer> residentIdCombo;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<String> repairTypeCombo;
    @FXML
    private ComboBox<String> priorityCombo;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private DatePicker requestedDatePicker;
    @FXML
    private DatePicker scheduledDatePicker;
    @FXML
    private DatePicker completedDatePicker;
    @FXML
    private TextField estimatedCostField;
    @FXML
    private TextField actualCostField;
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

    private static final LinkedHashMap<String, String> REPAIR_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> PRIORITY_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        REPAIR_TYPE_OPTIONS.put("PLUMBING", "Đường ống nước");
        REPAIR_TYPE_OPTIONS.put("ELECTRICAL", "Điện");
        REPAIR_TYPE_OPTIONS.put("HVAC", "Điều hòa");
        REPAIR_TYPE_OPTIONS.put("ELEVATOR", "Thang máy");
        REPAIR_TYPE_OPTIONS.put("DOOR", "Cửa");
        REPAIR_TYPE_OPTIONS.put("WINDOW", "Cửa sổ");
        REPAIR_TYPE_OPTIONS.put("WALL", "Tường");
        REPAIR_TYPE_OPTIONS.put("FLOOR", "Sàn");
        REPAIR_TYPE_OPTIONS.put("OTHER", "Khác");

        PRIORITY_OPTIONS.put("LOW", "Thấp");
        PRIORITY_OPTIONS.put("MEDIUM", "Trung bình");
        PRIORITY_OPTIONS.put("HIGH", "Cao");
        PRIORITY_OPTIONS.put("URGENT", "Khẩn cấp");

        STATUS_OPTIONS.put("PENDING", "Chờ xử lý");
        STATUS_OPTIONS.put("IN_PROGRESS", "Đang xử lý");
        STATUS_OPTIONS.put("COMPLETED", "Hoàn thành");
        STATUS_OPTIONS.put("CANCELLED", "Đã hủy");
    }

    private ObservableList<RepairRequest> repairRequests;
    private RepairRequest selectedRepairRequest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();

        // Delay nhỏ để đảm bảo UI đã sẵn sàng trước khi load dữ liệu
        javafx.application.Platform.runLater(() -> {
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(100));
            pause.setOnFinished(e -> {
                loadRepairRequests();
            });
            pause.play();
        });

        repairTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRepairRequest = newSelection;
                loadRepairRequestToForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });

        requestedDatePicker.setValue(LocalDate.now());
    }

    private void initializeTable() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colRepairType.setCellValueFactory(new PropertyValueFactory<>("repairType"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        repairRequests = FXCollections.observableArrayList();
        repairTable.setItems(repairRequests);
    }

    private void initializeComboBoxes() {
        ObservableList<String> repairTypes = FXCollections.observableArrayList(REPAIR_TYPE_OPTIONS.values());
        repairTypeCombo.setItems(repairTypes);

        ObservableList<String> priorities = FXCollections.observableArrayList(PRIORITY_OPTIONS.values());
        priorityCombo.setItems(priorities);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "PENDING"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);

        // Load apartment IDs and resident IDs from service
        try {
            List<Integer> apartmentIds = ApartmentService.getAllApartments().stream()
                    .map(com.example.quanlytoanhanhom4.model.Apartment::getId)
                    .collect(java.util.stream.Collectors.toList());
            apartmentIdCombo.setItems(FXCollections.observableArrayList(apartmentIds));
        } catch (Exception e) {
            logger.error("Lỗi khi load apartment IDs: {}", e.getMessage());
            apartmentIdCombo.setItems(FXCollections.observableArrayList());
        }
        try {
            List<Integer> residentIds = ResidentService.getAllResidents().stream()
                    .map(com.example.quanlytoanhanhom4.model.Resident::getId)
                    .collect(java.util.stream.Collectors.toList());
            residentIdCombo.setItems(FXCollections.observableArrayList(residentIds));
        } catch (Exception e) {
            logger.error("Lỗi khi load resident IDs: {}", e.getMessage());
            residentIdCombo.setItems(FXCollections.observableArrayList());
        }
    }

    private void loadRepairRequests() {
        try {
            // Đảm bảo repairRequests list đã được khởi tạo
            if (repairRequests == null) {
                repairRequests = FXCollections.observableArrayList();
            }
            if (repairTable != null && repairTable.getItems() != repairRequests) {
                repairTable.setItems(repairRequests);
            }

            // Đảm bảo filterStatusCombo đã được khởi tạo
            String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
                    ? filterStatusCombo.getValue() : ALL_LABEL;

            // Load dữ liệu
            List<RepairRequest> requestList;
            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                requestList = RepairRequestService.getAllRepairRequests();
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                requestList = RepairRequestService.getRepairRequestsByStatus(statusValue);
            }

            System.out.println("Đã lấy được " + (requestList != null ? requestList.size() : 0) + " yêu cầu sửa chữa từ service");

            // Cập nhật UI trên JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                try {
                    repairRequests.clear();
                    if (requestList != null && !requestList.isEmpty()) {
                        repairRequests.addAll(requestList);
                        System.out.println("Đã load " + requestList.size() + " yêu cầu sửa chữa vào bảng");
                    } else {
                        System.out.println("Không có dữ liệu yêu cầu sửa chữa nào được trả về từ service!");
                    }

                    // Refresh table để đảm bảo hiển thị
                    if (repairTable != null) {
                        repairTable.refresh();
                        // Force update columns
                        repairTable.getColumns().forEach(col -> col.setVisible(false));
                        repairTable.getColumns().forEach(col -> col.setVisible(true));
                        System.out.println("Đã refresh bảng yêu cầu sửa chữa, số lượng hiển thị: " + repairTable.getItems().size());
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật UI: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("Lỗi khi tải danh sách yêu cầu sửa chữa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRepairRequestToForm(RepairRequest request) {
        apartmentIdCombo.setValue(request.getApartmentId());
        residentIdCombo.setValue(request.getResidentId());
        titleField.setText(request.getTitle());
        descriptionArea.setText(request.getDescription());
        repairTypeCombo.setValue(toDisplay(REPAIR_TYPE_OPTIONS, request.getRepairType()));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, request.getPriority()));
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, request.getStatus()));
        requestedDatePicker.setValue(request.getRequestedDate());
        scheduledDatePicker.setValue(request.getScheduledDate());
        completedDatePicker.setValue(request.getCompletedDate());
        estimatedCostField.setText(request.getEstimatedCost() != null ? request.getEstimatedCost().toString() : "");
        actualCostField.setText(request.getActualCost() != null ? request.getActualCost().toString() : "");
        notesArea.setText(request.getNotes());
    }

    @FXML
    private void handleFilter() {
        loadRepairRequests();
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            RepairRequest request = new RepairRequest();
            request.setApartmentId(apartmentIdCombo.getValue());
            request.setResidentId(residentIdCombo.getValue());
            request.setTitle(titleField.getText().trim());
            request.setDescription(descriptionArea.getText().trim());
            request.setRepairType(toValue(REPAIR_TYPE_OPTIONS, repairTypeCombo.getValue()));
            request.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            request.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            request.setRequestedDate(requestedDatePicker.getValue());
            request.setScheduledDate(scheduledDatePicker.getValue());
            request.setCompletedDate(completedDatePicker.getValue());
            try {
                request.setEstimatedCost(estimatedCostField.getText().isEmpty() ? null : Double.parseDouble(estimatedCostField.getText().trim()));
                request.setActualCost(actualCostField.getText().isEmpty() ? null : Double.parseDouble(actualCostField.getText().trim()));
            } catch (NumberFormatException e) {
                statusLabel.setText("Vui lòng nhập đúng định dạng số!");
                return;
            }
            request.setNotes(notesArea.getText().trim());

            if (RepairRequestService.addRepairRequest(request)) {
                statusLabel.setText("Thêm yêu cầu sửa chữa thành công!");
                clearForm();
                loadRepairRequests();
            } else {
                statusLabel.setText("Lỗi khi thêm yêu cầu sửa chữa!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedRepairRequest != null && validateInput()) {
            selectedRepairRequest.setApartmentId(apartmentIdCombo.getValue());
            selectedRepairRequest.setResidentId(residentIdCombo.getValue());
            selectedRepairRequest.setTitle(titleField.getText().trim());
            selectedRepairRequest.setDescription(descriptionArea.getText().trim());
            selectedRepairRequest.setRepairType(toValue(REPAIR_TYPE_OPTIONS, repairTypeCombo.getValue()));
            selectedRepairRequest.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            selectedRepairRequest.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedRepairRequest.setRequestedDate(requestedDatePicker.getValue());
            selectedRepairRequest.setScheduledDate(scheduledDatePicker.getValue());
            selectedRepairRequest.setCompletedDate(completedDatePicker.getValue());
            try {
                selectedRepairRequest.setEstimatedCost(estimatedCostField.getText().isEmpty() ? null : Double.parseDouble(estimatedCostField.getText().trim()));
                selectedRepairRequest.setActualCost(actualCostField.getText().isEmpty() ? null : Double.parseDouble(actualCostField.getText().trim()));
            } catch (NumberFormatException e) {
                statusLabel.setText("Vui lòng nhập đúng định dạng số!");
                return;
            }
            selectedRepairRequest.setNotes(notesArea.getText().trim());

            if (RepairRequestService.updateRepairRequest(selectedRepairRequest)) {
                statusLabel.setText("Cập nhật yêu cầu sửa chữa thành công!");
                clearForm();
                loadRepairRequests();
            } else {
                statusLabel.setText("Lỗi khi cập nhật yêu cầu sửa chữa!");
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedRepairRequest != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa yêu cầu sửa chữa này?");
            alert.setContentText(selectedRepairRequest.getTitle());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (RepairRequestService.deleteRepairRequest(selectedRepairRequest.getId())) {
                    statusLabel.setText("Xóa yêu cầu sửa chữa thành công!");
                    clearForm();
                    loadRepairRequests();
                } else {
                    statusLabel.setText("Lỗi khi xóa yêu cầu sửa chữa!");
                }
            }
        }
    }

    @FXML
    private void handleBack() {
        ((Stage) repairTable.getScene().getWindow()).close();
    }

    private void clearForm() {
        apartmentIdCombo.setValue(null);
        residentIdCombo.setValue(null);
        titleField.clear();
        descriptionArea.clear();
        repairTypeCombo.setValue(null);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "MEDIUM"));
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "PENDING"));
        requestedDatePicker.setValue(LocalDate.now());
        scheduledDatePicker.setValue(null);
        completedDatePicker.setValue(null);
        estimatedCostField.clear();
        actualCostField.clear();
        notesArea.clear();
        selectedRepairRequest = null;
        repairTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateInput() {
        if (apartmentIdCombo.getValue() == null) {
            statusLabel.setText("Vui lòng chọn căn hộ!");
            return false;
        }
        if (residentIdCombo.getValue() == null) {
            statusLabel.setText("Vui lòng chọn cư dân!");
            return false;
        }
        if (titleField.getText().trim().isEmpty()) {
            statusLabel.setText("Vui lòng nhập tiêu đề!");
            return false;
        }
        if (descriptionArea.getText().trim().isEmpty()) {
            statusLabel.setText("Vui lòng nhập mô tả!");
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


