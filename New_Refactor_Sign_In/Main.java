package New_Refactor_Sign_In;

import javax.swing.SwingUtilities;

public class Main {
     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RefactoredSignIn frame = new RefactoredSignIn();
            frame.setVisible(true);
        });
    }
}
