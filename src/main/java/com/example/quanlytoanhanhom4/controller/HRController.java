package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.AttendanceRecord;
import com.example.quanlytoanhanhom4.model.Contract;
import com.example.quanlytoanhanhom4.model.ShiftSchedule;
import com.example.quanlytoanhanhom4.model.Staff;
import com.example.quanlytoanhanhom4.service.AttendanceService;
import com.example.quanlytoanhanhom4.service.ContractService;
import com.example.quanlytoanhanhom4.service.ShiftScheduleService;
import com.example.quanlytoanhanhom4.service.StaffService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HRController implements Initializable {

    // Staff tab
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, String> colStaffCode;
    @FXML private TableColumn<Staff, String> colStaffName;
    @FXML private TableColumn<Staff, String> colStaffPosition;
    @FXML private TableColumn<Staff, String> colStaffDepartment;
    @FXML private TableColumn<Staff, String> colStaffPhone;
    @FXML private TableColumn<Staff, String> colStaffStatus;
    @FXML private TextField staffCodeField;
    @FXML private TextField staffNameField;
    @FXML private TextField positionField;
    @FXML private TextField departmentField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private DatePicker staffStartDatePicker;
    @FXML private ComboBox<String> staffStatusCombo;
    @FXML private TextField salaryField;
    @FXML private TextArea staffNotesArea;

    // Attendance tab
    @FXML private TableView<AttendanceRecord> attendanceTable;
    @FXML private TableColumn<AttendanceRecord, String> colAttendanceStaff;
    @FXML private TableColumn<AttendanceRecord, LocalDate> colAttendanceDate;
    @FXML private TableColumn<AttendanceRecord, String> colAttendanceShift;
    @FXML private TableColumn<AttendanceRecord, String> colAttendanceCheckIn;
    @FXML private TableColumn<AttendanceRecord, String> colAttendanceCheckOut;
    @FXML private TableColumn<AttendanceRecord, String> colAttendanceStatus;
    @FXML private ComboBox<Staff> attendanceStaffCombo;
    @FXML private DatePicker attendanceDatePicker;
    @FXML private ComboBox<String> attendanceShiftCombo;
    @FXML private TextField checkInField;
    @FXML private TextField checkOutField;
    @FXML private ComboBox<String> attendanceStatusCombo;
    @FXML private TextArea attendanceNotesArea;

    // Shift tab
    @FXML private TableView<ShiftSchedule> shiftTable;
    @FXML private TableColumn<ShiftSchedule, String> colShiftStaff;
    @FXML private TableColumn<ShiftSchedule, String> colShiftName;
    @FXML private TableColumn<ShiftSchedule, LocalDate> colShiftStart;
    @FXML private TableColumn<ShiftSchedule, LocalDate> colShiftEnd;
    @FXML private TableColumn<ShiftSchedule, String> colShiftAssignedBy;
    @FXML private ComboBox<Staff> shiftStaffCombo;
    @FXML private TextField shiftNameField;
    @FXML private DatePicker shiftStartDatePicker;
    @FXML private DatePicker shiftEndDatePicker;
    @FXML private TextField assignedByField;
    @FXML private TextArea shiftNotesArea;

    // Contract tab
    @FXML private TableView<Contract> contractTable;
    @FXML private TableColumn<Contract, String> colContractStaff;
    @FXML private TableColumn<Contract, String> colContractType;
    @FXML private TableColumn<Contract, LocalDate> colContractStart;
    @FXML private TableColumn<Contract, LocalDate> colContractEnd;
    @FXML private TableColumn<Contract, Double> colContractSalary;
    @FXML private TableColumn<Contract, String> colContractStatus;
    @FXML private ComboBox<Staff> contractStaffCombo;
    @FXML private TextField contractTypeField;
    @FXML private DatePicker contractStartDatePicker;
    @FXML private DatePicker contractEndDatePicker;
    @FXML private TextField contractSalaryField;
    @FXML private ComboBox<String> contractStatusCombo;
    @FXML private TextArea contractDescriptionArea;

    private final ObservableList<Staff> staffData = FXCollections.observableArrayList();
    private final ObservableList<AttendanceRecord> attendanceData = FXCollections.observableArrayList();
    private final ObservableList<ShiftSchedule> shiftData = FXCollections.observableArrayList();
    private final ObservableList<Contract> contractData = FXCollections.observableArrayList();

    private Staff selectedStaff;
    private AttendanceRecord selectedAttendanceRecord;
    private ShiftSchedule selectedShift;
    private Contract selectedContract;

    // Vietnamese label mappings
    private static final LinkedHashMap<String, String> STAFF_STATUS_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> ATTENDANCE_STATUS_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> CONTRACT_STATUS_OPTIONS = new LinkedHashMap<>();

    static {
        STAFF_STATUS_OPTIONS.put("ACTIVE", "Đang làm việc");
        STAFF_STATUS_OPTIONS.put("ON_LEAVE", "Nghỉ phép");
        STAFF_STATUS_OPTIONS.put("INACTIVE", "Ngừng làm việc");

        ATTENDANCE_STATUS_OPTIONS.put("PRESENT", "Có mặt");
        ATTENDANCE_STATUS_OPTIONS.put("LATE", "Đi muộn");
        ATTENDANCE_STATUS_OPTIONS.put("ABSENT", "Vắng mặt");
        ATTENDANCE_STATUS_OPTIONS.put("REMOTE", "Làm việc từ xa");

        CONTRACT_STATUS_OPTIONS.put("ACTIVE", "Đang hiệu lực");
        CONTRACT_STATUS_OPTIONS.put("EXPIRED", "Hết hạn");
        CONTRACT_STATUS_OPTIONS.put("TERMINATED", "Đã chấm dứt");
        CONTRACT_STATUS_OPTIONS.put("ON_HOLD", "Tạm hoãn");
    }

    private String toDisplay(LinkedHashMap<String, String> map, String value) {
        if (value == null) return null;
        return map.getOrDefault(value, value);
    }

    private String toValue(LinkedHashMap<String, String> map, String display) {
        if (display == null) return null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(display)) {
                return entry.getKey();
            }
        }
        return display;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStaffTab();
        initializeAttendanceTab();
        initializeShiftTab();
        initializeContractTab();
        loadStaff();
        loadAttendance();
        loadShifts();
        loadContracts();
    }

    private void initializeStaffTab() {
        colStaffCode.setCellValueFactory(new PropertyValueFactory<>("staffCode"));
        colStaffName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colStaffPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colStaffDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colStaffPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colStaffStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        staffTable.setItems(staffData);

        staffStatusCombo.setItems(FXCollections.observableArrayList(STAFF_STATUS_OPTIONS.values()));
        staffStatusCombo.setValue(toDisplay(STAFF_STATUS_OPTIONS, "ACTIVE"));

        staffTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedStaff = newSelection;
            if (newSelection != null) {
                loadStaffToForm(newSelection);
            }
        });
    }

    private void initializeAttendanceTab() {
        colAttendanceStaff.setCellValueFactory(cell -> javafx.beans.binding.Bindings.createObjectBinding(cell.getValue()::getStaffName));
        colAttendanceDate.setCellValueFactory(new PropertyValueFactory<>("attendanceDate"));
        colAttendanceShift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        colAttendanceCheckIn.setCellValueFactory(cell -> javafx.beans.binding.Bindings.createObjectBinding(() -> formatTime(cell.getValue().getCheckIn())));
        colAttendanceCheckOut.setCellValueFactory(cell -> javafx.beans.binding.Bindings.createObjectBinding(() -> formatTime(cell.getValue().getCheckOut())));
        colAttendanceStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        attendanceTable.setItems(attendanceData);

        attendanceShiftCombo.setItems(FXCollections.observableArrayList("SÁNG", "CHIỀU", "ĐÊM"));
        attendanceStatusCombo.setItems(FXCollections.observableArrayList(ATTENDANCE_STATUS_OPTIONS.values()));

        attendanceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedAttendanceRecord = newSel;
            if (newSel != null) {
                loadAttendanceToForm(newSel);
            }
        });
    }

    private void initializeShiftTab() {
        colShiftStaff.setCellValueFactory(cell -> javafx.beans.binding.Bindings.createObjectBinding(cell.getValue()::getStaffName));
        colShiftName.setCellValueFactory(new PropertyValueFactory<>("shiftName"));
        colShiftStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colShiftEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colShiftAssignedBy.setCellValueFactory(new PropertyValueFactory<>("assignedBy"));
        shiftTable.setItems(shiftData);

        shiftTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedShift = newSel;
            if (newSel != null) {
                loadShiftToForm(newSel);
            }
        });
    }

    private void initializeContractTab() {
        colContractStaff.setCellValueFactory(cell -> javafx.beans.binding.Bindings.createObjectBinding(cell.getValue()::getStaffName));
        colContractType.setCellValueFactory(new PropertyValueFactory<>("contractType"));
        colContractStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colContractEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colContractSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colContractStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        contractTable.setItems(contractData);

        contractStatusCombo.setItems(FXCollections.observableArrayList(CONTRACT_STATUS_OPTIONS.values()));

        contractTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedContract = newSel;
            if (newSel != null) {
                loadContractToForm(newSel);
            }
        });
    }

    private void loadStaff() {
        staffData.setAll(StaffService.getAllStaff());
        refreshStaffCombos();
    }

    private void loadAttendance() {
        attendanceData.setAll(AttendanceService.getAllRecords());
    }

    private void loadShifts() {
        shiftData.setAll(ShiftScheduleService.getAllSchedules());
    }

    private void loadContracts() {
        contractData.setAll(ContractService.getAllContracts());
    }

    private void refreshStaffCombos() {
        ObservableList<Staff> options = FXCollections.observableArrayList(staffData);
        attendanceStaffCombo.setItems(options);
        shiftStaffCombo.setItems(options);
        contractStaffCombo.setItems(options);

        attendanceStaffCombo.setCellFactory(param -> createStaffCell());
        attendanceStaffCombo.setButtonCell(createStaffCell());
        shiftStaffCombo.setCellFactory(param -> createStaffCell());
        shiftStaffCombo.setButtonCell(createStaffCell());
        contractStaffCombo.setCellFactory(param -> createStaffCell());
        contractStaffCombo.setButtonCell(createStaffCell());
    }

    private ListCell<Staff> createStaffCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Staff item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getFullName() + " (" + item.getStaffCode() + ")");
            }
        };
    }

    private void loadStaffToForm(Staff staff) {
        staffCodeField.setText(staff.getStaffCode());
        staffNameField.setText(staff.getFullName());
        positionField.setText(staff.getPosition());
        departmentField.setText(staff.getDepartment());
        phoneField.setText(staff.getPhone());
        emailField.setText(staff.getEmail());
        staffStartDatePicker.setValue(staff.getStartDate());
        staffStatusCombo.setValue(toDisplay(STAFF_STATUS_OPTIONS, staff.getStatus()));
        salaryField.setText(staff.getBaseSalary() != null ? String.valueOf(staff.getBaseSalary()) : "");
        staffNotesArea.setText(staff.getNotes());
    }

    private void loadAttendanceToForm(AttendanceRecord record) {
        attendanceStaffCombo.setValue(findStaffById(record.getStaffId()));
        attendanceDatePicker.setValue(record.getAttendanceDate());
        attendanceShiftCombo.setValue(record.getShift());
        checkInField.setText(formatTime(record.getCheckIn()));
        checkOutField.setText(formatTime(record.getCheckOut()));
        attendanceStatusCombo.setValue(toDisplay(ATTENDANCE_STATUS_OPTIONS, record.getStatus()));
        attendanceNotesArea.setText(record.getNotes());
    }

    private void loadShiftToForm(ShiftSchedule schedule) {
        shiftStaffCombo.setValue(findStaffById(schedule.getStaffId()));
        shiftNameField.setText(schedule.getShiftName());
        shiftStartDatePicker.setValue(schedule.getStartDate());
        shiftEndDatePicker.setValue(schedule.getEndDate());
        assignedByField.setText(schedule.getAssignedBy());
        shiftNotesArea.setText(schedule.getNotes());
    }

    private void loadContractToForm(Contract contract) {
        contractStaffCombo.setValue(findStaffById(contract.getStaffId()));
        contractTypeField.setText(contract.getContractType());
        contractStartDatePicker.setValue(contract.getStartDate());
        contractEndDatePicker.setValue(contract.getEndDate());
        contractSalaryField.setText(contract.getSalary() != null ? String.valueOf(contract.getSalary()) : "");
        contractStatusCombo.setValue(toDisplay(CONTRACT_STATUS_OPTIONS, contract.getStatus()));
        contractDescriptionArea.setText(contract.getDescription());
    }

    private Staff findStaffById(int id) {
        return staffData.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    // Staff handlers
    @FXML
    private void handleAddStaff() {
        if (!validateStaffForm()) return;
        Staff staff = buildStaffFromForm(new Staff());
        if (staff == null) {
            return;
        }
        if (StaffService.addStaff(staff)) {
            showInfo("Đã thêm nhân viên!");
            clearStaffForm();
            loadStaff();
        } else {
            showError("Không thể thêm nhân viên.");
        }
    }

    @FXML
    private void handleUpdateStaff() {
        if (selectedStaff == null) return;
        if (!validateStaffForm()) return;
        Staff staff = buildStaffFromForm(selectedStaff);
        if (staff == null) {
            return;
        }
        if (StaffService.updateStaff(staff)) {
            showInfo("Đã cập nhật nhân viên!");
            clearStaffForm();
            loadStaff();
        } else {
            showError("Không thể cập nhật nhân viên.");
        }
    }

    @FXML
    private void handleDeleteStaff() {
        if (selectedStaff == null) return;
        if (confirm("Xóa nhân viên", "Bạn chắc chắn muốn xóa nhân viên này?")) {
            if (StaffService.deleteStaff(selectedStaff.getId())) {
                showInfo("Đã xóa nhân viên!");
                clearStaffForm();
                loadStaff();
            }
        }
    }

    @FXML
    private void handleClearStaff() {
        clearStaffForm();
    }

    private Staff buildStaffFromForm(Staff staff) {
        staff.setStaffCode(staffCodeField.getText().trim());
        staff.setFullName(staffNameField.getText().trim());
        staff.setPosition(positionField.getText().trim());
        staff.setDepartment(departmentField.getText().trim());
        staff.setPhone(phoneField.getText().trim());
        staff.setEmail(emailField.getText().trim());
        staff.setStartDate(staffStartDatePicker.getValue());
        staff.setStatus(staffStatusCombo.getValue() != null ? toValue(STAFF_STATUS_OPTIONS, staffStatusCombo.getValue()) : "ACTIVE");
        Double salary = parseDoubleField(salaryField.getText().trim(), "Lương cơ bản");
        if (salary == null && !salaryField.getText().trim().isEmpty()) {
            return null;
        }
        staff.setBaseSalary(salary);
        staff.setNotes(staffNotesArea.getText().trim());
        return staff;
    }

    private boolean validateStaffForm() {
        if (staffCodeField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập mã nhân viên.");
            return false;
        }
        if (staffNameField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập họ tên.");
            return false;
        }
        return true;
    }

    private void clearStaffForm() {
        selectedStaff = null;
        staffTable.getSelectionModel().clearSelection();
        staffCodeField.clear();
        staffNameField.clear();
        positionField.clear();
        departmentField.clear();
        phoneField.clear();
        emailField.clear();
        staffStartDatePicker.setValue(null);
        staffStatusCombo.setValue(toDisplay(STAFF_STATUS_OPTIONS, "ACTIVE"));
        salaryField.clear();
        staffNotesArea.clear();
    }

    // Attendance handlers
    @FXML
    private void handleAddAttendance() {
        if (!validateAttendanceForm()) return;
        AttendanceRecord record = buildAttendanceFromForm(new AttendanceRecord());
        if (record == null) return;
        if (AttendanceService.addRecord(record)) {
            showInfo("Đã thêm chấm công!");
            clearAttendanceForm();
            loadAttendance();
        } else {
            showError("Không thể thêm chấm công.");
        }
    }

    @FXML
    private void handleUpdateAttendance() {
        if (selectedAttendanceRecord == null) return;
        if (!validateAttendanceForm()) return;
        AttendanceRecord record = buildAttendanceFromForm(selectedAttendanceRecord);
        if (record == null) return;
        record.setId(selectedAttendanceRecord.getId());
        if (AttendanceService.updateRecord(record)) {
            showInfo("Đã cập nhật chấm công!");
            clearAttendanceForm();
            loadAttendance();
        } else {
            showError("Không thể cập nhật chấm công.");
        }
    }

    @FXML
    private void handleDeleteAttendance() {
        if (selectedAttendanceRecord == null) return;
        if (confirm("Xóa chấm công", "Bạn chắc chắn muốn xóa bản ghi này?")) {
            if (AttendanceService.deleteRecord(selectedAttendanceRecord.getId())) {
                showInfo("Đã xóa chấm công!");
                clearAttendanceForm();
                loadAttendance();
            }
        }
    }

    @FXML
    private void handleClearAttendance() {
        clearAttendanceForm();
    }

    private AttendanceRecord buildAttendanceFromForm(AttendanceRecord record) {
        Staff staff = attendanceStaffCombo.getValue();
        record.setStaffId(staff != null ? staff.getId() : 0);
        record.setAttendanceDate(attendanceDatePicker.getValue());
        record.setShift(attendanceShiftCombo.getValue());
        LocalTime checkIn = parseTimeField(checkInField.getText().trim(), "Giờ vào");
        if (checkIn == null && !checkInField.getText().trim().isEmpty()) return null;
        LocalTime checkOut = parseTimeField(checkOutField.getText().trim(), "Giờ ra");
        if (checkOut == null && !checkOutField.getText().trim().isEmpty()) return null;
        record.setCheckIn(checkIn);
        record.setCheckOut(checkOut);
        record.setStatus(attendanceStatusCombo.getValue() != null ? toValue(ATTENDANCE_STATUS_OPTIONS, attendanceStatusCombo.getValue()) : null);
        record.setNotes(attendanceNotesArea.getText().trim());
        return record;
    }

    private boolean validateAttendanceForm() {
        if (attendanceStaffCombo.getValue() == null) {
            showError("Vui lòng chọn nhân viên.");
            return false;
        }
        if (attendanceDatePicker.getValue() == null) {
            showError("Vui lòng chọn ngày chấm công.");
            return false;
        }
        return true;
    }

    private void clearAttendanceForm() {
        selectedAttendanceRecord = null;
        attendanceTable.getSelectionModel().clearSelection();
        attendanceStaffCombo.setValue(null);
        attendanceDatePicker.setValue(null);
        attendanceShiftCombo.setValue(null);
        checkInField.clear();
        checkOutField.clear();
        attendanceStatusCombo.setValue(null);
        attendanceNotesArea.clear();
    }

    // Shift handlers
    @FXML
    private void handleAddShift() {
        if (!validateShiftForm()) return;
        ShiftSchedule schedule = buildShiftFromForm(new ShiftSchedule());
        if (schedule == null) return;
        if (ShiftScheduleService.addSchedule(schedule)) {
            showInfo("Đã phân ca!");
            clearShiftForm();
            loadShifts();
        } else {
            showError("Không thể phân ca.");
        }
    }

    @FXML
    private void handleUpdateShift() {
        if (selectedShift == null) return;
        if (!validateShiftForm()) return;
        ShiftSchedule schedule = buildShiftFromForm(selectedShift);
        if (schedule == null) return;
        schedule.setId(selectedShift.getId());
        if (ShiftScheduleService.updateSchedule(schedule)) {
            showInfo("Đã cập nhật phân ca!");
            clearShiftForm();
            loadShifts();
        } else {
            showError("Không thể cập nhật phân ca.");
        }
    }

    @FXML
    private void handleDeleteShift() {
        if (selectedShift == null) return;
        if (confirm("Xóa lịch phân ca", "Bạn chắc chắn muốn xóa lịch này?")) {
            if (ShiftScheduleService.deleteSchedule(selectedShift.getId())) {
                showInfo("Đã xóa lịch phân ca!");
                clearShiftForm();
                loadShifts();
            }
        }
    }

    @FXML
    private void handleClearShift() {
        clearShiftForm();
    }

    private ShiftSchedule buildShiftFromForm(ShiftSchedule schedule) {
        Staff staff = shiftStaffCombo.getValue();
        schedule.setStaffId(staff != null ? staff.getId() : 0);
        schedule.setShiftName(shiftNameField.getText().trim());
        schedule.setStartDate(shiftStartDatePicker.getValue());
        schedule.setEndDate(shiftEndDatePicker.getValue());
        schedule.setAssignedBy(assignedByField.getText().trim());
        schedule.setNotes(shiftNotesArea.getText().trim());
        return schedule;
    }

    private boolean validateShiftForm() {
        if (shiftStaffCombo.getValue() == null) {
            showError("Vui lòng chọn nhân viên.");
            return false;
        }
        if (shiftNameField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tên ca.");
            return false;
        }
        return true;
    }

    private void clearShiftForm() {
        selectedShift = null;
        shiftTable.getSelectionModel().clearSelection();
        shiftStaffCombo.setValue(null);
        shiftNameField.clear();
        shiftStartDatePicker.setValue(null);
        shiftEndDatePicker.setValue(null);
        assignedByField.clear();
        shiftNotesArea.clear();
    }

    // Contract handlers
    @FXML
    private void handleAddContract() {
        if (!validateContractForm()) return;
        Contract contract = buildContractFromForm(new Contract());
        if (contract == null) return;
        if (ContractService.addContract(contract)) {
            showInfo("Đã thêm hợp đồng!");
            clearContractForm();
            loadContracts();
        } else {
            showError("Không thể thêm hợp đồng.");
        }
    }

    @FXML
    private void handleUpdateContract() {
        if (selectedContract == null) return;
        if (!validateContractForm()) return;
        Contract contract = buildContractFromForm(selectedContract);
        if (contract == null) return;
        contract.setId(selectedContract.getId());
        if (ContractService.updateContract(contract)) {
            showInfo("Đã cập nhật hợp đồng!");
            clearContractForm();
            loadContracts();
        } else {
            showError("Không thể cập nhật hợp đồng.");
        }
    }

    @FXML
    private void handleDeleteContract() {
        if (selectedContract == null) return;
        if (confirm("Xóa hợp đồng", "Bạn chắc chắn muốn xóa hợp đồng này?")) {
            if (ContractService.deleteContract(selectedContract.getId())) {
                showInfo("Đã xóa hợp đồng!");
                clearContractForm();
                loadContracts();
            }
        }
    }

    @FXML
    private void handleClearContract() {
        clearContractForm();
    }

    private Contract buildContractFromForm(Contract contract) {
        Staff staff = contractStaffCombo.getValue();
        contract.setStaffId(staff != null ? staff.getId() : 0);
        contract.setContractType(contractTypeField.getText().trim());
        contract.setStartDate(contractStartDatePicker.getValue());
        contract.setEndDate(contractEndDatePicker.getValue());
        Double salary = parseDoubleField(contractSalaryField.getText().trim(), "Lương hợp đồng");
        if (salary == null && !contractSalaryField.getText().trim().isEmpty()) return null;
        contract.setSalary(salary);
        contract.setStatus(contractStatusCombo.getValue() != null ? toValue(CONTRACT_STATUS_OPTIONS, contractStatusCombo.getValue()) : null);
        contract.setDescription(contractDescriptionArea.getText().trim());
        return contract;
    }

    private boolean validateContractForm() {
        if (contractStaffCombo.getValue() == null) {
            showError("Vui lòng chọn nhân viên.");
            return false;
        }
        if (contractTypeField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập loại hợp đồng.");
            return false;
        }
        return true;
    }

    private void clearContractForm() {
        selectedContract = null;
        contractTable.getSelectionModel().clearSelection();
        contractStaffCombo.setValue(null);
        contractTypeField.clear();
        contractStartDatePicker.setValue(null);
        contractEndDatePicker.setValue(null);
        contractSalaryField.clear();
        contractStatusCombo.setValue(null);
        contractDescriptionArea.clear();
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) staffTable.getScene().getWindow();
        stage.close();
    }

    private boolean confirm(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Double parseDoubleField(String value, String label) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            showError(label + " không hợp lệ.");
            return null;
        }
    }

    private LocalTime parseTimeField(String value, String label) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(value);
        } catch (Exception e) {
            showError(label + " phải đúng định dạng HH:mm.");
            return null;
        }
    }

    private String formatTime(LocalTime time) {
        return time != null ? time.toString() : "";
    }
}

