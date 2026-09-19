package battlescript;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        try {

            for (UIManager.LookAndFeelInfo info
                    : UIManager
                            .getInstalledLookAndFeels()) {

                if ("Nimbus"
                        .equals(info.getName())) {

                    UIManager.setLookAndFeel(
                            info.getClassName()
                    );

                    break;
                }
            }

        } catch (Exception ex) {

        }

        SwingUtilities.invokeLater(() -> {

            MainFrame frame =
                    new MainFrame();

            frame.setVisible(true);
        });
    }
}