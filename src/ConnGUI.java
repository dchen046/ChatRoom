import javax.swing.*;
import java.awt.*;
import java.net.Socket;

/*
    GUI for getting username, address, and port number from user
 */
public class ConnGUI {
    private JPanel cPanel;
    private JTextField usernameV, addrV, portV;

    public ConnGUI() {
        usernameV = new JTextField();
        addrV = new JTextField();
        portV = new JTextField();

        cPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        cPanel.add(new JLabel("Username:", SwingConstants.RIGHT));
        cPanel.add(usernameV);
        cPanel.add(new JLabel("Address:", SwingConstants.RIGHT));
        cPanel.add(addrV);
        cPanel.add(new JLabel("Port:", SwingConstants.RIGHT));
        cPanel.add(portV);
    }

    public void show() {
        int result;
        String text = "Please Enter Username, Address, and Port";
        do {
            result = JOptionPane.showConfirmDialog(null, cPanel, text, JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }
        } while (result == JOptionPane.OK_OPTION && !validCredentials());
    }

    public String getUsername() {
        return usernameV.getText();
    }

    public String getAddr() {
        return addrV.getText();
    }

    public int getPort() {
        return Integer.parseInt(portV.getText());
    }

    // tests connection with address and port
    private boolean validCredentials() {
        try {
            String addr = addrV.getText();
            int port = Integer.parseInt(portV.getText());
            Socket test = new Socket(addr, port);
            return true;
        } catch (Exception io) {
            addrV.setText("");
            portV.setText("");
            return false;
        }
    }
}
