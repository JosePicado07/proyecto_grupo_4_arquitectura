package services;

import models.User;
import models.User.UserType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AuthService {
    
    private final List<User> users;
    private final Map<String, Integer> loginAttempts;
    private final Map<String, LocalDateTime> lockoutTimes;
    private User currentUser;
    
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int LOCKOUT_MINUTES = 15;
    
    public AuthService() {
        this.users = new ArrayList<>();
        this.loginAttempts = new HashMap<>();
        this.lockoutTimes = new HashMap<>();
        this.currentUser = null;
        
        initializeTestUsers();
    }
    
    private void initializeTestUsers() {
        // Usuario administrador
        User admin = new User(
                "123456789",
                "Admin",
                "System",
                "Admin",
                "admin@techstore.com",
                "12345678",
                "Tech Store Office",
                LocalDate.of(1990, 1, 1),
                "admin123",
                UserType.ADMIN
        );
        
        // Usuario vendedor
        User seller = new User(
                "987654321",
                "Vendedor",
                "Demo",
                "Store",
                "vendedor@techstore.com",
                "87654321",
                "Tech Store Office",
                LocalDate.of(1995, 5, 5),
                "vendedor123",
                UserType.SELLER
        );
        
        // Usuario cliente
        User customer = new User(
                "111222333",
                "Cliente",
                "Demo",
                "User",
                "cliente@example.com",
                "55555555",
                "San José, Costa Rica",
                LocalDate.of(2000, 10, 10),
                "cliente123",
                UserType.USER
        );
        
        users.add(admin);
        users.add(seller);
        users.add(customer);
    }
    
    public boolean registerUser(User user) {
        // Verificar si ya existe un usuario con la misma cédula o email
        boolean exists = users.stream()
                .anyMatch(u -> u.getCedula().equals(user.getCedula()) 
                        || u.getEmail().equals(user.getEmail()));
        
        if (exists) {
            return false;
        }
        
        return users.add(user);
    }
    
    public LoginResult login(String email, String password) {
        // Verificar si el usuario está bloqueado
        if (isUserLocked(email)) {
            LocalDateTime unlockTime = lockoutTimes.get(email).plusMinutes(LOCKOUT_MINUTES);
            return new LoginResult(false, "Usuario bloqueado. Intente nuevamente después de " 
                    + unlockTime.toString(), null);
        }
        
        // Buscar usuario por email
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
        
        // Si no se encuentra el usuario
        if (userOpt.isEmpty()) {
            incrementLoginAttempts(email);
            return new LoginResult(false, "Email o contraseña incorrectos", null);
        }
        
        User user = userOpt.get();
        
        // Verificar contraseña
        if (!user.validateUser(email, password)) {
            incrementLoginAttempts(email);
            int attemptsLeft = MAX_LOGIN_ATTEMPTS - loginAttempts.get(email);
            
            if (attemptsLeft > 0) {
                return new LoginResult(false, "Email o contraseña incorrectos. Intentos restantes: " 
                        + attemptsLeft, null);
            } else {
                lockUser(email);
                return new LoginResult(false, "Demasiados intentos fallidos. Usuario bloqueado por " 
                        + LOCKOUT_MINUTES + " minutos", null);
            }
        }
        
        // Login exitoso
        resetLoginAttempts(email);
        this.currentUser = user;
        return new LoginResult(true, "Inicio de sesión exitoso", user);
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public List<String> getTestUserCredentials() {
        List<String> credentials = new ArrayList<>();
        
        users.forEach(user -> {
            credentials.add(String.format(
                "Usuario: %s %s (%s) | Email: %s | Contraseña: %s | Tipo: %s",
                user.getName(),
                user.getFirstLastName(),
                user.getCedula(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType()
            ));
        });
        
        return credentials;
    }
    
    // Métodos para manejo de intentos de login
    
    private boolean isUserLocked(String email) {
        if (!lockoutTimes.containsKey(email)) {
            return false;
        }
        
        LocalDateTime lockTime = lockoutTimes.get(email);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(lockTime.plusMinutes(LOCKOUT_MINUTES))) {
            // Si ya pasó el tiempo de bloqueo, lo quitamos
            lockoutTimes.remove(email);
            resetLoginAttempts(email);
            return false;
        }
        
        return true;
    }
    
    private void incrementLoginAttempts(String email) {
        loginAttempts.put(email, loginAttempts.getOrDefault(email, 0) + 1);
    }
    
    private void resetLoginAttempts(String email) {
        loginAttempts.put(email, 0);
    }
    
    private void lockUser(String email) {
        lockoutTimes.put(email, LocalDateTime.now());
    }
    
    public static class LoginResult {
        private final boolean success;
        private final String message;
        private final User user;
        
        public LoginResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public User getUser() {
            return user;
        }
    }
}