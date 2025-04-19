package views;

import controllers.UserController;
import models.User.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Vista para el registro de nuevos usuarios
 */
public class RegisterView extends View {

    private final UserController userController;

    // Componentes UI
    private JTextField cedulaField;
    private JTextField nameField;
    private JTextField firstLastNameField;
    private JTextField secondLastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField birthDateField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> userTypeComboBox;

    public RegisterView(UserController userController) {
        super();
        this.userController = userController;
        initComponents();
    }

    private void initComponents() {
        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        // Panel de formulario usando GridBagLayout para mejor control
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Crear componentes
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Cédula:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cedulaField = new JTextField(15);
        formPanel.add(cedulaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Primer Apellido:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        firstLastNameField = new JTextField(15);
        formPanel.add(firstLastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Segundo Apellido:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        secondLastNameField = new JTextField(15);
        formPanel.add(secondLastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Dirección:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addressField = new JTextField(15);
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Fecha de Nacimiento (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        birthDateField = new JTextField(15);
        formPanel.add(birthDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Confirmar Contraseña:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        confirmPasswordField = new JPasswordField(15);
        formPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Tipo de Usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userTypeComboBox = new JComboBox<>(new String[]{"Cliente", "Vendedor", "Administrador"});
        formPanel.add(userTypeComboBox, gbc);

        // Botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton registerButton = new JButton("Registrarse");
        registerButton.addActionListener(this::registerUser);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> {
            close();
            // Volver a la pantalla de login
            LoginView loginView = new LoginView(userController);
            loginView.initialize("Iniciar Sesión");
            loginView.display();
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        // Panel principal con scroll (para pantallas pequeñas)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Añadir todo al panel base
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void registerUser(ActionEvent e) {
        // Validación básica
        if (cedulaField.getText().isEmpty() ||
                nameField.getText().isEmpty() ||
                firstLastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() ||
                addressField.getText().isEmpty() ||
                birthDateField.getText().isEmpty()) {

            showError("Por favor, complete todos los campos obligatorios.");
            return;
        }

        // Validar contraseñas
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (password.isEmpty()) {
            showError("La contraseña no puede estar vacía.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden.");
            return;
        }

        // Validar fecha
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthDateField.getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException ex) {
            showError("Formato de fecha inválido. Use YYYY-MM-DD.");
            return;
        }

        // Obtener tipo de usuario seleccionado
        UserType userType;
        switch (userTypeComboBox.getSelectedIndex()) {
            case 0:
                userType = UserType.USER;
                break;
            case 1:
                userType = UserType.SELLER;
                break;
            case 2:
                userType = UserType.ADMIN;
                break;
            default:
                userType = UserType.USER;
        }

        // Registrar usuario
        boolean success = userController.registerUser(
                cedulaField.getText(),
                nameField.getText(),
                firstLastNameField.getText(),
                secondLastNameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                addressField.getText(),
                birthDate,
                password,
                userType
        );

        if (success) {
            showInfo("Usuario registrado exitosamente!");
            close();

            // Volver a la pantalla de login
            LoginView loginView = new LoginView(userController);
            loginView.initialize("Iniciar Sesión");
            loginView.display();
        } else {
            showError("Error al registrar usuario. El email o cédula ya existe.");
        }
    }
}