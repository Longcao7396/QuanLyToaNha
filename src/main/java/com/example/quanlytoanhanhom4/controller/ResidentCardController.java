package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ResourceBundle;

public class ResidentCardController implements Initializable {

    @FXML private TableView<ResidentCardRow> cardsTable;
    @FXML private TableColumn<ResidentCardRow, Integer> colId;
    @FXML private TableColumn<ResidentCardRow, Integer> colResidentId;
    @FXML private TableColumn<ResidentCardRow, Integer> colApartmentId;
    @FXML private TableColumn<ResidentCardRow, String> colCardNumber;
    @FXML private TableColumn<ResidentCardRow, String> colResidentName;
    @FXML private TableColumn<ResidentCardRow, String> colCardType;
    @FXML private TableColumn<ResidentCardRow, Date> colDateIssued;
    @FXML private TableColumn<ResidentCardRow, String> colRequest;

    @FXML private Button requestButton;
    @FXML private Label statusLabel;

    private final ObservableList<ResidentCardRow> cardList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set cell value factories by property names
        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        colResidentId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("residentId"));
        colApartmentId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("apartmentId"));
        colCardNumber.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cardNumber"));
        colResidentName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("residentName"));
        colCardType.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cardType"));
        colDateIssued.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateIssued"));
        colRequest.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("requestForRenewalText"));

        cardsTable.setItems(cardList);

        // disable button when no selection
        requestButton.disableProperty().bind(cardsTable.getSelectionModel().selectedItemProperty().isNull());

        // load current user's cards
        int residentId = resolveCurrentUserId();
        if (residentId > 0) {
            loadCardsForResident(residentId);
        } else {
            statusLabel.setText("Không xác định được ID người dùng hiện tại.");
        }
    }

    @FXML
    private void handleRefresh() {
        int residentId = resolveCurrentUserId();
        if (residentId > 0) {
            loadCardsForResident(residentId);
        } else {
            statusLabel.setText("Không xác định được ID người dùng hiện tại.");
        }
    }

    @FXML
    private void handleRequestNewCard() {
        ResidentCardRow selected = cardsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Vui lòng chọn một thẻ.");
            return;
        }
        String updateSql = "UPDATE resident_card SET request_for_renewal = 1 WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {

            ps.setInt(1, selected.getId());
            int changed = ps.executeUpdate();
            if (changed > 0) {
                selected.setRequestForRenewal(true);
                // refresh table view to show updated value
                cardsTable.refresh();
                statusLabel.setText("Đã gửi yêu cầu cấp thẻ mới cho id=" + selected.getId());
            } else {
                statusLabel.setText("Cập nhật thất bại cho id=" + selected.getId());
            }
        } catch (Exception e) {
            statusLabel.setText("Lỗi khi gửi yêu cầu.");
            e.printStackTrace();
        }
    }

    private int resolveCurrentUserId() {
        // Try to get username from UserSession (MainController pattern used earlier)
        try {
            String username = UserSession.getCurrentUsername();
            if (username != null && !username.isBlank()) {
                String sql = "SELECT id FROM `user` WHERE username = ? LIMIT 1";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, username);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("id");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // fallback: could not resolve
            e.printStackTrace();
        }
        return -1;
    }

    private void loadCardsForResident(int residentId) {
        cardList.clear();
        String sql = "SELECT id, resident_id, apartment_id, card_number, resident_name, card_type, date_issued, request_for_renewal FROM resident_card WHERE resident_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, residentId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    ResidentCardRow row = new ResidentCardRow(
                            rs.getInt("id"),
                            rs.getInt("resident_id"),
                            rs.getInt("apartment_id"),
                            rs.getString("card_number"),
                            rs.getString("resident_name"),
                            rs.getString("card_type"),
                            rs.getDate("date_issued"),
                            rs.getBoolean("request_for_renewal")
                    );
                    cardList.add(row);
                }
                if (!any) {
                    statusLabel.setText("Không có thẻ cho cư dân id=" + residentId);
                } else {
                    statusLabel.setText("Đã nạp thẻ cho cư dân id=" + residentId);
                }
            }
        } catch (Exception e) {
            statusLabel.setText("Lỗi khi nạp thẻ.");
            e.printStackTrace();
        }
    }

    // simple model class for table rows
    public static class ResidentCardRow {
        private final int id;
        private final int residentId;
        private final int apartmentId;
        private final String cardNumber;
        private final String residentName;
        private final String cardType;
        private final Date dateIssued;
        private boolean requestForRenewal;

        public ResidentCardRow(int id, int residentId, int apartmentId, String cardNumber, String residentName, String cardType, Date dateIssued, boolean requestForRenewal) {
            this.id = id;
            this.residentId = residentId;
            this.apartmentId = apartmentId;
            this.cardNumber = cardNumber;
            this.residentName = residentName;
            this.cardType = cardType;
            this.dateIssued = dateIssued;
            this.requestForRenewal = requestForRenewal;
        }

        public int getId() { return id; }
        public int getResidentId() { return residentId; }
        public int getApartmentId() { return apartmentId; }
        public String getCardNumber() { return cardNumber; }
        public String getResidentName() { return residentName; }
        public String getCardType() { return cardType; }
        public Date getDateIssued() { return dateIssued; }

        public boolean isRequestForRenewal() { return requestForRenewal; }
        public void setRequestForRenewal(boolean requestForRenewal) { this.requestForRenewal = requestForRenewal; }

        // text for display in request column
        public String getRequestForRenewalText() {
            return requestForRenewal ? "YES" : "NO";
        }
    }
}

