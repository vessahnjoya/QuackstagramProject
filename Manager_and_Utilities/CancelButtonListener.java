package Manager_and_Utilities;

import Refactor_UI.*;
import User.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CancelButtonListener implements ActionListener {

    private User currentUser;

    public CancelButtonListener(User user){
        currentUser = user;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        InstagramProfileUI igProfile = new InstagramProfileUI(currentUser);
        igProfile.setVisible(true);

    }
    
}
