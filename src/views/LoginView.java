package views;

import controllers.UserController;
import services.AuthService.LoginResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Vista para el inicio de sesión de usuarios
 */
public class LoginView extends View {

    private final UserController userController;

    // Componentes UI
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton testUsersButton;

    public LoginView(UserController userController) {
        super();
        this.userController = userController;
        initComponents();
    }

    private void initComponents() {
        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Tech Costa Rica");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de logo (simulado)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel("🖥️");
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 72));
        logoPanel.add(logoLabel);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel emailLabel = new JLabel("Correo electrónico:");
        emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(this::login);

        registerButton = new JButton("Registrarse");
        registerButton.addActionListener(this::openRegister);

        testUsersButton = new JButton("Ver Usuarios de Prueba");
        testUsersButton.addActionListener(this::showTestUsers);

        // Añadir componentes con GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(testUsersButton, gbc);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.add(logoPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Añadir todo al panel base
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);

        // Añadir un pie de página
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.add(new JLabel("© 2025 Tech Costa Rica - Todos los derechos reservados"));
        this.add(footerPanel, BorderLayout.SOUTH);
    }

    private void login(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showError("Por favor, complete todos los campos.");
            return;
        }

        LoginResult result = userController.login(email, password);

        if (result.isSuccess()) {
            showInfo("Inicio de sesión exitoso!");
            close();

            // Abrir la vista principal
            MainView mainView = new MainView(userController);
            mainView.initialize("Tech Costa Rica - Menú Principal");
            mainView.display();
        } else {
            showError(result.getMessage());
        }
    }

    private void openRegister(ActionEvent e) {
        RegisterView registerView = new RegisterView(userController);
        registerView.initialize("Registrarse");
        registerView.display();
        close();
    }

    private void showTestUsers(ActionEvent e) {
        StringBuilder message = new StringBuilder("Usuarios de prueba disponibles:\n\n");

        for (String credentials : userController.getTestUserCredentials()) {
            message.append(credentials).append("\n");
        }

        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Usuarios de Prueba",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}