package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.AdminTask;
import com.example.quanlytoanhanhom4.service.AdminTaskService;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    
    @FXML private TableView<AdminTask> taskTable;
    @FXML private TableColumn<AdminTask, String> colTaskType;
    @FXML private TableColumn<AdminTask, String> colTitle;
    @FXML private TableColumn<AdminTask, LocalDate> colDueDate;
    @FXML private TableColumn<AdminTask, String> colStatus;
    @FXML private TableColumn<AdminTask, String> colPriority;
    
    @FXML private ComboBox<String> filterStatusCombo;
    @FXML private ComboBox<String> taskTypeCombo;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<String> statusCombo;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private TextArea notesArea;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button completeButton;
    @FXML private Button deleteButton;
    @FXML private Label statusLabel;

    private static final LinkedHashMap<String, String> TASK_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> PRIORITY_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        TASK_TYPE_OPTIONS.put("NHÂN_SỰ", "Nhân sự");
        TASK_TYPE_OPTIONS.put("TÀI_CHÍNH", "Tài chính");
        TASK_TYPE_OPTIONS.put("TÀI_LIỆU", "Tài liệu");
        TASK_TYPE_OPTIONS.put("CUỘC_HỌP", "Cuộc họp");
        TASK_TYPE_OPTIONS.put("BÁO_CÁO", "Báo cáo");
        TASK_TYPE_OPTIONS.put("KIỂM_TRA", "Kiểm tra");
        TASK_TYPE_OPTIONS.put("ĐÀO_TẠO", "Đào tạo");
        TASK_TYPE_OPTIONS.put("NGÂN_SÁCH", "Ngân sách");
        TASK_TYPE_OPTIONS.put("KIỂM_TOÁN", "Kiểm toán");
        TASK_TYPE_OPTIONS.put("KHÁC", "Khác");

        STATUS_OPTIONS.put("CHỜ_XỬ_LÝ", "Chờ xử lý");
        STATUS_OPTIONS.put("ĐANG_XỬ_LÝ", "Đang xử lý");
        STATUS_OPTIONS.put("HOÀN_THÀNH", "Hoàn thành");
        STATUS_OPTIONS.put("ĐÃ_HỦY", "Đã hủy");

        PRIORITY_OPTIONS.put("THẤP", "Thấp");
        PRIORITY_OPTIONS.put("TRUNG_BÌNH", "Trung bình");
        PRIORITY_OPTIONS.put("CAO", "Cao");
        PRIORITY_OPTIONS.put("KHẨN_CẤP", "Khẩn cấp");
    }

    private ObservableList<AdminTask> tasks;
    private AdminTask selectedTask;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();
        loadTasks();
        
        taskTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTask = newSelection;
                loadTaskToForm(newSelection);
                updateButton.setDisable(false);
                completeButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });
    }
    
    private void initializeTable() {
        colTaskType.setCellValueFactory(cell -> {
            AdminTask task = cell.getValue();
            if (task == null) return new SimpleStringProperty("");
            String taskType = task.getTaskType();
            String display = toDisplay(TASK_TYPE_OPTIONS, taskType);
            return new SimpleStringProperty(display != null ? display : taskType);
        });
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colStatus.setCellValueFactory(cell -> {
            AdminTask task = cell.getValue();
            if (task == null) return new SimpleStringProperty("");
            String status = task.getStatus();
            String display = toDisplay(STATUS_OPTIONS, status);
            return new SimpleStringProperty(display != null ? display : status);
        });
        colPriority.setCellValueFactory(cell -> {
            AdminTask task = cell.getValue();
            if (task == null) return new SimpleStringProperty("");
            String priority = task.getPriority();
            String display = toDisplay(PRIORITY_OPTIONS, priority);
            return new SimpleStringProperty(display != null ? display : priority);
        });
        
        tasks = FXCollections.observableArrayList();
        taskTable.setItems(tasks);
    }
    
    private void initializeComboBoxes() {
        ObservableList<String> taskTypes = FXCollections.observableArrayList(TASK_TYPE_OPTIONS.values());
        taskTypeCombo.setItems(taskTypes);

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "CHỜ_XỬ_LÝ"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);

        ObservableList<String> priorities = FXCollections.observableArrayList(PRIORITY_OPTIONS.values());
        priorityCombo.setItems(priorities);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "TRUNG_BÌNH"));
    }
    
    private void loadTasks() {
        try {
            tasks.clear();
            String filterStatus = filterStatusCombo.getValue();

            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                tasks.addAll(AdminTaskService.getAllTasks());
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                tasks.addAll(AdminTaskService.getTasksByStatus(statusValue));
            }

            // Refresh table to ensure data is displayed
            taskTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu nhiệm vụ admin: " + e.getMessage());
        }
    }
    
    private void loadTaskToForm(AdminTask task) {
        taskTypeCombo.setValue(toDisplay(TASK_TYPE_OPTIONS, task.getTaskType()));
        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());
        dueDatePicker.setValue(task.getDueDate());
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, task.getStatus()));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, task.getPriority()));
        notesArea.setText(task.getNotes());
    }
    
    @FXML
    private void handleFilter() {
        loadTasks();
    }
    
    @FXML
    private void handleAdd() {
        if (validateInput()) {
            AdminTask task = new AdminTask();
            task.setTaskType(toValue(TASK_TYPE_OPTIONS, taskTypeCombo.getValue()));
            task.setTitle(titleField.getText().trim());
            task.setDescription(descriptionArea.getText().trim());
            task.setDueDate(dueDatePicker.getValue());
            task.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            task.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            task.setNotes(notesArea.getText().trim());
            task.setCreatedBy(UserSession.getCurrentUserId() != null ? UserSession.getCurrentUserId() : 1);
            
            if (AdminTaskService.addTask(task)) {
                statusLabel.setText("✅ Thêm nhiệm vụ thành công!");
                clearForm();
                loadTasks();
            } else {
                showAlert("Lỗi", "Không thể thêm nhiệm vụ!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleUpdate() {
        if (selectedTask == null) return;
        
        if (validateInput()) {
            selectedTask.setTaskType(toValue(TASK_TYPE_OPTIONS, taskTypeCombo.getValue()));
            selectedTask.setTitle(titleField.getText().trim());
            selectedTask.setDescription(descriptionArea.getText().trim());
            selectedTask.setDueDate(dueDatePicker.getValue());
            selectedTask.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedTask.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            selectedTask.setNotes(notesArea.getText().trim());
            
            if (AdminTaskService.updateTask(selectedTask)) {
                statusLabel.setText("✅ Cập nhật nhiệm vụ thành công!");
                clearForm();
                loadTasks();
                resetSelection();
            } else {
                showAlert("Lỗi", "Không thể cập nhật nhiệm vụ!", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleComplete() {
        if (selectedTask == null) return;
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Hoàn thành nhiệm vụ");
        dialog.setHeaderText("Nhập ghi chú hoàn thành");
        dialog.setContentText("Ghi chú:");
        
        dialog.showAndWait().ifPresent(notes -> {
            if (AdminTaskService.completeTask(selectedTask.getId(), LocalDate.now(), notes)) {
                statusLabel.setText("✅ Hoàn thành nhiệm vụ!");
                clearForm();
                loadTasks();
                resetSelection();
            }
        });
    }
    
    @FXML
    private void handleDelete() {
        if (selectedTask == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText("Xóa nhiệm vụ");
        confirm.setContentText("Bạn có chắc chắn muốn xóa nhiệm vụ này?");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (AdminTaskService.deleteTask(selectedTask.getId())) {
                statusLabel.setText("✅ Xóa nhiệm vụ thành công!");
                clearForm();
                loadTasks();
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
        Stage stage = (Stage) taskTable.getScene().getWindow();
        stage.close();
    }
    
    private void clearForm() {
        taskTypeCombo.setValue(null);
        titleField.clear();
        descriptionArea.clear();
        dueDatePicker.setValue(null);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "CHỜ_XỬ_LÝ"));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "TRUNG_BÌNH"));
        notesArea.clear();
        statusLabel.setText("");
    }
    
    private void resetSelection() {
        selectedTask = null;
        taskTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        completeButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setDisable(false);
    }
    
    private boolean validateInput() {
        if (taskTypeCombo.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn loại nhiệm vụ!", Alert.AlertType.ERROR);
            return false;
        }
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tiêu đề!", Alert.AlertType.ERROR);
            return false;
        }
        if (dueDatePicker.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn ngày hết hạn!", Alert.AlertType.ERROR);
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






