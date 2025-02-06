import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextField;

public class FieldsListener implements MouseListener {

    private JTextField textField;
    int count = 0;

    public FieldsListener(JTextField textField) {
        this.textField = textField;
    }

    // resets the data in the field once to prevent deletion upon missClick
    @Override
    public void mouseClicked(MouseEvent e) {
        if (count == 0) {
            textField.setText("");
            count++;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
