package controllers;

import models.User;
import models.User.UserType;
import services.AuthService;
import services.AuthService.LoginResult;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class UserController {
    
    private final AuthService authService;
    
    // Patrones para validación
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern CEDULA_PATTERN = 
            Pattern.compile("^[0-9]{9,12}$");
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^[0-9]{8,12}$");
    
    public UserController(AuthService authService) {
        this.authService = authService;
    }
    
    public LoginResult login(String email, String password) {
        if (!isValidEmail(email)) {
            return new LoginResult(false, "Formato de email inválido", null);
        }
        
        return authService.login(email, password);
    }
    
    public void logout() {
        authService.logout();
    }
    
    public boolean registerUser(String cedula, String name, String firstLastName, 
            String secondLastName, String email, String phone, String address, 
            LocalDate birthDate, String password, UserType userType) {
        
        // Validar datos
        if (!isValidCedula(cedula)) {
            return false;
        }
        
        if (!isValidEmail(email)) {
            return false;
        }
        
        if (!isValidPhone(phone)) {
            return false;
        }
        
        if (name == null || name.trim().isEmpty() || 
            firstLastName == null || firstLastName.trim().isEmpty() ||
            secondLastName == null || secondLastName.trim().isEmpty() ||
            address == null || address.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // Crear usuario
        User newUser = new User(
            cedula, name, firstLastName, secondLastName, 
            email, phone, address, birthDate, password, userType
        );
        
        return authService.registerUser(newUser);
    }
    
    public User getCurrentUser() {
        return authService.getCurrentUser();
    }
    
    public boolean isLoggedIn() {
        return authService.isLoggedIn();
    }
    
    public List<String> getTestUserCredentials() {
        return authService.getTestUserCredentials();
    }
    
    // Métodos de validación
    
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    private boolean isValidCedula(String cedula) {
        return cedula != null && CEDULA_PATTERN.matcher(cedula).matches();
    }
    
    private boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
}