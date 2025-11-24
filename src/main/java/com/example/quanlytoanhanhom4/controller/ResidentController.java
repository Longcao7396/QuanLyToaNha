package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ResidentController implements Initializable {

    @FXML
    private TextField idField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<ApartmentRow> apartmentsTable;

    @FXML
    private TableColumn<ApartmentRow, Integer> colOwnerId;

    @FXML
    private TableColumn<ApartmentRow, String> colApartmentNo;

    @FXML
    private TableColumn<ApartmentRow, Integer> colRooms;

    @FXML
    private TableColumn<ApartmentRow, Integer> colPeople;

    @FXML
    private TableColumn<ApartmentRow, Double> colArea;

    @FXML
    private TableColumn<ApartmentRow, String> colPrice;

    private final ObservableList<ApartmentRow> apartmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set up table columns
        colOwnerId.setCellValueFactory(new PropertyValueFactory<>("residentOwnerId"));
        colApartmentNo.setCellValueFactory(new PropertyValueFactory<>("apartmentNo"));
        colRooms.setCellValueFactory(new PropertyValueFactory<>("numberOfRooms"));
        colPeople.setCellValueFactory(new PropertyValueFactory<>("numberOfPeople"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("priceFormatted"));

        apartmentsTable.setItems(apartmentList);

        // Load current user by session username and then apartments
        String sessionUsername = UserSession.getCurrentUsername();
        if (sessionUsername != null && !sessionUsername.isBlank()) {
            loadByUsername(sessionUsername);
        } else {
            statusLabel.setText("No session user found. Please login.");
        }
    }

    @FXML
    private void handleRefresh() {
        String sessionUsername = UserSession.getCurrentUsername();
        if (sessionUsername != null && !sessionUsername.isBlank()) {
            loadByUsername(sessionUsername);
        } else {
            statusLabel.setText("No session user to refresh.");
        }
    }

    private void loadByUsername(String username) {
        String sql = "SELECT id, username, email, phone_number FROM `user` WHERE username = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    idField.setText(String.valueOf(id));
                    usernameField.setText(rs.getString("username"));
                    emailField.setText(rs.getString("email"));
                    phoneField.setText(rs.getString("phone_number"));
                    statusLabel.setText("Loaded user info.");
                    loadApartments(id);
                } else {
                    clearFields();
                    apartmentList.clear();
                    statusLabel.setText("No resident found for username: " + username);
                }
            }
        } catch (Exception e) {
            clearFields();
            apartmentList.clear();
            statusLabel.setText("Error loading resident.");
            e.printStackTrace();
        }
    }

    private void loadApartments(int residentOwnerId) {
        apartmentList.clear();
        String sql = "SELECT resident_owner_id, apartment_no, number_of_rooms, number_of_people, area, price FROM apartment WHERE resident_owner_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, residentOwnerId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    ApartmentRow row = new ApartmentRow(
                            rs.getInt("resident_owner_id"),
                            rs.getString("apartment_no"),
                            rs.getInt("number_of_rooms"),
                            rs.getInt("number_of_people"),
                            rs.getDouble("area"),
                            rs.getDouble("price")
                    );
                    apartmentList.add(row);
                }
                if (!any) {
                    statusLabel.setText("No apartments found for resident id " + residentOwnerId);
                } else {
                    statusLabel.setText("Loaded apartments.");
                }
            }
        } catch (Exception e) {
            apartmentList.clear();
            statusLabel.setText("Error loading apartments.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        idField.clear();
        usernameField.clear();
        emailField.clear();
        phoneField.clear();
    }

    // Inner class to hold apartment rows (no new file needed)
    public static class ApartmentRow {
        private final int residentOwnerId;
        private final String apartmentNo;
        private final int numberOfRooms;
        private final int numberOfPeople;
        private final double area;
        private final double price;

        public ApartmentRow(int residentOwnerId, String apartmentNo, int numberOfRooms, int numberOfPeople, double area, double price) {
            this.residentOwnerId = residentOwnerId;
            this.apartmentNo = apartmentNo;
            this.numberOfRooms = numberOfRooms;
            this.numberOfPeople = numberOfPeople;
            this.area = area;
            this.price = price;
        }

        public int getResidentOwnerId() {
            return residentOwnerId;
        }

        public String getApartmentNo() {
            return apartmentNo;
        }

        public int getNumberOfRooms() {
            return numberOfRooms;
        }

        public int getNumberOfPeople() {
            return numberOfPeople;
        }

        public double getArea() {
            return area;
        }

        // formatted price with " VND"
        public String getPriceFormatted() {
            if (Double.isNaN(price)) return "";
            // no locale-specific formatting applied here; append currency as requested
            return String.format("%.0f VND", price);
        }
    }
}
