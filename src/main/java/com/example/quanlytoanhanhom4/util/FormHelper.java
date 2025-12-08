package com.example.quanlytoanhanhom4.util;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class for creating modern, professional forms with visual validation feedback.
 * Provides helper methods for form styling, validation, and user experience enhancements.
 */
public final class FormHelper {

    private FormHelper() {
        // Utility class
    }

    /**
     * Creates a form field group with label, input, and helper text.
     * 
     * @param labelText The label text
     * @param control The input control (TextField, ComboBox, etc.)
     * @param required Whether the field is required
     * @return VBox containing the form field group
     */
    public static VBox createFormField(String labelText, Control control, boolean required) {
        VBox fieldGroup = new VBox(6);
        fieldGroup.getStyleClass().add("input-group");
        
        // Label
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        if (required) {
            label.getStyleClass().add("form-label-required");
            label.setText(labelText + " *");
        }
        
        // Helper text label (initially hidden)
        Label helperLabel = new Label();
        helperLabel.getStyleClass().add("form-helper");
        helperLabel.setVisible(false);
        helperLabel.setManaged(false);
        
        fieldGroup.getChildren().addAll(label, control, helperLabel);
        
        // Store helper label reference for later use
        control.getProperties().put("helperLabel", helperLabel);
        
        return fieldGroup;
    }

    /**
     * Shows validation error on a form field.
     * 
     * @param control The control to show error on
     * @param errorMessage The error message to display
     */
    public static void showError(Control control, String errorMessage) {
        control.getStyleClass().removeAll("error", "success");
        control.getStyleClass().add("error");
        
        Label helperLabel = (Label) control.getProperties().get("helperLabel");
        if (helperLabel != null) {
            helperLabel.getStyleClass().removeAll("form-helper", "form-error", "form-success");
            helperLabel.getStyleClass().add("form-error");
            helperLabel.setText(errorMessage);
            helperLabel.setVisible(true);
            helperLabel.setManaged(true);
        }
    }

    /**
     * Shows success state on a form field.
     * 
     * @param control The control to show success on
     * @param successMessage Optional success message
     */
    public static void showSuccess(Control control, String successMessage) {
        control.getStyleClass().removeAll("error", "success");
        control.getStyleClass().add("success");
        
        Label helperLabel = (Label) control.getProperties().get("helperLabel");
        if (helperLabel != null && successMessage != null && !successMessage.isEmpty()) {
            helperLabel.getStyleClass().removeAll("form-helper", "form-error", "form-success");
            helperLabel.getStyleClass().add("form-success");
            helperLabel.setText(successMessage);
            helperLabel.setVisible(true);
            helperLabel.setManaged(true);
        }
    }

    /**
     * Clears validation state from a form field.
     * 
     * @param control The control to clear
     */
    public static void clearValidation(Control control) {
        control.getStyleClass().removeAll("error", "success");
        
        Label helperLabel = (Label) control.getProperties().get("helperLabel");
        if (helperLabel != null) {
            helperLabel.setVisible(false);
            helperLabel.setManaged(false);
            helperLabel.setText("");
        }
    }

    /**
     * Validates a text field and shows error if invalid.
     * 
     * @param field The text field to validate
     * @param validator The validation function
     * @param errorMessage The error message if invalid
     * @return true if valid, false otherwise
     */
    public static boolean validateTextField(TextField field, java.util.function.Predicate<String> validator, String errorMessage) {
        String value = field.getText() != null ? field.getText().trim() : "";
        boolean isValid = validator.test(value);
        
        if (!isValid) {
            showError(field, errorMessage);
        } else {
            clearValidation(field);
        }
        
        return isValid;
    }

    /**
     * Validates a required text field.
     * 
     * @param field The text field to validate
     * @param fieldName The name of the field for error message
     * @return true if valid, false otherwise
     */
    public static boolean validateRequired(TextField field, String fieldName) {
        return validateTextField(field, 
            value -> value != null && !value.isEmpty(),
            fieldName + " không được để trống");
    }

