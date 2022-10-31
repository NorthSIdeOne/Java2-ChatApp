package client.gui;

import client.MainClient;
import client.listeners.DispacherService;
import client.services.AuthentificationController;
import client.services.ChatController;
import lib.dto.UserDTO;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.Arrays;

public class LoginWindow extends JFrame {
    private JPanel mainPanel;
    private JButton loginButton;
    private JButton createAccountButton;
    private JTextField emailTextField;
    private JPasswordField passwordTextField;
    private JLabel emailLabel;
    private JLabel passwordLabel;


    public LoginWindow() {

        loginButton.addActionListener(ev -> {
            UserDTO userDTO = new UserDTO(emailTextField.getText(), new String(passwordTextField.getPassword()));
            UserDTO loginResult = null;
            try {
                loginResult = AuthentificationController.getInstance().login(userDTO);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if(loginResult == null){
                JOptionPane.showMessageDialog(null,"Failed login: email or password are wrong");
            }else {
                JOptionPane.showMessageDialog(null,"Successfully login!");
                dispose();
                new MainChat(loginResult.getUsername());

            }
        });

        createAccountButton.addActionListener(ev -> {
            dispose();
            new CreateAccountWindow();
        });

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
