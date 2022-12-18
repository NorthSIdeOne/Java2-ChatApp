package client;

import client.gui.LoginWindow;

import javax.swing.*;

public class MainClient {


    public static void main(String[] args) {

        String message = "1. First, start the server after the client \n" +
                " 2. You need to login with email and password. If you don't have one, create an account by pressing 'Creat Account' \n " +
                "3. After you are logged in , create a new chat by adding in the left corner the user you want to start a chat with and press 'Create Chat'. " +
                "If the user does not exists you can't create a new chat :) \n" +
                "PS: You can create multiple accounts to try this feature." ;
        JOptionPane.showMessageDialog(null,message);
        new LoginWindow();
    }


}
