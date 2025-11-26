package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Utility;
import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.UtilityService;
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

public class UtilityController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(UtilityController.class);

    @FXML
    private TableView<Utility> utilityTable;
    @FXML
    private TableColumn<Utility, String> colApartmentId;
    @FXML
    private TableColumn<Utility, String> colUtilityType;
    @FXML
    private TableColumn<Utility, Double> colConsumption;
    @FXML
    private TableColumn<Utility, Double> colTotalAmount;
    @FXML
    private TableColumn<Utility, String> colStatus;

    @FXML
    private ComboBox<String> filterTypeCombo;
    @FXML
    private ComboBox<Integer> apartmentIdCombo;
    @FXML
    private ComboBox<String> utilityTypeCombo;
    @FXML
    private TextField previousReadingField;
    @FXML
    private TextField currentReadingField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private Spinner<Integer> periodMonthSpinner;
    @FXML
    private Spinner<Integer> periodYearSpinner;
    @FXML
    private DatePicker dueDatePicker;
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

    private static final LinkedHashMap<String, String> UTILITY_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        UTILITY_TYPE_OPTIONS.put("ĐIỆN", "Điện");
        UTILITY_TYPE_OPTIONS.put("NƯỚC", "Nước");
        UTILITY_TYPE_OPTIONS.put("PHI_DICH_VU", "Phí dịch vụ");
        UTILITY_TYPE_OPTIONS.put("INTERNET", "Internet");
        UTILITY_TYPE_OPTIONS.put("GARAGE", "Gara");

        STATUS_OPTIONS.put("CHỜ_THANH_TOÁN", "Chờ thanh toán");
        STATUS_OPTIONS.put("ĐÃ_THANH_TOÁN", "Đã thanh toán");
        STATUS_OPTIONS.put("QUÁ_HẠN", "Quá hạn");
        STATUS_OPTIONS.put("ĐÃ_HỦY", "Đã hủy");
    }

    private ObservableList<Utility> utilities;
    private Utility selectedUtility;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        initializeSpinners();

        // Delay nhỏ để đảm bảo UI đã sẵn sàng trước khi load dữ liệu
        javafx.application.Platform.runLater(() -> {
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(100));
            pause.setOnFinished(e -> {
                loadUtilities();
            });
            pause.play();
        });

        utilityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedUtility = newSelection;
                loadUtilityToForm(newSelection);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }

    private void initializeTable() {
        colApartmentId.setCellValueFactory(new PropertyValueFactory<>("apartmentId"));
        colUtilityType.setCellValueFactory(new PropertyValueFactory<>("utilityType"));
        colConsumption.setCellValueFactory(new PropertyValueFactory<>("consumption"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        utilities = FXCollections.observableArrayList();
        utilityTable.setItems(utilities);
    }

    private void initializeComboBoxes() {
        ObservableList<String> utilityTypes = FXCollections.observableArrayList(UTILITY_TYPE_OPTIONS.values());
        utilityTypeCombo.setItems(utilityTypes);

        ObservableList<String> filterTypes = FXCollections.observableArrayList(utilityTypes);
        filterTypes.add(0, ALL_LABEL);
        filterTypeCombo.setItems(filterTypes);
        filterTypeCombo.setValue(ALL_LABEL);

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "CHỜ_THANH_TOÁN"));

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
    }

    private void initializeSpinners() {
        periodMonthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, LocalDate.now().getMonthValue()));
        periodYearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2030, LocalDate.now().getYear()));
    }

    private void loadUtilities() {
        try {
            // Đảm bảo utilities list đã được khởi tạo
            if (utilities == null) {
                utilities = FXCollections.observableArrayList();
            }
            if (utilityTable != null && utilityTable.getItems() != utilities) {
                utilityTable.setItems(utilities);
            }

            // Đảm bảo filterTypeCombo đã được khởi tạo
            String filterType = (filterTypeCombo != null && filterTypeCombo.getValue() != null)
                    ? filterTypeCombo.getValue() : ALL_LABEL;

            // Load dữ liệu
            List<Utility> utilityList;
            if (filterType == null || filterType.equals(ALL_LABEL)) {
                utilityList = UtilityService.getAllUtilities();
            } else {
                String typeValue = toValue(UTILITY_TYPE_OPTIONS, filterType);
                utilityList = UtilityService.getUtilitiesByType(typeValue);
            }

            System.out.println("Đã lấy được " + (utilityList != null ? utilityList.size() : 0) + " tiện ích từ service");

            // Cập nhật UI trên JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                try {
                    utilities.clear();
                    if (utilityList != null && !utilityList.isEmpty()) {
                        utilities.addAll(utilityList);
                        System.out.println("Đã load " + utilityList.size() + " tiện ích vào bảng");
                    } else {
                        System.out.println("Không có dữ liệu tiện ích nào được trả về từ service!");
                    }

                    // Refresh table để đảm bảo hiển thị
                    if (utilityTable != null) {
                        utilityTable.refresh();
                        // Force update columns
                        utilityTable.getColumns().forEach(col -> col.setVisible(false));
                        utilityTable.getColumns().forEach(col -> col.setVisible(true));
                        System.out.println("Đã refresh bảng tiện ích, số lượng hiển thị: " + utilityTable.getItems().size());
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật UI: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("Lỗi khi tải danh sách tiện ích: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadUtilityToForm(Utility utility) {
        apartmentIdCombo.setValue(utility.getApartmentId());
        utilityTypeCombo.setValue(toDisplay(UTILITY_TYPE_OPTIONS, utility.getUtilityType()));
        previousReadingField.setText(utility.getPreviousReading() != null ? utility.getPreviousReading().toString() : "");
        currentReadingField.setText(utility.getCurrentReading() != null ? utility.getCurrentReading().toString() : "");
        unitPriceField.setText(utility.getUnitPrice() != null ? utility.getUnitPrice().toString() : "");
        if (utility.getPeriodMonth() != null) {
            periodMonthSpinner.getValueFactory().setValue(utility.getPeriodMonth());
        }
        if (utility.getPeriodYear() != null) {
            periodYearSpinner.getValueFactory().setValue(utility.getPeriodYear());
        }
        dueDatePicker.setValue(utility.getDueDate());
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, utility.getStatus()));
        notesArea.setText(utility.getNotes());
    }

    @FXML
    private void handleFilter() {
        loadUtilities();
    }

    @FXML
    private void handleCalculate() {
        try {
            double previous = previousReadingField.getText().isEmpty() ? 0 : Double.parseDouble(previousReadingField.getText().trim());
            double current = currentReadingField.getText().isEmpty() ? 0 : Double.parseDouble(currentReadingField.getText().trim());
            double unitPrice = unitPriceField.getText().isEmpty() ? 0 : Double.parseDouble(unitPriceField.getText().trim());

            double consumption = current - previous;
            double totalAmount = consumption * unitPrice;

            statusLabel.setText(String.format("Tiêu thụ: %.2f | Tổng tiền: %.2f VNĐ", consumption, totalAmount));
        } catch (NumberFormatException e) {
            statusLabel.setText("Vui lòng nhập đúng định dạng số!");
        }
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Utility utility = new Utility();
            utility.setApartmentId(apartmentIdCombo.getValue());
            utility.setUtilityType(toValue(UTILITY_TYPE_OPTIONS, utilityTypeCombo.getValue()));
            try {
                utility.setPreviousReading(previousReadingField.getText().isEmpty() ? null : Double.parseDouble(previousReadingField.getText().trim()));
                utility.setCurrentReading(currentReadingField.getText().isEmpty() ? null : Double.parseDouble(currentReadingField.getText().trim()));
                utility.setUnitPrice(unitPriceField.getText().isEmpty() ? null : Double.parseDouble(unitPriceField.getText().trim()));

                if (utility.getPreviousReading() != null && utility.getCurrentReading() != null) {
                    utility.setConsumption(utility.getCurrentReading() - utility.getPreviousReading());
                }
                if (utility.getConsumption() != null && utility.getUnitPrice() != null) {
                    utility.setTotalAmount(utility.getConsumption() * utility.getUnitPrice());
                }
            } catch (NumberFormatException e) {
                statusLabel.setText("Vui lòng nhập đúng định dạng số!");
                return;
            }
            utility.setPeriodMonth(periodMonthSpinner.getValue());
            utility.setPeriodYear(periodYearSpinner.getValue());
            utility.setDueDate(dueDatePicker.getValue());
            utility.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            utility.setNotes(notesArea.getText().trim());

            if (UtilityService.addUtility(utility)) {
                statusLabel.setText("Thêm tiện ích thành công!");
                clearForm();
                loadUtilities();
            } else {
                statusLabel.setText("Lỗi khi thêm tiện ích!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedUtility != null && validateInput()) {
            selectedUtility.setApartmentId(apartmentIdCombo.getValue());
            selectedUtility.setUtilityType(toValue(UTILITY_TYPE_OPTIONS, utilityTypeCombo.getValue()));
            try {
                selectedUtility.setPreviousReading(previousReadingField.getText().isEmpty() ? null : Double.parseDouble(previousReadingField.getText().trim()));
                selectedUtility.setCurrentReading(currentReadingField.getText().isEmpty() ? null : Double.parseDouble(currentReadingField.getText().trim()));
                selectedUtility.setUnitPrice(unitPriceField.getText().isEmpty() ? null : Double.parseDouble(unitPriceField.getText().trim()));

                if (selectedUtility.getPreviousReading() != null && selectedUtility.getCurrentReading() != null) {
                    selectedUtility.setConsumption(selectedUtility.getCurrentReading() - selectedUtility.getPreviousReading());
                }
                if (selectedUtility.getConsumption() != null && selectedUtility.getUnitPrice() != null) {
                    selectedUtility.setTotalAmount(selectedUtility.getConsumption() * selectedUtility.getUnitPrice());
                }
            } catch (NumberFormatException e) {
                statusLabel.setText("Vui lòng nhập đúng định dạng số!");
                return;
            }
            selectedUtility.setPeriodMonth(periodMonthSpinner.getValue());
            selectedUtility.setPeriodYear(periodYearSpinner.getValue());
            selectedUtility.setDueDate(dueDatePicker.getValue());
            selectedUtility.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedUtility.setNotes(notesArea.getText().trim());

            if (UtilityService.updateUtility(selectedUtility)) {
                statusLabel.setText("Cập nhật tiện ích thành công!");
                clearForm();
                loadUtilities();
            } else {
                statusLabel.setText("Lỗi khi cập nhật tiện ích!");
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedUtility != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa tiện ích này?");

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (UtilityService.deleteUtility(selectedUtility.getId())) {
                    statusLabel.setText("Xóa tiện ích thành công!");
                    clearForm();
                    loadUtilities();
                } else {
                    statusLabel.setText("Lỗi khi xóa tiện ích!");
                }
            }
        }
    }

    @FXML
    private void handleBack() {
        ((Stage) utilityTable.getScene().getWindow()).close();
    }

    private void clearForm() {
        apartmentIdCombo.setValue(null);
        utilityTypeCombo.setValue(null);
        previousReadingField.clear();
        currentReadingField.clear();
        unitPriceField.clear();
        periodMonthSpinner.getValueFactory().setValue(LocalDate.now().getMonthValue());
        periodYearSpinner.getValueFactory().setValue(LocalDate.now().getYear());
        dueDatePicker.setValue(null);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "CHỜ_THANH_TOÁN"));
        notesArea.clear();
        selectedUtility = null;
        utilityTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateInput() {
        if (apartmentIdCombo.getValue() == null) {
            statusLabel.setText("Vui lòng chọn căn hộ!");
            return false;
        }
        if (utilityTypeCombo.getValue() == null) {
            statusLabel.setText("Vui lòng chọn loại tiện ích!");
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


