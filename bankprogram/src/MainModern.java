import javax.swing.*;

public class MainModern {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            ModernBankApp app = new ModernBankApp();
            app.setVisible(true);
        });
    }
}