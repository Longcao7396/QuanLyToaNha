package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.model.Notification;
import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.NotificationService;
import com.example.quanlytoanhanhom4.service.ResidentService;
import com.example.quanlytoanhanhom4.util.AlertUtils;
import com.example.quanlytoanhanhom4.util.EmptyStateHelper;
import com.example.quanlytoanhanhom4.util.PaginationHelper;
import com.example.quanlytoanhanhom4.util.UserSession;
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
    private TableColumn<Notification, String> colPriority;
    @FXML
    private TableColumn<Notification, String> colExpiryDate;
    @FXML
    private TableColumn<Notification, String> colStatus;

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
    private Pagination pagination;
    @FXML
    private Label paginationInfoLabel;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentArea;
    @FXML
    private ComboBox<String> notificationTypeCombo;
    @FXML
    private ComboBox<String> targetTypeCombo;
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
        // Notification Type - Tiếng Việt
        NOTIFICATION_TYPE_OPTIONS.put("THÔNG_BÁO_CHUNG", "Thông báo chung");
        NOTIFICATION_TYPE_OPTIONS.put("THÔNG_BÁO_HÓA_ĐƠN", "Thông báo hóa đơn");
        NOTIFICATION_TYPE_OPTIONS.put("BẢO_TRÌ", "Bảo trì");
        NOTIFICATION_TYPE_OPTIONS.put("AN_NINH", "An ninh");
        NOTIFICATION_TYPE_OPTIONS.put("SỰ_KIỆN", "Sự kiện");
        NOTIFICATION_TYPE_OPTIONS.put("YÊU_CẦU", "Yêu cầu");
        NOTIFICATION_TYPE_OPTIONS.put("NHẮC_NỢ", "Nhắc nợ");
        // Hỗ trợ tương thích với dữ liệu cũ
        NOTIFICATION_TYPE_OPTIONS.put("GENERAL", "Thông báo chung");
        NOTIFICATION_TYPE_OPTIONS.put("BILLING", "Hóa đơn");
        NOTIFICATION_TYPE_OPTIONS.put("ANNOUNCEMENT", "Thông báo hóa đơn");
        NOTIFICATION_TYPE_OPTIONS.put("MAINTENANCE", "Bảo trì");
        NOTIFICATION_TYPE_OPTIONS.put("SECURITY", "An ninh");
        NOTIFICATION_TYPE_OPTIONS.put("EVENT", "Sự kiện");
        NOTIFICATION_TYPE_OPTIONS.put("TICKET", "Yêu cầu");
        NOTIFICATION_TYPE_OPTIONS.put("DEBT_REMINDER", "Nhắc nợ");

        // Target Type - Tiếng Việt
        TARGET_TYPE_OPTIONS.put("TẤT_CẢ", "Tất cả");
        TARGET_TYPE_OPTIONS.put("CĂN_HỘ", "Căn hộ");
        TARGET_TYPE_OPTIONS.put("CƯ_DÂN", "Cư dân");
        TARGET_TYPE_OPTIONS.put("NHÂN_VIÊN", "Nhân viên");
        // Hỗ trợ tương thích với dữ liệu cũ
        TARGET_TYPE_OPTIONS.put("ALL", "Tất cả");
        TARGET_TYPE_OPTIONS.put("APARTMENT", "Căn hộ");
        TARGET_TYPE_OPTIONS.put("RESIDENT", "Cư dân");
        TARGET_TYPE_OPTIONS.put("STAFF", "Nhân viên");

        // Priority - Tiếng Việt
        PRIORITY_OPTIONS.put("THẤP", "Thấp");
        PRIORITY_OPTIONS.put("BÌNH_THƯỜNG", "Bình thường");
        PRIORITY_OPTIONS.put("CAO", "Cao");
        PRIORITY_OPTIONS.put("KHẨN_CẤP", "Khẩn cấp");
        // Hỗ trợ tương thích với dữ liệu cũ
        PRIORITY_OPTIONS.put("LOW", "Thấp");
        PRIORITY_OPTIONS.put("NORMAL", "Bình thường");
        PRIORITY_OPTIONS.put("HIGH", "Cao");
        PRIORITY_OPTIONS.put("URGENT", "Khẩn cấp");

        // Status - Tiếng Việt
        STATUS_OPTIONS.put("NHÁP", "Nháp");
        STATUS_OPTIONS.put("ĐÃ_GỬI", "Đã gửi");
        STATUS_OPTIONS.put("ĐÃ_ĐỌC", "Đã đọc");
        // Hỗ trợ tương thích với dữ liệu cũ
        STATUS_OPTIONS.put("DRAFT", "Nháp");
        STATUS_OPTIONS.put("SENT", "Đã gửi");
        STATUS_OPTIONS.put("READ", "Đã đọc");
    }

    private ObservableList<Notification> notifications;
    private ObservableList<Notification> allNotifications; // Lưu tất cả notifications (chưa filter)
    private FilteredList<Notification> filteredNotifications; // Danh sách đã filter
    private Notification selectedNotification;
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

    }

    private void initializeTable() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("notificationType"));
        colTarget.setCellValueFactory(new PropertyValueFactory<>("targetType"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        
        // Format ngày hết hạn
        colExpiryDate.setCellValueFactory(cell -> {
            java.time.LocalDate date = cell.getValue().getExpiryDate();
            if (date != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        notifications = FXCollections.observableArrayList();
        notificationTable.setItems(notifications);
        
        // Set empty state với nút thêm mới
        EmptyStateHelper.setNotificationEmptyState(notificationTable, this::handleAdd);
    }

    private void initializeComboBoxes() {
        ObservableList<String> notificationTypes = FXCollections.observableArrayList(NOTIFICATION_TYPE_OPTIONS.values());
        notificationTypeCombo.setItems(notificationTypes);

        ObservableList<String> targetTypes = FXCollections.observableArrayList(TARGET_TYPE_OPTIONS.values());
        targetTypeCombo.setItems(targetTypes);
        targetTypeCombo.setValue(toDisplay(TARGET_TYPE_OPTIONS, "TẤT_CẢ"));

        ObservableList<String> priorities = FXCollections.observableArrayList(PRIORITY_OPTIONS.values());
        priorityCombo.setItems(priorities);
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "BÌNH_THƯỜNG"));

        ObservableList<String> statuses = FXCollections.observableArrayList(STATUS_OPTIONS.values());
        statusCombo.setItems(statuses);
        statusCombo.setValue(toDisplay(STATUS_OPTIONS, "NHÁP"));

        ObservableList<String> filterStatuses = FXCollections.observableArrayList(statuses);
        filterStatuses.add(0, ALL_LABEL);
        // Đã xóa filterStatusCombo khỏi top bar
        // filterStatusCombo.setItems(filterStatuses);
        // filterStatusCombo.setValue(ALL_LABEL);
        
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
            ObservableList<String> types = FXCollections.observableArrayList(NOTIFICATION_TYPE_OPTIONS.values());
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
        // Đã xóa filterStatusCombo khỏi top bar
        // if (filterStatusCombo != null) filterStatusCombo.setValue(ALL_LABEL);
        applyFilters();
    }
    
    private void applyFilters() {
        if (allNotifications == null) {
            return;
        }
        
        filteredNotifications = new FilteredList<>(allNotifications, p -> true);
        
        filteredNotifications.setPredicate(notification -> {
            // Tìm kiếm theo tiêu đề
            String searchText = searchField != null ? searchField.getText() : "";
            if (searchText != null && !searchText.trim().isEmpty()) {
                String lowerSearchText = searchText.toLowerCase().trim();
                String title = notification.getTitle() != null ? notification.getTitle().toLowerCase() : "";
                if (!title.contains(lowerSearchText)) {
                    return false;
                }
            }
            
            // Filter theo status - Đã xóa filterStatusCombo khỏi top bar
            // String filterStatus = filterStatusCombo != null ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status
            if (filterStatus != null && !filterStatus.equals(ALL_LABEL)) {
                String statusValue = toValue(STATUS_OPTIONS, filterStatus);
                if (!statusValue.equals(notification.getStatus())) {
                    return false;
                }
            }
            
            // Filter theo type
            String filterType = filterTypeCombo != null ? filterTypeCombo.getValue() : "Tất cả";
            if (filterType != null && !filterType.equals("Tất cả")) {
                String typeValue = toValue(NOTIFICATION_TYPE_OPTIONS, filterType);
                if (!typeValue.equals(notification.getNotificationType())) {
                    return false;
                }
            }
            
            // Filter theo priority
            String filterPriority = filterPriorityCombo != null ? filterPriorityCombo.getValue() : "Tất cả";
            if (filterPriority != null && !filterPriority.equals("Tất cả")) {
                String priorityValue = toValue(PRIORITY_OPTIONS, filterPriority);
                if (!priorityValue.equals(notification.getPriority())) {
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
        if (filteredNotifications == null || pagination == null) {
            return;
        }
        
        ObservableList<Notification> itemsToPaginate = FXCollections.observableArrayList(filteredNotifications);
        PaginationHelper.updatePagination(pagination, notificationTable, itemsToPaginate, itemsPerPage);
        
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

    private void loadNotifications() {
        try {
            // Đảm bảo notifications list đã được khởi tạo
            if (notifications == null) {
                notifications = FXCollections.observableArrayList();
            }
            if (notificationTable != null && notificationTable.getItems() != notifications) {
                notificationTable.setItems(notifications);
            }

            // Đảm bảo filterStatusCombo đã được khởi tạo - Đã xóa khỏi top bar
            // String filterStatus = (filterStatusCombo != null && filterStatusCombo.getValue() != null)
            //         ? filterStatusCombo.getValue() : ALL_LABEL;
            String filterStatus = ALL_LABEL; // Tạm thời bỏ qua filter theo status

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
                    // Lưu tất cả notifications vào allNotifications
                    if (allNotifications == null) {
                        allNotifications = FXCollections.observableArrayList();
                    }
                    allNotifications.clear();
                    if (notificationList != null && !notificationList.isEmpty()) {
                        allNotifications.addAll(notificationList);
                        System.out.println("Đã load " + notificationList.size() + " thông báo vào bảng");
                    } else {
                        System.out.println("Không có dữ liệu thông báo nào được trả về từ service!");
                    }

                    // Áp dụng filters và pagination
                    applyFilters();

                    // Update status label
                    if (statusLabel != null) {
                        statusLabel.setText("Đã tải " + allNotifications.size() + " thông báo");
                    }

                    System.out.println("Số lượng thông báo trong ObservableList: " + allNotifications.size());
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
            notification.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            notification.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            notification.setExpiryDate(expiryDatePicker.getValue());
            notification.setCreatedBy(UserSession.getCurrentUserId() != null ? UserSession.getCurrentUserId() : 1);

            if (NotificationService.addNotification(notification)) {
                AlertUtils.showSuccess("Thêm thông báo thành công!");
                clearForm();
                loadNotifications();
            } else {
                AlertUtils.showError("Lỗi khi thêm thông báo!");
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
            selectedNotification.setPriority(toValue(PRIORITY_OPTIONS, priorityCombo.getValue()));
            selectedNotification.setStatus(toValue(STATUS_OPTIONS, statusCombo.getValue()));
            selectedNotification.setExpiryDate(expiryDatePicker.getValue());

            if (NotificationService.updateNotification(selectedNotification)) {
                AlertUtils.showSuccess("Cập nhật thông báo thành công!");
                clearForm();
                loadNotifications();
            } else {
                AlertUtils.showError("Lỗi khi cập nhật thông báo!");
            }
        }
    }

    @FXML
    private void handleSend() {
        if (selectedNotification != null) {
            selectedNotification.setStatus("SENT");
            selectedNotification.setSentDate(LocalDateTime.now());

            if (NotificationService.updateNotification(selectedNotification)) {
                AlertUtils.showSuccess("Gửi thông báo thành công!");
                loadNotifications();
            } else {
                AlertUtils.showError("Lỗi khi gửi thông báo!");
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
                    AlertUtils.showSuccess("Xóa thông báo thành công!");
                    clearForm();
                    loadNotifications();
                } else {
                    AlertUtils.showError("Lỗi khi xóa thông báo!");
                }
            }
        }
    }

    // @FXML - Đã xóa nút quay lại khỏi top bar
    // private void handleBack() {
    //     ((Stage) notificationTable.getScene().getWindow()).close();
    // }

    private void clearForm() {
        titleField.clear();
        contentArea.clear();
        notificationTypeCombo.setValue(null);
        targetTypeCombo.setValue(toDisplay(TARGET_TYPE_OPTIONS, "ALL"));
        priorityCombo.setValue(toDisplay(PRIORITY_OPTIONS, "BÌNH_THƯỜNG"));
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
            AlertUtils.showWarning("Vui lòng nhập tiêu đề!");
            return false;
        }
        if (contentArea.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Vui lòng nhập nội dung!");
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


