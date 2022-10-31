package client.gui;

import client.listeners.DispacherService;
import client.services.AuthentificationController;
import lib.dto.UserDTO;

import javax.swing.*;
import java.rmi.RemoteException;

public class CreateAccountWindow extends JFrame{
    private JPanel mainPanel;
    private JTextField emailTextField;
    private JTextField nameTextField;
    private JPasswordField passwordTextField;
    private JButton createAccountButton;
    private JPanel nameLabel;
    private JLabel passwordLabel;
    private JLabel emailLabel;


    public CreateAccountWindow(){

        createAccountButton.addActionListener(ev -> {

            UserDTO userDTO = new UserDTO(nameTextField.getText(),
                    emailTextField.getText(), new String(passwordTextField.getPassword()));
            boolean singUpStatus = false;
            try {
                singUpStatus = AuthentificationController.getInstance().singUp(userDTO);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if(singUpStatus){
                JOptionPane.showMessageDialog(null,"Account created successfully!");
                dispose();
                new MainChat(nameTextField.getText());
            }else {
                JOptionPane.showMessageDialog(null,"Account couldn't be created. Username or email already exists, try again!");
            }

        });
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