    /**
     * Validates email format.
     * 
     * @param field The text field containing email
     * @return true if valid, false otherwise
     */
    public static boolean validateEmail(TextField field) {
        return validateTextField(field,
            value -> value.isEmpty() || ValidationUtils.isValidEmail(value),
            "Email không hợp lệ");
    }

    /**
     * Validates phone number format.
     * 
     * @param field The text field containing phone
     * @return true if valid, false otherwise
     */
    public static boolean validatePhone(TextField field) {
        return validateTextField(field,
            value -> value.isEmpty() || ValidationUtils.isValidPhone(value),
            "Số điện thoại không hợp lệ (10-11 chữ số)");
    }

    /**
     * Validates a combo box selection.
     * 
     * @param comboBox The combo box to validate
     * @param fieldName The name of the field for error message
     * @return true if valid, false otherwise
     */
    public static boolean validateComboBox(ComboBox<?> comboBox, String fieldName) {
        boolean isValid = comboBox.getValue() != null;
        
        if (!isValid) {
            showError(comboBox, "Vui lòng chọn " + fieldName);
        } else {
            clearValidation(comboBox);
        }
        
        return isValid;
    }

    /**
     * Validates a date picker.
     * 
     * @param datePicker The date picker to validate
     * @param fieldName The name of the field for error message
     * @return true if valid, false otherwise
     */
    public static boolean validateDatePicker(DatePicker datePicker, String fieldName) {
        boolean isValid = datePicker.getValue() != null;
        
        if (!isValid) {
            showError(datePicker, "Vui lòng chọn " + fieldName);
        } else {
            clearValidation(datePicker);
        }
        
        return isValid;
    }

