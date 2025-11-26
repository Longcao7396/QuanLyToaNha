package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Notification;
import com.example.quanlytoanhanhom4.service.NotificationService;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {

    @FXML
    private TableView<Notification> notificationTable;
    @FXML
    private TableColumn<Notification, String> colTitle;
    @FXML
    private TableColumn<Notification, String> colType;
    @FXML
    private TableColumn<Notification, String> colTarget;
    @FXML
    private TableColumn<Notification, String> colStatus;

    @FXML
    private ComboBox<String> filterStatusCombo;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentArea;
    @FXML
    private ComboBox<String> notificationTypeCombo;
    @FXML
    private ComboBox<String> targetTypeCombo;
    @FXML
    private ComboBox<Integer> targetIdCombo;
    @FXML
    private ComboBox<String> priorityCombo;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private DatePicker expiryDatePicker;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button sendButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label statusLabel;

    private static final LinkedHashMap<String, String> NOTIFICATION_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> TARGET_TYPE_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> PRIORITY_OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> STATUS_OPTIONS = new LinkedHashMap<>();
    private static final String ALL_LABEL = "Tất cả";

    static {
        NOTIFICATION_TYPE_OPTIONS.put("GENERAL", "Thông báo chung");
        NOTIFICATION_TYPE_OPTIONS.put("BILLING", "Hóa đơn");
        NOTIFICATION_TYPE_OPTIONS.put("MAINTENANCE", "Bảo trì");
        NOTIFICATION_TYPE_OPTIONS.put("SECURITY", "An ninh");
        NOTIFICATION_TYPE_OPTIONS.put("EVENT", "Sự kiện");

        TARGET_TYPE_OPTIONS.put("ALL", "Tất cả");
        TARGET_TYPE_OPTIONS.put("APARTMENT", "Căn hộ");
        TARGET_TYPE_OPTIONS.put("RESIDENT", "Cư dân");
        TARGET_TYPE_OPTIONS.put("STAFF", "Nhân viên");

        PRIORITY_OPTIONS.put("LOW", "Thấp");
        PRIORITY_OPTIONS.put("NORMAL", "Bình thường");
        PRIORITY_OPTIONS.put("HIGH", "Cao");
        PRIORITY_OPTIONS.put("URGENT", "Khẩn cấp");

        STATUS_OPTIONS.put("DRAFT", "Nháp");
        STATUS_OPTIONS.put("SENT", "Đã gửi");
        STATUS_OPTIONS.put("READ", "Đã đọc");
    }

    private ObservableList<Notification> notifications;
    private Notification selectedNotification;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComboBoxes();

        // Delay nhỏ để đảm bảo UI đã sẵn sàng trước khi load dữ liệu
        javafx.application.Platform.runLater(() -> {
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(100));
            pause.setOnFinished(e -> {
                loadNotifications();
            });
            pause.play();
        });

        notificationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedNotification = newSelection;
                loadNotificationToForm(newSelection);
                updateButton.setDisable(false);
                sendButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
            }
        });

        targetTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals("Tất cả")) {
                // TODO: Load target IDs based on target type
                targetIdCombo.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
            } else {
                targetIdCombo.setItems(FXCollections.observableArrayList());
            }
        });
    }

    private void initializeTable() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("notificationType"));
        colTarget.setCellValueFactory(new PropertyValueFactory<>("targetType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        notifications = FXCollections.observableArrayList();
        notificationTable.setItems(notifications);
    }

    private void initializeComboBoxes() {
        ObservableList<String> notificationTypes = FXCollections.observableArrayList(NOTIFICATION_TYPE_OPTIONS.values());
        notificationTypeCombo.setItems(notificationTypes);

        ObservableList<String> targetTypes = FXCollections.observableArrayList(TARGET_TYPE_OPTIONS.values());
        targetTypeCombo.setItems(targetTypes);
        targetTypeCombo.setValue(toDisplay(TARGET_TYPE_OPTIONS, "ALL"));

        ObservableList<String> priorities = FXCollections.observableArrayList(PRIORITY_OPTIONS.values());
        priorityCombo.setItems(priorities);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "NORMAL"));

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "DRAFT"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        filterStatusCombo.setItems(filterStatuses);
        filterStatusCombo.setValue(ALL_LABEL);
    }

    private void loadNotifications() {
        try {
            // Đảm bảo notifications list đã được khởi tạo
            if (notifications == null) {
                notifications = FXCollections.observableArrayList();
            }
            if (notificationTable != null && notificationTable.getItems() != notifications) {
                notificationTable.setItems(notifications);
            }

            // Đảm bảo filterStatusCombo đã được khởi tạo
            String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
                    ? filterStatusCombo.getValue() : ALL_LABEL;

            // Load dữ liệu
            List<Notification> notificationList;
            if (filterStatus == null || filterStatus.equals(ALL_LABEL)) {
                notificationList = NotificationService.getAllNotifications();
            } else {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                notificationList = NotificationService.getNotificationsByStatus(statusValue);
            }

            System.out.println("Đã lấy được " + (notificationList != null ? notificationList.size() : 0) + " thông báo từ service");

            // Cập nhật UI trên JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                try {
                    notifications.clear();
                    if (notificationList != null && !notificationList.isEmpty()) {
                        notifications.addAll(notificationList);
                        System.out.println("Đã load " + notificationList.size() + " thông báo vào bảng");
                    } else {
                        System.out.println("Không có dữ liệu thông báo nào được trả về từ service!");
                    }

                    // Refresh table để đảm bảo hiển thị
                    if (notificationTable != null) {
                        notificationTable.refresh();
                        // Force update columns
                        notificationTable.getColumns().forEach(col -> col.setVisible(false));
                        notificationTable.getColumns().forEach(col -> col.setVisible(true));
                        System.out.println("Đã refresh bảng thông báo, số lượng hiển thị: " + notificationTable.getItems().size());
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật UI: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("Lỗi khi tải danh sách thông báo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadNotificationToForm(Notification notification) {
        titleField.setText(notification.getTitle());
        contentArea.setText(notification.getContent());
        notificationTypeCombo.setValue(toDisplay(NOTIFICATION_TYPE_OPTIONS, notification.getNotificationType()));
        targetTypeCombo.setValue(toDisplay(TARGET_TYPE_OPTIONS, notification.getTargetType()));
        if (notification.getTargetId() != null) {
            targetIdCombo.setValue(notification.getTargetId());
        }
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, notification.getPriority()));
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, notification.getStatus()));
        expiryDatePicker.setValue(notification.getExpiryDate());
    }

    @FXML
    private void handleFilter() {
        loadNotifications();
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Notification notification = new Notification();
            notification.setTitle(titleField.getText().trim());
            notification.setContent(contentArea.getText().trim());
            notification.setNotificationType(toValue(NOTIFICATION_TYPE_OPTIONS, notificationTypeCombo.getValue()));
            notification.setTargetType(toValue(TARGET_TYPE_OPTIONS, targetTypeCombo.getValue()));
            if (targetIdCombo.getValue() != null && !targetTypeCombo.getValue().equals("Tất cả")) {
                notification.setTargetId(targetIdCombo.getValue());
            }
            notification.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            notification.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            notification.setExpiryDate(expiryDatePicker.getValue());
            notification.setCreatedBy(UserSession.getCurrentUserId() != null ? UserSession.getCurrentUserId() : 1);

            if (NotificationService.addNotification(notification)) {
                statusLabel.setText("Thêm thông báo thành công!");
                clearForm();
                loadNotifications();
            } else {
                statusLabel.setText("Lỗi khi thêm thông báo!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedNotification != null && validateInput()) {
            selectedNotification.setTitle(titleField.getText().trim());
            selectedNotification.setContent(contentArea.getText().trim());
            selectedNotification.setNotificationType(toValue(NOTIFICATION_TYPE_OPTIONS, notificationTypeCombo.getValue()));
            selectedNotification.setTargetType(toValue(TARGET_TYPE_OPTIONS, targetTypeCombo.getValue()));
            if (targetIdCombo.getValue() != null && !targetTypeCombo.getValue().equals("Tất cả")) {
                selectedNotification.setTargetId(targetIdCombo.getValue());
            } else {
                selectedNotification.setTargetId(null);
            }
            selectedNotification.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            selectedNotification.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedNotification.setExpiryDate(expiryDatePicker.getValue());

            if (NotificationService.updateNotification(selectedNotification)) {
                statusLabel.setText("Cập nhật thông báo thành công!");
                clearForm();
                loadNotifications();
            } else {
                statusLabel.setText("Lỗi khi cập nhật thông báo!");
            }
        }
    }

    @FXML
    private void handleSend() {
        if (selectedNotification != null) {
            selectedNotification.setStatus("SENT");
            selectedNotification.setSentDate(LocalDateTime.now());

            if (NotificationService.updateNotification(selectedNotification)) {
                statusLabel.setText("Gửi thông báo thành công!");
                loadNotifications();
            } else {
                statusLabel.setText("Lỗi khi gửi thông báo!");
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedNotification != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa thông báo này?");
            alert.setContentText(selectedNotification.getTitle());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (NotificationService.deleteNotification(selectedNotification.getId())) {
                    statusLabel.setText("Xóa thông báo thành công!");
                    clearForm();
                    loadNotifications();
                } else {
                    statusLabel.setText("Lỗi khi xóa thông báo!");
                }
            }
        }
    }

    @FXML
    private void handleBack() {
        ((Stage) notificationTable.getScene().getWindow()).close();
    }

    private void clearForm() {
        titleField.clear();
        contentArea.clear();
        notificationTypeCombo.setValue(null);
        targetTypeCombo.setValue(toDisplay(TARGET_TYPE_OPTIONS, "ALL"));
        targetIdCombo.setValue(null);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "NORMAL"));
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "DRAFT"));
        expiryDatePicker.setValue(null);
        selectedNotification = null;
        notificationTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        sendButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            statusLabel.setText("Vui lòng nhập tiêu đề!");
            return false;
        }
        if (contentArea.getText().trim().isEmpty()) {
            statusLabel.setText("Vui lòng nhập nội dung!");
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


