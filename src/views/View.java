package views;

import javax.swing.*;
import java.awt.*;

public abstract class View extends JPanel {

    protected JFrame mainFrame;

    public View() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Inicializa la vista en un frame propio
     */
    public void initialize(String title) {
        mainFrame = new JFrame(title);
        mainFrame.setContentPane(this);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        // Personalización visual básica
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la vista
     */
    public void display() {
        if (mainFrame != null) {
            mainFrame.setVisible(true);
        }
    }

    /**
     * Cierra la vista
     */
    public void close() {
        if (mainFrame != null) {
            mainFrame.dispose();
        }
    }

    /**
     * Muestra un mensaje de error
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Muestra un mensaje de información
     */
    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Información",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Muestra un diálogo de confirmación
     */
    protected boolean confirm(String message) {
        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );
        return option == JOptionPane.YES_OPTION;
    }
}