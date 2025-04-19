package utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    
    private static final Pattern CEDULA_PATTERN = 
            Pattern.compile("^[0-9]{9,12}$");
    
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^[0-9]{8,12}$");
    
    private static final Pattern CREDIT_CARD_PATTERN = 
            Pattern.compile("^[0-9]{16}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidCedula(String cedula) {
        return cedula != null && CEDULA_PATTERN.matcher(cedula).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidCreditCard(String cardNumber) {
        return cardNumber != null && CREDIT_CARD_PATTERN.matcher(cardNumber).matches() && 
               isLuhnValid(cardNumber);
    }
    
    // Algoritmo de Luhn para validar números de tarjeta
    private static boolean isLuhnValid(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }
    
    public static boolean isValidPrice(double price) {
        return price > 0 && price < 10000000;
    }
    
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0 && quantity <= 1000;
    }
    
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
    
    public static boolean isValidDiscountValue(double value, boolean isPercentage) {
        if (isPercentage) {
            return value > 0 && value <= 100;
        } else {
            return value > 0;
        }
    }
}