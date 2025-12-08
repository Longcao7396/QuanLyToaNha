package com.example.quanlytoanhanhom4.util;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Helper class for creating auto-complete functionality in ComboBox and TextField.
 * Provides intelligent search and filtering capabilities.
 */
public final class AutoCompleteHelper {

    private AutoCompleteHelper() {
        // Utility class
    }

    /**
     * Makes a ComboBox searchable with auto-complete functionality.
     * 
     * @param comboBox The combo box to make searchable
     * @param allItems All available items
     * @param searchFunction Function to extract searchable text from items
     * @param <T> Type of items
     */
    public static <T> void makeSearchable(ComboBox<T> comboBox, ObservableList<T> allItems, Function<T, String> searchFunction) {
        // Store original items
        comboBox.getProperties().put("allItems", allItems);
        comboBox.getProperties().put("searchFunction", searchFunction);
        
        // Set items
        comboBox.setItems(allItems);
        
        // Make editable
        comboBox.setEditable(true);
        
        // Get the editor (TextField inside ComboBox)
        TextField editor = comboBox.getEditor();
        
        // Add listener for text changes
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                comboBox.setItems(allItems);
                return;
            }
            
            // Filter items based on search text
            String searchText = newValue.toLowerCase().trim();
            ObservableList<T> filtered = allItems.stream()
                    .filter(item -> {
                        String searchableText = searchFunction.apply(item);
                        return searchableText != null && searchableText.toLowerCase().contains(searchText);
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            
            comboBox.setItems(filtered);
            
            // Show dropdown if there are filtered items
            if (!filtered.isEmpty() && !comboBox.isShowing()) {
                comboBox.show();
            }
        });
        
        // Handle keyboard navigation
        comboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Select first item if available
                if (!comboBox.getItems().isEmpty()) {
                    comboBox.setValue(comboBox.getItems().get(0));
                    comboBox.hide();
                    event.consume();
                }
            }
        });
    }

    /**
     * Makes a ComboBox searchable with simple string items.
     * 
     * @param comboBox The combo box
     * @param allItems All available string items
     */
    public static void makeSearchable(ComboBox<String> comboBox, ObservableList<String> allItems) {
        makeSearchable(comboBox, allItems, Function.identity());
    }

    /**
     * Creates an auto-complete TextField with suggestions.
     * 
     * @param textField The text field
     * @param suggestions List of suggestions
     * @param onSelect Callback when a suggestion is selected
     */
    public static void addAutoComplete(TextField textField, List<String> suggestions, java.util.function.Consumer<String> onSelect) {
        // Create popup for suggestions
        Popup popup = new Popup();
        ListView<String> listView = new ListView<>();
        ObservableList<String> filteredSuggestions = FXCollections.observableArrayList();
        listView.setItems(filteredSuggestions);
        listView.setPrefWidth(textField.getWidth());
        listView.setPrefHeight(150);
        
        popup.getContent().add(listView);
        
        // Filter suggestions based on input
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                popup.hide();
                return;
            }
            
            String searchText = newValue.toLowerCase();
            filteredSuggestions.setAll(
                suggestions.stream()
                    .filter(s -> s.toLowerCase().contains(searchText))
                    .limit(10)
                    .collect(Collectors.toList())
            );
            
            if (!filteredSuggestions.isEmpty()) {
                // Show popup below text field
                javafx.geometry.Bounds bounds = textField.localToScreen(textField.getBoundsInLocal());
                if (textField.getScene() != null && textField.getScene().getWindow() != null) {
                    popup.show(textField.getScene().getWindow(), bounds.getMinX(), bounds.getMaxY());
                }
            } else {
                popup.hide();
            }
        });
        
        // Handle selection
        listView.setOnMouseClicked(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                textField.setText(selected);
                popup.hide();
                if (onSelect != null) {
                    onSelect.accept(selected);
                }
            }
        });
        
        // Handle keyboard
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN && popup.isShowing()) {
                listView.requestFocus();
                if (!listView.getItems().isEmpty()) {
                    listView.getSelectionModel().select(0);
                }
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER && popup.isShowing()) {
                String selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    textField.setText(selected);
                    popup.hide();
                    if (onSelect != null) {
                        onSelect.accept(selected);
                    }
                }
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                popup.hide();
                event.consume();
            }
        });
        
        // Hide popup when text field loses focus
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Platform.runLater(() -> {
                    if (!listView.isFocused()) {
                        popup.hide();
                    }
                });
            }
        });
    }
}

