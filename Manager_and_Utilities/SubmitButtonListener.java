package Manager_and_Utilities;

import User.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubmitButtonListener implements ActionListener{

    private User user;
    private final String credentialsFilePath = "data/credentials.txt";
    private final String profilePhotoStoragePath = "img/storage/profile/";

    public SubmitButtonListener(User user){
        this.user = user;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       
    }
    
}
