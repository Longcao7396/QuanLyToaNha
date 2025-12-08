package com.example.quanlytoanhanhom4.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for input validation.
 */
public final class ValidationUtils {

    // Email pattern: allows most common email formats
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    // Phone pattern: 10-11 digits (Vietnamese phone numbers)
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10,11}$");
    
    // Identity card pattern: 9 or 12 digits
    private static final Pattern IDENTITY_CARD_PATTERN = 
        Pattern.compile("^[0-9]{9,12}$");

    private ValidationUtils() {
        // Utility class
    }

    /**
     * Validates an email address.
     * 
     * @param email The email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates a phone number (10-11 digits).
     * 
     * @param phone The phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Remove spaces and dashes
        String cleaned = phone.replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleaned).matches();
    }

    /**
     * Validates an identity card number (9-12 digits).
     * 
     * @param identityCard The identity card number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidIdentityCard(String identityCard) {
        if (identityCard == null || identityCard.trim().isEmpty()) {
            return false;
        }
        return IDENTITY_CARD_PATTERN.matcher(identityCard.trim()).matches();
    }

    /**
     * Validates that a string is not null or empty.
     * 
     * @param value The string to validate
     * @param fieldName The name of the field (for error messages)
     * @return ValidationResult with errors if invalid
     */
    public static ValidationResult validateRequired(String value, String fieldName) {
        ValidationResult result = new ValidationResult();
        if (value == null || value.trim().isEmpty()) {
            result.addError(fieldName + " không được để trống");
        }
        return result;
    }

    /**
     * Validates email format.
     * 
     * @param email The email to validate
     * @param fieldName The name of the field
     * @return ValidationResult with errors if invalid
     */
    public static ValidationResult validateEmail(String email, String fieldName) {
        ValidationResult result = new ValidationResult();
        if (email != null && !email.trim().isEmpty() && !isValidEmail(email)) {
            result.addError(fieldName + " không hợp lệ");
        }
        return result;
    }

    /**
     * Validates phone number format.
     * 
     * @param phone The phone number to validate
     * @param fieldName The name of the field
     * @return ValidationResult with errors if invalid
     */
    public static ValidationResult validatePhone(String phone, String fieldName) {
        ValidationResult result = new ValidationResult();
        if (phone != null && !phone.trim().isEmpty() && !isValidPhone(phone)) {
            result.addError(fieldName + " không hợp lệ (10-11 chữ số)");
        }
        return result;
    }

    /**
     * Validates identity card format.
     * 
     * @param identityCard The identity card number to validate
     * @param fieldName The name of the field
     * @return ValidationResult with errors if invalid
     */
    public static ValidationResult validateIdentityCard(String identityCard, String fieldName) {
        ValidationResult result = new ValidationResult();
        if (identityCard != null && !identityCard.trim().isEmpty() && !isValidIdentityCard(identityCard)) {
            result.addError(fieldName + " không hợp lệ (9-12 chữ số)");
        }
        return result;
    }

    /**
     * Validates string length.
     * 
     * @param value The string to validate
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @param fieldName The name of the field
     * @return ValidationResult with errors if invalid
     */
    public static ValidationResult validateLength(String value, int minLength, int maxLength, String fieldName) {
        ValidationResult result = new ValidationResult();
        if (value != null) {
            int length = value.trim().length();
            if (length < minLength) {
                result.addError(fieldName + " phải có ít nhất " + minLength + " ký tự");
            } else if (length > maxLength) {
                result.addError(fieldName + " không được vượt quá " + maxLength + " ký tự");
            }
        }
        return result;
    }

    /**
     * Result class for validation operations.
     */
    public static class ValidationResult {
        private final List<String> errors = new ArrayList<>();

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return new ArrayList<>(errors);
        }

        public String getErrorMessage() {
            return String.join("\n", errors);
        }

        public void merge(ValidationResult other) {
            this.errors.addAll(other.errors);
        }
    }
}

