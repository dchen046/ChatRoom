import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/*
    Server UI to get which port the server should allow connections from
 */
public class ServerGUI {
    private JFrame frame;
    private JLabel portLabel;
    private JTextField portNum;
    private JButton send, close;
    private JPanel base;

    public ServerGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 125);
        frame.setTitle("Server");
        frame.setResizable(false);

        portLabel = new JLabel("Port:", SwingConstants.RIGHT);
        portNum = new JTextField();
        send = new JButton("Start");
        close = new JButton("Close");
        setButtonActions();

        base = new JPanel(new GridLayout(2, 2, 5, 5));
        base.add(portLabel);
        base.add(portNum);
        base.add(close);
        base.add(send);
        frame.add(base);
    }

    private void setButtonActions(){
        send.addActionListener(e -> {
            if (portNum.getText().trim().isEmpty()) {
                String errMes = "Please enter a valid port number";
                String errTitle = "Port Error";
                JOptionPane.showMessageDialog(frame, errMes, errTitle, JOptionPane.PLAIN_MESSAGE);
            } else {
                send.setEnabled(false);
                portNum.setEnabled(false);
                int port = Integer.parseInt(portNum.getText());
                try {
                    new ChatServer(port).start();
                } catch (IOException io) { io.printStackTrace(); }
            }
        });

        close.addActionListener(e -> System.exit(0));
    }

    public void show() {
        frame.setVisible(true);
    }
}