    /**
     * Adds real-time validation to a text field.
     * 
     * @param field The text field
     * @param validator The validation function
     * @param errorMessage The error message if invalid
     */
    public static void addRealtimeValidation(TextField field, java.util.function.Predicate<String> validator, String errorMessage) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                validateTextField(field, validator, errorMessage);
            } else {
                clearValidation(field);
            }
        });
    }

    /**
     * Adds tooltip to a control.
     * 
     * @param control The control
     * @param tooltipText The tooltip text
     */
    public static void addTooltip(Control control, String tooltipText) {
        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(tooltipText);
        tooltip.getStyleClass().add("tooltip");
        tooltip.setShowDelay(javafx.util.Duration.millis(300));
        javafx.scene.control.Tooltip.install(control, tooltip);
    }

    /**
     * Creates a modern card container.
     * 
     * @param content The content to put in the card
     * @return VBox styled as a card
     */
    public static VBox createCard(javafx.scene.Node... content) {
        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.getChildren().addAll(content);
        return card;
    }

    /**
     * Creates a section header.
     * 
     * @param title The section title
     * @param subtitle Optional subtitle
     * @return VBox containing the section header
     */
    public static VBox createSectionHeader(String title, String subtitle) {
        VBox header = new VBox(4);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");
        
        header.getChildren().add(titleLabel);
        
        if (subtitle != null && !subtitle.isEmpty()) {
            Label subtitleLabel = new Label(subtitle);
            subtitleLabel.getStyleClass().add("section-subtitle");
            header.getChildren().add(subtitleLabel);
        }
        
        return header;
    }

    /**
     * Creates a search bar with icon.
     * 
     * @param searchField The search text field
     * @return HBox styled as search bar
     */
    public static HBox createSearchBar(TextField searchField) {
        HBox searchBar = new HBox(12);
        searchBar.getStyleClass().add("search-bar");
        searchBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label searchIcon = new Label("🔍");
        searchIcon.setStyle("-fx-font-size: 18px;");
        
        searchField.getStyleClass().add("search-field");
        searchField.setPromptText("Tìm kiếm...");
        
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        searchBar.getChildren().addAll(searchIcon, searchField);
        
        return searchBar;
    }

    /**
     * Creates a badge label.
     * 
     * @param text The badge text
     * @param type The badge type (success, warning, error, info, neutral)
     * @return Label styled as badge
     */
    public static Label createBadge(String text, String type) {
        Label badge = new Label(text);
        badge.getStyleClass().addAll("badge", "badge-" + type);
        return badge;
    }

    /**
     * Creates a stats card.
     * 
     * @param value The stat value
     * @param label The stat label
     * @param icon Optional icon emoji
     * @return VBox styled as stats card
     */
    public static VBox createStatsCard(String value, String label, String icon) {
        VBox card = new VBox(8);
        card.getStyleClass().add("stats-card");
        card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        HBox header = new HBox(12);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        if (icon != null && !icon.isEmpty()) {
            Label iconLabel = new Label(icon);
            iconLabel.getStyleClass().add("stats-card-icon");
            header.getChildren().add(iconLabel);
        }
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stats-card-value");
        header.getChildren().add(valueLabel);
        
        Label labelLabel = new Label(label);
        labelLabel.getStyleClass().add("stats-card-label");
        
        card.getChildren().addAll(header, labelLabel);
        
        return card;
    }

    /**
     * Adds keyboard shortcuts to a form.
     * 
     * @param scene The scene
     * @param onSave Runnable to execute on Ctrl+S
     * @param onCancel Runnable to execute on ESC
     */
    public static void addKeyboardShortcuts(javafx.scene.Scene scene, Runnable onSave, Runnable onCancel) {
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == javafx.scene.input.KeyCode.S) {
                event.consume();
                if (onSave != null) {
                    onSave.run();
                }
            } else if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                event.consume();
                if (onCancel != null) {
                    onCancel.run();
                }
            }
        });
    }

    /**
     * Creates a loading overlay.
     * 
     * @return StackPane with loading indicator
     */
    public static javafx.scene.layout.StackPane createLoadingOverlay() {
        javafx.scene.layout.StackPane overlay = new javafx.scene.layout.StackPane();
        overlay.getStyleClass().add("loading-overlay");
        overlay.setAlignment(javafx.geometry.Pos.CENTER);
        
        javafx.scene.control.ProgressIndicator spinner = new javafx.scene.control.ProgressIndicator();
        spinner.getStyleClass().add("loading-spinner");
        // ProgressIndicator is indeterminate by default, which shows spinning animation
        // No need to set progress for indeterminate spinner
        
        Label loadingLabel = new Label("Đang tải...");
        loadingLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
        
        VBox loadingContent = new VBox(12);
        loadingContent.setAlignment(javafx.geometry.Pos.CENTER);
        loadingContent.getChildren().addAll(spinner, loadingLabel);
        
        overlay.getChildren().add(loadingContent);
        
        return overlay;
    }

    /**
     * Creates an empty state view.
     * 
     * @param icon The icon emoji
     * @param title The title
     * @param description The description
     * @param actionButton Optional action button
     * @return VBox styled as empty state
     */
    public static VBox createEmptyState(String icon, String title, String description, javafx.scene.control.Button actionButton) {
        VBox emptyState = new VBox(16);
        emptyState.getStyleClass().add("empty-state");
        emptyState.setAlignment(javafx.geometry.Pos.CENTER);
        
        if (icon != null && !icon.isEmpty()) {
            Label iconLabel = new Label(icon);
            iconLabel.getStyleClass().add("empty-state-icon");
            emptyState.getChildren().add(iconLabel);
        }
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("empty-state-title");
        emptyState.getChildren().add(titleLabel);
        
        if (description != null && !description.isEmpty()) {
            Label descLabel = new Label(description);
            descLabel.getStyleClass().add("empty-state-description");
            emptyState.getChildren().add(descLabel);
        }
        
        if (actionButton != null) {
            actionButton.getStyleClass().add("btn-primary");
            emptyState.getChildren().add(actionButton);
        }
        
        return emptyState;
    }
}

