package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Apartment;
import com.example.quanlytoanhanhom4.service.ApartmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML
    private ComboBox<String> filterStatusCombo;
    @FXML
    private TextField apartmentNoField;
    @FXML
    private Spinner<Integer> floorNumberSpinner;
    @FXML
    private TextField buildingBlockField;
    @FXML
    private Spinner<Integer> numberOfRoomsSpinner;
    @FXML
    private Spinner<Integer> numberOfPeopleSpinner;
    @FXML
    private TextField areaField;
    @FXML
    private TextField priceField;
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

    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        STATUS_OPTIONS.put("OCCUPIED", "Đã cho thuê");
        STATUS_OPTIONS.put("VACANT", "Trống");
        STATUS_OPTIONS.put("RESERVED", "Đã đặt cọc");
        STATUS_OPTIONS.put("MAINTENANCE", "Đang bảo trì");
    }

    private ObservableList<Apartment> apartments;
    private Apartment selectedApartment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("========================================");
        System.out.println("KHỞI TẠO APARTMENT CONTROLLER");
        System.out.println("========================================");

        initializeTable();
        initializeComboBoxes();
        initializeSpinners();

        System.out.println("✓ Đã khởi tạo table, comboboxes, spinners");

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
    }

    private void initializeComboBoxes() {
        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "VACANT"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);
    }

    private void initializeSpinners() {
        floorNumberSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        numberOfRoomsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        numberOfPeopleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
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

            String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
                    ? filterStatusCombo.getValue() : ALL_LABEL;
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
                    apartments.clear();
                    if (apartmentList != null && !apartmentList.isEmpty()) {
                        apartments.addAll(apartmentList);
                        System.out.println("Đã load " + apartmentList.size() + " căn hộ vào bảng");
                    } else {
                        System.out.println("CẢNH BÁO: Không có dữ liệu căn hộ nào được trả về từ service!");
                    }

                    // Refresh table to ensure data is displayed
                    if (apartmentTable != null) {
                        apartmentTable.refresh();
                        // Force update columns
                        apartmentTable.getColumns().forEach(col -> col.setVisible(false));
                        apartmentTable.getColumns().forEach(col -> col.setVisible(true));
                        System.out.println("Đã refresh bảng căn hộ");
                    }

                    // Update status label
                    if (statusLabel != null) {
                        statusLabel.setText("Đã tải " + apartments.size() + " căn hộ");
                    }

                    System.out.println("Số lượng căn hộ trong ObservableList: " + apartments.size());
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật UI: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu căn hộ: " + e.getMessage());
            javafx.application.Platform.runLater(() -> {
                if (statusLabel != null) {
                    statusLabel.setText("Lỗi khi tải dữ liệu: " + e.getMessage());
                }
            });
        }
    }

    private void loadApartmentToForm(Apartment apartment) {
        apartmentNoField.setText(apartment.getApartmentNo());
        if (apartment.getFloorNumber() != null) {
            floorNumberSpinner.getValueFactory().setValue(apartment.getFloorNumber());
        }
        buildingBlockField.setText(apartment.getBuildingBlock());
        if (apartment.getNumberOfRooms() != null) {
            numberOfRoomsSpinner.getValueFactory().setValue(apartment.getNumberOfRooms());
        }
        if (apartment.getNumberOfPeople() != null) {
            numberOfPeopleSpinner.getValueFactory().setValue(apartment.getNumberOfPeople());
        }
        areaField.setText(apartment.getArea() != null ? apartment.getArea().toString() : "");
        priceField.setText(apartment.getPrice() != null ? apartment.getPrice().toString() : "");
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, apartment.getStatus()));
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
            apartment.setFloorNumber(floorNumberSpinner.getValue());
            apartment.setBuildingBlock(buildingBlockField.getText().trim());
            apartment.setNumberOfRooms(numberOfRoomsSpinner.getValue());
            apartment.setNumberOfPeople(numberOfPeopleSpinner.getValue());
            try {
                apartment.setArea(Double.parseDouble(areaField.getText().trim()));
            } catch (NumberFormatException e) {
                apartment.setArea(null);
            }
            try {
                apartment.setPrice(Double.parseDouble(priceField.getText().trim()));
            } catch (NumberFormatException e) {
                apartment.setPrice(null);
            }
            apartment.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));

            if (ApartmentService.addApartment(apartment)) {
                statusLabel.setText("Thêm căn hộ thành công!");
                clearForm();
                loadApartments();
            } else {
                statusLabel.setText("Lỗi khi thêm căn hộ!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedApartment != null && validateInput()) {
            selectedApartment.setApartmentNo(apartmentNoField.getText().trim());
            selectedApartment.setFloorNumber(floorNumberSpinner.getValue());
            selectedApartment.setBuildingBlock(buildingBlockField.getText().trim());
            selectedApartment.setNumberOfRooms(numberOfRoomsSpinner.getValue());
            selectedApartment.setNumberOfPeople(numberOfPeopleSpinner.getValue());
            try {
                selectedApartment.setArea(Double.parseDouble(areaField.getText().trim()));
            } catch (NumberFormatException e) {
                selectedApartment.setArea(null);
            }
            try {
                selectedApartment.setPrice(Double.parseDouble(priceField.getText().trim()));
            } catch (NumberFormatException e) {
                selectedApartment.setPrice(null);
            }
            selectedApartment.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));

            if (ApartmentService.updateApartment(selectedApartment)) {
                statusLabel.setText("Cập nhật căn hộ thành công!");
                clearForm();
                loadApartments();
            } else {
                statusLabel.setText("Lỗi khi cập nhật căn hộ!");
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
                    statusLabel.setText("Xóa căn hộ thành công!");
                    clearForm();
                    loadApartments();
                } else {
                    statusLabel.setText("Lỗi khi xóa căn hộ!");
                }
            }
        }
    }

    @FXML
    private void handleBack() {
        ((Stage) apartmentTable.getScene().getWindow()).close();
    }

    private void clearForm() {
        apartmentNoField.clear();
        floorNumberSpinner.getValueFactory().setValue(1);
        buildingBlockField.clear();
        numberOfRoomsSpinner.getValueFactory().setValue(1);
        numberOfPeopleSpinner.getValueFactory().setValue(1);
        areaField.clear();
        priceField.clear();
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "VACANT"));
        selectedApartment = null;
        apartmentTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateInput() {
        if (apartmentNoField.getText().trim().isEmpty()) {
            statusLabel.setText("Vui lòng nhập số căn hộ!");
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


