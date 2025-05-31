package zoologia;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application class for the Music Genre Explorer system.
 * This application allows users to explore musical genres through
 * frame-based knowledge representation and inheritance.
 */
public class Zoologia {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system Look and Feel");
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                new MusicGenreGUI().setVisible(true);
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
