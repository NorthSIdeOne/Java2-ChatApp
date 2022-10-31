package client;

import client.gui.LoginWindow;

public class MainClient {

    public static String username;

    public static void main(String[] args) {

        new LoginWindow();
    }

    public static void setUsername(String username) {
        MainClient.username = username;
    }

}
